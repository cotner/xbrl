package org.xbrlapi;

import java.net.URI;

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
     * @param namespace The namespace in which the reference part has been defined
     * @param localname The local name of the reference part
     * @throws XBRLException
     */
    public ReferencePart getReferencePart(URI namespace, String localname) throws XBRLException;
    

    


}