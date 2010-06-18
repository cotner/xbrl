package org.xbrlapi.aspects;

import org.xbrlapi.Entity;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class EntityIdentifierAspectValue extends BaseAspectValue {

    /**
     * 
     */
    private static final long serialVersionUID = 1629296721564235815L;

    public EntityIdentifierAspectValue(Aspect aspect, Entity entity)
            throws XBRLException {
        super(aspect, entity);
    }

}
