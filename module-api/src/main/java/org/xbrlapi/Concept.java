package org.xbrlapi;


import java.net.URI;
import java.util.List;
import java.util.Set;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */


public interface Concept extends ElementDeclaration {

    /**
     * @return the period attribute value (one of instant or duration) for concepts 
     * in the XBRL item substitution group and null for tuple concepts.
     * @throws XBRLException if the periodType is missing for concepts in 
     * the XBRL item substitution group.
     */
    public String getPeriodType() throws XBRLException;
    
    /**
     * Get the concept's balance, one of debit or credit
     * or null if the balance is not defined.
     *
     * @throws XBRLException
     */
    public String getBalance() throws XBRLException;
    
    /**
     * @return true if the concept is numeric.
     * @throws XBRLException
     */
    public boolean isNumeric() throws XBRLException;

    /**
     * @return the list of facts in the data store giving values for this concept
     * @throws XBRLException
     */
    public List<Fact> getFacts() throws XBRLException;
    
    /**
     * @return the number of facts for this concept
     * @throws XBRLException
     */
    public long getFactCount() throws XBRLException ;
    
    /**
     * @return the set of indices of facts for this concept
     * @throws XBRLException
     */
    public Set<String> getFactIndices() throws XBRLException;    
    
    /**
     * @return the list of extended link roles for the 
     * presentation networks involving this concept as a 
     * source or a target.
     * @throws XBRLException
     */
    public List<URI> getPresentationNetworkLinkroles() throws XBRLException;


    
 }
