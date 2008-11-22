package org.xbrlapi.examples.render;

import java.io.File;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
import org.xbrlapi.utilities.XBRLException;

/**
 * @author YangSt1 Steve Yang (steve2yang@yahoo.com)
 *
 */
public class InlineFormatter {

    protected static Logger logger = Logger.getLogger(InlineFormatter.class);
    
    private XBRLStore store = null;

	/**
	 * The root fragment of the target XBRL instance.
	 */
	private Instance instanceRoot = null;
	
	private String url = null;

	private Document doc = null;

	private DocumentStylesheet stylesheet = new DocumentStylesheet();

	private HashMap<String, Context> contexts = new HashMap<String, Context>();

	private HashMap<String, Unit> units = new HashMap<String, Unit>();

	private HashMap<String, Item> items = new HashMap<String, Item>();

	private HashMap<String, Integer> neContexts = new HashMap<String, Integer>();

	private int maxLevel = 1;

	/**
	 * @param store The data store containing the information that drives the rendering.
	 * @param stylesheet The CSS stylesheet.
	 * @param url The string representation of the URL of the instance to be rendered.
	 * @throws Exception
	 */
	public InlineFormatter(XBRLStore store, String stylesheet, String url) throws XBRLException {

	    if (store == null) {
	        throw new XBRLException("The data store must not be null.");
	    }
	    this.store = store;

	    // Get the root fragment of the target XBRL instance
	    try {
    	    FragmentList<Instance> instances = store.getFragmentsFromDocument(new URL(url),"Instance");
    	    if (instances.getLength() > 1) throw new XBRLException("The target instance is not a single XBRL instance.");
            if (instances.getLength() == 0) throw new XBRLException("The target document is not an XBRL instance.");
            this.instanceRoot = instances.get(0);
	    } catch (MalformedURLException e) {
            throw new XBRLException("The target instance URL is malformed.");
        }

		if (url == null) {
            throw new XBRLException("The URL of the target instance must not be null.");
		}
		this.url = url;

		try {
    		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
    		DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
    		doc = docBuilder.newDocument();
		} catch (ParserConfigurationException e) {
		    throw new XBRLException("The parser configuration failed.", e);
		}
        if (stylesheet == null) {
            throw new XBRLException("The CSS stylesheet must be specified.");
        }
		this.stylesheet.loadStylesheet(stylesheet);
	}

	public DocumentStylesheet getDocumentStyle() {
		return this.stylesheet;
	}

	/**
	 * Generate a DOM object representing the 
	 * XHTML rendering of the XBRL instance.
	 * @throws Exception
	 */
	public void render() throws Exception {

		Element body = XHTMLFormatter.createRoot(this.doc, this.stylesheet);

		XHTMLFormatter.createHeader(this.doc, body, "Rendered XBRL Document");

		// 1. Needs to be done first to populate context and unit maps.
		createNondisplaySection(body);

		// 2. Build tables of content, organized by extended role types.
		createMainSection(body);

		XHTMLFormatter.createFooter(this.doc, body, "");
		
	}

	private void createMainSection(Element parent) throws Exception {
		Element main = XHTMLFormatter.createDisplayBlock(this.doc, parent);

		this.buildFormattingModel(main);

		createFootnotes(main);
	}

	/**
	 * Populates the hidden part of the inline XBRL document.
	 * @param parent The container element for the hidden inline XBRL content.
	 * @throws Exception
	 */
	private void createNondisplaySection(Element parent) throws Exception {
		Element inlineXBRLHeaderElement = XHTMLFormatter.createNondisplayBlock(this.doc,
				parent);
		parseResources(inlineXBRLHeaderElement);
	}

