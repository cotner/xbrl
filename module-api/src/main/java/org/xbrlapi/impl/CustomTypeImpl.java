package org.xbrlapi.impl;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xbrlapi.CustomType;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.UsedOn;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class CustomTypeImpl extends FragmentImpl implements CustomType {

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
     * @throws XBRLExceptio if the custom URI is not specified.
     * @see org.xbrlapi.CustomType#getCustomURI()
     */
    public String getCustomURI() throws XBRLException {
    	if (getDataRootElement().hasAttribute("roleURI")) {
    		return getDataRootElement().getAttribute("roleURI");
    	}
    	if (getDataRootElement().hasAttribute("arcroleURI")) {
    		return getDataRootElement().getAttribute("arcroleURI");
    	}
    	throw new XBRLException("The value of the custom URI is not provided.");
    }
    


	/**
	 * Get the string value of the role Definition.
	 * @return the string value of the role description or null if none is provided.
	 * @throws XBRLException.
	 * @see org.xbrlapi.CustomType#getDefinition()
	 */
    public String getDefinition() throws XBRLException {
		Element data = this.getDataRootElement();
		NodeList definitions = data.getElementsByTagNameNS(Constants.XBRL21LinkNamespace,"definition");
		if (definitions.getLength() == 0) return null;
		Element definition = (Element) definitions.item(0);
		return definition.getTextContent().trim();		 	
    }
    

    
    /**
     * @see org.xbrlapi.CustomType#isUsedCorrectly(Fragment)
     */
    public boolean isUsedCorrectly(Fragment fragment) throws XBRLException {
    	FragmentList<UsedOn> usedOns = getUsedOns();
    	for (UsedOn usedOn: usedOns) {
    		if (usedOn.isUsedOn(fragment.getNamespaceURI(),fragment.getLocalname())) {
    			return true;
    		}
    	}
    	return false;
    }
    
    

    /**
     * Returns true only if the arcrole can be used on the specified element
     * @param namespaceURI The namespace of the element being tested for
     * @param localname The local name of the element being tested for
     * @throws XBRLException
     * @see org.xbrlapi.CustomType#isUsedOn(String,String)
     */
    public boolean isUsedOn(String namespaceURI, String localname) throws XBRLException {
    	FragmentList<UsedOn> fragments = this.getUsedOns();
    	for(int i=0; i<fragments.getLength(); i++) {
        	UsedOn usedOn = fragments.getFragment(i);
        	if (usedOn.isUsedOn(namespaceURI,localname)) return true;
    	}
    	return false;
    }
    



	
	/**
	 * @see org.xbrlapi.CustomType#getUsedOns()
	 */
	public FragmentList<UsedOn> getUsedOns() throws XBRLException {
		return this.<UsedOn>getChildren("org.xbrlapi.impl.UsedOnImpl");
	}    
    
}
