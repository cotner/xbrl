package org.xbrlapi.impl;

import java.net.URI;

import java.util.List;
import org.xbrlapi.ReferencePart;
import org.xbrlapi.ReferencePartDeclaration;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ReferencePartImpl extends FragmentImpl implements ReferencePart {

    /**
     * 
     */
    private static final long serialVersionUID = 2432919060384094098L;



    /**
     * Get the value of the reference part. 
     * @return The value of the reference part with spaces trimmed from
     * the front and end.
     * @throws XBRLException
     * @see org.xbrlapi.ReferencePart#getValue()
     */
    public String getValue() throws XBRLException {
    	return this.getDataRootElement().getTextContent().trim();
    }



    /**
     * @see org.xbrlapi.ReferencePart#getDeclaration()
     */
    public ReferencePartDeclaration getDeclaration() throws XBRLException {
    	String name = this.getLocalname();
    	URI namespace = this.getNamespace();
    	String query = "#roots#[@type='org.xbrlapi.impl.ReferencePartDeclarationImpl' and */xsd:element/@name='" + name + "']";
    	List<ReferencePartDeclaration> declarations = getStore().<ReferencePartDeclaration>queryForXMLResources(query);
    	for (ReferencePartDeclaration declaration: declarations) {
    		if (declaration.getTargetNamespace().equals(namespace)) {
    			return declaration;
    		}
    	}
    	throw new XBRLException("The reference part has no corresponding declaration in the data store.");
    }
    
}
