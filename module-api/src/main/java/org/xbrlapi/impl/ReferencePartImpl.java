package org.xbrlapi.impl;

import org.xbrlapi.FragmentList;
import org.xbrlapi.ReferencePart;
import org.xbrlapi.ReferencePartDeclaration;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ReferencePartImpl extends FragmentImpl implements ReferencePart {

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
     * Set the value of the reference part.
     * @param value The value of the reference part.
     * @throws XBRLException
     * @see org.xbrlapi.ReferencePart#setValue(String)
     */
    public void setValue(String value) throws XBRLException {
    	throw new XBRLException("Data update methods are not yet implemented.");
    }

    /**
     * @see org.xbrlapi.ReferencePart#getDeclaration()
     */
    public ReferencePartDeclaration getDeclaration() throws XBRLException {
    	String name = this.getLocalname();
    	String namespace = this.getNamespaceURI();
    	String query = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@type='org.xbrlapi.impl.ReferencePartDeclarationImpl' and "+ Constants.XBRLAPIPrefix+ ":" + "data/xsd:element/@name='" + name + "']";
    	FragmentList<ReferencePartDeclaration> declarations = getStore().<ReferencePartDeclaration>query(query);
    	for (ReferencePartDeclaration declaration: declarations) {
    		if (declaration.getTargetNamespaceURI().equals(namespace)) {
    			return declaration;
    		}
    	}
    	throw new XBRLException("The reference part has no corresponding declaration in the data store.");
    }
    
}
