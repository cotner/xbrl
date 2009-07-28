package org.xbrlapi.impl;


import org.xbrlapi.NonFragmentXML;
import org.xbrlapi.utilities.XBRLException;

/**
 * An implementation of the non-fragment XML interface.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class NonFragmentXMLImpl extends XMLImpl implements NonFragmentXML {
	
    /**
     * Used to eliminate the builder once the XML resource has been constructed.
     * Call this method at the end of constructors for XML resources that 
     * extend this class.
     * @throws XBRLException
     */
    protected void finalizeBuilder() throws XBRLException {
        this.setResource(getBuilder().getMetadata());
        this.setBuilder(null);
    }
    
}
