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
     * Get the concept's periodType, one of instant or duration
     * @return the period attribute value.
     * @throws XBRLException if the periodType is missing.
     */
    public String getPeriodType() throws XBRLException;
    
    /**
     * Get the concept's balance, one of debit or credit
     *
     * @throws XBRLException
     */
    public String getBalance() throws XBRLException;
    


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
