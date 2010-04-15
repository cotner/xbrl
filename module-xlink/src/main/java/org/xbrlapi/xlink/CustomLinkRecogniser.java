package org.xbrlapi.xlink;
/**
 * Provides a means of extending a standard XLink processor
 * to recognise simple links that use a syntax that is not
 * covered by the XLink specification.
 * 
 * This is particularly useful for anchor tags in XHTML documents
 * and for import and include tags in XML Schema documents.
 * 
 * The CustomLinkRecogniser is passed in to the XLink Processor during
 * its construction, thus enabling the XLink processor to recognise
 * a simple link using one of the defined custom syntaxes.
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import java.io.Serializable;

import org.xml.sax.Attributes;

public abstract class CustomLinkRecogniser implements Serializable {

    /**
     * The serial version UID.
     * @see 
     * http://java.sun.com/javase/6/docs/platform/serialization/spec/version.html#6678
     * for information about what changes will require the serial version UID to be
     * modified.
     */
    private static final long serialVersionUID = 5541842174210812036L;

    /**
	 * Returns true if the element with the supplied characteristics 
	 * is a custom simple link.
	 * Importantly his method can be called from 
	 * a SAX start element event and from a SAX end 
	 * element event.
	 *
	 * @param namespaceURI The namespace of the element.
	 * @param lName The localname of the element.
	 * @param qName The QName of the element.
	 * @param attrs The attributes of the element.
	 * @return true if the element is a custom simple link and false
	 * otherwise.
	 */
	public abstract boolean isLink(String namespaceURI, String lName, String qName, Attributes attrs);	
	
	/**
	 * Get the href equivalent for the link
	 * @param namespaceURI
	 * @param lName
	 * @param qName
	 * @param attrs
	 * @return the href
	 * @throws XLinkException
	 */
	public abstract String getHref(
			String namespaceURI, 
			String lName, 
			String qName, 
			Attributes attrs) 
	throws XLinkException;

	/**
	 * Get the simple link role equivalent if it exists
	 * @param namespaceURI
	 * @param lName
	 * @param qName
	 * @param attrs
	 * @return the role or null if it does not exist
	 * @throws XLinkException
	 */
	public String getRole(String namespaceURI, String lName, String qName, Attributes attrs) {
		return null;
	}

	/**
	 * Get the simple link arcrole equivalent if it exists
	 * @param namespaceURI
	 * @param lName
	 * @param qName
	 * @param attrs
	 * @return the arcrole or null if it does not exist
	 * @throws XLinkException
	 */
	public String getArcrole(String namespaceURI, String lName, String qName, Attributes attrs) {
		return null;
	}
	
	/**
	 * Get the simple link title attribute equivalent if it exists
	 * @param namespaceURI
	 * @param lName
	 * @param qName
	 * @param attrs
	 * @return the title or null if it does not exist
	 * @throws XLinkException
	 */
	public String getTitle(String namespaceURI, String lName, String qName, Attributes attrs) {
		return null;
	}

	/**
	 * Get the simple link show equivalent if it exists
	 * @param namespaceURI
	 * @param lName
	 * @param qName
	 * @param attrs
	 * @return the show or null if it does not exist
	 * @throws XLinkException
	 */
	public String getShow(String namespaceURI, String lName, String qName, Attributes attrs) {
		return null;
	}

	/**
	 * Get the simple link actuate equivalent if it exists
	 * @param namespaceURI
	 * @param lName
	 * @param qName
	 * @param attrs
	 * @return the actuate or null if it does not exist
	 * @throws XLinkException
	 */
	public String getActuate(String namespaceURI, String lName, String qName, Attributes attrs) {
		return null;
	}

	private String thing;

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return 1;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return true;
    }
	
}
