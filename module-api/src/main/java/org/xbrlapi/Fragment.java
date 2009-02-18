package org.xbrlapi;

import java.net.URI;
import java.util.List;
import java.util.Vector;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xbrlapi.networks.Networks;
import org.xbrlapi.networks.Relationship;
import org.xbrlapi.utilities.XBRLException;

/**
 * Defines the functionality exposed by any fragment.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */

public interface Fragment extends XML {

    /**
     * Get the root element of the fragment data.
     * @return an XML Element that is the root of the fragment data.
     * @throws XBRLException if the fragment does not have fragment data.
     */
    public Element getDataRootElement() throws XBRLException;
    
    /**
     * Tests if a fragment is new in the sense that it does not have a root data element.
     * This is only used by the SAX content builder to keep track of where the fragment
     * construction process is at.
     * TODO Decide if this can be eliminated by better decoupling the SAX content handler from the fragment implementation.
     * @return true if the fragment is new.
     * @throws XBRLException
     */
    public boolean isNewFragment() throws XBRLException;
    
    /**
     * Get the URI of the document containing this fragment.
     * @throws XBRLException
     */
    public URI getURI() throws XBRLException;
	
    /**
     * Set the URI of the fragment's document.
     * @param uri The string value of the document's absolute URI
     * @throws XBRLException.
     */
    public void setURI(URI uri) throws XBRLException;
    
    /**
     * Retrieves a list of all locators that target this fragment.
     * @return a list of all locators that target this fragment.  The list can be empty.
     * @throws XBRLException.
     */
    public FragmentList<Locator> getReferencingLocators() throws XBRLException;    
    
    /**
     * Get the index of the parent fragment or null if the fragment
     * does not have a parent fragment.
     * @return The index of the parent fragment or null if the 
     * fragment does not have a parent fragment.
     * @throws XBRLException if the parent fragment index is not available.
     */
    public String getParentIndex() throws XBRLException;
    
    /**
     * Get the sequence of steps through the parent fragment DOM to the parent element.
     * @return The sequence through the parent fragment data to the parent element of this fragment.
     * @throws XBRLException
     */
    public String[] getSequenceToParentElement() throws XBRLException;   
    
    /**
     * Get the sequence of steps through the parent fragment DOM to the parent element as a string.
     * @return The sequence through the parent fragment data to the parent element of this fragment.
     * @throws XBRLException
     */
    public String getSequenceToParentElementAsString() throws XBRLException;
    
    /**
     * Set the index of the parent fragment.
     * @param index The index of the parent fragment.
     * @throws XBRLException if the parent fragment index cannot be set.
     */
    public void setParentIndex(String index) throws XBRLException;    
    
    /**
     * Get the XPath to the element in the parent fragment that is the 
     * parent element of this fragment's root element.
     * @return The required xpath.
     * @throws XBRLException
     */
    public String getXPath() throws XBRLException;
    
    /**
     * Sets the sequence of child element counts that describe the
     * path through the parent fragment from its root to the element
     * that has this fragment's root as a child element.
     * @param children The vector of long integers giving the children counts for the parent fragment.
     * @throws XBRLException
     */
    public void setSequenceToParentElement(Vector<Long> children) throws XBRLException;
    
    /**
     * Get the number of children that precede this fragment's root element
     * in document order in the element that contains it (in the parent element).
     * @return The required number of preceding siblings.
     * @throws XBRLException
     */
    public String getPrecedingSiblings() throws XBRLException;
    
    /**
     * Set the number of children that precede this fragment's root element
     * in document order in the element that contains it (in the parent element).
     * @param children The list of children counts for the parent fragment.
     * @throws XBRLException if the parent fragment index cannot be set.
     */
    public void setPrecedingSiblings(Vector<Long> children) throws XBRLException;

    /**
     * Remove a DTS relationship to the fragment, identifying another
     * DTS fragment that this fragment is no longer related to.
     * @param index The index of the target of the relationship to be removed.
     * @throws XBRLException.
     */
    public void removeRelationship(String index) throws XBRLException;
    
    /**
     * Add an ID (used in XPointer resolution) to the metadata.
     * @param id The value of the ID.
     * @throws XBRLException.
     */
    public void appendID(String id) throws XBRLException;
    
