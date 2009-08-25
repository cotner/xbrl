package org.xbrlapi.impl;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xbrlapi.SchemaDeclaration;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class SchemaDeclarationImpl extends SchemaContentImpl implements SchemaDeclaration {
	
    /**
     * Get the name of structure being declared.
     * @return the name of the structure being declared.
     * @throws XBRLException
     * @see org.xbrlapi.SchemaDeclaration#getName()
     */
    public String getName() throws XBRLException {
    	return getDataRootElement().getAttribute("name");
    }
    



    



    

    



    
    /**
     * Gets the complex content fragment.
     * @throws XBRLException
     * @see org.xbrlapi.SchemaDeclaration#getComplexContent()
     */
    public Element getComplexContent() throws XBRLException {
    	try {
	    	NodeList complexContent = getDataRootElement().getElementsByTagNameNS(Constants.XMLSchemaNamespace.toString(),"complexContent");
	    	return (Element) complexContent.item(0);

    	} catch (Exception e) {
    		throw new XBRLException("The complex content could not be retrieved for the specified concept.");
    	}
    }

}