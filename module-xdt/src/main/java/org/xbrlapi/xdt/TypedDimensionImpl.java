package org.xbrlapi.xdt;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Vector;

import org.xbrlapi.Fragment;
import org.xbrlapi.SimpleLink;
import org.xbrlapi.Title;
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
    public URI getLinkRole() throws XBRLException {
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
    public List<Title> getTitleElements() throws XBRLException {
        return new Vector<Title>();
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
     * Get the URI of the document containing the fragment targetted
     * by the supplied URI.
     * @param uri The supplied URI for decomposition.
     * @return The URI of the the document containing the fragment targetted
     * by the supplied URI.
     * @throws XBRLException
     */
    protected URI getTargetDocumentURI(URI uri) throws XBRLException {
        try {
            return new URI(uri.getScheme(),null,uri.getHost(),uri.getPort(),uri.getPath(),null,null);
        } catch (URISyntaxException e) {
            throw new XBRLException("This exception can never be thrown.");
        }
    }    
    
    /**
     * Get the value of the XPointer that corresponds to the XPointer information
     * stored in the metadata of all fragments.
     * @param pointer The String value of the XPointer supplied in the URI.
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
            throw new XBRLException(pointer + " failed to parse as an XPointer for locator " + this.getIndex(), e);
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
     * @see org.xbrlapi.SimpleLink#setTarget(URI)
     */
    public void setTarget(URI uri) throws XBRLException {
        setMetaAttribute("absoluteHref",uri.toString());
        setMetaAttribute("targetDocumentURI",this.getTargetDocumentURI(uri).toString());
        setMetaAttribute("targetPointerValue",this.getTargetPointerValue(uri.getFragment()));
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
    public URI getAbsoluteHref() throws XBRLException {
        try {
            return new URI(this.getMetadataRootElement().getAttribute("absoluteHref"));
        } catch (URISyntaxException e) {
            throw new XBRLException("Absolute URI in the HREF of the locator is malformed.",e);
        }
    }        
    
    /**
     * @return the document URI for the target fragment.
     * @throws XBRLException.
     */
    private URI getTargetDocumentURI() throws XBRLException {
        try {
            return new URI(this.getMetadataRootElement().getAttribute("targetDocumentURI"));
        } catch (URISyntaxException e) {
            throw new XBRLException("Absolute URI in the HREF of the locator is malformed.",e);
        }
    }
    
    /**
     * Get the string value of the XPointer element scheme expression from the xlink:href attribute.
     * @return the string value of the XPointer element scheme expression from the xlink:href attribute.
     */
    private String getTargetPointerValue() {
        return this.getMetadataRootElement().getAttribute("targetPointerValue");
    }
    
    /**
     * @see org.xbrlapi.SimpleLink#getArcrole()
     */
    public String getArcrole() throws XBRLException {
        return null;
    }
    
    /**
     * @see org.xbrlapi.SimpleLink#getTarget()
     */
    public Fragment getTarget() throws XBRLException {

        String pointerCondition = " and @parentIndex='none'";
        String pointerValue = getTargetPointerValue();
        if (! pointerValue.equals("")) {
            pointerCondition = " and "+ Constants.XBRLAPIPrefix+ ":" + "xptr/@value='" + pointerValue + "'";
        }
        
        String query = "/*[@uri='" + getTargetDocumentURI() + "'" + pointerCondition + "]";
        List<Fragment> fragments = getStore().query(query);
        if (fragments.size() == 0) return null;
        if (fragments.size() > 1) throw new XBRLException("The simple link references more than one fragment.");
        return fragments.get(0);
    }    
    
}