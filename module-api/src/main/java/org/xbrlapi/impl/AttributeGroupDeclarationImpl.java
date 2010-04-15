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
     * The serial version UID.
     * @see 
     * http://java.sun.com/javase/6/docs/platform/serialization/spec/version.html#6678
     * for information about what changes will require the serial version UID to be
     * modified.
     */
    private static final long serialVersionUID = -7206086803716730160L;

    /**
     * @see org.xbrlapi.AttributeGroupDeclaration#getMembers()
     */
    public List<SchemaDeclaration> getMembers() throws XBRLException {
        String query = "for $root in #roots#[@parentIndex='" + getIndex() + "' and (@type='org.xbrlapi.impl.AttributeImpl' or @type='org.xbrlapi.impl.AttributeGroupImpl')] order by $root/@index return $root";
        return getStore().<SchemaDeclaration>queryForXMLResources(query);
    }

}