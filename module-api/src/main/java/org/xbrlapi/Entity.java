package org.xbrlapi;

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
     * Set the scheme for the entity identifier.
     * Throws an exception if the scheme is not
     * a valid URI.
     *
     * @param scheme the scheme for the entity identifier
     * @throws XBRLException 
     */
    public void setIdentifierScheme(String scheme) throws XBRLException;

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
     * Set the entity identifier
     *
     * @param identifierValue the local value identifying the entity 
     * within the specified scheme
     * @throws XBRLException 
     */
    public void setIdentifierValue(String identifierValue) throws XBRLException;
    
    /**
     * Get the segment of the entity
     *
     * @return the segment information for the entity
     * or null if the entity does not include segment information.
     * @throws XBRLException
     */
    public Segment getSegment() throws XBRLException;
    
    /**
     * Set the segment in the entity
     *
     * @param segment the segment to be added
     * @throws XBRLException 
     */
    public void setSegment(Segment segment) throws XBRLException;    

    /**
     * Remove the segment from the entity.
     *
     * @throws XBRLException 
     */
    public void removeSegment() throws XBRLException;      

}
