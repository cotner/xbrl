package org.xbrlapi.impl;

import org.xbrlapi.FragmentList;
import org.xbrlapi.ReferencePart;
import org.xbrlapi.ReferenceResource;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ReferenceResourceImpl extends MixedContentResourceImpl implements ReferenceResource {
	
    /**
     * Get a list of reference part fragments.
     * @return the list of reference part fragments that are children of the reference.
     * @throws XBRLException
     * @see org.xbrlapi.ReferenceResource#getReferenceParts()
     */
    public FragmentList<ReferencePart> getReferenceParts() throws XBRLException {
    	return this.getChildren("org.xbrlapi.impl.ReferencePartImpl");
    }

    /**
     * Get a specific reference part from a reference.
     * TODO How to handle a referencePart used more than once in a reference?
     * @param namespaceURI The namespace in which the reference part has been defined
     * @param localname The local name of the reference part
     * @return the first matching reference part or null if no such reference part exists.
     * @throws XBRLException
     * @see org.xbrlapi.ReferenceResource#getReferencePart(String,String)
     */
    public ReferencePart getReferencePart(String namespaceURI, String localname) throws XBRLException {
    	FragmentList<ReferencePart> candidates = getReferenceParts();
    	for (int i=0; i<candidates.getLength(); i++) {
    		ReferencePart part = candidates.getFragment(i);
    		if (part.getNamespaceURI().equals(namespaceURI) & part.getLocalname().equals(localname)) {
    			return part;
    		}
    	}
    	return null;
    }
    

    

	
}
