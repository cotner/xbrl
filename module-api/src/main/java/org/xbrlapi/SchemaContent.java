package org.xbrlapi;

import java.net.URI;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface SchemaContent extends Fragment {

    /**
     * @return the schema fragment that is the schema containing this element declaration.
     * @throws XBRLException if the schema content is not inside a schema.
     */
    public Schema getSchema() throws XBRLException;
    
    /**
     * @return the target namespace of the schema that contains this fragment
     * or null if no targetNamespace attribute is defined for the containing
     * schema fragment.
     * @throws XBRLException
     */
    public URI getTargetNamespace() throws XBRLException;

    /**
     * @return a list of other (non XML Schema defined ) attributes
     * on the root element of the Schema declaration.  The list is 
     * empty if there are no other attributes.
     * @throws XBRLException
     */
    public List<Node> getOtherAttributes() throws XBRLException;
        
    /**
     * Mirrors the hasAttributeNS method of the org.w3c.dom.Element class.
     * @param namespace The namespace of the attribute being tested for
     * @param localname The local name of the attribute being tested for
     * @return true if the root element of the fragment has the specified
     * attribute and false otherwise.
     * @throws XBRLException
     */
    public boolean hasOtherAttribute(URI namespace, String localname) throws XBRLException;    
    
    /**
     * Mirrors the getAttributeNS method of the org.w3c.dom.Element class.
     * @param namespace The namespace of the attribute being tested for
     * @param localname The local name of the attribute being tested for
     * @return the value of the attribute if it occurs on 
     * the root element of the fragment and null otherwise.
     */
    public String getOtherAttribute(URI namespace, String localname) throws XBRLException;  
    
    /**
     * @return a list of annotation child elements for the given fragment 
     * or the empty list if there are no child annotations.
     * @throws XBRLException
     */
    public List<Element> getAnnotations() throws XBRLException;    
    
    /**
     * @return the the XML Schema ID attribute value or null if the 
     * schema content root element has no XML Schema ID attribute.
     * @throws XBRLException
     */
    public String getSchemaId() throws XBRLException;

}
