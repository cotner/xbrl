package org.xbrlapi;

import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Linkbase extends Fragment {

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
    public List<SimpleLink> getArcroleRefs() throws XBRLException;

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
    public List<SimpleLink> getRoleRefs() throws XBRLException;

    /**
     * Gets the list of extended links in a linkbase
     * @return the list of extended links in the linkbase.
     * The extended links are ordered by their namespace and then local name.
     * @throws XBRLException
     */
    public List<ExtendedLink> getExtendedLinks() throws XBRLException;
    
    /**
     * @qname the qname of the element that is one of the required extended links.
     * @return the list of extended links in the linkbase that match the specified criteria.
     * @throws XBRLException
     */
    public List<ExtendedLink> getExtendedLinks(QName qname) throws XBRLException;    

    /**
     * Get the list of documentation fragments that are children of the linkbase.
     * Returns the list of documentation fragments in the linkbase.
     * @throws XBRLException
     */
    public List<XlinkDocumentation> getDocumentations() throws XBRLException;    
    
    /**
     * Remove a link documentation child fragment from the linkbase.
     * @param documentation The documentation fragment to be removed.
     * @throws XBRLException
     */
    public void removeDocumentation(Fragment documentation) throws XBRLException;

    
    
    /**
     * @return the set of different extended link QNames for the extended 
     * links contained in this linkbase.
     * @throws XBRLException
     */
    Set<QName> getExtendedLinkQNames() throws XBRLException;
    
}
