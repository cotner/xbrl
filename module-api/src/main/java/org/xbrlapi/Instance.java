package org.xbrlapi;

import java.util.List;

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
    public List<SimpleLink> getSchemaRefs() throws XBRLException;    

    
    
    

    /**
     * Get the list of linkbaseRef fragments in the instance
     * Returns null if none are contained by the XBRL instance.
     * @throws XBRLException
     */
    public List<SimpleLink> getLinkbaseRefs() throws XBRLException;    

    
    
    

    /**
     * Get list contexts contained in the instance.
     * Returns null if none are contained by the XBRL instance.
     * @throws XBRLException
     */
    public List<Context> getContexts() throws XBRLException;    

    /**
     * Get a specified context from the instance based on its id.
     * @param id The id of the context fragment
     * @return the context fragment
     * @throws XBRLException if the context does not exist
     */
    public Context getContext(String id) throws XBRLException;

    
    
    

    /**
     * Get the list of units contained in the instance.
     * Returns null if none are contained by the XBRL instance.
     *
     * @throws XBRLException
     */
    public List<Unit> getUnits() throws XBRLException;    

    /**
     * Get a specified unit from the instance based on its id.
     * @param id The id of the unit fragment
     * @return the unit fragment
     * @throws XBRLException if the unit does not exist
     */
    public Unit getUnit(String id) throws XBRLException;
    
    
    
    
    
    /**
     * Get a list of footnote link fragments
     * Returns null if none are contained by the XBRL instance.
     * @throws XBRLException
     */
    public List<ExtendedLink> getFootnoteLinks() throws XBRLException;    

    
    
    
    
    /**
     * Get the list of facts that are children of the instance.
     * Facts that are within tuples are not included in this list.
     * @return the list of facts that are children of the instance.
     * @throws XBRLException
     */
    public List<Fact> getFacts() throws XBRLException;
    
    /**
     * @return the list of all items that are children of the instance.
     * Tuples are not included in the list.
     * @throws XBRLException
     */
    public List<Item> getItems() throws XBRLException;    

    
    
    
    
}
