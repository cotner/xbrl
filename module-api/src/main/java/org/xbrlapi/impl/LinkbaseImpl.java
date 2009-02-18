package org.xbrlapi.impl;

import org.xbrlapi.ExtendedLink;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.Linkbase;
import org.xbrlapi.SimpleLink;
import org.xbrlapi.XlinkDocumentation;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class LinkbaseImpl extends FragmentImpl implements Linkbase  {

    /**
     * Adds an arcroleRef to a linkbase.
     * @param arcroleRef The arcroleRef to be added to the linkbase.
     * @throws XBRLException
     * @see org.xbrlapi.Linkbase#addArcroleRef(SimpleLink)
     */
    public void addArcroleRef(SimpleLink arcroleRef) throws XBRLException {
    	throw new XBRLException("Data update methods are not yet implemented.");
    }
    
    /**
     * Remove a arcroleRef from a linkbase.
     * @param arcroleRef The arcroleRef to be removed.
     * @throws XBRLException if the arcroleRef is not contained in the linkbase
     * @see org.xbrlapi.Linkbase#removeArcroleRef(SimpleLink)
     */
    public void removeArcroleRef(SimpleLink arcroleRef) throws XBRLException {
    	throw new XBRLException("Data update methods are not yet implemented.");
    }

    /**
     * Retrieve an arcroleRef from a linkbase.
     * @param uri The arcroleRef URI
	 * @return the required arcroleRef 
     * @throws XBRLException if the arcroleRef is not part of the linkbase
     * @see org.xbrlapi.Linkbase#getArcroleRef(String)
     */
    public SimpleLink getArcroleRef(String uri) throws XBRLException {
    	FragmentList<SimpleLink> refs = getArcroleRefs();
    	for (SimpleLink ref: refs) {
    		if (ref.getDataRootElement().getAttribute("arcroleURI").equals(uri))
    			return ref;
    	}
    	throw new XBRLException("The required arcroleRef is not part of the linkbase.");
    }
	
    /**
     * Gets the list of arcroleRefs in a linkbase.
     * @return list of arcroleRef fragments that are children of the linkbase.
     * The list can be empty.
     * @throws XBRLException
     * @see org.xbrlapi.Linkbase#getArcroleRefs()
     */
    public FragmentList<SimpleLink> getArcroleRefs() throws XBRLException {
    	FragmentList<SimpleLink> links = this.<SimpleLink>getChildren("org.xbrlapi.impl.SimpleLinkImpl");
    	for (SimpleLink link: links) {
            if (! (link.getNamespace().equals(Constants.XBRL21LinkNamespace) && link.getLocalname().equals("arcroleRef")))
                links.removeFragment(link);
    	}
    	return links;
    }

    /**
     * Adds an roleRef to a linkbase.
     * @param roleRef The roleRef to be added to the linkbase.
     * @throws XBRLException
     * @see org.xbrlapi.Linkbase#addRoleRef(SimpleLink)
     */
    public void addRoleRef(SimpleLink roleRef) throws XBRLException {
    	throw new XBRLException("Data update methods are not yet implemented.");
    }
    
    /**
     * Remove a roleRef from a linkbase.
     * @param roleRef The roleRef to be removed.
     * @throws XBRLException if the roleRef is not contained in the linkbase.
     * @see org.xbrlapi.Linkbase#removeRoleRef(SimpleLink)
     */
    public void removeRoleRef(SimpleLink roleRef) throws XBRLException {
    	throw new XBRLException("Data update methods are not yet implemented.");
    }

    /**
     * Retrieve an roleRef from a linkbase.
     * @param uri The roleRef URI
     * @throws XBRLException if the roleRef is not part of the linkbase
     * @see org.xbrlapi.Linkbase#getRoleRef(String)
     */
    public SimpleLink getRoleRef(String uri) throws XBRLException {
    	FragmentList<SimpleLink> refs = getRoleRefs();
    	for (SimpleLink ref: refs) {
    		if (ref.getDataRootElement().getAttribute("roleURI").equals(uri))
    			return ref;
    	}
    	throw new XBRLException("The required roleRef is not part of the linkbase.");

    }

    /**
     * Gets the list of roleRefs in a linkbase.
     * @return list of roleRef fragments that are children of the linkbase.
     * The list can be empty.
     * @throws XBRLException
     * @see org.xbrlapi.Linkbase#getRoleRefs()
     */
    public FragmentList<SimpleLink> getRoleRefs() throws XBRLException {
    	FragmentList<SimpleLink> links = this.<SimpleLink>getChildren("org.xbrlapi.impl.SimpleLinkImpl");
    	for (SimpleLink link: links) {
    		if (! (link.getNamespace().equals(Constants.XBRL21LinkNamespace) && link.getLocalname().equals("roleRef")))
    			links.removeFragment(link);
    	}
    	return links;
    }

    /**
     * Adds an extended link to a linkbase.
     * @param link The extended link to add
     * @throws XBRLException
     * @see org.xbrlapi.Linkbase#addExtendedLink(SimpleLink)
     */
    public void addExtendedLink(SimpleLink link) throws XBRLException {
    	throw new XBRLException("Data update methods are not yet implemented.");  	
    }
    
    /**
     * Remove a roleRef from a linkbase.
     * @param link The extended link to remove
     * @throws XBRLException if the extended link  is not contained 
	 * in the linkbase.
     * @see org.xbrlapi.Linkbase#removeExtendedLink(SimpleLink)
     */
    public void removeExtendedLink(SimpleLink link) throws XBRLException {
    	throw new XBRLException("Data update methods are not yet implemented.");
    }

    /**
     * Gets the list of extended links in a linkbase
     * @return the list of extended links in the linkbase or null if there are none.
     * @throws XBRLException
     * @see org.xbrlapi.Linkbase#getExtendedLinks()
     */
    public FragmentList<ExtendedLink> getExtendedLinks() throws XBRLException {
    	String xpath = "/" + Constants.XBRLAPIPrefix + ":" + "fragment[@parentIndex='" + getIndex() + "' and " + Constants.XBRLAPIPrefix + ":" + "data/*/@xlink:type='extended']";
    	FragmentList<ExtendedLink> fragments = getStore().<ExtendedLink>query(xpath);
    	if (fragments.getLength() == 0) return null;
    	return fragments;
    }

    /**
     * Get the list of documentation fragments that are children of the linkbase.
     * Returns the list of documentation fragments in the linkbase.
     * @throws XBRLException
     * @see org.xbrlapi.Linkbase#getDocumentations()
     */
    public FragmentList<XlinkDocumentation> getDocumentations() throws XBRLException {
    	return this.<XlinkDocumentation>getChildren("org.xbrlapi.impl.XlinkDocumentationImpl");
    }
    
    /**
     * Remove a link documentation child fragment from the linkbase.
     * @param documentation The documentation fragment to be removed.
     * @throws XBRLException
     * @see org.xbrlapi.Linkbase#removeDocumentation(Fragment)
     */
    public void removeDocumentation(Fragment documentation) throws XBRLException {
    	throw new XBRLException("Data update methods are not yet implemented.");
    }
    
    /**
     * Add a link documentation fragment to the end of the linkbase.
     * @param documentation documentation to be added to the end of linkbase.
     * @throws XBRLException
     * @see org.xbrlapi.Linkbase#addDocumentation(Fragment)
     */
    public void addDocumentation(Fragment documentation) throws XBRLException {
    	throw new XBRLException("Data update methods are not yet implemented.");
    }    
    
}
