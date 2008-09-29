package org.xbrlapi.examples.render;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xbrlapi.Arc;
import org.xbrlapi.Concept;
import org.xbrlapi.Context;
import org.xbrlapi.ExtendedLink;
import org.xbrlapi.FootnoteResource;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.Instance;
import org.xbrlapi.Item;
import org.xbrlapi.Locator;
import org.xbrlapi.NonNumericItem;
import org.xbrlapi.Resource;
import org.xbrlapi.RoleType;
import org.xbrlapi.Scenario;
import org.xbrlapi.Segment;
import org.xbrlapi.SimpleLink;
import org.xbrlapi.SimpleNumericItem;
import org.xbrlapi.Unit;
import org.xbrlapi.data.XBRLStore;
import org.xbrlapi.networks.Network;
import org.xbrlapi.networks.Networks;
import org.xbrlapi.networks.Relationship;
import org.xbrlapi.utilities.Constants;

/**
 * @author YangSt1 Steve Yang (steve2yang@yahoo.com)
 *
 */
public class InlineFormatter {

	private XBRLStore store = null;

	private FragmentList<Instance> instances = null;

	private String url = null;

	private Document doc = null;

	private DocumentStylesheet stylesheet = new DocumentStylesheet();

	private HashMap<String, Context> contexts = new HashMap<String, Context>();

	private HashMap<String, Unit> units = new HashMap<String, Unit>();

	private HashMap<String, Item> items = new HashMap<String, Item>();

	private HashMap<String, Integer> neContexts = new HashMap<String, Integer>();

	private int maxLevel = 1;

	public InlineFormatter(XBRLStore store, String style, String url)
			throws Exception {
		this.store = store;
		this.instances = store.<Instance> getFragments("Instance");
		this.url = url;

		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
		doc = docBuilder.newDocument();

		this.stylesheet.loadStylesheet(style);
	}

	public DocumentStylesheet getDocumentStyle() {
		return this.stylesheet;
	}

	public void render() throws Exception {
		// create the base structure
		double startTime = System.currentTimeMillis();

		Element body = XHTMLFormatter.createRoot(this.doc, this.stylesheet);

		XHTMLFormatter.createHeader(this.doc, body, "Rendered XBRL Document");

		// 1. Needs to be done first to populate context and unit maps.
		createNondisplaySection(body);

		// 2. Build talbes visible parts organized by extended role types.
		createMainSection(body);

		XHTMLFormatter.createFooter(this.doc, body, "");

		String time = (new Double(System.currentTimeMillis() - startTime))
				.toString();
		if (time.length() > 4)
			time = time.substring(0, 4);
		Logger.getRootLogger().info(
				"Total time for rendering: " + time + " milisecond(s)");
	}

	private void createMainSection(Element parent) throws Exception {
		Element main = XHTMLFormatter.createDisplayBlock(this.doc, parent);

		this.buildFormattingModel(main);

		createFootnotes(main);
	}

	private void createNondisplaySection(Element parent) throws Exception {
		Element nondisplay = XHTMLFormatter.createNondisplayBlock(this.doc,
				parent);
		parseResources(nondisplay);
	}

	private void buildFormattingModel(Element parent) throws Exception {
		// Load all the items to maps
		processItems();

		for (String linkrole : store
				.getLinkRoles(Constants.PresentationArcRole).keySet()) {
			FragmentList<Fragment> rootLocators = store.getNetworkRoots(
					Constants.XBRL21LinkNamespace, "presentationLink",
					linkrole, Constants.XBRL21LinkNamespace, "presentationArc",
					Constants.PresentationArcRole);

			String title = linkrole;
			for (RoleType rt : store.getRoleTypes(linkrole)) {
				if (rt != null) {
					title = rt.getDefinition();
				}
			}
			Logger.getRootLogger().info(title);

			this.maxLevel = 1;
			HashMap<String, String> conceptLabelMap = new HashMap<String, String>();
			ArrayList<String> conceptList = new ArrayList<String>();
			for (Fragment rootLocator : rootLocators) {
				Concept rootConcept = (Concept) ((Locator) rootLocator)
						.getTargetFragment();

				Float order = this.getPresentationOrder(linkrole, null,
						rootConcept, "", conceptLabelMap);
				parsePresentation("", null, rootConcept, order.floatValue(),
						linkrole, conceptLabelMap, conceptList);
			}

			ArrayList<String> contextList = buildContexts(conceptList);

			XHTMLFormatter.createTable(this.doc, parent, title,
					conceptLabelMap, conceptList, contextList, this.items,
					this.contexts, this.maxLevel);
		}
	}

