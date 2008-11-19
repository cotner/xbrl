package org.xbrlapi.aspects;

import org.xbrlapi.utilities.XBRLException;


/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public interface AspectValueTransformer {

    /**
     * @param value The aspect value to check to see if it will work
     * with this transformer.
     * @throws XBRLException if the aspect value can be identified as 
     * being incompatible with this transformer.
     */
    public void validate(AspectValue value) throws XBRLException;    
    
    /**
     * @param value The aspect value to process.
     * @return the string value of this aspect value to a string
     * that uniquely identifies the semantic value of the aspect (
     * as opposed to the object that is the aspect value itself).
     * @throws XBRLException if the transformation fails.
     */
    public String getIdentifier(AspectValue value) throws XBRLException;
    
    /**
     * @param value The aspect value to process.
     * @return the human readable version of the aspect value.
     * @throws XBRLException if the transformation fails.
     */
    public String getLabel(AspectValue value) throws XBRLException;

}
