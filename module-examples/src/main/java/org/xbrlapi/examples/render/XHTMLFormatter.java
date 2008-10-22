package org.xbrlapi.examples.render;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xbrlapi.Context;
import org.xbrlapi.Item;
import org.xbrlapi.NonNumericItem;
import org.xbrlapi.SimpleNumericItem;
import org.xbrlapi.Unit;

/**
 * @author YangSt1 Steve Yang (steve2yang@yahoo.com)
 *
 */
public class XHTMLFormatter {

	public final static String TAG_TABLE = "table";

	public final static String TAG_TABLE_CAPTION = "caption";

	public final static String TAG_TABLE_COLGROUP = "colgroup";

	public final static String TAG_TABLE_COL = "col";

	public final static String TAG_TABLE_TH = "th";

	public final static String TAG_TABLE_TR = "tr";

	public final static String TAG_TABLE_TD = "td";

	public static Element createRoot(Document doc, DocumentStylesheet stylesheet)
			throws Exception {
		// create the root element and add it to the document
		// <html version="-//XBRL International//DTD XHTML Inline XBRL 0.1//EN"
		// xmlns="http://www.w3.org/1999/xhtml"
		// xmlns:ix="http://www.xbrl.org/2008/inlineXBRL"
		// xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		// xmlns:xbrli="http://www.xbrl.org/2003/instance"
		// xmlns:link="http://www.xbrl.org/2003/linkbase"
		// xmlns:xlink="http://www.w3.org/1999/xlink"
		// xmlns:iso4217="http://www.xbrl.org/2003/iso4217"
		// xsi:schemaLocation="http://www.w3.org/1999/xhtml
		// http://schemas.corefiling.com/thirdParty/xbrl.org/iXBRL/IWD/2008-06-27/xhtml-inlinexbrl-0_1.xsd">
		Element root = doc.createElement("html");
		root.setAttribute("xmlns:xsi",
				"http://www.w3.org/2001/XMLSchema-instance");
		root.setAttribute("version",
				"-//XBRL International//DTD XHTML Inline XBRL 0.1//EN");
		root.setAttribute("xmlns", "http://www.w3.org/1999/xhtml");
		root.setAttribute("xmlns:ix", "http://www.xbrl.org/2008/inlineXBRL");
		root.setAttribute("xmlns:xsi",
				"http://www.w3.org/2001/XMLSchema-instance");
		root.setAttribute("xmlns:xbrli", "http://www.xbrl.org/2003/instance");
		root.setAttribute("xmlns:link", "http://www.xbrl.org/2003/linkbase");
		root.setAttribute("xmlns:iso4217", "http://www.xbrl.org/2003/iso4217");

		doc.appendChild(root);

		// create a comment and put it in the root element
		Comment comment = doc
				.createComment("XBRL Inline Rendering Version, Northrop Grumman Corp");
		root.appendChild(comment);

		// create head
		Element head = doc.createElement("head");
		Element title = doc.createElement("title");
		Text text = doc.createTextNode("XBRL Inline Document");
		title.appendChild(text);
		head.appendChild(title);

		Element style = doc.createElement("style");
		style.setAttribute("type", "text/css");
		Text styleText = doc.createTextNode(stylesheet.getStylesheet());
		style.appendChild(styleText);
		head.appendChild(style);

		Element meta = doc.createElement("meta");
		meta.setAttribute("http-equiv", "Content-Type");
		meta.setAttribute("content", "text/html; charset=utf-8");
		head.appendChild(meta);

		Element body = doc.createElement("body");

		root.appendChild(head);
		root.appendChild(body);

		return body;
	}

	public static Element createHeader(Document doc, Element parent,
			String headerText) throws Exception {
		Element header = doc.createElement("div");
		header.setAttribute("class", "header");

		Element page = doc.createElement("div");
		page.setAttribute("class", "pagetitle");

		Element headerNode = doc.createElement("div");
		headerNode.setAttribute("class", "normal");
		Text text = doc.createTextNode(headerText);
		headerNode.appendChild(text);

		page.appendChild(headerNode);
		header.appendChild(page);
		parent.appendChild(header);

		return header;
	}

