package org.xbrlapi.aspects;

import org.xbrlapi.utilities.XBRLException;


/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public interface AspectValueTransformer {

    /**
     * @return the string value of this aspect value to a string
     * that uniquely indicates the value of the aspect in a human 
     * readable form.
     * @throws XBRLException if the transformation fails.
     */
    public String transform(AspectValue value) throws XBRLException;

}
