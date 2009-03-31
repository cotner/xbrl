package org.xbrlapi.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xbrlapi.ExtendedLink;
import org.xbrlapi.Fragment;
import org.xbrlapi.LabelResource;
import org.xbrlapi.Locator;
import org.xbrlapi.ReferenceResource;
import org.xbrlapi.SimpleLink;
import org.xbrlapi.builder.Builder;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * Implements the functionality that is common to all types of XBRL fragments.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class FragmentImpl extends XMLImpl implements Fragment {
	
	protected static Logger logger = Logger.getLogger(FragmentImpl.class);	

    /**
     * @see org.xbrlapi.Fragment#getAncestorOrSelf(String)
     */
    public Fragment getAncestorOrSelf(String type) throws XBRLException {
    	if (getType().equals(type)) return this;
    	Fragment parent = this.getParent();
    	if (parent == null) throw new XBRLException("No ancestor (or self) fragments match the given type: " + type);
        return parent.getAncestorOrSelf(type);
    }
    
    /**
     * @see org.xbrlapi.Fragment#getChildren(String)
     */
    public <F extends Fragment> List<F> getChildren(String type) throws XBRLException {
    	String query = "/*[@parentIndex='" + getIndex() + "' and @type='" + type + "']";
    	List<F> fragments = getStore().<F>query(query);
    	return fragments;
    }
    
    /**
     * @see org.xbrlapi.Fragment#getSimpleLinks()
     */
    public List<SimpleLink> getSimpleLinks() throws XBRLException {
    	return this.getStore().<SimpleLink>getChildFragments("SimpleLink",this.getIndex());
    }
    
    /**
     * @see org.xbrlapi.Fragment#getAllChildren()
     */
    public List<Fragment> getAllChildren() throws XBRLException {
    	String xpath = "/*[@parentIndex='" + getIndex() + "']";
    	List<Fragment> fragments = getStore().<Fragment>query(xpath);
    	return fragments;
    }
    
    /**
     * @see org.xbrlapi.Fragment#getChild(String, int)
     */
    public Fragment getChild(String type, int index) throws XBRLException {
    	List<Fragment> children = getChildren(type);
    	if (children == null) return null;
    	if (index >= children.size()) throw new XBRLException("The index is too high.");
    	if (index < 0) throw new XBRLException("The index is too low.");
    	return children.get(index);   
    }

    /**
     * @see org.xbrlapi.Fragment#getDataRootElement()
     */
    public Element getDataRootElement() throws XBRLException {
    	
    	if (getBuilder() != null) {
    		return getBuilder().getData();
    	}

    	Element metadata = getMetadataRootElement();
    	Element dataContainer = (Element) metadata.getElementsByTagNameNS(Constants.XBRLAPINamespace,Constants.FragmentDataContainerElementName).item(0);
    	NodeList children = dataContainer.getChildNodes();
    	for (int i=0; i< children.getLength(); i++) 
    		if (children.item(i).getNodeType() == Node.ELEMENT_NODE) return (Element) children.item(i);
    	throw new XBRLException("The data element of the fragment could not be found.");
    }
    

    
    /**
     * @see org.xbrlapi.Fragment#isNewFragment()
     */
    public boolean isNewFragment() throws XBRLException {
    	if (getBuilder() == null) return false;
    	return getBuilder().isNewFragment();
    }

    /**
     * @see org.xbrlapi.Fragment#getURI()
     */
    public URI getURI() throws XBRLException {
        try {
            return new URI(this.getMetaAttribute("uri"));
        } catch (URISyntaxException e) {
            throw new XBRLException(this.getMetaAttribute("uri") + " has an invalid URI syntax.");
        }
    }
    

    
    /**
     * @see org.xbrlapi.Fragment#setURI(URI)
     */
    public void setURI(URI uri) throws XBRLException {
        this.setMetaAttribute("uri",uri.toString());
    }
    
    /**
     * @see org.xbrlapi.Fragment#getReferencingLocators()
     */
    public List<Locator> getReferencingLocators() throws XBRLException {
    	
    	// Construct the Query
    	String predicate = "*/link:loc and @targetDocumentURI='"+ getURI() +"' and (";
    	NodeList xptrs = this.getMetadataRootElement().getElementsByTagNameNS(Constants.XBRLAPINamespace,"xptr");
    	for (int i=0; i<xptrs.getLength(); i++) {
    		String value = ((Element) xptrs.item(i)).getAttribute("value").trim();
    		predicate += "@targetPointerValue='" + value +"'";
    		if (i == (xptrs.getLength()-1)) 
    			predicate += ")";
    		else
    			predicate += " or ";
    	}
    	String query = "/*[" + predicate + "]";
    	
    	return getStore().<Locator>query(query);
    }
    

    
    /**
     * @see org.xbrlapi.Fragment#getLabels()
     */
    public List<LabelResource> getLabels() throws XBRLException {
        List<LabelResource> labels = getStore().<LabelResource>getTargets(getIndex(),null,Constants.LabelArcrole());
        List<LabelResource> genericLabels = getStore().<LabelResource>getTargets(getIndex(),null,Constants.GenericLabelArcrole());
        labels.addAll(genericLabels);
        return labels;
    }    
    

    
    /**
     * @see org.xbrlapi.Fragment#getLabelsWithRole(String)
     */
    public List<LabelResource> getLabelsWithRole(String role) throws XBRLException {
        
        List<LabelResource> result = new Vector<LabelResource>();
        List<LabelResource> labels = getLabels();
        for (LabelResource label: labels) {
            URI r = label.getResourceRole();
            if (r != null) {
                if (r.equals(role)) result.add(label);
            }
        }
        return result;
    }    
    

    
    /**
     * @see org.xbrlapi.Fragment#getLabelsWithLanguage(String)
     */
    public List<LabelResource> getLabelsWithLanguage(String language) throws XBRLException {
        List<LabelResource> labels = new Vector<LabelResource>();
        List<LabelResource> candidates = this.getLabels();
        for (LabelResource label: candidates) {
            String l = label.getLanguage();
            if (l == null) if (l == null) labels.add(label);
            else if (l.equals(language)) labels.add(label);
        }
        return labels;
    }


    
    /**
     * @see org.xbrlapi.Fragment#getLabelsWithLanguageAndResourceRole(String, URI)
     */
    public List<LabelResource> getLabelsWithLanguageAndResourceRole(String language, URI role) throws XBRLException {
        List<LabelResource> labels = new Vector<LabelResource>();
        List<LabelResource> candidates = this.getLabels();
        for (LabelResource label: candidates) {
            String l = label.getLanguage();
            URI r = label.getResourceRole();
            if (l != null && r != null) {
                if (l.equals(language) && r.equals(role)) labels.add(label);
            } else if (l != null) {
                if (l.equals(language) && role == null) labels.add(label);
            } else if (r != null) {
                if (r.equals(role) && language == null) labels.add(label);
            } else {
                if (role == null && language == null) labels.add(label);
            }
        }
        return labels;
    }
    
    /**
     * @see org.xbrlapi.Fragment#getLabelsWithLanguageAndResourceRole(String, URI)
     */
    public List<LabelResource> getLabelsWithLanguageAndResourceRoleAndLinkRole(String language, URI resourceRole, URI linkRole) throws XBRLException {
        List<LabelResource> labels = new Vector<LabelResource>();
        List<LabelResource> candidates = this.getLabels();
        for (LabelResource label: candidates) {
            if (linkRole == null || label.getExtendedLink().getLinkRole().equals(linkRole)) {
                String l = label.getLanguage();
                URI r = label.getResourceRole();
                if (l != null && r != null) {
                    if (l.equals(language) && r.equals(resourceRole)) labels.add(label);
                } else if (l != null) {
                    if (l.equals(language) && resourceRole == null) labels.add(label);
                } else if (r != null) {
                    if (r.equals(resourceRole) && language == null) labels.add(label);
                } else {
                    if (resourceRole == null && language == null) labels.add(label);
                }
            }
        }
        return labels;
    }    
    
    
    

    
    /**
     * @see org.xbrlapi.Fragment#getReferences()
     */
    public List<ReferenceResource> getReferences() throws XBRLException {
        List<ReferenceResource> references = getStore().getTargets(getIndex(),null,Constants.ReferenceArcrole());
        List<ReferenceResource> genericReferences = getStore().getTargets(getIndex(),null,Constants.GenericReferenceArcrole());
        references.addAll(genericReferences);
        return references;
    }
    
    /**
     * @see org.xbrlapi.Fragment#getReferencesWithLanguage(String)
     */
    public List<ReferenceResource> getReferencesWithLanguage(String language) throws XBRLException {
        List<ReferenceResource> references = new Vector<ReferenceResource>();
        List<ReferenceResource> candidates = this.getReferences();
        for (ReferenceResource reference: candidates) {
            String l = reference.getLanguage();
            if (l == null) if (l == null) references.add(reference);
            else if (l.equals(language)) references.add(reference);
        }
        return references;
    }
    
    /**
     * @see org.xbrlapi.Fragment#getReferencesWithLanguageAndRole(String, URI)
     */
    public List<ReferenceResource> getReferencesWithLanguageAndRole(String language, URI role) throws XBRLException {
        List<ReferenceResource> references = new Vector<ReferenceResource>();
        List<ReferenceResource> candidates = this.getReferences();
        for (ReferenceResource reference: candidates) {
            String l = reference.getLanguage();
            URI r = reference.getResourceRole();
            if (l != null && r != null) {
                if (l.equals(language) && r.equals(role)) references.add(reference);
            } else if (l != null) {
                if (l.equals(language) && role == null) references.add(reference);
            } else if (r != null) {
                if (r.equals(role) && language == null) references.add(reference);
            } else {
                if (role == null && language == null) references.add(reference);
            }
        }
        return references;
    }
    
    /**
     * @see org.xbrlapi.Fragment#getReferencesWithLanguageAndResourceRole(String, URI)
     */
    public List<ReferenceResource> getReferencesWithLanguageAndResourceRoleAndLinkRole(String language, URI resourceRole, URI linkRole) throws XBRLException {
        List<ReferenceResource> references = new Vector<ReferenceResource>();
        List<ReferenceResource> candidates = this.getReferences();
        for (ReferenceResource reference: candidates) {
            if (linkRole == null || reference.getExtendedLink().getLinkRole().equals(linkRole)) {
                String l = reference.getLanguage();
                URI r = reference.getResourceRole();
                if (l != null && r != null) {
                    if (l.equals(language) && r.equals(resourceRole)) references.add(reference);
                } else if (l != null) {
                    if (l.equals(language) && resourceRole == null) references.add(reference);
                } else if (r != null) {
                    if (r.equals(resourceRole) && language == null) references.add(reference);
                } else {
                    if (resourceRole == null && language == null) references.add(reference);
                }
            }
        }
        return references;
    }    
    



    

    


    /**
     * Map of extended links.
     */
    private Map<String,ExtendedLink> links = new HashMap<String,ExtendedLink>();
    




    /**
     * @see org.xbrlapi.Fragment#getParentIndex()
     */
    public String getParentIndex() throws XBRLException {
    	return getMetaAttribute("parentIndex");
    }
    
    /**
     * @see org.xbrlapi.Fragment#isRoot()
     */
    public boolean isRoot() throws XBRLException {
        return (getParentIndex() == null);
    }
    
    /**
     * @see org.xbrlapi.Fragment#setParentIndex(String)
     */
    public void setParentIndex(String index) throws XBRLException {
        setMetaAttribute("parentIndex",index);
    }

    /**
     * @see org.xbrlapi.Fragment#getXPath()
     */
    public String getXPath() throws XBRLException {

    	String value = getMetaAttribute("SequenceToParentElement");
    	if (value.equals("")) return ".";
    	
    	String[] sequence = value.split(" ");
    	StringBuffer xpath = new StringBuffer(".");
    	for (int i=0; i<sequence.length; i++) {
    		xpath.append("/*[" + sequence[i] + "]");
    	}    	
    	return xpath.toString();
    }
    
    
    /**
     * Determines the sibling order of 
     * @param current The element that we are determining
     * the sibling order for.  
     * @return the order of the element, counting sibling 
     * elements from the left in document order.
     */
    private int getSiblingOrder(Element current) {
        
        Node previous = current.getPreviousSibling();
        
        while (previous != null) {
            if (previous.getNodeType() != Node.ELEMENT_NODE) 
                previous = previous.getPreviousSibling();
            else break;
        }

        if (previous == null) return 1;
        return getSiblingOrder((Element) previous) + 1;
    }
    
    /**
     * @see org.xbrlapi.Fragment#setSequenceToParentElement(Fragment)
     */
    public void setSequenceToParentElement(Fragment parent) throws XBRLException {

        Builder parentBuilder = parent.getBuilder();
        if (parentBuilder == null) throw new XBRLException("This method is not usable after the fragment has been built.");
        Element current = parentBuilder.getInsertionPoint();
        Element next = (Element) current.getParentNode();
        Stack<Integer> values = new Stack<Integer>();

        while (! next.getNamespaceURI().equals(Constants.XBRLAPINamespace)) {
            values.push(new Integer(getSiblingOrder(current)));
            current = next;
            next = (Element) next.getParentNode();
        }

        StringBuffer value = new StringBuffer("");
    	while (! values.empty()) {
    	    Integer v = values.pop();
			if (value.length() == 0) {
				value.append(v.toString());
			} else {
				value.append(" " + v.toString()); 
			}
		}

    	String result = value.toString();
    	if (! result.equals(""))
    	setMetaAttribute("SequenceToParentElement",result);
    	
    }
    
    /**
     * @see org.xbrlapi.Fragment#getSequenceToParentElement()
     */
    public String[] getSequenceToParentElement() throws XBRLException {
    	return this.getSequenceToParentElementAsString().split(" ");
    }
    
    /**
     * @see org.xbrlapi.Fragment#getSequenceToParentElementAsString()
     */
    public String getSequenceToParentElementAsString() throws XBRLException {
    	String value = this.getMetaAttribute("SequenceToParentElement");
    	if (value == null) return "";
    	return value;
    }    

    /**
     * @see org.xbrlapi.Fragment#getPrecedingSiblings()
     */
    public String getPrecedingSiblings() throws XBRLException {
    	return getMetaAttribute("precedingSiblings");
    }




    
    /**
     * @see org.xbrlapi.Fragment#appendID(String)
     * TODO Eliminate the ID metadata element given the existence of the xptr elements.
     */
    public void appendID(String id) throws XBRLException {
    	HashMap<String,String> attributes = new HashMap<String,String>();
    	attributes.put("id",id);
		appendMetadataElement("ID",attributes);
    }

    
    
    /**
     * @see org.xbrlapi.Fragment#appendElementSchemeXPointer(String)
     */
    public void appendElementSchemeXPointer(String expression) throws XBRLException {
    	HashMap<String,String> attributes = new HashMap<String,String>();
    	attributes.put("value",expression);
		appendMetadataElement("xptr",attributes);
    }

    
    
    /**
     * @see org.xbrlapi.Fragment#getNamespace()
     */
    public URI getNamespace() throws XBRLException {
    	if (getDataRootElement() == null) {
    		throw new XBRLException("The XML fragment root node is null.");
    	}
    	try {
    	    return new URI(getDataRootElement().getNamespaceURI());
    	} catch (URISyntaxException e) {
    	    throw new XBRLException("The data root element namespace is not a valid URI.");
    	}
    }
    
    /**
     * @see org.xbrlapi.Fragment#getLocalname()
     */
    public String getLocalname() throws XBRLException {
    	return getDataRootElement().getLocalName();
    }
    
    /**
     * @see org.xbrlapi.Fragment#getPrefixFromQName(String)
     */
    public String getPrefixFromQName(String qname) {
    	// Get the required namespace prefix from the QName
    	String prefix = "";
		int delimiterIndex = qname.indexOf(':');
    	if (delimiterIndex > 0) {
    		prefix = qname.substring(0,delimiterIndex);
    	}
    	return prefix;
    }
    

    
    /**
     * Algorithm is as follows:
     * <ol>
     *  <li>If the node is an attribute, redo with the parent node.</li>
     *  <li>If the node is a XBRLAPI metadata element redo with right node in parent fragment.</li>
     *  <li>If the node is an element in the fragment data:</li>
     *   <ol>
     *    <li>Generate namespace declaration attribute name - 'xmlns:...' to search for.</li>
     *    <li>Try to match the QName prefix to the element's prefix to see if the element namespace is appropriate to return.</li>
     *    <li>Try to find the attribute doing the namespace declaration on the element and use that.</li>
     *    <li>If that fails, redo the search using the parent node.</li>
     *   </ol>
     * </ol>
     * 
     * @see org.xbrlapi.Fragment#getNamespaceFromQName(String, Node)
     */
    public URI getNamespaceFromQName(String qname, Node node) throws XBRLException {
                
        // If we have an attribute - go straight to working with the parent element.
        if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
            Node parent = node.getParentNode();
            if (parent == null) throw new XBRLException("The attribute has no parent element so the namespace for " + qname + " cannot be determined.");
            return getNamespaceFromQName(qname, parent);
        }
        
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;

            // Go to parent fragment if we are looking at the container element for the fragment data.
            String elementNamespace = element.getNamespaceURI();
            if (elementNamespace != null) {
                if (elementNamespace.equals(Constants.XBRLAPINamespace)) {
                    if (this.isRoot()) throw new XBRLException("No namespace is defined for QName " + qname);
                    Fragment parent = getParent();
                    if (parent == null) throw new XBRLException("A parent fragment is missing from the data store preventing QName resolution for " + qname);
                    Element parentElement = getParentElement(parent.getDataRootElement());
                    return parent.getNamespaceFromQName(qname, parentElement);
                }
            }
            
            // Try to exploit a known namespace mapping for the element or one of its attributes
            String prefix = getPrefixFromQName(qname);
            if ((node.getPrefix() != null) && (node.getPrefix().equals(prefix))) {
                try {
                    return new URI(node.getNamespaceURI());
                } catch (URISyntaxException e) {
                    throw new XBRLException("The namespace URI " + node.getNamespaceURI() + " has invalid syntax.",e);
                }
            }
            NamedNodeMap attrs = node.getAttributes();
            for (int i=0; i<attrs.getLength(); i++) {
                Node attr = attrs.item(i);
                if ((attr.getPrefix() != null) && (attr.getPrefix().equals(prefix))) {
                    try {
                        return new URI(attr.getNamespaceURI());
                    } catch (URISyntaxException e) {
                        throw new XBRLException("The namespace URI " + attr.getNamespaceURI() + " has invalid syntax.",e);
                    }
                }
            }
            
            // Create NS prefix declaration for the QName being sought.
            String declaration = "xmlns";
            if (!prefix.equals("")) {
                declaration = declaration + ":" + prefix;
            }
            
            // Check for a namespace declaration on the current element
            if (element.hasAttribute(declaration)) {
                String ns = element.getAttribute(declaration);
                if (ns.equals("")) {
                    return null;// The namespace prefix has been undefined by the declaration.
                }
                try {
                    return new URI(ns);
                } catch (URISyntaxException e) {
                    throw new XBRLException("The namespace URI " + ns + " has invalid syntax.",e);
                }
            }
            
            return getNamespaceFromQName(qname, element.getParentNode());
            
        }
            
        throw new XBRLException("An element or attribute node is expected.");

    }
    

    /**
     * @see org.xbrlapi.Fragment#getParent()
     */
    public Fragment getParent() throws XBRLException {
    	String parentIndex = this.getParentIndex();
    	if (parentIndex == null) return null;
    	return getStore().getFragment(parentIndex);
    }
    
    /**
     * This method operates as follows:
     * <ol>
     *  <li>If the sequence to the parent element indicates that the 
     *  data root element is the parent - use that.</li>
     *  <li>Loop through the sequence to the parent element.</li>
     * </ol>
     * @see org.xbrlapi.Fragment#getParentElement(Element)
     */
    public Element getParentElement(Element parentDataRootElement) throws XBRLException {

    	String[] sequence = getSequenceToParentElement();
    	
    	// If there is no data about the sequence then just return the given parent data root element
    	if (sequence[0].equals("")) {
    	    return parentDataRootElement;
    	}

    	// Traverse the parent data DOM to find the parent element of this fragment's root element.
    	Element current = parentDataRootElement;
    	for (String value: sequence) {
            int elementOrder = (new Integer(value)).intValue();
            int elementsFound = 0;
            NodeList children = current.getChildNodes();
            int j = 0;
            // While there are still children to consider and we have not found 
            while ((elementsFound < elementOrder) && (j < children.getLength())) {
                Node child = children.item(j);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    elementsFound++;
                    if (elementsFound == elementOrder) current = (Element) child;
                }
                j++;
            }
            if ((j==children.getLength()) && (elementsFound < elementOrder)) {
                throw new XBRLException("The sequence to the parent element is incorrect.");
            }
    	}
    	return current;

    }

    /**
     * @see org.xbrlapi.Fragment#getLocalnameFromQName(String)
     */
    public String getLocalnameFromQName(String qname) {
    	String localname = qname;
		int delimiterIndex = qname.indexOf(':');
    	if (delimiterIndex > 0) {
    		localname = qname.substring(delimiterIndex+1,qname.length());
    	}
    	return localname;
    }
    



}
