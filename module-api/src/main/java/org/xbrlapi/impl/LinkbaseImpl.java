package org.xbrlapi.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.xml.namespace.QName;

import org.xbrlapi.ExtendedLink;
import org.xbrlapi.Fragment;
import org.xbrlapi.Linkbase;
import org.xbrlapi.SimpleLink;
import org.xbrlapi.XlinkDocumentation;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class LinkbaseImpl extends FragmentImpl implements Linkbase  {

    /**
     * 
     */
    private static final long serialVersionUID = -5344292511314590729L;



    /**
     * Retrieve an arcroleRef from a linkbase.
     * @param uri The arcroleRef URI
	 * @return the required arcroleRef 
     * @throws XBRLException if the arcroleRef is not part of the linkbase
     * @see org.xbrlapi.Linkbase#getArcroleRef(String)
     */
    public SimpleLink getArcroleRef(String uri) throws XBRLException {
    	List<SimpleLink> refs = getArcroleRefs();
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
    public List<SimpleLink> getArcroleRefs() throws XBRLException {
        String query = "for $root in #roots#[@type='" + SimpleLinkImpl.class.getName() + "' and @parentIndex='" + this.getIndex() + "'] where $root/xbrlapi:data/link:arcroleRef return $root";
        return this.getStore().<SimpleLink>queryForXMLResources(query);
    }

    /**
     * Retrieve an roleRef from a linkbase.
     * @param uri The roleRef URI
     * @throws XBRLException if the roleRef is not part of the linkbase
     * @see org.xbrlapi.Linkbase#getRoleRef(String)
     */
    public SimpleLink getRoleRef(String uri) throws XBRLException {
    	List<SimpleLink> refs = getRoleRefs();
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
    public List<SimpleLink> getRoleRefs() throws XBRLException {

        String query = "for $root in #roots#[@type='" + SimpleLinkImpl.class.getName() + "' and @parentIndex='" + this.getIndex() + "'] where $root/xbrlapi:data/link:roleRef return $root";
        return this.getStore().<SimpleLink>queryForXMLResources(query);

    }



    /**
     * @see org.xbrlapi.Linkbase#getExtendedLinks()
     */
    public List<ExtendedLink> getExtendedLinks() throws XBRLException {
    	String xpath = "for $root in #roots#[@parentIndex='" + getIndex() + "'] let $data := $root/xbrlapi:data/* where $data/@xlink:type='extended' order by namespace-uri($data), local-name($data), $data/@xlink:role return $root";
    	List<ExtendedLink> fragments = getStore().<ExtendedLink>queryForXMLResources(xpath);
    	return fragments;
    }
    
    /**
     * @see org.xbrlapi.Linkbase#getExtendedLinks(QName)
     */
    public List<ExtendedLink> getExtendedLinks(QName qname) throws XBRLException {
        List<ExtendedLink> fragments = getExtendedLinks();
        List<ExtendedLink> result = new Vector<ExtendedLink>();
        for (ExtendedLink link: fragments) {
            if (link.getNamespace().toString().equals(qname.getNamespaceURI()))
                if (link.getLocalname().equals(qname.getLocalPart())) {
                    result.add(link);
                }
        }
            
        return result;
    }    

    /**
     * Get the list of documentation fragments that are children of the linkbase.
     * Returns the list of documentation fragments in the linkbase.
     * @throws XBRLException
     * @see org.xbrlapi.Linkbase#getDocumentations()
     */
    public List<XlinkDocumentation> getDocumentations() throws XBRLException {
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
     * @see Linkbase#getExtendedLinkQNames()
     */
    public Set<QName> getExtendedLinkQNames() throws XBRLException {
        Set<QName> result = new HashSet<QName>();
        for (ExtendedLink link: this.getExtendedLinks()) {
            QName qname = new QName(link.getNamespace().toString(), link.getLocalname());
            result.add(qname);
        }
        return result;
    }
    
}
