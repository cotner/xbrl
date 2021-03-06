package org.xbrlapi.xlink;

import java.io.Serializable;

import org.xml.sax.Attributes;

/**
 * Event handler for the XLink processor,
 * responsible for performing the user required responses
 * to the XLink structures being processed by the XLink processor.
 * The XLink handler is very similar in nature to the SAX content
 * handler.  The XLink processor receives events from the SAX 
 * parser that is parsing in the XLinks and then passes those events
 * through to the nominated XLinkHandler, augmented with a range of
 * state information relating to where abouts in an XLink structure
 * the XLink event has been triggered from.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public interface XLinkHandler extends Serializable {

	/**
	 * Handles the start of processing a simple link (and custom links).
	 * @param namespaceURI
	 * @param lName
	 * @param qName
	 * @param attrs
	 * @param href
	 * @param role
	 * @param arcrole
	 * @param title
	 * @param show
	 * @param actuate
	 * @throws XLinkException
	 */
	public void startSimpleLink(
			String namespaceURI, 
			String lName, 
			String qName, 
			Attributes attrs,
			String href,
			String role,
			String arcrole,
			String title,
			String show,
			String actuate
	) throws XLinkException;
	
	/**
	 * Handles straight pass through of SAX event for 
	 * the end of simple link elements.
	 * @param namespaceURI
	 * @param sName
	 * @param qName
	 * @throws XLinkException
	 */
	public void endSimpleLink(
			String namespaceURI, 
			String sName, 
			String qName
			) throws XLinkException;
	
	/**
	 * Handles straight pass through of SAX event for 
	 * XLink title elements
	 * @param namespaceURI
	 * @param lName
	 * @param qName
	 * @param attrs
	 * @throws XLinkException
	 */
	public void startTitle(
			String namespaceURI, 
			String lName, 
			String qName, 
			Attributes attrs
	) throws XLinkException;

	/**
	 * Handles straight pass through of SAX event for 
	 * the end of simple title elements.
	 * @param namespaceURI
	 * @param sName
	 * @param qName
	 * @throws XLinkException
	 */
	public void endTitle(
			String namespaceURI, 
			String sName, 
			String qName
			) throws XLinkException;	
	
	/**
	 * Handles the character content of a title element 
	 * @param buf
	 * @param offset
	 * @param len
	 * @throws XLinkException
	 */
	public void titleCharacters(char buf[], int offset, int len) throws XLinkException;

	/**
	 * Handles the start of processing an extended link.
	 * @param namespaceURI
	 * @param lName
	 * @param qName
	 * @param attrs
	 * @param role
	 * @param title
	 * @throws XLinkException
	 */
	public void startExtendedLink(
			String namespaceURI, 
			String lName, 
			String qName, 
			Attributes attrs,
			String role,
			String title
	) throws XLinkException;
	
	/**
	 * Handles straight pass through of SAX event for 
	 * the end of extended link elements.
	 * @param namespaceURI
	 * @param sName
	 * @param qName
	 * @throws XLinkException
	 */
	public void endExtendedLink(
			String namespaceURI, 
			String sName, 
			String qName
			) throws XLinkException;

	/**
	 * Handles the start of processing a local resource.
	 * @param namespaceURI
	 * @param lName
	 * @param qName
	 * @param attrs
	 * @param role
	 * @param title
	 * @param label
	 * @throws XLinkException
	 */
	public void startResource(
			String namespaceURI, 
			String lName, 
			String qName, 
			Attributes attrs,
			String role,
			String title,
			String label
	) throws XLinkException;
	
	/**
	 * Handles straight pass through of SAX event for 
	 * the end of local resources.
	 * @param namespaceURI
	 * @param sName
	 * @param qName
	 * @throws XLinkException
	 */
	public void endResource(
			String namespaceURI, 
			String sName, 
			String qName
			) throws XLinkException;
	
	/**
	 * Handles the start of processing a locator.
	 * @param namespaceURI
	 * @param lName
	 * @param qName
	 * @param attrs
	 * @param href
	 * @param role
	 * @param title
	 * @param label
	 * @throws XLinkException
	 */
	public void startLocator(
			String namespaceURI, 
			String lName, 
			String qName, 
			Attributes attrs,
			String href,
			String role,
			String title,
			String label
			) throws XLinkException;
	
	/**
	 * Handles straight pass through of SAX event for 
	 * the end of locators.
	 * @param namespaceURI
	 * @param sName
	 * @param qName
	 * @throws XLinkException
	 */
	public void endLocator(
			String namespaceURI, 
			String sName, 
			String qName
			) throws XLinkException;

	/**
	 * Handles the start of processing an arc.
	 * @param namespaceURI
	 * @param lName
	 * @param qName
	 * @param attrs
	 * @param from
	 * @param to
	 * @param arcrole
	 * @param title
	 * @param show
	 * @param actuate
	 * @throws XLinkException
	 */
	public void startArc(
			String namespaceURI, 
			String lName, 
			String qName, 
			Attributes attrs,
			String from,
			String to,
			String arcrole,
			String title,
			String show,
			String actuate
	) throws XLinkException;
	
	/**
	 * Handles straight pass through of SAX event for 
	 * the end of simple link elements.
	 * @param namespaceURI
	 * @param sName
	 * @param qName
	 * @throws XLinkException
	 */
	public void endArc(
			String namespaceURI, 
			String sName, 
			String qName
			) throws XLinkException;
	
	/**
	 * Handles the start of an element, providing any XML Base
	 * attribute value.
	 * @param value The value of the XML Base attribute
	 * @throws XLinkException
	 */
	public void xmlBaseStart(String value) throws XLinkException;

	/**
	 * Handles end of an element so that XML base related
	 * operations can to done
	 * @throws XLinkException
	 */
	public void xmlBaseEnd() throws XLinkException;

	/**
	 * Handle an error message from the XLink processor
	 * allowing the handler to decide how seriously to take
	 * the error.
	 * @param namespaceURI Namespace of the element generating the error
	 * @param lName local name of the element generating the error
	 * @param qName qName of the element generating the error
	 * @param attrs attributes of the element generating the error
	 * @param message The text of the error message
	 * @throws XLinkException if the handler does not deal with
	 * the XLink problem internally
	 */
	public void error(String namespaceURI, String lName, String qName,
			Attributes attrs,String message) throws XLinkException;
	
	/**
	 * Handle a warning that the document may not be 
	 * doing what the creator of the XLink intended
	 * 
	 * @param namespaceURI Namespace of the element generating the error
	 * @param lName local name of the element generating the error
	 * @param qName qName of the element generating the error
	 * @param attrs attributes of the element generating the error
	 * @param message The text of the warning message
	 * @throws XLinkException if the handler does not deal with
	 * the XLink warning internally
	 */
	public void warning(String namespaceURI, String lName, String qName,
			Attributes attrs,String message) throws XLinkException;

}
