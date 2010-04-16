package org.xbrlapi.xlink.tests;

/**
 * Default XLinkHandler implementation, does nothing for any of the events.
 * Extend this class to create your own XLinkHandler.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
import org.xbrlapi.xlink.XLinkException;
import org.xbrlapi.xlink.XLinkHandlerDefaultImpl;
import org.xml.sax.Attributes;


public class ResourceHandler extends XLinkHandlerDefaultImpl {

	/**
     * 
     */
    private static final long serialVersionUID = -949369711610082374L;
    ResourceTestCase test;
	
	/**
	 * Constructor
	 * @param test The test case that uses this handler
	 */
	public ResourceHandler(ResourceTestCase test) {
		super();
		this.test = test;
	}
	
	/**
	 * Test the start of XLink resources
	 * @see org.xbrlapi.xlink.XLinkHandler#startResource(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void startResource(String namespaceURI, String lName, String qName,
			Attributes attrs, String role, String title, String label)
			throws XLinkException {

		if (lName.equals("child1")) {
    		test.confirmFail("Resources should not be recognised outside of extended links.");
    	}

		if (lName.equals("child2")) {
    		test.confirmFail("Resources should not be recognised outside of extended links.");
    	}

		if (lName.equals("child4")) {
    		test.checkIsNull(role);
    		test.checkIsNull(label);
    	}

		if (lName.equals("child5")) {
    		test.checkEqual("Human readable title",title);
    	}

		if (lName.equals("child7")) {
    		test.confirmFail("child7 resource should not have been found as it is not an extended link child");
    	}

	}

	/**
	 * Test the end of XLink resources
	 * @see org.xbrlapi.xlink.XLinkHandler#endResource(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endResource(String namespaceURI, String sName, String qName)
			throws XLinkException {
		if (sName.equals("child7")) {
    		test.confirmFail("child7 resource should not have been found as it is not an extended link child");
    	}
	}

	/**
	 * Check the XLink label error
	 */
	public void error(String namespaceURI, String lName, String qName,
			Attributes attrs,String message) throws XLinkException {
		test.checkEqual("The XLink label must be an NCName.",message);
	}
	
}
