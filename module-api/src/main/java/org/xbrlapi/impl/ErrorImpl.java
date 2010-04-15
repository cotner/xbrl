package org.xbrlapi.impl;

/**
 * An XML resource in the database used to 
 * store information about errors discovered or encountered
 * while loading or processing data in the store.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import java.net.URI;
import java.net.URISyntaxException;

import org.xbrlapi.Error;
import org.xbrlapi.builder.BuilderImpl;
import org.xbrlapi.utilities.XBRLException;

public class ErrorImpl extends XMLImpl implements Error {
	
	/**
     * 
     */
    private static final long serialVersionUID = 2965459874023212117L;

    /**
	 * No argument constructor.
	 * @throws XBRLException
	 */
	public ErrorImpl() throws XBRLException {
		super();
        setBuilder(new BuilderImpl());
	}
	
	/**
	 * @param id The unique id of the error being created.
	 * within the scope of the containing data store.
	 * @throws XBRLException
	 */
	public ErrorImpl(String id) throws XBRLException {
		this();
		this.setIndex(id);
	}

	/**
	 * Use this constructor if the problem is not related to a specific
	 * fragment.
     * @param id The unique id of the error being created.
	 * @param document The URI of the affected document.
	 * @param explanation The explanation of the problem.
	 * @throws XBRLException
	 */
    public ErrorImpl(String id, URI document, String explanation) throws XBRLException {
        this(id);
        this.setExplanation(explanation);
        this.setResourceURI(document);
    }
	
    /**
     * @param id The unique id of the error being created.
     * @param document The URI of the affected document.
     * @param index The index of the fragment causing the problem.
     * @param explanation The explanation of the problem.
     * @throws XBRLException
     */
    public ErrorImpl(String id,URI document, String index, String explanation) throws XBRLException {
        this(id,document,explanation);
        this.setProblemIndex(index);
    }
    
    /**
     * @see org.xbrlapi.Error#getProblemIndex()
     */
    public String getProblemIndex() throws XBRLException {
        return this.getMetaAttribute("problemIndex");
    }

    /**
     * @see org.xbrlapi.Error#setProblemIndex(String)
     */
    public void setProblemIndex(String index) throws XBRLException {
        if (index == null) this.removeMetaAttribute("problemIndex");
        else this.setMetaAttribute("problemIndex",index);
    }
	
    /**
     * @see org.xbrlapi.Error#getProblemURI()
     */
    public URI getProblemURI() throws XBRLException {
        String uri = "";
        try {
            uri  = this.getMetaAttribute("problemURI");
            return new URI(uri);
        } catch (URISyntaxException e) {
            throw new XBRLException(" URI: " + uri + " has incorrect syntax .", e);
        }
    }
    
    /**
     * @see org.xbrlapi.Error#setResourceURI(URI)
     */
    public void setResourceURI(URI uri) throws XBRLException {
        if (uri == null) throw new XBRLException("The URI of the document causing the problem must not be null.");
        this.setMetaAttribute("problemURI",uri.toString());
    }
    
    /**
     * @see org.xbrlapi.Error#setExplanation(String)
     */
    public void setExplanation(String problem) throws XBRLException {
        if (problem == null) throw new XBRLException("The explanation must not be null.");
        this.setMetaAttribute("explanation",problem);
    }    
	
    /**
     * @see org.xbrlapi.Error#getExplanation()
     */
    public String getExplanation() throws XBRLException {
        return this.getMetaAttribute("explanation"); 
    }

}
