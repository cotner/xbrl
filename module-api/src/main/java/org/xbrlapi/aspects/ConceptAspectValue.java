package org.xbrlapi.aspects;

import org.xbrlapi.Concept;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class ConceptAspectValue extends BaseAspectValue {

    public ConceptAspectValue(Aspect aspect, Concept concept) throws XBRLException {
        super(aspect, concept);
    }

}
