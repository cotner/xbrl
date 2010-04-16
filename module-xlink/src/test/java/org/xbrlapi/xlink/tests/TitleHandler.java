package org.xbrlapi.xlink.tests;

/**
 * Default XLinkHandler implementation, does nothing for any of the events.
 * Extend this class to create your own XLinkHandler.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.xlink.XLinkException;
import org.xbrlapi.xlink.XLinkHandlerDefaultImpl;
import org.xml.sax.Attributes;


public class TitleHandler extends XLinkHandlerDefaultImpl {

	/**
     * 
     */
    private static final long serialVersionUID = 1739572682277206224L;
    TitleTestCase test;
	
	/**
	 * Constructor
	 * @param test The test case that uses this handler
	 */
	public TitleHandler(TitleTestCase test) {
		super();
		this.test = test;
	}
	
	/**
	 * Test the handling of title ends
	 * @see org.xbrlapi.xlink.XLinkHandler#startTitle(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startTitle(String namespaceURI, String lName, String qName,
			Attributes attrs) throws XLinkException {
		if (lName.equals("child1")) {
    		test.confirmFail("Titles must be children of XLink elements.");
    	}
		if (lName.equals("child3")) {
    		test.checkEqual("title",attrs.getValue(Constants.XLinkNamespace.toString(),"type"));
    	}
		if (lName.equals("child5")) {
    		test.checkEqual("title",attrs.getValue(Constants.XLinkNamespace.toString(),"type"));
    	}
		if (lName.equals("child6")) {
    		test.confirmFail("XLink title elements cannot be children of XLink title elements");
    	}

		if (lName.equals("child8")) {
    		test.confirmFail("XLink title elements cannot be children of XLink resource elements");
    	}
	}

	/**
	 * Test the handling of title starts
	 * @see org.xbrlapi.xlink.XLinkHandler#endTitle(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endTitle(String namespaceURI, String sName, String qName)
			throws XLinkException {
		;
	}

	/**
	 * Test the handling of title content
	 * @see org.xbrlapi.xlink.XLinkHandler#titleCharacters(char[], int, int)
	 */
	public void titleCharacters(char[] buf, int offset, int len)
			throws XLinkException {
		;
	}

	/**
	 * Ignore any XLink parsing errors
	 */
	public void error(String namespaceURI, String lName, String qName,
			Attributes attrs,String message) throws XLinkException {
		;
	}
	
}
