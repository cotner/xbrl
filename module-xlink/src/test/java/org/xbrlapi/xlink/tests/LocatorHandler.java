package org.xbrlapi.xlink.tests;

/**
 * Default XLinkHandler implementation, does nothing for any of the events.
 * Extend this class to create your own XLinkHandler.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
import org.xbrlapi.xlink.XLinkException;
import org.xbrlapi.xlink.XLinkHandlerDefaultImpl;
import org.xml.sax.Attributes;


public class LocatorHandler extends XLinkHandlerDefaultImpl {

	/**
     * 
     */
    private static final long serialVersionUID = -4391828566465369024L;
    LocatorTestCase test;
	
	/**
	 * Constructor
	 * @param test The test case that uses this handler
	 */
	public LocatorHandler(LocatorTestCase test) {
		super();
		this.test = test;
	}
	
	boolean e5a = false;
	/**
	 * Check the xlink attributes on the locator
	 */
	public void startLocator(String namespaceURI, String lName,
		String qName, Attributes attrs, String href, String role, String title, String label) {

		if (lName.equals("child1")) {
    		test.confirmFail("Locators should not be recognised outside of extended links.");
    	}

		if (lName.equals("child2")) {
    		test.confirmFail("Locators should not be recognised outside of extended links.");
    	}

		if (lName.equals("child5")) {
    		test.checkEqual("http://www.xbrlapi.org/role/",role);
    	}

		if (lName.equals("child5a")) {
			if ( ! e5a ) {
				test.confirmFail("child5a should have raised a 'disallowed attribute' error");
			}
		}
		
		if (lName.equals("child6")) {
			test.confirmFail("child6 should not have been found as it is not an extended link child");		
		}
		
		if (lName.equals("child7")) {
    		test.confirmFail("child7 should not have been found as it is not an extended link child");
    	}
		
        if (lName.equals("child8")) {
            test.confirmFail("child8 should not have been found because it has an xlink:wrong attribute.");
        }

        if (lName.equals("child9")) {
            test.confirmFail("child9 should not have been found because it has an xlink:invalid attribute.");
        }
        
        
	}

	/**
	 * Test the handling of the end of a locator
	 */
	public void endLocator(String namespaceURI, String sName, String qName)
			throws XLinkException {
		if (sName.equals("child7")) {
    		test.confirmFail("child7 should not have been found as it is not an extended link child");
    	}
	}

	/**
	 * Check the href attribute presence.
	 */
	public void error(String namespaceURI, String lName, String qName,
			Attributes attrs,String message) throws XLinkException {
		
		if (lName.equals("child5a")) {
			test.checkEqual("show attribute not allowed for this type of XLink.",message);
			e5a = true;
		} else if (lName.equals("child8")) {
            test.checkEqual("wrong is not defined in the XLink namespace.",message);
        } else if (lName.equals("child9")) {
            test.checkEqual("invalid is not defined in the XLink namespace.",message);
		} else {
			test.checkEqual("The XLink href attribute must be provided on a locator.",message);
		}
	}
	
}
