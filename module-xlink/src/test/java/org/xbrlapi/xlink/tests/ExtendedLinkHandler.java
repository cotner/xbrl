package org.xbrlapi.xlink.tests;

/**
 * Default XLinkHandler implementation, does nothing for any of the events.
 * Extend this class to create your own XLinkHandler.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
import org.xbrlapi.xlink.XLinkException;
import org.xbrlapi.xlink.XLinkHandlerDefaultImpl;
import org.xml.sax.Attributes;


public class ExtendedLinkHandler extends XLinkHandlerDefaultImpl {

	/**
     * 
     */
    private static final long serialVersionUID = -9203460799877129016L;
    ExtendedLinkTestCase test;
	
	/**
	 * Constructor
	 * @param test The test case that uses this handler
	 */
	public ExtendedLinkHandler(ExtendedLinkTestCase test) {
		super();
		this.test = test;
	}
	
	/**
	 * @see org.xbrlapi.xlink.XLinkHandler#startExtendedLink(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes, java.lang.String, java.lang.String)
	 */
	public void startExtendedLink(String namespaceURI, String lName,
			String qName, Attributes attrs, String role, String title)
			throws XLinkException {
		
		if (lName.equals("child2")) {
    		test.checkIsNull(role);
    	}

		if (lName.equals("child4")) {
    		test.checkEqual("Human readable extended link title",title);
    		test.checkIsNull(role);
    	}

		if (lName.equals("child5")) {
    		test.confirmFail("Extended links cannot be nested in extended links");
    	}
	}

	/**
	 * @see org.xbrlapi.xlink.XLinkHandler#endExtendedLink(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endExtendedLink(String namespaceURI, String sName, String qName)
			throws XLinkException {
		;
	}

	/**
	 * Check the role error
	 */
	public void error(String namespaceURI, String lName, String qName,
			Attributes attrs,String message) throws XLinkException {	
		if ( lName.equals("child3") ) {
			test.checkEqual("The XLink role must be an absolute URI",message);
		} else {
			test.confirmFail("Unexpected error: " + message);
		}
	}
	
}
