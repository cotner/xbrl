package org.xbrlapi.impl;

import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xbrlapi.XML;
import org.xbrlapi.Fragment;
import org.xbrlapi.builder.Builder;
import org.xbrlapi.builder.BuilderImpl;
import org.xbrlapi.data.Store;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

public class XMLImpl implements XML {

    protected static Logger logger = Logger.getLogger(XMLImpl.class);  
    
    /**
     * The unique index value for this fragment, within the scope of the
     * data store that this fragment is in.  This property is immutable 
     * and is set during construction of the fragment.
     */
    private String index;
        
    /**
     * The Fragment builder - used when building fragments during DTS discovery.
     */
    private Builder builder = null;

    /**
     * The data store that manages this fragment.
     */
    private Store store = null;
    
    /**
     * The DOM instantiation of the fragment's root element or null
     * if the fragment has not been built.
     */
    private Element rootElement = null;

    /**
     * @see org.xbrlapi.XML#isa(String)
     */
    @SuppressWarnings("unchecked")
    public boolean isa(String superType) throws XBRLException {
        
        Class superClass = FragmentFactory.getClass(superType);
        Class candidateClass = this.getClass();
        while (candidateClass != null) {
            if (candidateClass.equals(superClass)) return true;
            candidateClass = candidateClass.getSuperclass();
        }
        
        return false;
    }
    
    /**
     * @see org.xbrlapi.XML#hashCode()
     */
    public int hashCode() {
        return getIndex().hashCode();
    }
    
    /**
     * @see org.xbrlapi.XML#equals(Object)
     */
    public boolean equals(Object o1) throws ClassCastException {
        Fragment f1 = (Fragment) o1;
        if (this.getIndex().equals(f1.getIndex()))
            return true;
        return false;
    }
    
    /**
     * Comparison is based upon the fragment index.
     * @see java.lang.Comparable#compareTo(Object o)
     */
    public int compareTo(XML other) throws ClassCastException {
        return this.getIndex().compareTo(other.getIndex());     
    }
    
    /**
     * @see org.xbrlapi.XML#setResource(Element)
     */
    public void setResource(Element rootElement) throws XBRLException {
        builder = null;
        if (rootElement == null) throw new XBRLException("The XML resource is null.");
        this.rootElement = rootElement;
    }    
    
    /**
     * @see org.xbrlapi.XML#getDocumentNode()
     */
    public Document getDocumentNode() {
        if (builder != null) return builder.getData().getOwnerDocument();
        return getResource().getOwnerDocument();
    }

    /**
     * Get the XML resource that is the fragment from the data store.
     * @return the DOM root element of the fragment or null if the resource
     * has not been initialised to a DOM root element.
     */
    private Element getResource() {
        return rootElement;
    }    

    /**
     * @see org.xbrlapi.XML#setStore(Store)
     */
    public void setStore(Store store) throws XBRLException {
        if (this.store != null) {
            throw new XBRLException("The data store has already been specified for this fragment.");
        }
        this.store = store;
    }

    /**
     * @see org.xbrlapi.XML#setBuilder(Builder)
     */
    public void setBuilder(Builder builder) {
        builder.setMetaAttribute("type",getType());
        this.builder = builder;
    }
    
    /**
     * @see org.xbrlapi.XML#getStore()
     */
    public Store getStore() {
        return store;
    }

    /**
     * Update this fragment in the data store by storing it again.
     * @throws XBRLException if this fragment cannot be updated in the data store.
     */
    private void updateStore() throws XBRLException {
        store.storeFragment(this);
    }

    /**
     * @see org.xbrlapi.XML#getBuilder()
     */
    public Builder getBuilder() {
        return builder;
    }

    /**
     * @see org.xbrlapi.XML#getMetadataRootElement()
     */
    public Element getMetadataRootElement() {
        if (builder != null) return builder.getMetadata();
        return getResource();
    }

    /**
     * @see org.xbrlapi.XML#getIndex()
     */
    public String getIndex() {
        return this.index;
    }
    
    /**
     * @see org.xbrlapi.XML#setIndex(String)
     */
    public void setIndex(String index) throws XBRLException {
        if (index == null) throw new XBRLException("The index must not be null.");
        if (index.equals("")) throw new XBRLException("A fragment index must not be an empty string.");
        this.index = index;
        if (this.getResource() == null) {
            setBuilder(new BuilderImpl());
        }
        if (builder != null) builder.setMetaAttribute("index",index);
    }
 
    /**
     * @see org.xbrlapi.XML#getType()
     */
    public String getType() {
        return this.getClass().getName();
    }

