package org.xbrlapi.impl;

/**
 * An implementation of the match XML resource.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xbrlapi.Match;
import org.xbrlapi.builder.BuilderImpl;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * Note that the first match child element of the metadata
 * is the URI of the matching document that is actually stored
 * in the data store.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class MatchImpl extends NonFragmentXMLImpl implements Match {
	
	/**
     * 
     */
    private static final long serialVersionUID = 3738588761407998990L;

    /**
	 * No argument constructor.
	 * @throws XBRLException
	 */
	public MatchImpl() throws XBRLException {
		super();
        setBuilder(new BuilderImpl());
	}
	
	/**
	 * @param id The unique id of the XML resource being created,
	 * within the scope of the containing data store.
	 * @throws XBRLException
	 */
	public MatchImpl(String id) throws XBRLException {
		this();
		this.setIndex(id);
		this.finalizeBuilder();
	}
	
	/**
	 * @see Match#addMatchedURI(URI)
	 */
	public void addMatchedURI(URI uri) throws XBRLException {
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
	                logger.debug(((Element) node).getAttribute("value"));
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
     * @see Match#getURIs()
     */
    public List<URI> getURIs() throws XBRLException {
        List<URI> result = new Vector<URI>();
        NodeList nodes = this.getMetadataRootElement().getElementsByTagNameNS(Constants.XBRLAPINamespace.toString(),"match");
        for (int i=0; i<nodes.getLength(); i++) {
            Element match = (Element) nodes.item(i);
            try {
                result.add(new URI(match.getAttribute("value")));
            } catch (URISyntaxException e) {
                throw new XBRLException("The URI is not valid.",e);
            }
        }
        return result;

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
