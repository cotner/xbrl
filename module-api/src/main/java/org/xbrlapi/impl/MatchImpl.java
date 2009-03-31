package org.xbrlapi.impl;

/**
 * An implementation of the match XML resource.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xbrlapi.Match;
import org.xbrlapi.utilities.XBRLException;

/**
 * Note that the first match child element of the metadata
 * is the URI of the matching document that is actually stored
 * in the data store.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class MatchImpl extends XMLImpl implements Match {
	
	/**
	 * No argument constructor.
	 * @throws XBRLException
	 */
	public MatchImpl() throws XBRLException {
		super();
	}
	
	/**
	 * @param id The unique id of the XML resource being created,
	 * within the scope of the containing data store.
	 * @throws XBRLException
	 */
	public MatchImpl(String id) throws XBRLException {
		this();
		this.setIndex(id);
	}
	
	/**
	 * @see Match#setResourceURI(URI)
	 */
	public void setResourceURI(URI uri) throws XBRLException {
	    if (uri == null) {
	        throw new XBRLException("The URI must not be null.");
	    }
        HashMap<String,String> attr = new HashMap<String,String>();
        attr.put("value",uri.toString());
        this.appendMetadataElement("match",attr);
	}
	
	/**
	 * @see Match#getMatch()
	 */
	public URI getMatch() throws XBRLException {
	    Node node = this.getMetadataRootElement().getFirstChild();
	    while (node != null) {
	        if (node.getNodeType() == Node.ELEMENT_NODE && node.getLocalName().equals("match")) {
	            try {
	                URI result = new URI(((Element) node).getAttribute("value"));
	                logger.info(((Element) node).getAttribute("value"));
	                return result;
	            } catch (URISyntaxException e) {
	                throw new XBRLException("The matching URI is not valid.",e);
	            }
	        }
	        node = node.getNextSibling();
	    }
        return null;

	}
	
	/**
	 * @see Match#deleteURI(URI)
	 */
	public void deleteURI(URI uri) throws XBRLException {
	    
        Node node = this.getMetadataRootElement().getFirstChild();
        SEARCH: while (node != null) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                    if (uri.toString().equals(((Element) node).getAttribute("value"))) {
                        this.getMetadataRootElement().removeChild(node);
                        break SEARCH; 
                    }
            }
            node = node.getNextSibling();
        }
        
        if (this.getMatch() == null) getStore().remove(this);
        
	}
	
}
