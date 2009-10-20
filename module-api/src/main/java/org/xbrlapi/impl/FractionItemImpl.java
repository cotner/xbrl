package org.xbrlapi.impl;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xbrlapi.FractionItem;
import org.xbrlapi.utilities.XBRLException;

/**
 * TODO Create fraction item examples in the conformance suite to test fraction items.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class FractionItemImpl extends NumericItemImpl implements FractionItem {

	/**
	 * @see org.xbrlapi.FractionItem#getNumerator()
	 */
    public String getNumerator() throws XBRLException {
        Element data = getDataRootElement();
        Node child = data.getFirstChild();
        while (child.getNodeType() != Node.ELEMENT_NODE) {
            child = child.getNextSibling();
            if (child == null) throw new XBRLException("The fraction numerator is missing.");
        }
        return child.getTextContent().trim();
    }


	/**
	 * @see org.xbrlapi.FractionItem#getDenominator()
	 */
    public String getDenominator() throws XBRLException {
        Element data = getDataRootElement();
        Node child = data.getFirstChild();
        while (child.getNodeType() != Node.ELEMENT_NODE) {
            child = child.getNextSibling();
            if (child == null) throw new XBRLException("The fraction numerator is missing.");
        }
        child = child.getNextSibling();
        if (child == null) throw new XBRLException("The fraction denominator is missing.");
        while (child.getNodeType() != Node.ELEMENT_NODE) {
            child = child.getNextSibling();
            if (child == null) throw new XBRLException("The fraction denominator is missing.");
        }
        return child.getTextContent().trim();
    }
    
    
}