	public static Element createTable(Document doc, Element parent,
			String title, HashMap<String, String> conceptLabelMap,
			List<String> conceptList, List<String> contextList,
			HashMap<String, Item> items, HashMap<String, Context> contexts,
			int maxLevel) throws Exception {
		Element table = doc.createElement("table");
		table.setAttribute("class", "value");
		table.setAttribute("style", "width : 900.0px;");
		parent.appendChild(table);

		Element caption = doc.createElement("caption");
		caption.setAttribute("class", "value");
		table.appendChild(caption);
		Text captionText = doc.createTextNode(title);
		caption.appendChild(captionText);

		// Build colgroup
		Element colgroup = doc.createElement("colgroup");
		table.appendChild(colgroup);
		for (int i = 0; i < maxLevel + 1; i++) {
			Element cg0 = doc.createElement("col");
			// cg0.setAttribute("style", "width : 20.0px; ");
			colgroup.appendChild(cg0);
		}

		for (@SuppressWarnings("unused") String contextStr : contextList) {
			Element cg1 = doc.createElement("col");
			colgroup.appendChild(cg1);
		}

		// Build th
		Element row = doc.createElement("tr");
		table.appendChild(row);
		for (int i = 0; i < maxLevel + 1; i++) {
			Element col0 = doc.createElement("th");
			col0.setAttribute("colspan", "1");
			col0.setAttribute("rowspan", "1");
			col0.setAttribute("class", "indent");
			row.appendChild(col0);
		}
		for (String contextStr : contextList) {
			if (contextStr == null || contextStr.length() < 1)
				continue;

			Element col1 = doc.createElement("th");
			col1.setAttribute("colspan", "1");
			col1.setAttribute("rowspan", "1");
			col1.setAttribute("class", "value");
			row.appendChild(col1);

			Element val = doc.createElement("div");
			col1.appendChild(val);
			val.setAttribute("class", "normal");

			Context ct = contexts.get(contextStr);
			Text text = doc.createTextNode(XBRLFormatter.formatContext(ct));
			val.appendChild(text);
		}

		// Create main table
		int counter = 1;
		for (String concept : conceptList) {
			if (concept == null || concept.length() < 1)
				continue;

			String label = conceptLabelMap.get(concept);

			// Row
			counter++;
			Element rrow = doc.createElement("tr");
			if (counter % 2 >= 1) {
				if (label.toLowerCase().trim().endsWith(", total")) {
					rrow.setAttribute("class", "row1total");
				} else {
					rrow.setAttribute("class", "row1");
				}
			} else {
				if (label.toLowerCase().trim().endsWith(", total")) {
					rrow.setAttribute("class", "row2total");
				} else {
					rrow.setAttribute("class", "row2");
				}
			}
			table.appendChild(rrow);

			buildLabelColumn(doc, rrow, label, maxLevel + 1);

			for (String context : contextList) {
				if (context == null || context.length() < 1)
					continue;

				// Column
				Element ccol1 = doc.createElement("td");
				ccol1.setAttribute("class", "value");

				Element val = doc.createElement("div");
				ccol1.appendChild(val);
				rrow.appendChild(ccol1);

				String key = context + "|" + concept;
				Item item = items.get(key);
				if (item == null) {
					String fmtVal = "";
					Text text = doc.createTextNode(fmtVal);
					val.appendChild(text);
					Logger.getRootLogger().debug(
							label + " " + context + " = " + fmtVal);
					continue;
				}

				if (item.isNumeric()) {
					SimpleNumericItem sni = (SimpleNumericItem) item;

					String qname = item.getConcept().getName();
					String prefix = item.getPrefixFromQName(qname);
					Element num = doc.createElement("ix:nonFraction");
					num.setAttribute("ix:name", prefix + ":" + qname);
					num.setAttribute("contextRef", item.getContextId());
					num.setAttribute("unitRef", item.getUnitId());
					num.setAttribute("decimals", "0");
					num.setAttribute("id", item.getFragmentIndex());
					num.setAttribute("ix:format", "commadot");
					num.setAttribute("ix:scale", "0");
					val.appendChild(num);

					val.setAttribute("class", "numeric");
					String fmtVal = XBRLFormatter.formatNumeric(sni.getValue());
					Text text = doc.createTextNode(fmtVal);
					num.appendChild(text);

					Logger.getRootLogger().debug(
							item.getLocalname() + " " + item.getContextId()
									+ " = " + fmtVal);
				} else {
					NonNumericItem nni = (NonNumericItem) item;
					val.setAttribute("class", "nonnumeric");
					StringBuffer hide = new StringBuffer(nni.getValue());
					int len = Math.min(255, hide.length());
					String fmtVal = hide.substring(0, len) + " ...";
					Text text = doc.createTextNode(fmtVal);
					val.appendChild(text);

					Logger.getRootLogger().debug(
							item.getLocalname() + " " + item.getContextId()
									+ " = " + fmtVal);
				}
			}
		}
		Element linebreak = doc.createElement("br");
		linebreak.setAttribute("class", "indent");
		parent.appendChild(linebreak);

		return table;
	}

