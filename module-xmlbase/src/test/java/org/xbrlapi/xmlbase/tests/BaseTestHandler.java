package org.xbrlapi.xmlbase.tests;

/**
 * Enables xml:base testing as part of handling 
 * SAX events.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import org.xbrlapi.xmlbase.BaseURISAXResolver;
import org.xbrlapi.xmlbase.XMLBaseException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class BaseTestHandler extends DefaultHandler {

	private BaseURISAXResolverImplTestCase resolverTests;
	private BaseURISAXResolver resolver;
	
	/**
	 * Test handler constructor.
	 */
	public BaseTestHandler(BaseURISAXResolverImplTestCase resolverTests,BaseURISAXResolver resolver) {
		super();
		this.resolverTests = resolverTests;
		this.resolver = resolver;
	}

    /**
     * call Base URI functionality for the element in question
     */
    public void startElement( String namespaceURI, String lName, String qName, Attributes attrs) throws SAXException 
	{
		try {
			resolver.addBaseURI(attrs.getValue("http://www.w3.org/XML/1998/namespace","base"));
		} catch (XMLBaseException e) {
			throw new SAXException("The Base URI Resolver could not update the base URI",e);
		}

    	if (lName.equals("root")) {
    		resolverTests.checkSAXBaseURIHandling("http://www.xbrlapi.org/root.xml",attrs,"http://www.xbrlapi.org/document.xml");
    	}
    	if (lName.equals("child1")) {
    		resolverTests.checkSAXBaseURIHandling("http://www.xbrlapi.org/child1.xml",attrs,"http://www.xbrlapi.org/root.xml");
    	}
    	if (lName.equals("child2")) {
    		resolverTests.checkSAXBaseURIHandling("http://www.xbrlapi.org/child2.xml",attrs,"http://www.xbrlapi.org/root.xml");
    	}
    	if (lName.equals("child3")) {
    		resolverTests.checkSAXBaseURIHandling("http://www.xbrlapi.org/root.xml",attrs,"http://www.xbrlapi.org/root.xml");
    	}
    	if (lName.equals("child4")) {
    		resolverTests.checkSAXBaseURIHandling("http://www.xbrlapi.org/root.xml",attrs,"http://www.xbrlapi.org/root.xml");
    	}
	}
    
    /**
     * call Base URI functionality for the element in question
     */
    public void endElement( String namespaceURI, String lName, String qName) throws SAXException 
	{
		try {
			resolver.removeBaseURI();
		} catch (XMLBaseException e) {
			throw new SAXException("The Base URI Resolver could not update the base URI",e);
		}
	}

}
