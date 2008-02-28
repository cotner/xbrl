package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Linkbase extends Fragment {

    /**
     * Adds an arcroleRef to a linkbase.
     *
     * @param arcroleRef The arcroleRef to be added to the linkbase.
     * @throws XBRLException
     */
    public void addArcroleRef(SimpleLink arcroleRef) throws XBRLException;
    
    /**
     * Remove a arcroleRef from a linkbase.
     *
     * @param arcroleRef The arcroleRef to be removed.
     * @throws XBRLException if the arcroleRef is not contained in the linkbase
     */
    public void removeArcroleRef(SimpleLink arcroleRef) throws XBRLException;

    /**
     * Retrieve an arcroleRef from a linkbase.
     *
     * @param uri The arcroleRef URI
	 * @return the required arcroleRef 
     * @throws XBRLException if the arcroleRef is not part of the linkbase
     */
    public SimpleLink getArcroleRef(String uri) throws XBRLException;

    /**
     * Gets the list of arcroleRefs in a linkbase.
     * @return list of arcroleRef fragments that are children of the linkbase.
     * The list can be empty.
     * @throws XBRLException
     */
    public FragmentList<SimpleLink> getArcroleRefs() throws XBRLException;

    /**
     * Adds an roleRef to a linkbase.
     *
     * @param roleRef The roleRef to be added to the linkbase.
     * @throws XBRLException
     */
    public void addRoleRef(SimpleLink roleRef) throws XBRLException;
    
    /**
     * Remove a roleRef from a linkbase.
     *
     * @param roleRef The roleRef to be removed.
     * @throws XBRLException if the roleRef is not contained in the linkbase.
     */
    public void removeRoleRef(SimpleLink roleRef) throws XBRLException;

    /**
     * Retrieve an roleRef from a linkbase.
     *
     * @param uri The roleRef URI
     * @return the required roleRef
     * @throws XBRLException if the roleRef is not part of the linkbase
     */
    public SimpleLink getRoleRef(String uri) throws XBRLException;

    /**
     * Gets the list of roleRefs in a linkbase.
     * @return list of roleRef fragments that are children of the linkbase.
     * The list can be empty.
     * @throws XBRLException
     */
    public FragmentList<SimpleLink> getRoleRefs() throws XBRLException;

    /**
     * Adds an extended link to a linkbase.
     *
     * @param link The extended link to add
     * @throws XBRLException
     */
    public void addExtendedLink(SimpleLink link) throws XBRLException;
    
    /**
     * Remove a roleRef from a linkbase.
     *
     * @param link The extended link to remove
     * @throws XBRLException if the extended link  is not contained 
	 * in the linkbase.
     */
    public void removeExtendedLink(SimpleLink link) throws XBRLException;

    /**
     * Gets the list of extended links in a linkbase
     * @return the list of extended links in the linkbase or null if there are none.
     * @throws XBRLException
     */
    public FragmentList<ExtendedLink> getExtendedLinks() throws XBRLException;

    /**
     * Get the list of documentation fragments that are children of the linkbase.
     * Returns the list of documentation fragments in the linkbase.
     * @throws XBRLException
     */
    public FragmentList<XlinkDocumentation> getDocumentations() throws XBRLException;    
    
    /**
     * Remove a link documentation child fragment from the linkbase.
     * @param documentation The documentation fragment to be removed.
     * @throws XBRLException
     */
    public void removeDocumentation(Fragment documentation) throws XBRLException;

    /**
     * Add a link documentation fragment to the end of the linkbase.
     * @param documentation documentation to be added to the end of linkbase.
     * @throws XBRLException
     */
    public void addDocumentation(Fragment documentation) throws XBRLException;    
    
}
