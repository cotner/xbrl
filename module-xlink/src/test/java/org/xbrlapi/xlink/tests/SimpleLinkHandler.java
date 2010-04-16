package org.xbrlapi.xlink.tests;

/**
 * Default XLinkHandler implementation, does nothing for any of the events.
 * Extend this class to create your own XLinkHandler.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
import org.xbrlapi.xlink.XLinkException;
import org.xbrlapi.xlink.XLinkHandlerDefaultImpl;
import org.xml.sax.Attributes;


public class SimpleLinkHandler extends XLinkHandlerDefaultImpl {

	/**
     * 
     */
    private static final long serialVersionUID = -3879631975506311204L;
    SimpleLinkTestCase test;
	
	/**
	 * Constructor
	 * @param test The test case that uses this handler
	 */
	public SimpleLinkHandler(SimpleLinkTestCase test) {
		super();
		this.test = test;
	}
	
	/**
	 * Check the xlink attributes on the simple link
	 */
	public void startSimpleLink(String namespaceURI, String lName,
			String qName, Attributes attrs, String href, String role,
			String arcrole, String title, String show, String actuate)
			throws XLinkException {

		// Test child1 - a simple link with only a type and an href
		if (lName.equals("child1")) {
    		test.checkEqual("http://www.xbrlapi.org/",href);
    	}
		if (lName.equals("child1")) {
    		test.checkIsNull(role);
    	}
		if (lName.equals("child1")) {
    		test.checkIsNull(arcrole);
    	}
		if (lName.equals("child1")) {
    		test.checkIsNull(title);
    	}
		if (lName.equals("child1")) {
    		test.checkIsNull(show);
    	}
		if (lName.equals("child1")) {
    		test.checkIsNull(actuate);
    	}

		// Test child2 - a simple link with all attributes
		if (lName.equals("child2")) {
			test.checkEqual("http://www.xbrlapi.org/",href);
    	}
		if (lName.equals("child2")) {
    		test.checkEqual("http://www.xbrlapi.org/role/",role);
    	}
		if (lName.equals("child2")) {
    		test.checkEqual("http://www.xbrlapi.org/arcrole/",arcrole);
    	}
		if (lName.equals("child2")) {
    		test.checkEqual("Human readable title",title);
    	}
		if (lName.equals("child2")) {
    		test.checkEqual("new",show);
    	}
		if (lName.equals("child2")) {
    		test.checkEqual("onLoad",actuate);
    	}

		// Test child4 - should not ever be reached
		if (lName.equals("child4")) {
    		test.confirmFail("Incorrectly identified child4 as a simple link but it is inside an extended link.");
    	}

		// Make sure that simple links are not ignored inside false locators
		if (lName.equals("child7")) {
    		test.confirmFail("Incorrectly identified child7 as a simple link but it is inside an extended link.");
    	}

		// Make sure that invalid role attributes are detected
		if (lName.equals("child7")) {
    		test.confirmFail("Incorrectly identified child7 as a simple link but it is inside an extended link.");
    	}
				
	}

	/**
	 * Test handling of the end of a simple link
	 */
	public void endSimpleLink(String namespaceURI, String sName, String qName)
			throws XLinkException {
		if (sName.equals("child7")) {
    		test.confirmFail("Incorrectly identified child7 as a simple link but it is inside an extended link.");
    	}
	}

	/**
	 * Check the malformed role attribute
	 */
	public void error(String namespaceURI, String lName, String qName,
			Attributes attrs,String message) throws XLinkException {
		
		if ( lName.equals("child8") ) {
			// Make sure malformed role attributes are detected
			test.checkEqual("The XLink role must have valid URI syntax",message);
			test.noteE8();
		} else {
			test.confirmFail("Unexpected error: " + message);	
		}
	}
	
	
	
}
