package org.xbrlapi.impl;

import java.net.URI;

import java.util.List;
import org.xbrlapi.ReferencePart;
import org.xbrlapi.ReferenceResource;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ReferenceResourceImpl extends MixedContentResourceImpl implements ReferenceResource {
	
    /**
     * 
     */
    private static final long serialVersionUID = 2397300422957891874L;

    /**
     * Get a list of reference part fragments.
     * @return the list of reference part fragments that are children of the reference.
     * @throws XBRLException
     * @see org.xbrlapi.ReferenceResource#getReferenceParts()
     */
    public List<ReferencePart> getReferenceParts() throws XBRLException {
    	return this.getChildren("org.xbrlapi.impl.ReferencePartImpl");
    }

    /**
     * Get a specific reference part from a reference.
     * TODO How to handle a referencePart used more than once in a reference?
     * @param namespace The namespace in which the reference part has been defined
     * @param localname The local name of the reference part
     * @return the first matching reference part or null if no such reference part exists.
     * @throws XBRLException
     * @see org.xbrlapi.ReferenceResource#getReferencePart(URI,String)
     */
    public ReferencePart getReferencePart(URI namespace, String localname) throws XBRLException {
    	List<ReferencePart> candidates = getReferenceParts();
    	for (int i=0; i<candidates.size(); i++) {
    		ReferencePart part = candidates.get(i);
    		if (part.getNamespace().equals(namespace) & part.getLocalname().equals(localname)) {
    			return part;
    		}
    	}
    	return null;
    }
	
}