	private ArrayList<String> buildContexts(ArrayList<String> conceptList)
			throws Exception {
		ArrayList<String> contextList = new ArrayList<String>();
		TreeMap<String, String> localContexts = new TreeMap<String, String>();
		for (String context : this.neContexts.keySet()) {
			Context contextObj = (Context) this.contexts.get(context);
			String date = XBRLFormatter.getContextDate(contextObj);
			for (String concept : conceptList) {
				int count = (Integer) this.neContexts.get(context).intValue();
				String key = context + "|" + concept;
				if (count > 0 && this.items.get(key) != null) {
					localContexts.put(date, context);
					break;
				}
			}
		}

		for (String context : localContexts.values()) {
			// TODO: Dimension members
			Context contextObj = this.contexts.get(context);
			Segment segment = contextObj.getEntity().getSegment();
			Scenario scenario = contextObj.getScenario();
			if (segment != null) {
				// Dimension in segment
				NodeList nl = segment.getComplexContent();
				for (int i = 0; i < nl.getLength(); i++) {
					if (nl.item(i).getNamespaceURI() == XBRLFormatter.DimensionRole) {
						String dimension = nl.item(i).getAttributes()
								.getNamedItem("dimension").getNodeValue();
						String member = nl.item(i).getTextContent();
					}
				}
				continue;
			} else if (scenario != null) {
				// Dimension in scenario
				continue;
			} else {
				// Normal contexts
			}
			
			contextList.add(context);
			Logger.getRootLogger().debug("\tcontext: " + context);
		}
		Collections.reverse(contextList);

		return contextList;
	}

	private void parsePresentation(String indent, Fragment parentFragment,
			Fragment fragment, float order, String linkrole,
			HashMap<String, String> conceptLabelMap,
			ArrayList<String> conceptList) throws Exception {
		Concept parent = (Concept) parentFragment;
		Concept concept = (Concept) fragment;

		String label = XBRLFormatter.getLabel(concept,
				Constants.StandardLabelRole);
		if (label == null || label.length() == 0) {
			label = concept.getName();
		}

		String key = concept.getTargetNamespaceURI() + "|" + concept.getName();
		label = indent + label;
		int level = updateMaxIndentLevel(label);
		if (!conceptLabelMap.containsKey(key)) {
			// Standard label should not overwrite preferred label
			conceptLabelMap.put(key, label);
		}
		conceptList.add(key);

		if (parent != null) {
			Logger.getRootLogger().debug(
					"\tLEVEL:\t" + level + "\tCONCEPT:\t" + concept.getName());

			Logger.getRootLogger().debug(
					"Arc:\t" + "order:" + order + "\tfrom:\t"
							+ parent.getName() + "\n\t\t\t\tto:\t"
							+ concept.getName());

		}

		Logger.getRootLogger().debug(
				indent + concept.getTargetNamespaceURI() + ":"
						+ concept.getName());
		Networks networks = concept
				.getNetworksWithArcrole(Constants.PresentationArcRole); // Some

		if (networks.getSize() > 0) {
			FragmentList<Fragment> children = networks.getTargetFragments(
					concept.getFragmentIndex(), Constants.PresentationArcRole,
					linkrole);

			TreeMap<Float, Fragment> orderKeys = new TreeMap<Float, Fragment>();
			for (Fragment child : children) {
				Concept childConcept = (Concept) child;

				if (concept != null && childConcept != null) {
					Float orderNum = this.getPresentationOrder(linkrole,
							concept, childConcept, indent + " ",
							conceptLabelMap);
					orderKeys.put(orderNum, child);
				}
			}

			for (Float orderNum : orderKeys.keySet()) {
				Fragment child = orderKeys.get(orderNum);
				parsePresentation(indent + " ", fragment, child, orderNum
						.floatValue(), linkrole, conceptLabelMap, conceptList);
			}
		}
	}

	private Float getPresentationOrder(String linkrole, Concept parent,
			Concept child, String indent,
			HashMap<String, String> conceptLabelMap) throws Exception {
		Float preOrder = new Float(0.0);

		if (parent == null)
			return preOrder;

		Networks networks = child
				.getNetworksWithArcrole(Constants.PresentationArcRole); // Some

		if (networks.getSize() > 0) {

			Network nt = networks.getNetwork(Constants.PresentationArcRole,
					linkrole);
			for (String index : nt.getRootFragmentIndexes()) {
				for (Relationship rel : nt.getActiveRelationshipsFrom(index)) {
					Concept fromConcept = (Concept) rel.getSource();
					Concept toConcept = (Concept) rel.getTarget();
					Arc arc = rel.getArc();
					String order = arc.getOrder();

					boolean parent_child = false;
					boolean child_parent = false;
					if (fromConcept.getName().equals(parent.getName())
							&& toConcept.getName().equals(child.getName())) {
						parent_child = true;
					}

					if (fromConcept.getName().equals(child.getName())
							&& toConcept.getName().equals(parent.getName())) {
						child_parent = true;
					}

					if (parent_child || child_parent) {
						// Update preferred label
						if (conceptLabelMap != null
								&& arc.hasAttribute("preferredLabel")) {

							String key = toConcept.getTargetNamespaceURI()
									+ "|" + toConcept.getName();

							String label = XBRLFormatter.getLabel(toConcept,
									arc.getAttribute("preferredLabel"));
							if (label != null && label.length() > 0)
								conceptLabelMap.put(key, indent + label);
						}

						// Parse string order to float
						try {
							preOrder = Float.parseFloat(order);
						} catch (Exception e) {
							Logger.getRootLogger().info(
									"Invalid order for concept "
											+ nt.getArcRole());
						}
						break;
					}
				}
			}
		}
		return preOrder;
	}

