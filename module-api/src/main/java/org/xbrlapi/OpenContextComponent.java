package org.xbrlapi;

import java.util.Vector;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface OpenContextComponent extends ContextComponent {

    /**
     * Gets the complex content
     *
     * @return the complex content contained by the open
     * context component as a linked list of complex content
     * XML nodes
     * @throws XBRLException
     */
    public NodeList getComplexContent() throws XBRLException;


    
	/**
	 * Test c-equality of this open context component and another.
	 * @param other the other open context component.
	 * @return true if the two components are c-equal and false otherwise.
	 */
    public boolean equals(OpenContextComponent other) throws XBRLException;    
}
