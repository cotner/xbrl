package org.xbrlapi.xmlbase.tests;

/**
 * Enables xml:base testing as part of handling 
 * SAX events.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import org.xbrlapi.xmlbase.BaseURLSAXResolver;
import org.xbrlapi.xmlbase.XMLBaseException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class BaseTestHandler extends DefaultHandler {

	private BaseURLSAXResolverImplTestCase resolverTests;
	private BaseURLSAXResolver resolver;
	
	/**
	 * Test handler constructor.
	 */
	public BaseTestHandler(BaseURLSAXResolverImplTestCase resolverTests,BaseURLSAXResolver resolver) {
		super();
		this.resolverTests = resolverTests;
		this.resolver = resolver;
	}

    /**
     * call Base URL functionality for the element in question
     */
    public void startElement( String namespaceURI, String lName, String qName, Attributes attrs) throws SAXException 
	{
		try {
			resolver.addBaseURL(attrs.getValue("http://www.w3.org/XML/1998/namespace","base"));
		} catch (XMLBaseException e) {
			throw new SAXException("The Base URL Resolver could not update the base URL",e);
		}

    	if (lName.equals("root")) {
    		resolverTests.checkSAXBaseURLHandling("http://www.xbrlapi.org/root.xml",attrs,"http://www.xbrlapi.org/document.xml");
    	}
    	if (lName.equals("child1")) {
    		resolverTests.checkSAXBaseURLHandling("http://www.xbrlapi.org/child1.xml",attrs,"http://www.xbrlapi.org/root.xml");
    	}
    	if (lName.equals("child2")) {
    		resolverTests.checkSAXBaseURLHandling("http://www.xbrlapi.org/child2.xml",attrs,"http://www.xbrlapi.org/root.xml");
    	}
    	if (lName.equals("child3")) {
    		resolverTests.checkSAXBaseURLHandling("http://www.xbrlapi.org/root.xml",attrs,"http://www.xbrlapi.org/root.xml");
    	}
    	if (lName.equals("child4")) {
    		resolverTests.checkSAXBaseURLHandling("http://www.xbrlapi.org/root.xml",attrs,"http://www.xbrlapi.org/root.xml");
    	}
	}
    
    /**
     * call Base URL functionality for the element in question
     */
    public void endElement( String namespaceURI, String lName, String qName) throws SAXException 
	{
		try {
			resolver.removeBaseURL();
		} catch (XMLBaseException e) {
			throw new SAXException("The Base URL Resolver could not update the base URL",e);
		}
	}

}
