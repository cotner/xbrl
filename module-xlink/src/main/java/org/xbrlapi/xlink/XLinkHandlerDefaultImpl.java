package org.xbrlapi.xlink;

/**
 * Default XLinkHandler implementation, does nothing for any of the events.
 * Extend this class to create your own XLinkHandler.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
import java.io.Serializable;

import org.xml.sax.Attributes;


public class XLinkHandlerDefaultImpl implements XLinkHandler, Serializable {

	/**
     * 
     */
    private static final long serialVersionUID = -6060131708671079013L;

    /**
	 * Default XLink handler constructor
	 */
	public XLinkHandlerDefaultImpl() {
		super();
	}
	
	/**
	 * @see org.xbrlapi.xlink.XLinkHandler#startSimpleLink(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void startSimpleLink(String namespaceURI, String lName,
			String qName, Attributes attrs, String href, String role,
			String arcrole, String title, String show, String actuate)
			throws XLinkException {
		;
	}

	/**
	 * @see org.xbrlapi.xlink.XLinkHandler#endSimpleLink(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endSimpleLink(String namespaceURI, String sName, String qName)
			throws XLinkException {
		;
	}

	/**
	 * @see org.xbrlapi.xlink.XLinkHandler#startTitle(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startTitle(String namespaceURI, String lName, String qName,
			Attributes attrs) throws XLinkException {
		;
	}

	/**
	 * @see org.xbrlapi.xlink.XLinkHandler#endTitle(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endTitle(String namespaceURI, String sName, String qName)
			throws XLinkException {
		;
	}

	/**
	 * @see org.xbrlapi.xlink.XLinkHandler#titleCharacters(char[], int, int)
	 */
	public void titleCharacters(char[] buf, int offset, int len)
			throws XLinkException {
		;
	}

	/**
	 * @see org.xbrlapi.xlink.XLinkHandler#startExtendedLink(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes, java.lang.String, java.lang.String)
	 */
	public void startExtendedLink(String namespaceURI, String lName,
			String qName, Attributes attrs, String role, String title)
			throws XLinkException {
		;
	}

	/**
	 * @see org.xbrlapi.xlink.XLinkHandler#endExtendedLink(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endExtendedLink(String namespaceURI, String sName, String qName)
			throws XLinkException {
		;
	}

	/**
	 * @see org.xbrlapi.xlink.XLinkHandler#startResource(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void startResource(String namespaceURI, String lName, String qName,
			Attributes attrs, String role, String title, String label)
			throws XLinkException {
		;
	}

	/**
	 * @see org.xbrlapi.xlink.XLinkHandler#endResource(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endResource(String namespaceURI, String sName, String qName)
			throws XLinkException {
		;
	}

	/**
	 * @see org.xbrlapi.xlink.XLinkHandler#startLocator(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void startLocator(String namespaceURI, String lName, String qName,
			Attributes attrs, String href, String role, String title,
			String label) throws XLinkException {
		;
	}

	/**
	 * @see org.xbrlapi.xlink.XLinkHandler#endLocator(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endLocator(String namespaceURI, String sName, String qName)
			throws XLinkException {
		;
	}

	/**
	 * @see org.xbrlapi.xlink.XLinkHandler#startArc(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void startArc(String namespaceURI, String lName, String qName,
			Attributes attrs, String from, String to, String arcrole,
			String title, String show, String actuate) throws XLinkException {
		;
	}

	/**
	 * @see org.xbrlapi.xlink.XLinkHandler#endArc(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endArc(String namespaceURI, String sName, String qName)
			throws XLinkException {
		;
	}
	
	/**
	 * @see org.xbrlapi.xlink.XLinkHandler#xmlBaseStart(java.lang.String)
	 */
	public void xmlBaseStart(String value) throws XLinkException {
		;
	}

	/**
	 * @see org.xbrlapi.xlink.XLinkHandler#xmlBaseEnd()
	 */
	public void xmlBaseEnd() throws XLinkException {
		;
	}
	
	/**
	 * Default error behaviour is to throw an XLink Exception
	 * @see org.xbrlapi.xlink.XLinkHandler#error(java.lang.String,java.lang.String, java.lang.String, org.xml.sax.Attributes, java.lang.String)
	 */
	public void error(String namespaceURI, String lName, String qName,
			Attributes attrs,String message) throws XLinkException {
		throw new XLinkException(message);
	}
	
	/**
	 * Default warning behaviour is to ignore the warning
	 * @see org.xbrlapi.xlink.XLinkHandler#warning(java.lang.String,java.lang.String, java.lang.String, org.xml.sax.Attributes, java.lang.String)
	 */
	public void warning(String namespaceURI, String lName, String qName,
			Attributes attrs,String message) throws XLinkException {
		;
	}
	
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
