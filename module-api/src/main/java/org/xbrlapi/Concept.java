package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */


public interface Concept extends ElementDeclaration {

    /**
     * Get the concept's periodType, one of instant or duration
     * @return the period attribute value.
     * @throws XBRLException if the periodType is missing.
     */
    public String getPeriodType() throws XBRLException;
    
    /**
     * Set the periodType of the concept, one of instant or duration
     *
     * @param periodType The periodType value for the concept.
     * @throws XBRLException
     */
    public void setPeriodType(String periodType) throws XBRLException;

    /**
     * Get the concept's balance, one of debit or credit
     *
     * @throws XBRLException
     */
    public String getBalance() throws XBRLException;
    
    /**
     * Set the balance of the concept, one of debit or credit
     *
     * @param balance The balance value for the concept.
     * @throws XBRLException
     */
    public void setBalance(String balance) throws XBRLException;

    /**
     * @return the list of facts in the data store giving values for this concept
     * @throws XBRLException
     */
    public FragmentList<Fact> getFacts() throws XBRLException;
    
    
}
