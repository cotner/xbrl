package org.xbrlapi.aspects;

import org.xbrlapi.utilities.XBRLException;


/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public interface AspectValueTransformer {    
    
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
     * @return the human readable version of the aspect value or null if
     * the aspect value is a missing aspect value (i.e. the aspect value 
     * fragment is null).
     * @throws XBRLException if the transformation fails.
     */
    public String getLabel(AspectValue value) throws XBRLException;

    /**
     * Empties the map of entity identifiers so that there are no
     * aspect values pointing to identifiers anymore.  This leaves the
     * map from identifiers to labels intact though.
     */
    public void clearIdentifiers();
    
}
