package org.xbrlapi.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xbrlapi.CustomType;
import org.xbrlapi.Fragment;
import org.xbrlapi.UsedOn;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class CustomTypeImpl extends FragmentImpl implements CustomType {

    /**
     * The serial version UID.
     * @see 
     * http://java.sun.com/javase/6/docs/platform/serialization/spec/version.html#6678
     * for information about what changes will require the serial version UID to be
     * modified.
     */
    private static final long serialVersionUID = -3481150284083534384L;

    /**
     * Get the id of the customType.
     * @return the id of the custom type or null if none is provided
     * @throws XBRLException
     * @see org.xbrlapi.CustomType#getCustomTypeId()
     */
    public String getCustomTypeId() throws XBRLException {
    	Element root = getDataRootElement();
    	if (root.hasAttribute("id"))
    		return root.getAttribute("id");
    	return null;
    }
    
    /**
     * Get the custom URI being defined.
     * @return the custom URI being defined.
     * @throws XBRLExceptio if the custom URI is not specified
     * or is not a valid URI.
     * @see org.xbrlapi.CustomType#getCustomURI()
     */
    public URI getCustomURI() throws XBRLException {
        try {
            if (getDataRootElement().hasAttribute("roleURI")) {
                return new URI(getDataRootElement().getAttribute("roleURI"));
            }
            if (getDataRootElement().hasAttribute("arcroleURI")) {
                return new URI(getDataRootElement().getAttribute("arcroleURI"));
            }
        } catch (URISyntaxException e) {
            throw new XBRLException("The custom URI is not a valid URI.",e);
        }
    	throw new XBRLException("The value of the custom URI is not provided.");
    }
    


	/**
	 * @see org.xbrlapi.CustomType#getDefinition()
	 */
    public String getDefinition() throws XBRLException {
		Element data = this.getDataRootElement();
		NodeList definitions = data.getElementsByTagNameNS(Constants.XBRL21LinkNamespace.toString(),"definition");
		if (definitions.getLength() == 0) return null;
		Element definition = (Element) definitions.item(0);
		return definition.getTextContent().trim();		 	
    }
    

    
    /**
     * @see org.xbrlapi.CustomType#isUsedCorrectly(Fragment)
     */
    public boolean isUsedCorrectly(Fragment fragment) throws XBRLException {
    	List<UsedOn> usedOns = getUsedOns();
    	for (UsedOn usedOn: usedOns) {
    		if (usedOn.isUsedOn(fragment.getNamespace(),fragment.getLocalname())) {
    			return true;
    		}
    	}
    	return false;
    }
    
    

    /**
     * @see org.xbrlapi.CustomType#isUsedOn(URI,String)
     */
    public boolean isUsedOn(URI namespace, String localname) throws XBRLException {
    	List<UsedOn> fragments = this.getUsedOns();
    	for(int i=0; i<fragments.size(); i++) {
        	UsedOn usedOn = fragments.get(i);
        	if (usedOn.isUsedOn(namespace,localname)) return true;
    	}
    	return false;
    }
    



	
	/**
	 * @see org.xbrlapi.CustomType#getUsedOns()
	 */
	public List<UsedOn> getUsedOns() throws XBRLException {
		return this.<UsedOn>getChildren("org.xbrlapi.impl.UsedOnImpl");
	}    
    
}
