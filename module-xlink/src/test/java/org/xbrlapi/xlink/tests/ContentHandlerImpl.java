package org.xbrlapi.xlink.tests;

/**
 * Enables xml:base testing as part of handling 
 * SAX events.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import org.xbrlapi.xlink.ElementState;
import org.xbrlapi.xlink.XLinkException;
import org.xbrlapi.xlink.XLinkProcessor;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

public class ContentHandlerImpl extends DefaultHandler {

	/**
	 * The XLinkProcessor to use in processing
	 * the XLinks found in parsed documents
	 */
	private XLinkProcessor xlinkProcessor;
	
    /**
     * Data required to track the element scheme XPointer 
     * expressions that can be used to identify XBRL fragments.
     */
    private ElementState state = null;
    
    /**
     * @param state The element state
     */
    protected void setState(ElementState state) {
        this.state = state;
    }
    
    /**
     * @return the state for the element currently being parsed.
     */
    protected ElementState getState() {
        return state;
    }    
		
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
    public void startElement(String namespaceURI, String lName, String qName, Attributes attrs) throws SAXException 
	{

        // Update the information about the state of the current element
        setState(new ElementState(getState(),new AttributesImpl( attrs )));
  
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
        
        // Get the attributes of the element being ended.
        Attributes attrs = getState().getAttributes();        
        
    	try {
    		xlinkProcessor.endElement(namespaceURI, lName, qName, attrs);
    	} catch (XLinkException e) {
    		throw new SAXException("The XLink processor startElement failed.",e);
    	}

        // Update the information about the state of the current element
        setState(getState().getParent());
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
