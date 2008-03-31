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
    
    

      

}
