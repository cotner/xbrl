package org.xbrlapi.xdt;

import org.xbrlapi.utilities.Constants;
import org.xbrlapi.xlink.XLinkException;
import org.xbrlapi.xlink.handler.XBRLCustomLinkRecogniserImpl;
import org.xml.sax.Attributes;

/**
 * This class provides an example custom link
 * recogniser tailored to the needs of the non-xlink
 * links used by XBRL.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class CustomLinkRecogniserImpl extends XBRLCustomLinkRecogniserImpl {

    /**
     * The serial version UID.
     * @see 
     * http://java.sun.com/javase/6/docs/platform/serialization/spec/version.html#6678
     * for information about what changes will require the serial version UID to be
     * modified.
     */
    private static final long serialVersionUID = 6126089270751428220L;

    /**
	 * Constructor for the XBRL custom link recogniser
	 */
	public CustomLinkRecogniserImpl() {
		super();
	}

	/**
	 * Added the ability to detect custom links from typed dimensions to their domain declarations.
	 * @see org.xbrlapi.xlink.CustomLinkRecogniser#isLink(String, String, String, Attributes)
	 */
	public boolean isLink(String namespaceURI, String lName, String qName, Attributes attrs) {
		if (namespaceURI.equals(Constants.XMLSchemaNamespace.toString())) {
			if (lName.equals("import") || lName.equals("include")) {
				return true;
		    }
		    if (lName.equals("import") && (attrs.getValue(XDTConstants.XBRLDTNamespace.toString(),"typedDomainRef") != null)) {
		        return true;
		    }
	    }
		return false;
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
