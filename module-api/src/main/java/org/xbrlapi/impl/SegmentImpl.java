package org.xbrlapi.impl;

import org.xbrlapi.Entity;
import org.xbrlapi.Segment;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class SegmentImpl extends OpenContextComponentImpl implements Segment {

    /**
     * 
     */
    private static final long serialVersionUID = 2635173824228869410L;

    /**
     * Gets the containing entity
     *
     * @return the entity that contains this segment
     * @throws XBRLException
     * @see org.xbrlapi.Segment#getEntity()
     */
    public Entity getEntity() throws XBRLException {
    	return (Entity) this.getAncestorOrSelf("org.xbrlapi.impl.EntityImpl");
    }
	
}