package org.xbrlapi;

import java.util.List;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Entity extends ContextComponent {

    /**
     * Get the entity identifier scheme.
     *
     * @return the entity identifier scheme URI 
     * @throws XBRLException
     */
    public String getIdentifierScheme() throws XBRLException;
    


    /**
     * Get the entity identifier
     *
     * @return the string corresponding to the entity from
     * among the full set of valid entity identifiers in the
     * nominated scheme.
     * @throws XBRLException
     */
    public String getIdentifierValue() throws XBRLException;
    

    
    /**
     * Get the segment of the entity
     *
     * @return the segment information for the entity
     * or null if the entity does not include segment information.
     * @throws XBRLException
     */
    public Segment getSegment() throws XBRLException;
    
    /**
     * @return a list of all the entity resources with the entity scheme and value 
     * of this entity fragment. The list is empty if there are no entity resources
     * that match the relevant criteria.
     * @throws XBRLException
     */
    public List<EntityResource> getEntityResources() throws XBRLException;
    
    /**
     * @return the list of labels for the entity identified by this fragment,
     * NOT taking into account the labels for the given identifier and the labels
     * for any equivalent identifiers.
     * @throws XBRLException
     */
    public List<LabelResource> getEntityLabels() throws XBRLException;
    
    /**
     * @return the list of labels for the entity identified by this fragment,
     * taking into account the labels for the given identifier and the labels
     * for any equivalent identifiers.
     * @throws XBRLException
     */
    public List<LabelResource> getAllEntityLabels() throws XBRLException;   
    
}
