package org.xbrlapi.impl;

import java.util.List;

import org.xbrlapi.AttributeGroupDeclaration;
import org.xbrlapi.SchemaDeclaration;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class AttributeGroupDeclarationImpl extends SchemaDeclarationImpl implements AttributeGroupDeclaration {

    /**
     * @see org.xbrlapi.AttributeGroupDeclaration#getMembers()
     */
    public List<SchemaDeclaration> getMembers() throws XBRLException {
        String query = "for $root in #roots#[@parentIndex='" + getIndex() + "' and (@type='org.xbrlapi.impl.AttributeImpl' or @type='org.xbrlapi.impl.AttributeGroupImpl')] order by $root/@index return $root";
        return getStore().<SchemaDeclaration>queryForXMLResources(query);
    }

}