    /**
     * Remove an ID from the metadata.
     * @param id The id to remove.
     * @throws XBRLException
     */
    public void removeID(String id) throws XBRLException;
    
    
    
    /**
     * Add an element Scheme XPointer Expression to the metadata.
     * @param expression The XPointer expression
     * @throws XBRLException.
     */
    public void appendElementSchemeXPointer(String expression) throws XBRLException;
    
    /**
     * Remove an element Scheme XPointer Expression from the metadata.
     * @param expression The XPointer expression.
     * @throws XBRLException.
     */
    public void removeElementSchemeXPointer(String expression) throws XBRLException;
    
    /**
     * Get the namespace of the fragment root element.
     *
     * @throws XBRLException
     */
    public URI getNamespace() throws XBRLException;

    /**
     * Get the local name of the fragment's root element
     *
     * @throws XBRLException
     */
    public String getLocalname() throws XBRLException;

    /**
     * Get the element in the parent fragment that has this fragment as its child.
     * @param parentDataRootElement The root element of the parent fragment's data.
     * @return the element of the parent fragment that has this fragment as
     * its child (or null if no parent exists).
     * @throws XBRLException
     */
    public Element getParentElement(Element parentDataRootElement) throws XBRLException;

    /**
     * Get the parent fragment of this fragment or null if there is none.
     * @return the parent fragment or null if none exists.
     * @throws XBRLException
     */
    public Fragment getParent() throws XBRLException;
    
    /**
     * Gets all child fragments.
     * @return the fragment list of children fragments or the empty list if no child
     * fragments exist in the data store.
     * @throws XBRLException
     */
    public FragmentList<Fragment> getAllChildren() throws XBRLException;
    
    /**
     * @return the list of simple links that are children of this fragment.
     * @throws XBRLException
     */
    public FragmentList<SimpleLink> getSimpleLinks() throws XBRLException;    
    
    /**
     * Gets the ancestor (or self) fragment with the specified fragment type.
     * @param type The required fragment type of the ancestor (or self).
     * @return the first ancestor (or self) fragment that matches the specified fragment type
     * working up the XML document structure from the supplied fragment to the root
     * of the XML document.
     * TODO Modify to use Generics so the fragment returned can be of a specific type.
     * @throws XBRLException if no such ancestor fragment exists.
     */
    public Fragment getAncestorOrSelf(String type) throws XBRLException;
    
    /**
     * Returns the Namespace for a QName in the context of a node in the fragment.
     * @param qname The qName value to resolve
     * @param node The node in the fragment data (as a DOM representation) to start QName resolution from.
     * @return the namespace declared on the fragment for the QName
     * @throws XBRLException if the namespace is not declared
     */
    public String getNamespaceFromQName(String qname, Node node) throws XBRLException;
    
    /**
     * Returns the local name for a QName
     * 
     * @param qname The qName value to resolve
     * @return the local name for the QName.
     */
    public String getLocalnameFromQName(String qname);
    
    /**
     * Returns the Namespace prefix for a QName
     * 
     * @param qname The qName value to resolve
     * @return the namespace prefix in the QName
     * @throws XBRLException 
     */
    public String getPrefixFromQName(String qname);
 
    /**
     * @return a list of label resources for the fragment.
     * @throws XBRLException
     */
    public FragmentList<LabelResource> getLabels() throws XBRLException;
    
    /**
     * @param networks The networks to use to obtain the labels.
     * @return a list of label resources for the fragment.
     * @throws XBRLException
     */
    public FragmentList<LabelResource> getLabels(Networks networks) throws XBRLException;    
    
    /**
     * @param role The XLink role value
     * @return the list of labels for this fragment with the specified XLink role.
     * @throws XBRLException
     */
    public FragmentList<LabelResource> getLabelsWithRole(String role) throws XBRLException;
    
    /**
     * @param networks The networks to get the labels from.
     * @param role The XLink role value
     * @return the list of labels for this fragment with the specified XLink role.
     * @throws XBRLException
     */
    public FragmentList<LabelResource> getLabelsWithRole(Networks networks, String role) throws XBRLException;    
    
    /**
     * @param language The xml:lang language codevalue
     * @return the list of labels for this fragment with the specified language code.
     * @throws XBRLException
     */
    public FragmentList<LabelResource> getLabelsWithLanguage(String language) throws XBRLException;
    
