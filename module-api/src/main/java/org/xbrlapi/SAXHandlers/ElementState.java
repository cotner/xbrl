package org.xbrlapi.SAXHandlers;

import java.util.Vector;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
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
 */
public class ElementState {

	/**
	 * The parent element state if a parent exists and null otherwise
	 */
	private ElementState parent = null;

	private long order = 1;
	
	private long childrenSoFar = 0;
	
	/**
	 * The ID for the element - if one is supplied
	 */
	private String id = null;
		
	/**
	 * Initialise the own order using information about children of parent element.
	 */
	public ElementState(ElementState parent) {
		//super();
		this.parent = parent;
		if (hasParent()) {
			parent.addChild();
			this.order = parent.getChildrenSoFar();
		}
	}

	public ElementState() {
		super();
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
