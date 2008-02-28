package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface CalculationArc extends Arc {

    /**
     * Get the weight attribute value.
     * @return the numeric value of the weight attribute on the calculation arc.
     * @throws XBRLException if the weight attribute is missing or has a zero value.
     */
    public double getWeight() throws XBRLException;

    /**
     * Set the weight attribute value on a calculation arc.  The
     * weight attribute cannot be set equal to zero.
     *
     * @param weight The value of the weight attribute
     * @throws XBRLException
     */
    public void setWeight(double weight) throws XBRLException;

}
