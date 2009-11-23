package org.xbrlapi;

import java.net.URI;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
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
    public List<Locator> getReferencingLocators() throws XBRLException;    
    
    /**
     * Get the index of the parent fragment or null if the fragment
     * does not have a parent fragment.
     * @return The index of the parent fragment or null if the 
     * fragment does not have a parent fragment.
     * @throws XBRLException if the parent fragment index is not available.
     */
    public String getParentIndex() throws XBRLException;
    
    /**
     * @return true if the fragment is the root of an XML document
     * and false otherwise.
     * @throws XBRLException
     * @see Fragment#isChild()
     */
    public boolean isRoot() throws XBRLException;
    
    /**
     * Get the sequence of steps through the parent fragment DOM to the parent element.
     * @return The sequence through the parent fragment data to the parent element of this fragment.
     * @throws XBRLException
     */
    public String[] getSequenceToParentElement() throws XBRLException;   
    
    /**
     * @return the sequence of steps through the parent fragment DOM to the 
     * parent element of this fragment.
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
     * Specifies the set of ancestor elements of the element in the parent
     * fragment that is the insertion point for this fragment's root element.
     * The ancestor elements are identified by their sibling order in the 
     * parent fragment (after all other fragments have been carved out of it).
     * Note that the root element of the parent fragment is not part of the sequence
     * because that is always identified by a value of 1 - being an only child.
     * @param parent The parent fragment.
     * @throws XBRLException
     */
    public void setSequenceToParentElement(Fragment parent) throws XBRLException;
    

    



    
    /**
     * Add an ID (used in XPointer resolution) to the metadata.
     * @param id The value of the ID.
     * @throws XBRLException.
     */
    public void appendID(String id) throws XBRLException;
    

    
    
    
    /**
     * Add an element Scheme XPointer Expression to the metadata.
     * @param expression The XPointer expression
     * @throws XBRLException
     */
    public void appendElementSchemeXPointer(String expression) throws XBRLException;
    
    /**
     * @return the ID XPointer expression for this fragment or
     * the element Scheme XPointer expression if there is no shorthand 
     * ID-based XPointer expression.  What is returned is the entire string
     * that is to be appended to the relevant URI to identify the fragment, including
     * the # symbol and everything that follows it.
     * 
     * @throws XBRLException
     */
    public String getXPointerExpression() throws XBRLException;
    
    /**
     * @return the element scheme XPointer expression for this fragment.
     * @throws XBRLException
     */
    public String getElementSchemeXPointerExpression() throws XBRLException;
    
    /**
     * @return the shorthand ID XPointer expression for this fragment or null
     * if there is none.
     * @throws XBRLException
     */
    public String getIDXPointerExpression() throws XBRLException;
    

    
    /**
     * Get the namespace of the fragment root element.
     *
     * @throws XBRLException
     */
    public URI getNamespace() throws XBRLException;

    /**
     * Get the local name of the fragment's root element
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
     * @return true iff the fragment has a parent fragment and
     * false otherwise.
     * @throws XBRLException
     */
    public boolean isChild() throws XBRLException;
    
    /**
     * @param descendant The candidate descendant fragment.
     * @return true if the candidate descendant is actually 
     * a descendant fragment and false otherwise.
     * @throws XBRLException
     */
    public boolean isAncestorOf(Fragment descendant) throws XBRLException;
    
    /**
     * Gets all child fragments.
     * @return the fragment list of children fragments or the empty list if no child
     * fragments exist in the data store.
     * @throws XBRLException
     */
    public List<Fragment> getAllChildren() throws XBRLException;
    
    /**
     * @return the set of indices of all child fragments of this fragment.
     * @throws XBRLException
     */
    public Set<String> getAllChildrenIndices() throws XBRLException;
    
    
    /**
     * @return the list of simple links that are children of this fragment.
     * @throws XBRLException
     */
    public List<SimpleLink> getSimpleLinks() throws XBRLException;    
    
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
    public URI getNamespaceFromQName(String qname, Node node) throws XBRLException;
    
    
    
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
    public List<LabelResource> getLabels() throws XBRLException;
    
    
    
    /**
     * @param role The XLink role value
     * @return the list of labels for this fragment with the specified XLink role.
     * @throws XBRLException
     */
    public List<LabelResource> getLabelsWithResourceRole(URI role) throws XBRLException;
    
    /**
     * @param languages the list of language codes in order of preference from most preferred to least
     * preferred, eventually allowing any language if no explicit preference match.
     * @param labelRoles the list of label resource roles in order of preference from most preferred 
     * to least preferred, eventually allowing any resource role.
     * @return the list of labels that best match the specified search criteria.
     * Note that a label role preference which 
     * takes precedence over a language preference.
     * @throws XBRLException
     */
    public List<LabelResource> getLabels(List<String> languages, List<URI> labelRoles) throws XBRLException;
    
    /**
     * @param languages the list of language codes in order of preference from most preferred to least
     * preferred, eventually allowing any language if no explicit preference match.
     * @param labelRoles the list of label resource roles in order of preference from most preferred 
     * to least preferred, eventually allowing any resource role.
     * @param linkRoles the list of extended link roles in order of preference from most preferred 
     * to least preferred, eventually allowing any link role.
     * @return the list of labels that best match the specified search criteria.
     * Note that a link role preference takes precedence over a label role preference which 
     * takes precedence over a language preference.
     * @throws XBRLException
     */
    public List<LabelResource> getLabels(List<String> languages, List<URI> labelRoles, List<URI> linkRoles) throws XBRLException;    
    
    
    /**
     * @param role The XLink role value
     * @return the list of references for this fragment with the specified XLink role.
     * @throws XBRLException
     */
    public List<ReferenceResource> getReferencesWithResourceRole(String role) throws XBRLException;
    
    
    
    
    /**
     * @param language The xml:lang language codevalue
     * @return the list of labels for this fragment with the specified language code.
     * @throws XBRLException
     */
    public List<LabelResource> getLabelsWithLanguage(String language) throws XBRLException;
    
    


    
    /**
     * @param language The xml:lang language code value
     * @param role The XLink role value
     * @return the list of labels for this fragment with the specified language code and XLink role.
     * @throws XBRLException
     */
    public List<LabelResource> getLabelsWithLanguageAndResourceRole(String language, URI role) throws XBRLException;
    
    /**
     * @param language The xml:lang language codevalue
     * @param resourceRole The XLink resource role value on the label
     * @param linkRole The XLink extended link role value on the
     * extended link containing the label.
     * @return the list of labels for this fragment with the specified language code 
     * and XLink resource and extended link roles.
     * @throws XBRLException
     */
    public List<LabelResource> getLabelsWithLanguageAndResourceRoleAndLinkRole(String language, URI resourceRole, URI linkRole) throws XBRLException;    
    
    /**
     * @return a list of references for the fragment based on XBRL 2.1 reference arcs.
     * @throws XBRLException
     */
    public List<ReferenceResource> getReferences() throws XBRLException;
    
    /**
     * @param language The xml:lang language code value
     * @return the list of references for this fragment with the specified language code.
     * @throws XBRLException
     */
    public List<ReferenceResource> getReferencesWithLanguage(String language) throws XBRLException;
    
    /**
     * @param language The xml:lang language code value
     * @param role The XLink role value
     * @return the list of references for this fragment with the specified language code and XLink role.
     * @throws XBRLException
     */
    public List<ReferenceResource> getReferencesWithLanguageAndRole(String language, URI role) throws XBRLException;
    
    /**
     * @param language The xml:lang language code value
     * @param resourceRole The XLink resource role value on the reference
     * @param linkRole The XLink extended link role value on the
     * extended link containing the reference.
     * @return the list of references for this fragment with the specified language code and XLink resource and link roles.
     * @throws XBRLException
     */
    public List<ReferenceResource> getReferencesWithLanguageAndResourceRoleAndLinkRole(String language, URI resourceRole, URI linkRole) throws XBRLException;    
    
    /**
     * Get a specific child fragment.
     * @param type The fragment type of the required child
     * @param index The index of the required child fragment (among other children of the same type)
     * with the first child being at index 0.
     * @return the child fragment or null if there are no children fragments of the specified type.
     * @throws XBRLException if the index is out of bounds
     */
    public Fragment getChild(String type, int index) throws XBRLException;

    /**
     * Gets the child fragments with the specified fragment type.
     * @param type The required fragment type of the child.
     * EG: If a list of
     *  org.xbrlapi.impl.ReferenceArcImpl fragments is required then
     *  this parameter would have a value of "ReferenceArc".
     *  Note that if the parameter contains full stops, then it is used directly
     *  as the value for the fragment type, enabling fragment extensions to exploit this
     *  method without placing fragment implementations in the org.xbrlapi package.
     * @return the fragment list of children fragments that match the specified fragment type
     * @throws XBRLException
     */
    public <F extends Fragment> List<F> getChildren(String type) throws XBRLException;
    
    /**
     * Gets the child fragments with the specified fragment type.
     * @param requiredClass The required fragment class of the child fragments.
     * @return the fragment list of children fragments that match the specified fragment type
     * @throws XBRLException
     */
    public <F extends Fragment> List<F> getChildren(Class<?> requiredClass) throws XBRLException;

    /**
     * @param type The fragment type
     * @return The list of all fragments of the given fragment type.
     * @throws XBRLException
     */
    public Set<String> getChildrenIndices(String type) throws XBRLException;
    
    
    
}
