package org.xbrlapi.xlink.handler;

import org.apache.log4j.Logger;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.xlink.CustomLinkRecogniser;
import org.xbrlapi.xlink.XLinkException;
import org.xml.sax.Attributes;

/**
 * This class provides an example custom link
 * recogniser tailored to the needs of the non-xlink
 * links used by XBRL.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class XBRLCustomLinkRecogniserImpl extends CustomLinkRecogniser {

    protected static Logger logger = Logger.getLogger(XBRLCustomLinkRecogniserImpl.class);

	/**
	 * Constructor for the XBRL custom link recogniser
	 */
	public XBRLCustomLinkRecogniserImpl() {
		super();
	}

	/**
	 * @see org.xbrlapi.xlink.CustomLinkRecogniser#isLink(String, String, String, Attributes)
	 */
	public boolean isLink(String namespaceURI, String lName, String qName, Attributes attrs) {
	    boolean result = false;
		if (namespaceURI.equals(Constants.XMLSchemaNamespace))
			if (lName.equals("import") || lName.equals("include"))
				result = true;
        logger.debug("Testing " + lName + " for custom link status: " + result);
        return result;
	}

	/**
	 * Get the href from the custom link
	 */
	public String getHref(
			String namespaceURI, 
			String lName, 
			String qName,
			Attributes attrs) 
	throws XLinkException {
		return attrs.getValue("schemaLocation");
	}

}
