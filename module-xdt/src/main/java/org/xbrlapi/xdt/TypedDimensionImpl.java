package org.xbrlapi.xdt;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.SimpleLink;
import org.xbrlapi.Title;
import org.xbrlapi.impl.FragmentListImpl;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xpointer.ParseException;
import org.xbrlapi.xpointer.PointerGrammar;
import org.xbrlapi.xpointer.PointerPart;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class TypedDimensionImpl extends DimensionImpl implements TypedDimension, SimpleLink {

    // ********************************************************************************
    // The code in this section implements the Link interface.
    // ********************************************************************************    

    /**
     * @see org.xbrlapi.Link#getLinkRole()
     */
    public String getLinkRole() throws XBRLException {
        return null;
    }
    
    // ********************************************************************************
    // The code in this section implements the XLink interface.
    // ********************************************************************************

    /**
     * @see org.xbrlapi.Xlink#getXlinkType()
     */
    public String getXlinkType() throws XBRLException {
        return "simple";
    }

    /**
     * @see org.xbrlapi.Xlink#getTitleAttribute()
     */
    public String getTitleAttribute() throws XBRLException {
        return null;
    }
    
    /**
     * @see org.xbrlapi.Xlink#getTitleElements()
     */
    public FragmentList<Title> getTitleElements() throws XBRLException {
        return new FragmentListImpl<Title>();
    }
    
    /**
     * Get a specific title fragment 
     * @param index The index of the required title element
     * @return the title fragment or null if there are no title children elements.
     * @throws XBRLException if the index is out of bounds
     * @see org.xbrlapi.Xlink#getTitleElement(int)
     */
    public Title getTitleElement(int index) throws XBRLException {
        return null;
    }

    /**
     * @see org.xbrlapi.Xlink#getAttribute(String, String)
     */
    public String getAttribute(String namespaceURI, String localname) throws XBRLException {
        if (namespaceURI.equals(Constants.XLinkNamespace)) throw new XBRLException("XLink attributes must not be accessed using the getAttribute method on XLink fragments.");
        return getDataRootElement().getAttributeNS(namespaceURI,localname);
    }

    /**
     * @see org.xbrlapi.Xlink#getAttribute(String)
     */
    public String getAttribute(String name) throws XBRLException {
        return getDataRootElement().getAttribute(name);
    }
    
    /** 
     * Get the URL of the document containing the fragment targetted
     * by the supplied URL.
     * @param url The supplied URL for decomposition.
     * @return The URL of the the document containing the fragment targetted
     * by the supplied URL.
     * @throws XBRLException
     */
    protected URL getTargetDocumentURL(URL url) throws XBRLException {
        try {
            return new URL(url.getProtocol(),url.getHost(),url.getPort(),url.getFile());
        } catch (MalformedURLException e) {
            throw new XBRLException("This exception can never be thrown.");
        }
    }    
    
    /**
     * Get the value of the XPointer that corresponds to the XPointer information
     * stored in the metadata of all fragments.
     * @param pointer The String value of the XPointer supplied in the URL.
     * @return The value of the XPointer corresponding to the XPointer information
     * stored in the metadata of all fragments.  Returns the empty string if the XPointer does
     * not specify an element scheme or ID based shorthand pointer value.
     * @throws XBRLException
     */
    @SuppressWarnings("unchecked")
    protected String getTargetPointerValue(String pointer) throws XBRLException {
        
        if (pointer == null) return "";
        if (pointer.equals("")) return "";
        
        java.io.StringReader sr = new java.io.StringReader(pointer);
        java.io.Reader reader = new java.io.BufferedReader(sr);
        PointerGrammar parser = new PointerGrammar(reader);
        Vector pointerParts = null;
        
        try {
            pointerParts = parser.Pointer();
        } catch (ParseException e) {
            throw new XBRLException(pointer + " failed to parse as an XPointer for locator " + this.getFragmentIndex(), e);
        }
            
        for (int i=0; i<pointerParts.size(); i++) {
            
            PointerPart part = (PointerPart) pointerParts.get(i);
            String data = part.getUnescapedSchemeData();
            if (part.getSchemeNamespace().toString().equals(PointerPart.DefaultPointerNamespace) && part.getSchemeLocalName().equals("element")) {
                return data;
            }
        }

        return "";
        
    }   
    
    // ********************************************************************************
    // The code from here onward is adapted from the SimpleLink implementation.
    // ********************************************************************************
    
    /**
     * @see org.xbrlapi.SimpleLink#setTarget(URL)
     */
    public void setTarget(URL url) throws XBRLException {
        setMetaAttribute("absoluteHref",url.toString());
        setMetaAttribute("targetDocumentURL",this.getTargetDocumentURL(url).toString());
        setMetaAttribute("targetPointerValue",this.getTargetPointerValue(url.getRef()));
    }

    /**
     * @see org.xbrlapi.SimpleLink#getHref()
     */
    public String getHref() throws XBRLException {
        return getDataRootElement().getAttributeNS(XDTConstants.XBRLDTNamespace,"typedDomainRef");
    }

    /**
     * @see org.xbrlapi.SimpleLink#getAbsoluteHref()
     */
    public URL getAbsoluteHref() throws XBRLException {
        try {
            return new URL(this.getMetadataRootElement().getAttribute("absoluteHref"));
        } catch (MalformedURLException e) {
            throw new XBRLException("Absolute URL in the HREF of the locator is malformed.",e);
        }
    }        
    
    /**
     * @return the document URL for the target fragment.
     * @throws XBRLException.
     */
    private URL getTargetDocumentURL() throws XBRLException {
        try {
            return new URL(this.getMetadataRootElement().getAttribute("targetDocumentURL"));
        } catch (MalformedURLException e) {
            throw new XBRLException("Absolute URL in the HREF of the locator is malformed.",e);
        }
    }
    
    /**
     * Get the string value of the XPointer element scheme expression from the xlink:href attribute.
     * @return the string value of the XPointer element scheme expression from the xlink:href attribute.
     * @throws XBRLException.
     */
    private String getTargetPointerValue() throws XBRLException {
        return this.getMetadataRootElement().getAttribute("targetPointerValue");
    }
    
    /**
     * @see org.xbrlapi.SimpleLink#getArcrole()
     */
    public String getArcrole() throws XBRLException {
        return null;
    }
    
    /**
     * @see org.xbrlapi.SimpleLink#getTargetFragment()
     */
    public Fragment getTargetFragment() throws XBRLException {

        String pointerCondition = " and @parentIndex='none'";
        String pointerValue = getTargetPointerValue();
        if (! pointerValue.equals("")) {
            pointerCondition = " and "+ Constants.XBRLAPIPrefix+ ":" + "xptr/@value='" + pointerValue + "'";
        }
        
        String query = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@url='" + getTargetDocumentURL() + "'" + pointerCondition + "]";
        FragmentList<Fragment> fragments = getStore().query(query);
        if (fragments.getLength() == 0) return null;
        if (fragments.getLength() > 1) throw new XBRLException("The simple link references more than one fragment.");
        return fragments.getFragment(0);
    }    
    
}