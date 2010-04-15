package org.xbrlapi.impl;

import java.util.List;
import java.util.Vector;

import org.xbrlapi.ElementDeclaration;
import org.xbrlapi.Fragment;
import org.xbrlapi.SchemaCompositor;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class SchemaCompositorImpl extends SchemaContentImpl implements SchemaCompositor {

    /**
     * 
     */
    private static final long serialVersionUID = 985343575694203047L;

    /**
     * @see org.xbrlapi.SchemaCompositor#getMembers()
     */
    public List<ElementDeclaration> getMembers() throws XBRLException {
        List<ElementDeclaration> result = new Vector<ElementDeclaration>();
        for (Fragment child: this.getAllChildren()) {
            result.add((ElementDeclaration) child);
        }
        return result;
    }	

}