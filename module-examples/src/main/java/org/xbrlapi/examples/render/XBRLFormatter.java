package org.xbrlapi.examples.render;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xbrlapi.Concept;
import org.xbrlapi.Context;
import org.xbrlapi.LabelResource;
import org.xbrlapi.Unit;

/**
 * @author YangSt1 Steve Yang (steve2yang@yahoo.com)
 *
 */
public class XBRLFormatter {

	public static String DimensionRole = "http://xbrl.org/2006/xbrldi";
	
	public static String formatContext(Context context) throws Exception {
		String contextStr = "";
		contextStr += context.getId();
		if (context.getPeriod().isInstantPeriod()) {
			contextStr += " (";
			contextStr += context.getPeriod().getInstant();
			contextStr += ")";
		} else {
			contextStr += " (";
			contextStr += context.getPeriod().getStart();
			contextStr += " - ";
			contextStr += context.getPeriod().getEnd();
			contextStr += ")";
		}
		return contextStr;
	}
	
	public static String formatUnit(Unit unit) throws Exception {
		String contextStr = "";
		contextStr += unit.getId();
		if(unit.hasDenominator())
		{
			NodeList nl = unit.getNumeratorMeasures();
			for(int i = 0; i < nl.getLength(); i++){
				Node node = nl.item(i);
				contextStr += node.getTextContent();
			}
		}
		else
		{
			NodeList nl = unit.getNumeratorMeasures();
			for(int i = 0; i < nl.getLength(); i++){
				Node node = nl.item(i);
				contextStr += node.getTextContent();
			}
		}
		return contextStr;
	}

	public static String formatNumeric(String value) throws Exception {
		int decPos = 0;
		String symble = "$";

		if (value.indexOf('.') >= 0) {
			decPos = 2;
		}
		java.text.NumberFormat nf = java.text.DecimalFormat
				.getInstance(java.util.Locale.US);
		nf.setMaximumFractionDigits(decPos);
		nf.setMinimumFractionDigits(decPos);

		java.text.NumberFormat cf = java.text.DecimalFormat
				.getCurrencyInstance(java.util.Locale.US);
		cf.setMaximumFractionDigits(decPos);
		cf.setMinimumFractionDigits(decPos);

		java.text.DecimalFormatSymbols dfs = new java.text.DecimalFormatSymbols(
				java.util.Locale.US);
		dfs.setCurrencySymbol(symble);

		if (value != null || value.trim().length() > 0) {
			try {
				Double db = Double.parseDouble(value);
				value = cf.format(db.doubleValue());
			} catch (Exception e) {
				//
			}
		}
		return value;
	}

	public static String getLabel(Concept concept, String labelRole) throws Exception
	{
		String label = "";
		for (LabelResource lr : concept.getLabels()) {
			if (lr != null) {
				if (lr.getRole().equals(labelRole)) {
					label = lr.getStringValue();
					break;
				} 
			}
		}
		return label;
	}

	public static String getContextDate(Context context) throws Exception {
		String contextStr = "";
		contextStr += context.getId();
		if (context.getPeriod().isInstantPeriod()) {
			contextStr += context.getPeriod().getInstant();
		} else {
			contextStr += context.getPeriod().getEnd();
		}
		return contextStr;
	}	
}