	private static void buildLabelColumn(Document doc, Element parent,
			String label, int max) throws Exception {
		char[] buff = label.toCharArray();
		if (label.trim().length() == 0) {
			Logger.getRootLogger().debug("Found empty label!");
		}
		boolean indent = true;
		boolean value = false;
		int level = 0;
		for (int i = 0; i < max; i++) {
			if (buff[i] == ' ' && indent) {
				// indent column
				Element ccol0 = doc.createElement("td");
				ccol0.setAttribute("class", "indent");
				ccol0.setAttribute("colspan", "1");
				parent.appendChild(ccol0);
				level++;
			} else {
				indent = false;
				if (!value) {
					// label column
					Element ccol0 = doc.createElement("td");
					ccol0.setAttribute("class", "label");
					int colspan = max - level;
					ccol0.setAttribute("colspan", "" + colspan);
					Text text = doc.createTextNode(label.trim());
					ccol0.appendChild(text);
					parent.appendChild(ccol0);
					value = true;
				} else {
					break;
				}
			}
		}
	}

	public static Element createDisplayBlock(Document doc, Element parent)
			throws Exception {
		Element main = doc.createElement("div");
		main.setAttribute("class", "main");
		parent.appendChild(main);
		return main;
	}

	public static Element createNondisplayBlock(Document doc, Element parent)
			throws Exception {
		Element nondisplay = doc.createElement("div");
		nondisplay.setAttribute("style", "display: none");
		parent.appendChild(nondisplay);

		Element header = doc.createElement("ix:header");
		nondisplay.appendChild(header);

		return header;
	}

	public static Element createResource(Document doc, Element parent)
			throws Exception {
		Element resource = doc.createElement("ix:resource");
		parent.appendChild(resource);
		return resource;
	}

	public static Element createReference(Document doc, Element parent)
			throws Exception {
		Element reference = doc.createElement("ix:reference");
		parent.appendChild(reference);
		return reference;
	}

	public static Element createSchemaRef(Document doc, Element parent,
			String type, String ref) throws Exception {
		Element refNode = doc.createElement("link:schemaRef");
		refNode.setAttribute("xlink:type", type);
		refNode.setAttribute("xlink:href", ref);
		parent.appendChild(refNode);
		return refNode;
	}

	public static Element createContext(Document doc, Element parent,
			Context context) throws Exception {
		Element contextNode = doc.createElement("xbrli:context");
		contextNode.setAttribute("id", context.getId());
		parent.appendChild(contextNode);

		// Build entity
		Element entity = doc.createElement("xbrli:entity");
		Element identifier = doc.createElement("xbrli:identifier");
		identifier.setAttribute("scheme", context.getEntity()
				.getIdentifierScheme());
		Text value = doc.createTextNode(context.getEntity()
				.getIdentifierValue());
		identifier.appendChild(value);
		entity.appendChild(identifier);

		// Build period
		Element period = doc.createElement("xbrli:period");
		if (context.getPeriod().isInstantPeriod()) {
			Element instant = doc.createElement("xbrli:instant");
			Text insText = doc.createTextNode(context.getPeriod().getInstant());
			instant.appendChild(insText);
			period.appendChild(instant);
		} else {
			Element start = doc.createElement("xbrli:startDate");
			Text startDate = doc.createTextNode(context.getPeriod().getStart());
			start.appendChild(startDate);
			period.appendChild(start);
			Element end = doc.createElement("xbrli:endDate");
			Text endDate = doc.createTextNode(context.getPeriod().getEnd());
			end.appendChild(endDate);
			period.appendChild(end);
		}

		contextNode.appendChild(entity);
		contextNode.appendChild(period);
		return contextNode;
	}

	public static Element createUnit(Document doc, Element parent, Unit unit)
			throws Exception {
		Element unitNode = doc.createElement("xbrli:unit");
		unitNode.setAttribute("id", unit.getId());
		parent.appendChild(unitNode);

		Element measureNode = doc.createElement("xbrli:measure");
		unitNode.appendChild(measureNode);

		Text valueNode = doc.createTextNode(XBRLFormatter.formatUnit(unit));
		measureNode.appendChild(valueNode);

		return unitNode;
	}

	public static Element createFooter(Document doc, Element parent,
			String footerText) throws Exception {
		// <div class="footer">Thu Jun 12 16:41:19 EDT 2008, Northrop Grumman
		// Corporation</div>
		Element hr = doc.createElement("hr");
		parent.appendChild(hr);

		Element footer = doc.createElement("div");
		footer.setAttribute("class", "footer");

		Date now = new Date();
		SimpleDateFormat fmt = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
		Text text = doc.createTextNode(footerText + fmt.format(now));
		footer.appendChild(text);
		parent.appendChild(footer);

		return footer;
	}

}
