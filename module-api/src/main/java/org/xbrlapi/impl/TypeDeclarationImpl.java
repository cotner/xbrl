package org.xbrlapi.impl;

import org.xbrlapi.TypeDeclaration;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class TypeDeclarationImpl extends SchemaDeclarationImpl implements TypeDeclaration {	

    /**
     * @see org.xbrlapi.TypeDeclaration#getFinal()
     */
    public String getFinal() throws XBRLException {
        return getDataRootElement().getAttributeNS(Constants.XMLSchemaNamespace.toString(),"final");
    }
    
}