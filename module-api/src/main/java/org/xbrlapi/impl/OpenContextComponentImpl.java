package org.xbrlapi.impl;

import java.util.List;
import java.util.Vector;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xbrlapi.OpenContextComponent;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class OpenContextComponentImpl extends ContextComponentImpl implements OpenContextComponent {
	
    /**
     * 
     */
    private static final long serialVersionUID = -4911717815217702354L;



    /**
     * @see org.xbrlapi.OpenContextComponent#getComplexContent()
     */
    public NodeList getComplexContent() throws XBRLException {
    	return getDataRootElement().getChildNodes();
    }
    
    /**
     * @see org.xbrlapi.OpenContextComponent#getChildElements()
     */
    public List<Element> getChildElements() throws XBRLException {
        List<Element> result = new Vector<Element>();
        NodeList nodes = this.getComplexContent();
        for (int i=0; i<nodes.getLength(); i++) {
            if (nodes.item(i).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                result.add((Element) nodes.item(i));
            }
        }
        return result;
    }    


    
	/**
	 * Test c-equality of this open context component and another.
	 * @param other The other open context component.
	 * @return true if the two components are c-equal and false otherwise.
	 * @see org.xbrlapi.OpenContextComponent#equals(OpenContextComponent)
	 */
    public boolean equals(OpenContextComponent other) throws XBRLException {
    	
    	if (! this.getType().equals(other.getType())) return false;
    	
    	NodeList thisContent = this.getComplexContent();
    	NodeList otherContent = other.getComplexContent();
    	if (thisContent.getLength() != otherContent.getLength()) return false;
    	for (int i=0; i<thisContent.getLength(); i++) {
    		if (! thisContent.item(i).isEqualNode(otherContent.item(i))) return false;
    	}
    	return true;
    }
}
