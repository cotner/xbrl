package org.xbrlapi;

import java.util.List;

import org.xbrlapi.networks.Networks;
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
    public FragmentList<Fact> getFacts() throws XBRLException;
    
 
    /**
     * @return the networks of presentation relationships involving
     * this concept as a source or a target.
     * @throws XBRLException
     */
    public Networks getPresentationNetworks() throws XBRLException;
 
    /**
     * @return the list of extended link roles for the 
     * presentation networks involving this concept as a 
     * source or a target.
     * @throws XBRLException
     */
    public List<String> getPresentationNetworkLinkroles() throws XBRLException;
    
 }
