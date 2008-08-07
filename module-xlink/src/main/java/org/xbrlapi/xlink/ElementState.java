package org.xbrlapi.xlink;

import java.util.Vector;

import org.xml.sax.Attributes;

/**
 * Assists in the tracking of document state information
 * during SAX parsing, retaining the necessary information
 * to construct all element scheme XPointer expressions
 * that can be used to identify each element in a document.
 * 
 * The state is started again when a new document is started.
 * As each new element is parsed in, a new element state is created,
 * wrapping the parent element's state as a property.
 * 
 * The state keeps track of ancestor element states, the id of the current
 * element, if any, and the order which is set equal to the number of children 
 * so far for the parent element state.
 *
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class ElementState {

	/**
	 * The parent element state if a parent exists and null otherwise
	 */
	private ElementState parent = null;

	/**
	 * The set of attributes on the element.
	 * These need to be tracked so that they are available
	 * to the SAX Handler's endElement method.
	 */
	private Attributes attributes = null;
	
	/**
	 * @return the attributes of the element.
	 */
	public Attributes getAttributes() {
	    return attributes;
	}
	
	/**
	 * @param attributes The attributes of the element.
	 */
	public void setAttributes(Attributes attributes) {
	    this.attributes = attributes;
	}
	
	private long order = 1;
	
	private long childrenSoFar = 0;
	
	/**
	 * The ID for the element - if one is supplied
	 */
	private String id = null;



	/**
	 * @param parent The state of the parent element
	 * @param attrs The attributes of the element
	 */
    public ElementState(ElementState parent, Attributes attrs) {
        this.parent = parent;
        if (hasParent()) {
            parent.addChild();
            this.order = parent.getChildrenSoFar();
        }
        this.setAttributes(attrs);
    }
	
	public boolean hasParent() {
		if (parent == null) return false;
		return true;
	}
	
	private String getOrderAsString() {
		return (new Long(order)).toString();
	}  	
	
	public ElementState getParent() {
		return parent;
	}
	
	public void addChild() {
		childrenSoFar++;
	}
	
	public long getChildrenSoFar() {
		return childrenSoFar;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public boolean hasId() {
		if (id == null) return false;
		return true;
	}
	
	public Vector<String> getElementSchemePointers() {
		
		Vector<String> pointers;
		
		if (hasParent()) {
			pointers = getParent().getElementSchemePointers();
			for (int i=0; i<pointers.size(); i++) {
				String pointer = pointers.get(i);
				pointer = pointer + "/" + getOrderAsString();
				pointers.remove(i);
				pointers.add(i,pointer);
			}
		} else {
			pointers = new Vector<String>();
			pointers.add("/" + getOrderAsString());
		}

		if (hasId()) {
			pointers.add(getId());
		}
		return pointers;
	}
	

}
