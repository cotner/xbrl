package org.xbrlapi;

import java.net.URI;
import java.util.List;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Resource extends ArcEnd {

	/**
	 * Get the value of the resource role (xlink:role attribute) for the resource.
	 * @return the value of the resource role (xlink:role attribute) for the resource or 
	 * null if none is specified.
	 * @throws XBRLException.
	 */
	public URI getResourceRole() throws XBRLException;
	
	/**
	 * @return the XML language code for the resource or null
	 * if none is specified.
	 * @throws XBRLException
	 */
    public String getLanguage() throws XBRLException;
    
    /**
     * @param languageNameEncoding The language encoding of the language name, available
     * in the data store in the form of a Language XML resource.
     * @return the name of the language used for the resource or 
     * null if none is specified or no appropriate Language resource 
     * is available in the data store.
     * @throws XBRLException
     */
    public String getLanguageName(String languageNameEncoding) throws XBRLException;
    
    /**
     * @return the name of the language used for the resource or null if none is 
     * specified or none is available. The name is obtained by working through the
     * list of language encodings from first to last, returning the first name that 
     * is available in the data store in the form of a Language XML resource.
     * @throws XBRLException
     */
    public String getLanguageName(List<String> languageNameEncodings) throws XBRLException;    
}