	private void buildFormattingModel(Element parent) throws Exception {

		processItems();

		// Iterate the presentation linkroles in the DTS
		for (String linkrole : store.getLinkRoles(Constants.PresentationArcRole).keySet()) {
		    // TODO ensure that this getNetworkRoots method returns resources rather than locators.
		    FragmentList<Fragment> rootLocators = 
			    store.getNetworkRoots(
					Constants.XBRL21LinkNamespace, "presentationLink",
					linkrole, Constants.XBRL21LinkNamespace, "presentationArc",
					Constants.PresentationArcRole);

			String title = linkrole;
			for (RoleType rt : store.getRoleTypes(linkrole)) {
				if (rt != null) {
					title = rt.getDefinition();
				}
			}
			logger.info(title);

			this.maxLevel = 1;
			HashMap<String, String> conceptLabelMap = new HashMap<String, String>();
			ArrayList<String> conceptList = new ArrayList<String>();
			for (Fragment rootLocator : rootLocators) {
				Concept rootConcept = (Concept) ((Locator) rootLocator).getTargetFragment();

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
			Context contextObj = this.contexts.get(context);
			String date = XBRLFormatter.getContextDate(contextObj);
			for (String concept : conceptList) {
				int count = this.neContexts.get(context).intValue();
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
                        String dimension = nl.item(i).getAttributes().getNamedItem("dimension").getNodeValue();
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
			logger.debug("\tcontext: " + context);
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
			logger.debug(
					"\tLEVEL:\t" + level + "\tCONCEPT:\t" + concept.getName());

			logger.debug(
					"Arc:\t" + "order:" + order + "\tfrom:\t"
							+ parent.getName() + "\n\t\t\t\tto:\t"
							+ concept.getName());

		}

		logger.debug(
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
							logger.info(
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

	/**
	 * Builds the items map of top level items in the XBRL instance, 
	 * indexed by a combination of the context ID, and the item's QName.
	 * Builds the neContexts map of numbers of top level items for each context,
	 * indexed by the context ID.
	 * @throws Exception
	 */
	private void processItems() throws Exception {

		FragmentList<Item> items = instanceRoot.getItems();
		logger.info("Putting top level items in the target instance into a map.");
		for (Item item : items) {
			String key = item.getContextId() + "|"
					+ item.getConcept().getTargetNamespaceURI() + "|"
					+ item.getConcept().getName();

			this.items.put(key, item);
			
            // TODO Handle fractions.
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
				this.neContexts.put(item.getContextId(), new Integer(increment));
			} else {
				int count = this.neContexts.get(item.getContextId()).intValue();
				count = count + increment;
				this.neContexts.put(item.getContextId(), new Integer(count));
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

	/**
	 * Responsible for populating the inline XBRL resource section of
	 * the output XHTML document.
	 * @param parent The parent element that will be populated.
	 * @throws Exception
	 */
	private void parseResources(Element parent) throws Exception {
		Element resources = XHTMLFormatter.createResource(this.doc, parent);

		logger.info("Build Inline resource section ...");

		logger.debug("Linkbase Refs in the instance");
		for (SimpleLink linkbaseReference : instanceRoot.getLinkbaseRefs()) {
			String ref = linkbaseReference.getHref();
			// TODO Create the necessary linkbase reference in the inline XBRL document.
			logger.debug(ref);
		}

		Run.reportTime("Rendering linkbase references");

		logger.debug("Schema Refs in the instance");
		Element reference = XHTMLFormatter
				.createReference(this.doc, parent);
		for (SimpleLink schemaReference : instanceRoot.getSchemaRefs()) {
			String ref = schemaReference.getHref();
			String[] parts = ref.trim().replace('\\', '/').split("/");

			XHTMLFormatter.createSchemaRef(this.doc, reference, "simple",
					parts[parts.length - 1]);
			logger.debug(ref);
		}

		Run.reportTime("Rendering schema references");

		// List contexts
		FragmentList<Context> contexts = instanceRoot.getContexts();
		logger.debug("Contexts in the instance.");
		for (Context context : contexts) {
			logger.debug("Context ID: " + context.getId());
			XHTMLFormatter.createContext(this.doc, resources, context);
			this.contexts.put(context.getId(), context);
		}
        Run.reportTime("Rendering contexts");

		// List units
		FragmentList<Unit> units = instanceRoot.getUnits();
		logger.debug("Units in the instance.");
		for (Unit unit : units) {
			logger.info("Unit ID: " + unit.getId());
			XHTMLFormatter.createUnit(this.doc, resources, unit);
			this.units.put(unit.getId(), unit);
		}
        Run.reportTime("Rendering units");

	}

	private void createFootnotes(Element parent) throws Exception {
		FragmentList<ExtendedLink> links = instanceRoot.getFootnoteLinks();
		logger.debug("Footnote links in the instance.");
		for (ExtendedLink link : links) {
			FragmentList<Resource> resources = link.getResources();
			for (Resource resource : resources) {
				FootnoteResource fnr = (FootnoteResource) resource;
				logger.debug("Footnote resource: " + fnr.getDataRootElement().getTextContent());
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
