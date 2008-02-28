package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;


/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Xlink extends Fragment {

    /**
     * Get the xlink type (The xxlink:type attribute value).
     * This is one of simple or extended
     *
     * @throws XBRLException
     */
    public String getXlinkType() throws XBRLException;
    
    /**
     * Set the xlink type (The xxlink:type attribute value).
     *
     * @param type One of simple or extended
     * @throws XBRLException
     */
    public void setXlinkType(String type) throws XBRLException;

    /**
     * Get the  title (The xlink:title attribute value).
     * @return the value of the xlink:title attribute or null if none is supplied. 
     * @throws XBRLException
     */
    public String getTitleAttribute() throws XBRLException;    

    /**
     * Set the title (The xlink:title attribute value).
     * @param title The value of the title attribute
     * @throws XBRLException
     */
    public void setTitleAttribute(String title) throws XBRLException;

    /**
     * Get a list of titles (The xlink:title children elements).
     * @return a fragment list of title elements or null if there are none.
     * @throws XBRLException
     */

    public FragmentList<Title> getTitleElements() throws XBRLException;
    /**
     * Append a title element to the XLink fragment
     *
     * @param title The title to append
     * @throws XBRLException
     */
    public void appendTitle(Title title) throws XBRLException;
    /**
     * Get a list of titles (The xlink:title children elements).
     * Returns null if there are no title children elements.
     *
     * @param index The index of the required title element
     * @return The specified title fragment
     * @throws XBRLException
     */
    public Title getTitleElement(int index) throws XBRLException;

    
    /**
     * Get a non-xlink attribute value.
     * @param namespaceURI The namespace of the attribute to retrieve
     * @param localname The local name of the attribute to retrieve
     * @throws XBRLException if the namespace URI is for the XLink namespace.
     */
    public String getAttribute(String namespaceURI, String localname) throws XBRLException;

    /**
     * Get a no-namespace attribute value.
     * @param name The name of the no-namespace attribute to retrieve
     * @throws XBRLException
     */
    public String getAttribute(String name) throws XBRLException;
    
    /**
     * Set a non-xlink attribute value
     *
     * @param namespaceURI The namespace of the attribute to set
     * @param qName The qualified local name of the attribute to set
     * @param value The value of the attribute to set
     * @throws XBRLException
     */
    public void setAttribute(String namespaceURI, String qName, String value) throws XBRLException;
    
}
