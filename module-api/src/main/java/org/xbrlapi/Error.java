package org.xbrlapi;

import java.net.URI;

import org.xbrlapi.utilities.XBRLException;

public interface Error extends XML {

    /**
     * @return the index of the problematic fragment.
     * @throws XBRLException if the URI syntax is incorrect.
     */
    public String getProblemIndex() throws XBRLException;
    
    /**
     * The index can be null if the problem cannot be sheeted back
     * to a specific fragment.
     * @param index The index of the fragment causing the problem.
     */
    public void setProblemIndex(String index) throws XBRLException;    
    
    /**
     * @return the URI of the affected document.
     * @throws XBRLException if the URI syntax is incorrect.
     */
    public URI getProblemURI() throws XBRLException;
    
    /**
     * @param uri the URI of the document causing the problem.
     * @throws XBRLException if the URI is null
     */
    public void setResourceURI(URI uri) throws XBRLException;    
    
    /**
     * @return the problem explanation.
     * @throws XBRLException
     */
    public String getExplanation() throws XBRLException;
    
    /**
     * @param explanation The problem explanation.
     * @throws XBRLException if the explanation is null
     */
    public void setExplanation(String explanation) throws XBRLException;        
}
