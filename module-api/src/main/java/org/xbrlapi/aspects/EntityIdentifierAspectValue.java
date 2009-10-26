package org.xbrlapi.aspects;

import org.xbrlapi.Entity;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class EntityIdentifierAspectValue extends BaseAspectValue {

    public EntityIdentifierAspectValue(Aspect aspect, Entity entity)
            throws XBRLException {
        super(aspect, entity);
    }

}
