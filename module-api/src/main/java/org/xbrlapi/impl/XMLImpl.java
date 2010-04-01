package org.xbrlapi.impl;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xbrlapi.XML;
import org.xbrlapi.builder.Builder;
import org.xbrlapi.data.Store;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.utilities.XMLDOMBuilder;

public class XMLImpl implements XML {

    protected final static Logger logger = Logger.getLogger(XMLImpl.class);  
    
    public XMLImpl() {
        super();
    }
    
    protected void finalize() throws Throwable
    {
      super.finalize(); 
    }    
        
    /**
     * The Fragment builder - used when building fragments during DTS discovery.
     */
    transient private Builder builder;

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
    public boolean isa(String type) throws XBRLException {
        
        Class targetClass = FragmentFactory.getClass(type);
        Class candidateClass = this.getClass();
        while (candidateClass != null) {
            if (candidateClass.equals(targetClass)) return true;
            candidateClass = candidateClass.getSuperclass();
        }
        
        return false;
    }
    
    /**
     * @see org.xbrlapi.XML#isa(Class)
     */
    public boolean isa(Class<?> targetClass) throws XBRLException {
        Class<?> candidateClass = this.getClass();
        while (candidateClass != null) {
            if (candidateClass.equals(targetClass)) return true;
            candidateClass = candidateClass.getSuperclass();
        }
        return false;
    }    
    
    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((rootElement == null) ? 0 : getIndex().hashCode());
        result = prime * result + ((store == null) ? 0 : store.hashCode());
        return result;
    }
    
    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        XMLImpl other = (XMLImpl) obj;

        if (store == null) {
            if (other.store != null)
                return false;
        } else if (!store.equals(other.store))
            return false;
        
        String index = this.getIndex();
        if (index == null) return false;
        if (!index.equals(other.getIndex())) return false;

        return true;
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
        if (rootElement == null) throw new XBRLException("The XML resource is null.");
        this.rootElement = rootElement;
        setBuilder(null);
    }    
    
    /**
     * @see org.xbrlapi.XML#getDocumentNode()
     */
    public Document getDocumentNode() {
        if (builder != null) return getBuilder().getMetadata().getOwnerDocument();
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
        if (builder == null) {
            this.builder = null;
            return;
        }
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
        getStore().persist(this);
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
        if (builder != null) return getBuilder().getMetadata();
        return getResource();
    }

    /**
     * @see org.xbrlapi.XML#getIndex()
     */
    public String getIndex() {
        return this.getMetaAttribute("index");
        //return this.index;
    }
    
    /**
     * @see org.xbrlapi.XML#setIndex(String)
     */
    public void setIndex(String index) throws XBRLException {
        if (index == null) throw new XBRLException("The index must not be null.");
        if (index.equals("")) throw new XBRLException("A fragment index must not be an empty string.");
        setMetaAttribute("index",index);
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
    public String getMetaAttribute(String name) {
        if (getBuilder() != null) {
            return getBuilder().getMetaAttribute(name);
        }

        Element root = getMetadataRootElement();
        if (! root.hasAttribute(name)) return null;
        return root.getAttribute(name);
    }

    /**
     * @see org.xbrlapi.XML#getMetaAttribute(String)
     */
    public boolean hasMetaAttribute(String name) {
        Builder builder = getBuilder();
        if (builder != null) {
            return builder.hasMetaAttribute(name);
        }
        return getMetadataRootElement().hasAttribute(name);
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
        Element child = document.createElementNS(Constants.XBRLAPINamespace.toString(),Constants.XBRLAPIPrefix + ":" + eName);

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
        NodeList children = element.getElementsByTagNameNS(Constants.XBRLAPINamespace.toString(),eName);
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
        if (this.getStore() == null) throw new XBRLException("The fragment can only be serialized if it has been associated with a data store.");
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
        if (store.hasXMLResource(this.getIndex())) {
            store.remove(this.getIndex());
        }
        store.persist(this);
    }
 
    /**
     * Handles object serialization
     * @param out The input object stream used to store the serialization of the object.
     * @throws IOException
     */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        if (this.getBuilder() != null) {
            logger.error(this.getIndex() + " still has a builder.");
            throw new IOException("The XML Resource could not be serialized because it is still being built.");
        }
        out.defaultWriteObject( );
        try {
            String xml = store.serializeToString(rootElement);
            out.writeObject(xml);
            out.writeObject(store);
        } catch (XBRLException e) {
            throw new IOException("Could not convert the store content to a string representation of the XML.",e);
        }
    }
    
    /**
     * Handles object inflation.
     * @param in The input object stream used to access the object's serialization.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {

        in.defaultReadObject();
        try {
            XMLDOMBuilder builder = new XMLDOMBuilder();
            Document dom = builder.newDocument((String) in.readObject());
            rootElement = dom.getDocumentElement();
            store = (Store) in.readObject();
        } catch (XBRLException e) {
            throw new IOException("The XML Resource could not be de-serialized.",e);
        }
    }
    
}