    /**
     * @param networks The networks to get the labels from.
     * @param language The xml:lang language codevalue
     * @return the list of labels for this fragment with the specified language code.
     * @throws XBRLException
     */
    public FragmentList<LabelResource> getLabelsWithLanguage(Networks networks, String language) throws XBRLException;    

    /**
     * @param networks The networks to get the labels from.
     * @param language The xml:lang language codevalue
     * @param role The XLink role value
     * @return the list of labels for this fragment with the specified language code and XLink role.
     * @throws XBRLException
     */
    public FragmentList<LabelResource> getLabelsWithLanguageAndRole(Networks networks, String language, String role) throws XBRLException;
    
    /**
     * @param language The xml:lang language codevalue
     * @param role The XLink role value
     * @return the list of labels for this fragment with the specified language code and XLink role.
     * @throws XBRLException
     */
    public FragmentList<LabelResource> getLabelsWithLanguageAndRole(String language, String role) throws XBRLException;    
    
    /**
     * @return a list of references for the fragment based on XBRL 2.1 reference arcs.
     * @throws XBRLException
     */
    public FragmentList<ReferenceResource> getReferences() throws XBRLException;    
    
    /**
     * @return the collection of networks including
     * relationships that involve this node as a source 
     * or a target.  Note that the networks are not 
     * completed in the sense that arcs are followed to
     * nodes that are then used to gather further relationships. 
     * @throws XBRLException
     */
    public Networks getNetworks() throws XBRLException;
    
    /**
     * @param arcrole the required arcrole value.
     * @return the collection of networks including
     * relationships that involve this node as a source 
     * or a target and that are expressed by arcs with the given
     * arcrole value.  Note that the networks are not 
     * completed in the sense that arcs are followed to
     * nodes that are then used to gather further relationships. 
     * @throws XBRLException
     */
    public Networks getNetworksWithArcrole(String arcrole) throws XBRLException;
    
    /**
     * @param arcrole the required arcrole value.
     * @return the collection of networks including
     * relationships that involve this node as a source 
     * (not a target) and that are expressed by arcs with the given
     * arcrole value.  Note that the networks are not 
     * completed in the sense that arcs are followed to
     * nodes that are then used to gather further relationships. 
     * @throws XBRLException
     */
    public Networks getNetworksFromWithArcrole(String arcrole) throws XBRLException;
    
    /**
     * @param arcrole the required arcrole value.
     * @return a list of relationships (active and otherise) 
     * that involve this fragment as a target node
     * (not a source) and that are expressed by arcs with the given
     * arcrole value. 
     * @throws XBRLException
     */
    public List<Relationship> getRelationshipsToWithArcrole(String arcrole) throws XBRLException;    
    
    /**
     * @param linkrole the required linkrole value.
     * @param arcrole the required arcrole value.
     * @return the collection of networks including
     * relationships that involve this node as a source 
     * (not a target) and that are expressed by arcs with the given
     * arcrole value in extended links with the given linkrole value.  
     * Note that the networks are not 
     * completed in the sense that arcs are followed to
     * nodes that are then used to gather further relationships. 
     * @throws XBRLException
     */
    public Networks getNetworksFromWithRoleAndArcrole(String linkrole, String arcrole) throws XBRLException;    
    
    /**
     * @param arcrole The <code>xlink:arcrole</code> attribute on the arcs expressing the 
     * relationships to the relative fragments.
     * @param linkrole The <code>xlink:role</code> attribute on the extended links containing
     * the relationships to the relative fragments.  This criteria is not used if the value is null.
     * @param resourcerole The required <code>xlink:role</code> attribute value on the 
     * related fragments. This criteria is not used if the value is null.
     * @param language The required value of the <code>xml:language</code> language 
     * encoding on the relative fragments.  This criteria is not used if the value is null.
     * @param getTargets The boolean value that is set to true if the relatives are required
     * to be targets of relationships from this fragment and that is set to false otherwise.
     * @return a list of fragments that meet the specified selection criteria.
     * @throws XBRLException
     */
    public <F extends Fragment> FragmentList<F> getRelatives(String arcrole, String linkrole, String resourcerole, String language, boolean getTargets) throws XBRLException;    
    

}
