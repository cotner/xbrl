package org.xbrlapi;

import java.util.List;

import org.xbrlapi.utilities.XBRLException;


/**
 * Attribute group declaration interface.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface AttributeGroupDeclaration extends SchemaDeclaration {

    /**
     * @return a list of the child attribute and attribute group fragments, in document order.
     * @throws XBRLException
     */
    public List<SchemaDeclaration> getMembers() throws XBRLException;
    
}
