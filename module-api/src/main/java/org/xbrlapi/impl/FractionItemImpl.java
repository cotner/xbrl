package org.xbrlapi.impl;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xbrlapi.FractionItem;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class FractionItemImpl extends NumericItemImpl implements FractionItem {

	/**
     * 
     */
    private static final long serialVersionUID = -6689670696924267352L;

    /**
	 * @see org.xbrlapi.FractionItem#getNumerator()
	 */
    public double getNumerator() throws XBRLException {
        Element data = getDataRootElement();
        Node child = data.getFirstChild();
        while (child.getNodeType() != Node.ELEMENT_NODE) {
            child = child.getNextSibling();
            if (child == null) throw new XBRLException("The fraction numerator is missing.");
        }
        return new Double(child.getTextContent().trim()).doubleValue();
    }

	/**
	 * @see org.xbrlapi.FractionItem#getDenominator()
	 */
    public double getDenominator() throws XBRLException {
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
        return new Double(child.getTextContent().trim()).doubleValue();

    }

}