	private void processItems() throws Exception {
		for (Instance instance : instances) {
			String url = instance.getURL();
			if (this.url == null || !url.equals(this.url))
				continue;

			FragmentList<Item> items = instance.getItems();
			Logger.getRootLogger().info("Top level items in the instance.");
			for (Item item : items) {
				String key = item.getContextId() + "|"
						+ item.getConcept().getTargetNamespaceURI() + "|"
						+ item.getConcept().getName();

				this.items.put(key, item);
				String value = null;
				if (item.isNumeric()) {
					SimpleNumericItem sni = (SimpleNumericItem) item;
					value = sni.getValue();
				} else {
					NonNumericItem nni = (NonNumericItem) item;
					value = nni.getValue();
				}
				int increment = 1;
				if (value == null || value.length() < 1) {
					increment = 0;
				}
				if (!this.neContexts.containsKey(item.getContextId())) {
					this.neContexts.put(item.getContextId(), new Integer(
							increment));
				} else {
					int count = (Integer) this.neContexts.get(
							item.getContextId()).intValue();
					count = count + increment;
					this.neContexts
							.put(item.getContextId(), new Integer(count));
				}
			}
		}
	}

	private int updateMaxIndentLevel(String label) throws Exception {
		char[] buff = label.toCharArray();
		int indent = 0;
		for (char ch : buff) {
			if (ch == ' ') {
				indent++;
			} else {
				break;
			}
		}
		this.maxLevel = Math.max(indent, this.maxLevel);
		return indent;
	}

	private void parseResources(Element parent) throws Exception {
		Element resources = XHTMLFormatter.createResource(this.doc, parent);

		Logger.getRootLogger().info("Build Inline resource section ...");
		for (Instance instance : instances) {
			String url = instance.getURL();
			if (this.url == null || !url.equals(this.url))
				continue;

			Logger.getRootLogger().debug("Linkbase Refs in the instance");
			for (SimpleLink slink : instance.getLinkbaseRefs()) {
				String ref = slink.getHref();
				Logger.getRootLogger().debug(ref);
			}

			Logger.getRootLogger().debug("Schema Refs in the instance");
			Element reference = XHTMLFormatter
					.createReference(this.doc, parent);
			for (SimpleLink slink : instance.getSchemaRefs()) {
				String ref = slink.getHref();
				String[] parts = ref.trim().replace('\\', '/').split("/");

				XHTMLFormatter.createSchemaRef(this.doc, reference, "simple",
						parts[parts.length - 1]);
				Logger.getRootLogger().debug(ref);
			}

			// List contexts
			FragmentList<Context> contextList = instance.getContexts();
			Logger.getRootLogger().debug("Contexts in the instance.");
			for (Context context : contextList) {
				Logger.getRootLogger().debug("Context ID: " + context.getId());
				XHTMLFormatter.createContext(this.doc, resources, context);
				this.contexts.put(context.getId(), context);
			}

			// List units
			FragmentList<Unit> units = instance.getUnits();
			Logger.getRootLogger().debug("Units in the instance.");
			for (Unit unit : units) {
				Logger.getRootLogger().info("Unit ID: " + unit.getId());
				XHTMLFormatter.createUnit(this.doc, resources, unit);
				this.units.put(unit.getId(), unit);
			}
		}

	}

	private void createFootnotes(Element parent) throws Exception {
		for (Instance instance : instances) {
			FragmentList<ExtendedLink> links = instance.getFootnoteLinks();
			Logger.getRootLogger().debug("Footnote links in the instance.");
			for (ExtendedLink link : links) {
				FragmentList<Resource> resources = link.getResources();
				for (Resource resource : resources) {
					FootnoteResource fnr = (FootnoteResource) resource;
					Logger.getRootLogger()
							.debug(
									"Footnote resource: "
											+ fnr.getDataRootElement()
													.getTextContent());
				}
			}
		}
	}

	public String transform(String outputFile) throws Exception {
		// set up a transformer
		TransformerFactory transfac = TransformerFactory.newInstance();
		Transformer trans = transfac.newTransformer();
		trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		trans.setOutputProperty(OutputKeys.INDENT, "yes");

		// create string from xml tree
		File file = new File(outputFile);
		FileWriter fw = new FileWriter(file);
		StreamResult result = new StreamResult(fw);
		DOMSource source = new DOMSource(doc);
		trans.transform(source, result);
		fw.close();

		return file.getCanonicalPath();
	}
}
