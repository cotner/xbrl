package org.xbrlapi.impl;
import org.w3c.dom.Element;
import org.xbrlapi.CalculationArc;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class CalculationArcImpl extends ArcImpl implements CalculationArc {

    /**
     * Get the weight attribute value.
     * @return the numeric value of the weight attribute on the calculation arc.
     * @throws XBRLException if the weight attribute is missing or has a zero value.
     * @see org.xbrlapi.CalculationArc#getWeight()
     */
    public double getWeight() throws XBRLException {
    	Element root = getDataRootElement();
    	if (! root.hasAttribute("weight")) throw new XBRLException("Calculation arcs MUST have a weight attribute.");
    	String weight = getDataRootElement().getAttribute("weight");
    	if (weight.equals("")) throw new XBRLException("Calculation arcs MUST have a non-zero decimal value.  They cannot be empty.");
    	double value = (new Double(weight)).doubleValue();
    	if (value == 0.0) throw new XBRLException("Calculation arcs MUST have a non-zero decimal value.  They cannot be empty.");
    	return value;
    }
    
    /**
     * Set the weight attribute value on a calculation arc.  The
     * weight attribute cannot be set equal to zero.
     * @param weight The value of the weight attribute
     * @throws XBRLException
     * @see org.xbrlapi.CalculationArc#setWeight(double)
     */
    public void setWeight(double weight) throws XBRLException {
    	throw new XBRLException("Data update methods are not yet implemented.");
    	//getDataRootElement().setAttribute("weight", (new Double(weight)).toString());
    }
	
}
