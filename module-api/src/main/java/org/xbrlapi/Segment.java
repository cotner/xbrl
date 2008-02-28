package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;

/**
 * TODO Extend the Segment and scenario interfaces.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Segment extends OpenContextComponent {
    
    /**
     * Gets the containing entity
     * @return the entity that contains this segment
     * @throws XBRLException
     */
    public Entity getEntity() throws XBRLException;

}
