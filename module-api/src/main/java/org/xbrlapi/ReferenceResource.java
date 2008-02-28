package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface ReferenceResource extends MixedContentResource {

    /**
     * Get a list of reference part fragments.
     * @return the list of reference part fragments that are children of the reference.
     * @throws XBRLException
     */
    public FragmentList<ReferencePart> getReferenceParts() throws XBRLException;

    /**
     * Get a specific reference part from a reference.
     * Returns null if no such reference part exists.
     * 
     * @param namespaceURI The namespace in which the reference part has been defined
     * @param localName The local name of the reference part
     * @throws XBRLException
     */
    public ReferencePart getReferencePart(String namespaceURI, String localName) throws XBRLException;
    
    /**
     * Add a reference part to a reference.  The reference part 
     * is added as the last reference part in document order, within
     * the reference.
     *
     * @param referencePart The reference part to be inserted into the reference text node or XHTML fragment root node
     * @throws XBRLException
     */
    public void addReferencePart(ReferencePart referencePart) throws XBRLException;
    
    /**
     * Remove a reference part
     *
     * @param referencePart The reference part to remove
     * @throws XBRLException
     */
    public void removeReferencePart(ReferencePart referencePart) throws XBRLException;

}