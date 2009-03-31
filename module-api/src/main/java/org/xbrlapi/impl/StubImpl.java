package org.xbrlapi.impl;

/**
 * A stub XML resource in the database used to 
 * store information about documents that have not
 * loaded yet or correctly.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import java.net.URI;
import java.net.URISyntaxException;

import org.xbrlapi.Stub;
import org.xbrlapi.builder.BuilderImpl;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

public class StubImpl extends XMLImpl implements Stub {
	
	/**
	 * No argument constructor.
	 * @throws XBRLException
	 */
	public StubImpl() throws XBRLException {
		super();
		this.setBuilder(new BuilderImpl());
		getBuilder().appendElement(
		        Constants.XBRLAPINamespace,"fragment",
		        Constants.XBRLAPIPrefix + ":fragment");	
	}
	
	/**
	 * @param id The unique id of the fragment being created,
	 * within the scope of the containing data store.
	 * @throws XBRLException
	 */
	public StubImpl(String id) throws XBRLException {
		this();
		this.setIndex(id);
	}

    /**
     * @see org.xbrlapi.Stub#getReason()
     */
    public String getReason() throws XBRLException {
        return this.getMetaAttribute("reason"); 
    }

    /**
     * @see org.xbrlapi.Stub#getResourceURI()
     */
    public URI getResourceURI() throws XBRLException {
        String uri = "";
        try {
            uri  = this.getMetaAttribute("resourceURI");
            return new URI(uri);
        } catch (URISyntaxException e) {
            throw new XBRLException(" URI: " + uri + " has incorrect syntax .", e);
        }
    }
    
    /**
     * @see org.xbrlapi.Stub#setResourceURI(URI)
     */
    public void setResourceURI(URI uri) throws XBRLException {
        if (uri == null) throw new XBRLException("The stub URI must not be null.");
        this.setMetaAttribute("resourceURI",uri.toString());
    }
    
    /**
     * @see org.xbrlapi.Stub#setReason()
     */
    public void setReason(String reason) throws XBRLException {
        if (reason == null) throw new XBRLException("The reason must not be null.");
        this.setMetaAttribute("reason",reason);
    }    
	
	
	
}
