package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;


/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface XlinkDocumentation extends Fragment {

	/**
	 * Get the value of the documentation fragment.
	 * @return the text content of the documentation with leading and trailing spaces trimmed.
	 * @throws XBRLException.
	 */
	public String getValue() throws XBRLException;
	
}