    /**
     * @see org.xbrlapi.XML#setMetaAttribute(String, String)
     */
    public void setMetaAttribute(String name, String value) throws XBRLException {

        if (getBuilder() != null) {
            getBuilder().setMetaAttribute(name,value);
            return;
        }
        
        Element element = this.getMetadataRootElement();
        element.setAttribute(name,value);
        updateStore();
    }
    
    /**
     * @see org.xbrlapi.XML#removeMetaAttribute(String)
     */
    public void removeMetaAttribute(String name) throws XBRLException {
        if (getBuilder() != null) {
            getBuilder().removeMetaAttribute(name);
            return;
        }
        
        Element element = this.getMetadataRootElement();
        if (element == null) throw new XBRLException("The metadata does not contain a root element.");      
        element.removeAttribute(name);
        updateStore();
    }
    
    /**
     * @see org.xbrlapi.XML#getMetaAttribute(String)
     */
    public String getMetaAttribute(String name) throws XBRLException {
        if (builder != null) {
            String value = getBuilder().getMetaAttribute(name);
            if (value == null) return null;
            if (value.equals("")) return null;
            return value;
        }
        
        String value = getMetadataRootElement().getAttribute(name);
        if (value.equals("")) return null;
        return value;
    }

    /**
     * @see org.xbrlapi.XML#appendMetadataElement(String, HashMap)
     */
    public void appendMetadataElement(String eName, HashMap<String,String> attributes) throws XBRLException {
        if (eName == null) throw new XBRLException("An element name must be specified.");
        
        if (getBuilder() != null) {
            getBuilder().appendMetadataElement(eName, attributes);
            return;
        }

        Element root = getMetadataRootElement();
        Document document = root.getOwnerDocument();
        Element child = document.createElementNS(Constants.XBRLAPINamespace,Constants.XBRLAPIPrefix + ":" + eName);

        for (String aName: attributes.keySet()) {
            String aValue = attributes.get(aName);
            if (aName != null) {
                if (aValue == null) throw new XBRLException("A metadata element is being added but attribute, " + aName + ", has a null value.");
                child.setAttribute(aName,aValue); 
            } else throw new XBRLException("A metadata element is being added with an attribute with a null name.");
        }
        root.appendChild(child);
        updateStore();
    }
    
    /**
     * @see org.xbrlapi.XML#removeMetadataElement(String, HashMap)
     */
    public void removeMetadataElement(String eName, HashMap<String,String> attributes) throws XBRLException {

        if (eName == null) throw new XBRLException("An element name must be specified.");

        if (getBuilder() != null) {
            getBuilder().removeMetadataElement(eName, attributes);
            return;
        }
        
        // If the fragment has been stored update the data store
        Element element = this.getMetadataRootElement();
        if (element == null) throw new XBRLException("The metadata does not contain a root element.");
        NodeList children = element.getElementsByTagNameNS(Constants.XBRLAPINamespace,eName);
        for (int i=0; i<children.getLength(); i++) {
            boolean match = true;
            Element child = (Element) children.item(i);
            Iterator<String> attributeNames = attributes.keySet().iterator();
            while (attributeNames.hasNext()) {
                String aName = attributeNames.next();
                String aValue = attributes.get(aName);
                if (aName != null) {
                    if (aValue == null) throw new XBRLException("A metadata element is being checked but attribute, " + aName + ", has a null value.");
                    if (! child.getAttribute(aName).equals(aValue)) {
                        match = false;
                    }
                } else throw new XBRLException("A metadata element is being checked against an attribute with a null name.");
            }
            
            if (match) {
                element.removeChild(child);
                break;
            }
        }
        updateStore();
        
    }
    
    /**
     * @see org.xbrlapi.XML#serialize()
     */
    public void serialize() throws XBRLException {
        getStore().serialize(this);
    }
    
    /**
     * @see org.xbrlapi.XML#serialize(File)
     */
    public void serialize(File file) throws XBRLException {
        getStore().serialize(this.getMetadataRootElement(), file);
    }
    
    /**
     * @see org.xbrlapi.XML#serialize(OutputStream)
     */
    public void serialize(OutputStream outputStream) throws XBRLException {
        getStore().serialize(this.getMetadataRootElement(), outputStream);
    }
    
    /**
     * @see org.xbrlapi.XML#serialize(String)
     */
    public void serialize(String string) throws XBRLException {
        getStore().serialize(this.getMetadataRootElement(), string);
    }

    /**
     * @see org.xbrlapi.XML#updateInStore()
     */
    public void updateInStore() throws XBRLException {
        Store store = this.getStore();
        if (store == null) return;
        store.storeFragment(this);
    }
    
}
