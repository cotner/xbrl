package org.xbrlapi.xlink.tests;

/**
 * Default XLinkHandler implementation, does nothing for any of the events.
 * Extend this class to create your own XLinkHandler.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
import org.xbrlapi.xlink.XLinkException;
import org.xbrlapi.xlink.XLinkHandlerDefaultImpl;
import org.xml.sax.Attributes;


public class ArcHandler extends XLinkHandlerDefaultImpl {

    /**
     * The serial version UID.
     * @see 
     * http://java.sun.com/javase/6/docs/platform/serialization/spec/version.html#6678
     * for information about what changes will require the serial version UID to be
     * modified.
     */
    private static final long serialVersionUID = -6344245275761559721L;
    ArcTestCase test;
	
	/**
	 * Constructor
	 * @param test The test case that uses this handler
	 */
	public ArcHandler(ArcTestCase test) {
		super();
		this.test = test;
	}
	
	/**
	 * Test the event handling for the start of XLink arcs
	 * @see org.xbrlapi.xlink.XLinkHandler#startArc(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void startArc(String namespaceURI, String lName, String qName,
			Attributes attrs, String from, String to, String arcrole,
			String title, String show, String actuate) throws XLinkException {
		
		if (lName.equals("child1")) {
    		test.confirmFail("Arcs should not be recognised outside of extended links.");
    	}

		if (lName.equals("child2")) {
    		test.confirmFail("Arcs should not be recognised outside of extended links.");
    	}

		if (lName.equals("child4")) {
    		test.checkIsNull(arcrole);
    	}

		if (lName.equals("child5")) {
			// Modified by Henry S Thompson
    		//test.checkEqual("http://www.xbrlapi.org/arcrole/",arcrole);
			test.checkIsNull(arcrole);
    	}

		if (lName.equals("child7")) {
    		test.confirmFail("child7 arc should not have been found as it is not an extended link child");
    	}

	}

	/**
	 * Test the handling of XLink arc starts
	 */
	public void endArc(String namespaceURI, String sName, String qName)
			throws XLinkException {
		if (sName.equals("child7")) {
    		test.confirmFail("child7 arc should not have been found as it is not an extended link child");
    	}

	}
	
	/**
	 * Test the handling of XLink arc ends
	 */
	public void error(String namespaceURI, String lName, String qName,
			Attributes attrs,String message) throws XLinkException {
		// Make sure malformed role attributes are detected
		test.checkEqual("The XLink label must be an NCName.",message);
	}
	
}
