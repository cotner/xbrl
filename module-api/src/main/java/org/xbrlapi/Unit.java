package org.xbrlapi;

import java.util.List;

import org.w3c.dom.NodeList;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Unit extends FactDimensionContainer {

    /**
     * Get the numerator set of measures for the unit
     * @return the numerator measure set
     * @throws XBRLException
     */
    public NodeList getNumeratorMeasures() throws XBRLException;

    /**
     * Determines if the unit has a denominator
     * 
     * @return true if the unit has a denominator that has 
     * at least one measure in it, and false otherwise.
     * @throws XBRLException
     */
    public boolean hasDenominator() throws XBRLException;

    /**
     * Get the set of measure elements in the denominator.
     * @return the denominator measure set.
     * @throws XBRLException if the denominator does not exist.
     */
    public NodeList getDenominatorMeasures() throws XBRLException;
    

    
    /**
     * Tests if the unit is u-equal to another unit
     * returning true if they are u-equal and false otherwise
     *
     * @param unit The unit being compared.
     * @return true if the two units are equal and false otherwise
     * @throws XBRLException
     */
    public boolean equals(Unit unit) throws XBRLException;

    /**
     * @return a list of resolved numerator measure qname values each of which 
     * is the namespace followed by the separator string |:|:| followed by the local name.
     * @throws XBRLException
     */
    public List<String> getResolvedNumeratorMeasures() throws XBRLException;
    
    /**
     * @return a list of resolved denominator measure qname values each of which 
     * is the namespace followed by the separator string |:|:| followed by the local name.
     * If there is no denominator then it returns an empty list.
     * @throws XBRLException
     */
    public List<String> getResolvedDenominatorMeasures() throws XBRLException;
    
    
}
