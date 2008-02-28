package org.xbrlapi.xlink.tests;

/**
 * Enables xml:base testing as part of handling 
 * SAX events.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import org.xbrlapi.xlink.XLinkException;
import org.xbrlapi.xlink.XLinkProcessor;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ContentHandlerImpl extends DefaultHandler {

	/**
	 * The XLinkProcessor to use in processing
	 * the XLinks found in parsed documents
	 */
	private XLinkProcessor xlinkProcessor;
	
	/**
	 * Test handler constructor.
	 */
	public ContentHandlerImpl(XLinkProcessor processor) {
		super();
		this.xlinkProcessor = processor;
	}

    /**
     * Fire off necessary XLink events on start of elements
     */
    public void startElement( String namespaceURI, String lName, String qName, Attributes attrs) throws SAXException 
	{
    	try {
    		xlinkProcessor.startElement(namespaceURI, lName, qName, attrs);
    	} catch (XLinkException e) {
    		throw new SAXException("The XLink processor startElement failed.",e);
    	}
	}
    
    /**
     * Fire off necessary XLink events on end of elements
     */
    public void endElement( String namespaceURI, String lName, String qName) throws SAXException 
	{
    	try {
    		xlinkProcessor.endElement(namespaceURI, lName, qName);
    	} catch (XLinkException e) {
    		throw new SAXException("The XLink processor startElement failed.",e);
    	}
	}

    /**
     * Handle SAX Character events
     */
    public void characters(char buf[], int offset, int len) throws SAXException 
	{
    	try {
    		xlinkProcessor.titleCharacters(buf, offset, len);
    	} catch (XLinkException e) {
    		throw new SAXException("The XLink processor startElement failed.",e);
    	}
	}    
    
}
