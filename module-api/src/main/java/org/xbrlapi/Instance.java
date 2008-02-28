package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Instance extends Fragment {

    /**
     * Get the list of schemaRef fragments in the instance
     * Returns null if none are contained by the XBRL instance.
     * @throws XBRLException
     */
    public FragmentList<SimpleLink> getSchemaRefs() throws XBRLException;    

    /**
     * Add a schemaRef to the XBRL instance
     * @param schemaRef the schemaRef to add to the XBRL instance
     * @throws XBRLException if the schemaRef is already part of the
     * XBRL instance.
     */
    public void addSchemaRef(SimpleLink schemaRef) throws XBRLException;    
    
    /**
     * Remove a schemaRef from the XBRL instance.
     * @param schemaRef the schemaRef to remove from the XBRL instance
     * @throws XBRLException if the schema ref is not a part of the 
     * XBRL instance.
     */
    public void removeSchemaRef(SimpleLink schemaRef) throws XBRLException;    

    /**
     * Get the list of linkbaseRef fragments in the instance
     * Returns null if none are contained by the XBRL instance.
     * @throws XBRLException
     */
    public FragmentList<SimpleLink> getLinkbaseRefs() throws XBRLException;    

    /**
     * Add a linkbaseRef to the XBRL instance
     * @param linkbaseRef the linkbaseRef to add to the XBRL instance
     * @throws XBRLException if the linkbaseRef is already part of the
     * XBRL instance. 
     */
    public void addLinkbaseRef(SimpleLink linkbaseRef) throws XBRLException;    
    
    /**
     * Remove a linkbaseRef from the XBRL instance.
     * @param linkbaseRef the linkbaseRef to remove from the XBRL instance
     * @throws XBRLException if the linkbase ref is not a part of the 
     * XBRL instance.
     */
    public void removeLinkbaseRef(SimpleLink linkbaseRef) throws XBRLException;    

    /**
     * Get list contexts contained in the instance.
     * Returns null if none are contained by the XBRL instance.
     * @throws XBRLException
     */
    public FragmentList<Context> getContexts() throws XBRLException;    

    /**
     * Get a specified context from the instance based on its id.
     * @param id The id of the context fragment
     * @return the context fragment
     * @throws XBRLException if the context does not exist
     */
    public Context getContext(String id) throws XBRLException;

    /**
     * Add a context to the XBRL instance
     * @param context the context to add to the XBRL instance
     * @throws XBRLException if the context is already part of the
     * XBRL instance.
     */
    public void addContext(Context context) throws XBRLException;    
    
    /**
     * Remove a context from the XBRL instance.
     * @param context the context to remove from the XBRL instance
     * @throws XBRLException if the context is not a part of the 
     * XBRL instance.
     */
    public void removeContext(Context context) throws XBRLException;    

    /**
     * Get the list of units contained in the instance.
     * Returns null if none are contained by the XBRL instance.
     *
     * @throws XBRLException
     */
    public FragmentList<Unit> getUnits() throws XBRLException;    

    /**
     * Get a specified unit from the instance based on its id.
     * @param id The id of the unit fragment
     * @return the unit fragment
     * @throws XBRLException if the unit does not exist
     */
    public Unit getUnit(String id) throws XBRLException;
    
    /**
     * Add a unit to the XBRL instance
     * @param unit the unit to add to the XBRL instance
     * @throws XBRLException if the unit is already part of the
     * XBRL instance.
     */
    public void addUnit(Unit unit) throws XBRLException;    
    
    /**
     * Remove a unit from the XBRL instance.
     * @param unit the unit to remove from the XBRL instance
     * @throws XBRLException if the unit is not a part of the 
     * XBRL instance.
     */
    public void removeUnit(Unit unit) throws XBRLException;    
    
    /**
     * Get a list of footnote link fragments
     * Returns null if none are contained by the XBRL instance.
     * @throws XBRLException
     */
    public FragmentList<ExtendedLink> getFootnoteLinks() throws XBRLException;    

    /**
     * Add a footnoteLink to the XBRL instance
     * @param footnoteLink the footnoteLink to add to the XBRL instance
     * @throws XBRLException if the footnoteLink is already part of the
     * XBRL instance.
     */
    public void addFootnoteLink(ExtendedLink footnoteLink) throws XBRLException;    
    
    /**
     * Remove a footnoteLink from the XBRL instance.
     * @param footnoteLink the footnoteLink to remove from the XBRL instance
     * @throws XBRLException if the footnoteLink is not a part of the 
     * XBRL instance.
     */
    public void removeFootnoteLink(ExtendedLink footnoteLink) throws XBRLException;    
    
    /**
     * Get the list of facts that are children of the instance.
     * Facts that are within tuples are not included in this list.
     * @return the list of facts that are children of the instance.
     * @throws XBRLException
     */
    public FragmentList<Fact> getFacts() throws XBRLException;    

    /**
     * Add a fact to the XBRL instance
     * @param fact the fact to add to the XBRL instance
     * @throws XBRLException if the fact is already part of the
     * XBRL instance.
     */
    public void addFact(Fact fact) throws XBRLException;    
    
    /**
     * Remove a fact from the XBRL instance.
     * @param fact the fact to remove from the XBRL instance
     * @throws XBRLException if the fact is not a part of the 
     * XBRL instance.
     */
    public void removeFact(Fact fact) throws XBRLException;    
    
}
