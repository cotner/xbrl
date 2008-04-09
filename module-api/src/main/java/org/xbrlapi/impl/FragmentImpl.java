package org.xbrlapi.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xbrlapi.Arc;
import org.xbrlapi.ArcEnd;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.LabelResource;
import org.xbrlapi.Locator;
import org.xbrlapi.Networks;
import org.xbrlapi.ReferenceResource;
import org.xbrlapi.Relationship;
import org.xbrlapi.SimpleLink;
import org.xbrlapi.builder.Builder;
import org.xbrlapi.builder.BuilderImpl;
import org.xbrlapi.data.Store;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * Implements the functionality that is common to all types of XBRL fragments.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class FragmentImpl implements Fragment {
	
	protected static Logger logger = Logger.getLogger(FragmentImpl.class);	
	
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
	 * The DOM instantiation of the 
	 * fragment's root element.
	 */
	private Element rootElement = null;

	/**
	 * No argument constructor
	 * @throws XBRLEception
	 */
	public FragmentImpl() {
	}
	
	/**
	 * @see org.xbrlapi.Fragment#setStore(Store)
	 */
	public void setStore(Store store) throws XBRLException {
		if (this.store != null) {
			throw new XBRLException("The data store has already been specified for this fragment.");
		}
		this.store = store;
	}

	/**
	 * @see org.xbrlapi.Fragment#setBuilder(Builder)
	 */
	public void setBuilder(Builder builder) {
		builder.setMetaAttribute("type",getType());
		this.builder = builder;
	}
	
	/**
	 * @see org.xbrlapi.Fragment#getStore()
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
     * @see org.xbrlapi.Fragment#getAncestorOrSelf(String)
     */
    public Fragment getAncestorOrSelf(String type) throws XBRLException {
    	if (getType().equals(type)) return this;
    	Fragment parent = this.getParent();
    	if (parent == null) throw new XBRLException("No ancestor (or self) fragments match the given type: " + type);
        return parent.getAncestorOrSelf(type);
    }
    
    /**
     * Gets the child fragments with the specified fragment type.
     * @param type The required fragment type of the child.
     * @return the fragment list of children fragments that match the specified fragment type
     * @throws XBRLException
     */
    protected <F extends Fragment> FragmentList<F> getChildren(String type) throws XBRLException {
    	String query = "/" + Constants.XBRLAPIPrefix + ":fragment[@parentIndex='" + getFragmentIndex() + "' and @type='" + type + "']";
    	FragmentList<F> fragments = getStore().<F>query(query);
    	return fragments;
    }
    
    /**
     * @see org.xbrlapi.Fragment#getSimpleLinks()
     */
    public FragmentList<SimpleLink> getSimpleLinks() throws XBRLException {
    	return this.getStore().<SimpleLink>getChildFragments("SimpleLink",this.getFragmentIndex());
    }
    
    /**
     * @see org.xbrlapi.Fragment#getAllChildren()
     */
    public FragmentList<Fragment> getAllChildren() throws XBRLException {
    	String xpath = "/*[@parentIndex='" + getFragmentIndex() + "']";
    	FragmentList<Fragment> fragments = getStore().query(xpath);
    	return fragments;
    }
    
    /**
     * Get a specific child fragment.
     * @param type The fragment type of the required child
     * @param index The index of the required child fragment (among other children of the same type).
     * @return the child fragment or null if there are no children fragments of the specified type.
     * @throws XBRLException if the index is out of bounds
     */
    protected Fragment getChild(String type, int index) throws XBRLException {
    	FragmentList<Fragment> children = getChildren(type);
    	if (children == null) return null;
    	if (index >= children.getLength()) throw new XBRLException("The index is too high.");
    	if (index < 0) throw new XBRLException("The index is too low.");
    	return children.getFragment(index);   
    }
	
	/**
	 * @see org.xbrlapi.Fragment#getBuilder()
	 */
	public Builder getBuilder() {
		return builder;
	}

	/**
     * @see org.xbrlapi.Fragment#getResource()
     */
    public Element getResource() throws XBRLException {
    	return rootElement;
    }
	
	/**
	 * @see org.xbrlapi.Fragment#setResource(Element)
	 */
	public void setResource(Element rootElement) throws XBRLException {
		builder = null;
		if (rootElement == null) throw new XBRLException("The XML resource is null.");
		this.rootElement = rootElement;
	}

    /**
     * @see org.xbrlapi.Fragment#getDataRootElement()
     */
    public Element getDataRootElement() throws XBRLException {
    	
    	if (builder != null) {
    		return builder.getData();
    	}

    	Element metadata = getMetadataRootElement();
    	Element dataContainer = (Element) metadata.getElementsByTagNameNS(Constants.XBRLAPINamespace,Constants.FragmentDataContainerElementName).item(0);
    	NodeList children = dataContainer.getChildNodes();
    	for (int i=0; i< children.getLength(); i++) 
    		if (children.item(i).getNodeType() == Node.ELEMENT_NODE) return (Element) children.item(i);
    	throw new XBRLException("The data element of the fragment could not be found.");
    }
    
    /**
     * @see org.xbrlapi.Fragment#getMetadataRootElement()
     */
    public Element getMetadataRootElement() throws XBRLException {
    	if (builder != null) return builder.getMetadata();
    	return getResource();
    }

    /**
     * @see org.xbrlapi.Fragment#getDocumentNode()
     */
    public Document getDocumentNode() throws XBRLException {
    	if (builder != null) return builder.getData().getOwnerDocument();
    	return getResource().getOwnerDocument();
    }
    
    /**
     * @see org.xbrlapi.Fragment#isNewFragment()
     */
    public boolean isNewFragment() throws XBRLException {
    	if (getBuilder() == null) return false;
    	return getBuilder().isNewFragment();
    }

    /**
     * @see org.xbrlapi.Fragment#getFragmentIndex()
     */
    public String getFragmentIndex() {
    	return this.index;
    }
    
    /**
     * @see org.xbrlapi.Fragment#setFragmentIndex(String)
     */
    public void setFragmentIndex(String index) throws XBRLException {
    	if (index == null) throw new XBRLException("The index must not be null.");
		if (index.equals("")) throw new XBRLException("A fragment index must not be an empty string.");
		this.index = index;
		if (this.getResource() == null) {
			setBuilder(new BuilderImpl());
		}
		if (builder != null) builder.setMetaAttribute("index",index);
		// TODO Handle updating of the resource index
    }
 
    /**
     * @see org.xbrlapi.Fragment#getType()
     */
    public String getType() {
    	return this.getClass().getName();
    }

    /**
     * @see org.xbrlapi.Fragment#setMetaAttribute(String, String)
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
	 * @see org.xbrlapi.Fragment#removeMetaAttribute(String)
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
     * @see org.xbrlapi.Fragment#getMetaAttribute(String)
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
     * @see org.xbrlapi.Fragment#appendMetadataElement(String, HashMap)
     */
    public void appendMetadataElement(String eName, HashMap<String,String> attributes) throws XBRLException {
    	
    	if (eName == null) throw new XBRLException("An element name must be specified.");
    	
    	if (getBuilder() != null) {
        	getBuilder().appendMetadataElement(eName, attributes);
    		return;
    	}

    	Element root = getMetadataRootElement();
    	Document document = root.getOwnerDocument();
		Element child = document.createElement(eName);
		
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
     * @see org.xbrlapi.Fragment#removeMetadataElement(String, HashMap)
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
     * @see org.xbrlapi.Fragment#getURL()
     */
    public String getURL() throws XBRLException {
    	return getMetaAttribute("url");
    } 
    
    /**
     * @see org.xbrlapi.Fragment#setURL(String)
     */
    public void setURL(String url) throws XBRLException {
        HashMap<String,String> attributes = new HashMap<String,String>();
        attributes.put("value",url);
        appendMetadataElement("url",attributes);    
    }
    
    /**
     * @see org.xbrlapi.Fragment#removeURL(String)
     */
    public void removeURL(String url) throws XBRLException {
        HashMap<String,String> attributes = new HashMap<String,String>();
        attributes.put("value",url);
        removeMetadataElement("url",attributes);
    }    
    
    /**
     * @see org.xbrlapi.Fragment#getReferencingLocators()
     */
    public FragmentList<Locator> getReferencingLocators() throws XBRLException {
    	
    	// Construct the Query
    	String predicate = Constants.XBRLAPIPrefix + ":" + "data/link:loc and @targetDocumentURL='"+ getURL() +"' and (";
    	NodeList xptrs = this.getMetadataRootElement().getElementsByTagNameNS(Constants.XBRLAPINamespace,"xptr");
    	for (int i=0; i<xptrs.getLength(); i++) {
    		String value = ((Element) xptrs.item(i)).getAttribute("value").trim();
    		predicate += "@targetPointerValue='" + value +"'";
    		if (i == (xptrs.getLength()-1)) 
    			predicate += ")";
    		else
    			predicate += " or ";
    	}
    	String query = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[" + predicate + "]";
    	
    	return getStore().<Locator>query(query);
    }
    
    /**
     * TODO Add methods to get labels based on language and role.
     * @see org.xbrlapi.Fragment#getLabels()
     */
    public FragmentList<LabelResource> getLabels() throws XBRLException {
        Networks networks = this.getNetworks();
    	FragmentList<LabelResource> labels = networks.<LabelResource>getTargetFragments(this.getFragmentIndex(),Constants.LabelArcRole);
        FragmentList<LabelResource> genericLabels = networks.<LabelResource>getTargetFragments(this.getFragmentIndex(),Constants.GenericLabelArcRole);
    	labels.addAll(genericLabels);
    	return labels;
    }
    
    /**
     * @see org.xbrlapi.Fragment#getLabelsWithRole(String)
     */
    public FragmentList<LabelResource> getLabelsWithRole(String role) throws XBRLException {
        logger.info("Getting labels for role: " + role);
        Networks networks = this.getNetworks();
        FragmentList<LabelResource> result = new FragmentListImpl<LabelResource>();

        FragmentList<LabelResource> labels = networks.<LabelResource>getTargetFragments(this.getFragmentIndex(),Constants.LabelArcRole);
        for (LabelResource label: labels) {
            String r = label.getResourceRole();
            if (r != null) {
                if (r.equals(role)) result.add(label);
            }
        }
        
        labels = networks.<LabelResource>getTargetFragments(this.getFragmentIndex(),Constants.GenericLabelArcRole);
        for (LabelResource label: labels) {
            String r = label.getResourceRole();
            if (r != null) {
                if (r.equals(role)) result.add(label);
            }
        }
        
        return result;
    }
    
    /**
     * @see org.xbrlapi.Fragment#getLabelsWithLanguage(String)
     */
    public FragmentList<LabelResource> getLabelsWithLanguage(String language) throws XBRLException {
        Networks networks = this.getNetworks();
        FragmentList<LabelResource> result = new FragmentListImpl<LabelResource>();

        FragmentList<LabelResource> labels = networks.<LabelResource>getTargetFragments(this.getFragmentIndex(),Constants.LabelArcRole);
        for (LabelResource label: labels) {
            String l = label.getLanguage();
            if (l != null) {
                if (label.getLanguage().equals(language)) result.add(label);
            }
        }
        
        labels = networks.<LabelResource>getTargetFragments(this.getFragmentIndex(),Constants.GenericLabelArcRole);
        for (LabelResource label: labels) {
            String l = label.getLanguage();
            if (l != null) {
                if (label.getLanguage().equals(language)) result.add(label);
            }
        }
        
        return result;
    }

    /**
     * @see org.xbrlapi.Fragment#getLabelsWithLanguageAndRole(String, String)
     */
    public FragmentList<LabelResource> getLabelsWithLanguageAndRole(String language, String role) throws XBRLException {
        Networks networks = this.getNetworks();
        FragmentList<LabelResource> result = new FragmentListImpl<LabelResource>();

        FragmentList<LabelResource> labels = networks.<LabelResource>getTargetFragments(this.getFragmentIndex(),Constants.LabelArcRole);
        for (LabelResource label: labels) {
            String l = label.getLanguage();
            String r = label.getResourceRole();
            if (l != null && r != null) {
                if (l.equals(language) && r.equals(role)) result.add(label);
            }
        }
        
        labels = networks.<LabelResource>getTargetFragments(this.getFragmentIndex(),Constants.GenericLabelArcRole);
        for (LabelResource label: labels) {
            String l = label.getLanguage();
            String r = label.getResourceRole();
            if (l != null && r != null) {
                if (l.equals(language) && r.equals(role)) result.add(label);
            }
        }
        
        return result;
    }

    
    /**
     * TODO Add methods to get references based on language and role.
     * @see org.xbrlapi.Fragment#getReferences()
     */
    public FragmentList<ReferenceResource> getReferences() throws XBRLException {
    	Networks networks = this.getNetworks();
        FragmentList<ReferenceResource> references = networks.<ReferenceResource>getTargetFragments(this.getFragmentIndex(),Constants.ReferenceArcRole);
        references.addAll(networks.<ReferenceResource>getTargetFragments(this.getFragmentIndex(),Constants.GenericReferenceArcRole));
        return references;
    }
    
    /**
     * @see org.xbrlapi.Fragment#getNetworks()
     */
    public Networks getNetworks() throws XBRLException {
    	
    	logger.debug("Getting networks for fragment " + getFragmentIndex());
    	Networks networks = new NetworksImpl();
		Relationship relationship = null;
    	
    	// If we have a resource, it could be related directly via arcs to relatives.
    	if (this.isa("org.xbrlapi.impl.ResourceImpl")) {
    		FragmentList<Arc> arcs = ((ArcEnd) this).getArcsFrom();
    		logger.debug("Fragment " + this.getFragmentIndex() + " is a XLink resource with " + arcs.getLength() + " direct arcs FROM it.");
        	for (Arc arc: arcs) {
        		FragmentList<ArcEnd> targets = arc.getTargetFragments();
        		for (ArcEnd end: targets) {
            		if (end.getType().equals("org.xbrlapi.impl.LocatorImpl")) {
            			Fragment target = ((Locator) end).getTargetFragment();
            			relationship = new RelationshipImpl(arc,this,target);
            		} else {
            			relationship = new RelationshipImpl(arc,this,end);
            		}        		
        			networks.addRelationship(relationship);
        		}
        	}
        	
    		arcs = ((ArcEnd) this).getArcsTo();
    		logger.debug("Fragment " + this.getFragmentIndex() + " is a XLink resource with " + arcs.getLength() + " direct arcs TO it.");
        	for (Arc arc: arcs) {
        		FragmentList<ArcEnd> sources = arc.getSourceFragments();
        		for (ArcEnd end: sources) {
            		if (end.getType().equals("org.xbrlapi.impl.LocatorImpl")) {
            			Fragment source = ((Locator) end).getTargetFragment();
            			relationship = new RelationshipImpl(arc,source,this);
            		} else {
            			relationship = new RelationshipImpl(arc,end,this);
            		}        		
        			networks.addRelationship(relationship);
        		}
        	}
    	}

    	// Next get the locators for the fragment to find indirect relatives
    	FragmentList<Locator> locators = this.getReferencingLocators();
		logger.debug("Fragment " + this.getFragmentIndex() + " has " + locators.getLength() + " locators locating it.");
    	for (Locator locator: locators) {        	
    		FragmentList<Arc> arcs = locator.getArcsFrom();
    		logger.debug("Locator " + locator.getFragmentIndex() + " has " + arcs.getLength() + " direct arcs FROM it.");    		
        	for (Arc arc: arcs) {
        		FragmentList<ArcEnd> targets = arc.getTargetFragments();
        		logger.debug("Arc " + arc.getFragmentIndex() + " has " + targets.getLength() + " targets.");    		
        		for (ArcEnd end: targets) {
            		if (end.getType().equals("org.xbrlapi.impl.LocatorImpl")) {
            			Fragment target = ((Locator) end).getTargetFragment();
            			relationship = new RelationshipImpl(arc,this,target);
            		} else {
            			relationship = new RelationshipImpl(arc,this,end);
            		}
        			networks.addRelationship(relationship);
        		}
        		
        	}
    		
    		arcs = locator.getArcsTo();
    		logger.debug("Locator " + locator.getFragmentIndex() + " has " + arcs.getLength() + " direct arcs TO it.");    		
        	for (Arc arc: arcs) {
        		FragmentList<ArcEnd> sources = arc.getSourceFragments();
        		logger.debug("Arc " + arc.getFragmentIndex() + " has " + sources.getLength() + " sources.");    		
        		for (ArcEnd end: sources) {
            		if (end.getType().equals("org.xbrlapi.impl.LocatorImpl")) {
            			Fragment source = ((Locator) end).getTargetFragment();
            			relationship = new RelationshipImpl(arc,source,this);
            		} else {
            			relationship = new RelationshipImpl(arc,end,this);
            		}        		
        			networks.addRelationship(relationship);
        		}
        	}
    	}

    	return networks;
    	
    }
    
    
    /**
     * @see org.xbrlapi.Fragment#isa(String)
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
     * @see org.xbrlapi.Fragment#getParentIndex()
     */
    public String getParentIndex() throws XBRLException {
    	return getMetaAttribute("parentIndex");
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
     * @see org.xbrlapi.Fragment#setSequenceToParentElement(Vector)
     */
    public void setSequenceToParentElement(Vector<Long> children) throws XBRLException {
    	
    	StringBuffer value = new StringBuffer("");
    	for (int i=0; i<children.size()-1; i++) {
    		String child = children.get(i).toString();
			if (value.length() == 0) {
				value.append(child);
			} else {
				value.append(" " + child); 
			}
		}

    	setMetaAttribute("SequenceToParentElement",value.toString());
    	
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
     * @see org.xbrlapi.Fragment#setPrecedingSiblings(Vector)
     */
    public void setPrecedingSiblings(Vector<Long> children) throws XBRLException {
    	Long value = new Long((children.get(children.size()-1)).longValue() - 1);
    	String precedingSiblings = value.toString();
    	setMetaAttribute("precedingSiblings",precedingSiblings);
    }

    /**
     * @see org.xbrlapi.Fragment#removeRelationship(String)
     */
    public void removeRelationship(String index) throws XBRLException {
    	
    	HashMap<String,String> attributes = new HashMap<String,String>();
    	attributes.put("targetIndex",index);
    	removeMetadataElement("xlinkRelationship",attributes);
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
     * @see org.xbrlapi.Fragment#removeID(String)
     * TODO remove the redundant parameter from this method.
     */
    public void removeID(String id) throws XBRLException {
    	HashMap<String,String> attributes = new HashMap<String,String>();
    	attributes.put("ID",id);
    	removeMetadataElement("ID",attributes);
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
     * @see org.xbrlapi.Fragment#removeElementSchemeXPointer(String)
     */
    public void removeElementSchemeXPointer(String expression) throws XBRLException {
    	HashMap<String,String> attributes = new HashMap<String,String>();
    	attributes.put("value",expression);
    	removeMetadataElement("xptr",attributes);
    }    
    
    /**
     * @see org.xbrlapi.Fragment#getNamespaceURI()
     */
    public String getNamespaceURI() throws XBRLException {
    	if (getDataRootElement() == null) {
    		throw new XBRLException("The XML fragment root node is null.");
    	}
    	return getDataRootElement().getNamespaceURI();
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
     * @see org.xbrlapi.Fragment#getNamespaceFromQName(String, Node)
     */
    public String getNamespaceFromQName(String qname, Node node) throws XBRLException {
    	
        // Create NS prefix declaration for the QName being sought.
        String prefix = getPrefixFromQName(qname);
        String declaration = "xmlns";
        if (!prefix.equals("")) {
            declaration = declaration + ":" + prefix;
        }

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            
            // Check for a namespace declaration on the current node
            String ns = element.getAttribute(declaration);
            if (! ns.equals("")) {
                return ns;
            }            
            
            if (element.isSameNode(this.getMetadataRootElement())) {
                throw new XBRLException("the QName prefix is not declared for " + qname);
            } else {
                return getNamespaceFromQName(qname, element.getParentNode());
            }
            
        } else if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
            Node parent = node.getParentNode();
            if (parent == null) throw new XBRLException("No namespace is defined for QName " + qname);
            return getNamespaceFromQName(qname, parent);
        } else {
            throw new XBRLException("An element node is expected.");
        }

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
     * @see org.xbrlapi.Fragment#getParentElement(Element)
     */
    public Element getParentElement(Element parentDataRootElement) throws XBRLException {

    	String[] sequence = getSequenceToParentElement();
    	if (sequence[0].equals("")) {
    	    return parentDataRootElement;
    	}

    	// Traverse the parent data DOM to find the parent element
    	Element current = parentDataRootElement;
    	for (int i=0; i<sequence.length; i++) { // Iterate the sequence of steps through the parent fragment
    		int elementOrder = (new Integer(sequence[i])).intValue();  // The sibling position
    		int elementsFound = 0;
    		NodeList children = current.getChildNodes();
    		int j = 0;
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
    	String localname = "";
		int delimiterIndex = qname.indexOf(':');
    	if (delimiterIndex > 0) {
    		localname = qname.substring(delimiterIndex+1,qname.length());
    	}
    	return localname;
    }
    
    /**
     * @see org.xbrlapi.Fragment#hashCode()
     */
	public int hashCode() {
	    return getFragmentIndex().hashCode();
	}
	
	/**
	 * @see org.xbrlapi.Fragment#equals(Object)
	 */
	public boolean equals(Object o1) throws ClassCastException {
		Fragment f1 = (Fragment) o1;
		if (this.getFragmentIndex().equals(f1.getFragmentIndex()))
			return true;
		return false;
	}

}
