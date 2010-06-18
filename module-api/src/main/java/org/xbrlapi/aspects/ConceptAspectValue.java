package org.xbrlapi.aspects;

import org.xbrlapi.Concept;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class ConceptAspectValue extends BaseAspectValue {

    /**
     * The serial version UID.
     * @see 
     * http://java.sun.com/javase/6/docs/platform/serialization/spec/version.html#6678
     * for information about what changes will require the serial version UID to be
     * modified.
     */
    private static final long serialVersionUID = -7073798258124819286L;

    public ConceptAspectValue(Aspect aspect, Concept concept) throws XBRLException {
        super(aspect, concept);
    }

}
