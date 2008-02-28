package org.xbrlapi.xlink;

import org.xml.sax.Attributes;

/**
 * The XLink processor is responsible for taking the input from a 
 * SAX event and recognising any XLink events resulting from that
 * SAX event and then passing the XLink event through to the 
 * chosen XLinkHandler.  This makes the job of the XLink handler
 * simplify down to one of Syntax recognition and XLink state tracking
 * to ensure that only actual XLink are triggering XLink events.
 * Note that the XLink specification is pretty persnicketty regarding
 * nested XLink structures.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */


public interface XLinkProcessor {

	public final static Integer SIMPLE_LINK = new Integer(1);
	public final static Integer EXTENDED_LINK = new Integer(2);
	public final static Integer CUSTOM_LINK = new Integer(4);
	public final static Integer RESOURCE = new Integer(8);
	public final static Integer LOCATOR = new Integer(16);
	public final static Integer ARC = new Integer(32);
	public final static Integer TITLE = new Integer(64);
	public final static Integer NOT_XLINK = new Integer(0);
	
    /**
     * Set the custom link recogniser
     * @param customLinkRecogniser The class that indicates if a custom link
     * has been recognised.
     */
    public void setCustomLinkRecogniser(CustomLinkRecogniser customLinkRecogniser);

    /**
     * Respond to the start of an element, 
     * examining the element for XLink features
     * 
     * @param namespaceURI
     * @param lName
     * @param qName
     * @param attrs
     * @throws XLinkException
     */
	public void startElement(
			String namespaceURI, 
			String lName, 
			String qName, 
			Attributes attrs) throws XLinkException;
	
    /**
     * Respond to the end of an element, 
     * examining the element for XLink features
     * 
     * @param namespaceURI
     * @param lName
     * @param qName
     * @throws XLinkException
     */
	public void endElement(
			String namespaceURI, 
			String lName, 
			String qName) throws XLinkException;

	/**
	 * Handles the character content for a title element 
	 * @param buf
	 * @param offset
	 * @param len
	 * @throws XLinkException
	 */
	public void titleCharacters(char buf[], int offset, int len) throws XLinkException;
   
	/**
	 * Provides access to the XLink handler being used by the XLink processor.
	 * @return the XLink handler being used by the XLink processor.
	 */
	public XLinkHandler getXLinkHandler();

}
