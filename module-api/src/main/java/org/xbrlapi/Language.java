package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Language extends Fragment {

	/**
	 * @return the name of a language that the name of the language is
	 * expressed in. 
	 * @throws XBRLException.
	 */
	public String getEncoding() throws XBRLException;
	
	/**
	 * @return the language code that a name is being supplied for. 
	 * @throws XBRLException.
	 */
	public String getCode() throws XBRLException;
	
	/**
	 * @return the name of the language that corresponds to the language code, expressed
	 * in the language corresponding to the getLanguage result.
	 * @throws XBRLException.
	 */
	public String getName() throws XBRLException;
	
}
