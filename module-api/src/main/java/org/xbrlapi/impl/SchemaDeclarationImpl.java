package org.xbrlapi.impl;

import org.xbrlapi.SchemaDeclaration;
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
    



    



    

    



    


}