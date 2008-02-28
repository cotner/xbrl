package org.xbrlapi.impl;

import org.w3c.dom.Element;
import org.xbrlapi.Schema;
import org.xbrlapi.SchemaContent;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class SchemaContentImpl extends FragmentImpl implements SchemaContent {

    /**
     * Gets the schema fragment that is the schema containing this element declaration.
     * @return the schema fragment that is the schema containing this element declaration.
     * @throws XBRLException if the schema content is not inside a schema.
     * @see org.xbrlapi.SchemaContent#getSchema()
     */
    public Schema getSchema() throws XBRLException {
    	return (Schema) getAncestorOrSelf("org.xbrlapi.impl.SchemaImpl");
    }
    
    /**
     * Get the target namespace of the schema that contains this fragment.
     * @return the target namespace of the schema that contains this fragment
     * or null if no targetNamespace attribute is defined for the containing
     * schema fragment.
     * @throws XBRLException
     * @see org.xbrlapi.SchemaContent#getTargetNamespaceURI()
     */
    public String getTargetNamespaceURI() throws XBRLException {
    	Schema s = getSchema();
    	Element e = s.getDataRootElement();
    	if (e.hasAttribute("targetNamespace")) {
    		return e.getAttribute("targetNamespace");
    	}
    	return null;
    }
    
    

}