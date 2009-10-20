package org.xbrlapi.aspects;

import org.xbrlapi.Fragment;
import org.xbrlapi.impl.ConceptImpl;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class ConceptAspectValue extends BaseAspectValue {

    public ConceptAspectValue(Aspect aspect, Fragment fragment)
            throws XBRLException {
        super(aspect, fragment);
        if (! fragment.isa(ConceptImpl.class)) {
            throw new XBRLException("Fragment does not match the type of the aspect value.");
        }
    }

}
