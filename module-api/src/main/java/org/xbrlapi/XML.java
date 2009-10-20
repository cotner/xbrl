package org.xbrlapi;

import java.io.File;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xbrlapi.builder.Builder;
import org.xbrlapi.data.Store;
import org.xbrlapi.utilities.XBRLException;

public abstract interface XML extends Comparable<XML>, Serializable {

    /**
     * Override the Object hashCode method to provide for equality comparisons
     * that are based on the fragment index.
     */
    public int hashCode();       

    /**
     * Override the Object equals method to base equality on the
     * fragment index.
     * @throws ClassCastException if the object that the fragment
     * being compared to does not cast to a fragment.
     */
    public boolean equals(Object o1) throws ClassCastException;    
    
    /**
     * @param superType The specified fragment type to test against.
     * @return true if the fragment is an extension of the specified fragment type.
     * @throws XBRLException
     * @see isa(Class)
     */
    public boolean isa(String superType) throws XBRLException;
    
    /**
     * @param targetClass The specified XML resource subclass to test against.
     * @return true if the fragment is an extension of the specified fragment type.
     * @throws XBRLException
     */
    public boolean isa(Class<?> targetClass) throws XBRLException;    
    
    /**
     * Closes down the fragment builder and sets the data and metadata
     * resources for the fragment.  This should only be used by Store implementations
     * at the point where a newly built fragment is stored.
     * @param rootElement The fragment data.
     * @throws XBRLException If the builder cannot be shut down or if the 
     * resource cannot be set or is null.
     */
    public void setResource(Element rootElement) throws XBRLException;
    
    /**
     * Get the XML DOM Document for the fragment data.
     * @return an XML DOM document for the fragment or null if none exists.
     */
    public Document getDocumentNode();
    
    /**
     * Get the root element of the fragment metadata.
     * @return an XML Element that is the root of the fragment metadata.
     */
    public Element getMetadataRootElement();

    /**
     * Set the data store that manages this fragment.
     * @param store The data store.
     * @throws XBRLException if the data store has already been set.
     */
    public void setStore(Store store) throws XBRLException;
    
    /**
     * Set the builder that constructs the fragment XML during parsing.
     * @param builder The builder object used to construct the fragment XML.
     * @throws XBRLException if the builder cannot be set or is null.
     */
    public void setBuilder(Builder builder) throws XBRLException;   

    /**
     * Get the data store that manages this fragment.
     * @return the data store that manages this fragment or 
     * null if the fragment has not been stored.
     */
    public Store getStore();
    
    /**
     * Get the fragment builder.  Note that the builder is null
     * if the fragment has already been stored in a data store.
     * TODO Should fragments hide the builder property?
     * @return the fragment builder or null if one is not available.
     */
    public Builder getBuilder();

    /**
     * Get the Fragment type.  The fragment type is immutable. 
     * No public method is available to set the fragment type.
     * @return The full class name of the fragment.
     */
    public String getType();

    /**
     * Get the index of the XML resource.  This is the 
     * name used for the XML resource in the data store.
     */
    public String getIndex();
    
    /**
     * Set the fragment index.  Note that no checks are
     * performed to ensure that the fragment index is
     * unique within the data store.
     * This method instantiates a fragment builder for fragments that do
     * not have a resource property.
     * @throws an XBRLException if the index is null or an empty string.
     */
    public void setIndex(String index) throws XBRLException;
    
    /**
     * Set a fragment metadata attribute.
     * @param name the name of the attribute
     * @param value the value to give to the metadata attribute
     * @throws XBRLException
     */
    public void setMetaAttribute(String name, String value) throws XBRLException;

    /**
     * Get a fragment metadata attribute.
     * @param name the name of the attribute.
     * @return The value of the metadata attribute or null if none exists.
     */
    public String getMetaAttribute(String name);

    /**
     * @param name the name of the attribute.
     * @return true if the XML resource has a metadata attribute with the specfied name.
     */
    public boolean hasMetaAttribute(String name);
    
    /**
     * Removes a metadata attribute
     * @param name The name of the attribute to remove
     * @throws XBRLException
     */
    public void removeMetaAttribute(String name) throws XBRLException;
    
    /**
     * Appends a child element to the root metadata element.
     * @param eName Name of the element to be added (no namespaces are used).
     * @param attributes A hashmap from attribute name keys to attribute values.
     * @throws XBRLException.
     */
    public void appendMetadataElement(String eName, HashMap<String,String> attributes) throws XBRLException;
    
    /**
     * removes a child element from the metadata root element by specifying the name of the child and
     * the value of the element's text content and/or the value of a named attribute.  All specified information
     * must match for the deletion to succeed.
     * @param eName Name of the element to be added (no namespaces are used).
     * @param attributes A hashmap from attribute name keys to attribute values.
     * @throws XBRLException If no deletion happens.
     */
    public void removeMetadataElement(String eName, HashMap<String,String> attributes) throws XBRLException;
        
    /**
     * serializes the XML to standard output.
     * @throws XBRLException if the XML resource is not
     * associated with a data store.
     */
    public void serialize() throws XBRLException;
    
    /**
     * Serializes the XML to the specified file.
     * @param file The specified output file.
     */
    public void serialize(File file) throws XBRLException;
   
    /**
     * Serializes the XML to the specified output stream.
     * @param outputStream The specified output stream.
     */
    public void serialize(OutputStream outputStream) throws XBRLException;
    
    /**
     * Serializes the XML to the specified output string.
     * @param string The specified output string.
     */
    public void serialize(String string) throws XBRLException;

    /**
     * Updates the data store to reflect the current state of
     * this XML object.  If the XML object has not yet been
     * stored then no action is taken.  If there is a XML resource
     * in the store with the same index, then that earlier version
     * is first deleted from the data store.
     * @throws XBRLException
     */
    public void updateInStore() throws XBRLException;
    
    
}
