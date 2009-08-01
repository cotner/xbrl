package org.xbrlapi.xlink;

/**
 * SAX Content handler used by the XLink validator.
 * This is a good content handler example for real
 * world XLink processing examples.
 * Add in your own SAX event handling in addition
 * to the XLink event triggers that are already included.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

public class ValidatorSAXContentHandlerImpl extends DefaultHandler {

    /**
     * Data required to track the element scheme XPointer 
     * expressions that can be used to identify XBRL fragments.
     */
    transient private ElementState state = null;
    
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
	 * The XLinkProcessor to use in processing
	 * the XLinks found in parsed documents
	 */
	private XLinkProcessor xlinkProcessor;
	
	/**
	 * Test handler constructor.
	 */
	public ValidatorSAXContentHandlerImpl(XLinkProcessor processor) {
		super();
		this.xlinkProcessor = processor;
	}

    /**
     * Fire off necessary XLink events on start of elements
     */
    public void startElement( String namespaceURI, String lName, String qName, Attributes attrs) throws SAXException 
	{
        
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
