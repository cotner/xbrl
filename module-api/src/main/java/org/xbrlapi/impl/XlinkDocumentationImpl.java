package org.xbrlapi.impl;

import org.xbrlapi.XlinkDocumentation;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class XlinkDocumentationImpl extends FragmentImpl implements XlinkDocumentation {
	
	/**
     * 
     */
    private static final long serialVersionUID = -8362102502686134852L;

    /**
	 * TODO Provide a set value method for Xlink documentation fragments.
	 * Get the value of the documentation fragment.
	 * @return the text content of the documentation with leading and trailing spaces trimmed.
	 * @throws XBRLException.
	 * @see org.xbrlapi.XlinkDocumentation#getValue()
	 */
	public String getValue() throws XBRLException {
		return this.getDataRootElement().getTextContent().trim();
	}

}
