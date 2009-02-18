package org.xbrlapi.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xbrlapi.Arc;
import org.xbrlapi.ArcEnd;
import org.xbrlapi.ExtendedLink;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.LabelResource;
import org.xbrlapi.Locator;
import org.xbrlapi.ReferenceResource;
import org.xbrlapi.Resource;
import org.xbrlapi.SimpleLink;
import org.xbrlapi.networks.Network;
import org.xbrlapi.networks.Networks;
import org.xbrlapi.networks.NetworksImpl;
import org.xbrlapi.networks.Relationship;
import org.xbrlapi.networks.RelationshipImpl;
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
     * Gets the child fragments with the specified fragment type.
     * @param type The required fragment type of the child.
     * @return the fragment list of children fragments that match the specified fragment type
     * @throws XBRLException
     */
    protected <F extends Fragment> FragmentList<F> getChildren(String type) throws XBRLException {
    	String query = "/*[@parentIndex='" + getIndex() + "' and @type='" + type + "']";
    	FragmentList<F> fragments = getStore().<F>query(query);
    	return fragments;
    }
    
    /**
     * @see org.xbrlapi.Fragment#getSimpleLinks()
     */
    public FragmentList<SimpleLink> getSimpleLinks() throws XBRLException {
    	return this.getStore().<SimpleLink>getChildFragments("SimpleLink",this.getIndex());
    }
    
    /**
     * @see org.xbrlapi.Fragment#getAllChildren()
     */
    public FragmentList<Fragment> getAllChildren() throws XBRLException {
    	String xpath = "/*[@parentIndex='" + getIndex() + "']";
    	FragmentList<Fragment> fragments = getStore().<Fragment>query(xpath);
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
    public FragmentList<Locator> getReferencingLocators() throws XBRLException {
    	
    	// Construct the Query
    	String predicate = Constants.XBRLAPIPrefix + ":" + "data/link:loc and @targetDocumentURI='"+ getURI() +"' and (";
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
     * @see org.xbrlapi.Fragment#getLabels(org.xbrlapi.networks.Networks)
     */
    public FragmentList<LabelResource> getLabels(Networks networks) throws XBRLException {

        FragmentList<LabelResource> labels = new FragmentListImpl<LabelResource>();
        for (Network network: networks.getNetworks(Constants.LabelArcRole)) {
            List<Relationship> relationships = network.getActiveRelationshipsFrom(this.getIndex());
            for (Relationship relationship: relationships) {
                labels.add((LabelResource) relationship.getTarget());
            }
        }
        for (Network network: networks.getNetworks(Constants.GenericLabelArcRole)) {
            List<Relationship> relationships = network.getActiveRelationshipsFrom(this.getIndex());
            for (Relationship relationship: relationships) {
                labels.add((LabelResource) relationship.getTarget());
            }
        }
    	return labels;
    }
    
    /**
     * @see org.xbrlapi.Fragment#getLabels()
     */
    public FragmentList<LabelResource> getLabels() throws XBRLException {

        Networks labelNetworks = this.getNetworksWithArcrole(Constants.LabelArcRole);
        FragmentList<LabelResource> labels = labelNetworks.<LabelResource>getTargetFragments(this.getIndex(),Constants.LabelArcRole);

        Networks genericLabelNetworks = this.getNetworksWithArcrole(Constants.GenericLabelArcRole);
        FragmentList<LabelResource> genericLabels = genericLabelNetworks.<LabelResource>getTargetFragments(this.getIndex(),Constants.GenericLabelArcRole);

        labels.addAll(genericLabels);
        return labels;
    }    
    
    /**
     * @see org.xbrlapi.Fragment#getLabelsWithRole(Networks, String)
     */
    public FragmentList<LabelResource> getLabelsWithRole(Networks networks, String role) throws XBRLException {
        FragmentList<LabelResource> labels = new FragmentListImpl<LabelResource>();
        for (Network network: networks.getNetworks(Constants.LabelArcRole)) {
            List<Relationship> relationships = network.getActiveRelationshipsFrom(this.getIndex());
            for (Relationship relationship: relationships) {
                LabelResource label = (LabelResource) relationship.getTarget();
                String r = label.getResourceRole();
                if (r != null) {
                    if (r.equals(role)) labels.add(label);
                }
            }
        }
        for (Network network: networks.getNetworks(Constants.GenericLabelArcRole)) {
            List<Relationship> relationships = network.getActiveRelationshipsFrom(this.getIndex());
            for (Relationship relationship: relationships) {
                LabelResource label = (LabelResource) relationship.getTarget();
                String r = label.getResourceRole();
                if (r != null) {
                    if (r.equals(role)) labels.add(label);
                }
            }
        }
        return labels;

    }
    
    /**
     * @see org.xbrlapi.Fragment#getLabelsWithRole(String)
     */
    public FragmentList<LabelResource> getLabelsWithRole(String role) throws XBRLException {

        Networks networks = this.getNetworksWithArcrole(Constants.LabelArcRole);
        FragmentList<LabelResource> result = new FragmentListImpl<LabelResource>();

        FragmentList<LabelResource> labels = networks.<LabelResource>getTargetFragments(this.getIndex(),Constants.LabelArcRole);
        for (LabelResource label: labels) {
            String r = label.getResourceRole();
            if (r != null) {
                if (r.equals(role)) result.add(label);
            }
        }
        
        networks = this.getNetworksWithArcrole(Constants.GenericLabelArcRole);
        labels = networks.<LabelResource>getTargetFragments(this.getIndex(),Constants.GenericLabelArcRole);

        for (LabelResource label: labels) {
            String r = label.getResourceRole();
            if (r != null) {
                if (r.equals(role)) result.add(label);
            }
        }
        
        return result;
    }    
    
    /**
     * @see org.xbrlapi.Fragment#getLabelsWithLanguage(Networks, String)
     */
    public FragmentList<LabelResource> getLabelsWithLanguage(Networks networks, String language) throws XBRLException {

        FragmentList<LabelResource> labels = new FragmentListImpl<LabelResource>();
        for (Network network: networks.getNetworks(Constants.LabelArcRole)) {
            List<Relationship> relationships = network.getActiveRelationshipsFrom(this.getIndex());
            for (Relationship relationship: relationships) {
                LabelResource label = (LabelResource) relationship.getTarget();
                String l = label.getLanguage();
                if (l != null) {
                    if (label.getLanguage().equals(language)) labels.add(label);
                }
            }
        }
        for (Network network: networks.getNetworks(Constants.GenericLabelArcRole)) {
            List<Relationship> relationships = network.getActiveRelationshipsFrom(this.getIndex());
            for (Relationship relationship: relationships) {
                LabelResource label = (LabelResource) relationship.getTarget();
                String l = label.getLanguage();
                if (l != null) {
                    if (label.getLanguage().equals(language)) labels.add(label);
                }
            }
        }
        return labels;

    }
    
    /**
     * @see org.xbrlapi.Fragment#getLabelsWithLanguage(String)
     */
    public FragmentList<LabelResource> getLabelsWithLanguage(String language) throws XBRLException {

        Networks networks = this.getNetworksWithArcrole(Constants.LabelArcRole);
        FragmentList<LabelResource> result = new FragmentListImpl<LabelResource>();

        FragmentList<LabelResource> labels = networks.<LabelResource>getTargetFragments(this.getIndex(),Constants.LabelArcRole);
        for (LabelResource label: labels) {
            String l = label.getLanguage();
            if (l != null) {
                if (label.getLanguage().equals(language)) result.add(label);
            }
        }

        networks = this.getNetworksWithArcrole(Constants.GenericLabelArcRole);
        labels = networks.<LabelResource>getTargetFragments(this.getIndex(),Constants.GenericLabelArcRole);

        for (LabelResource label: labels) {
            String l = label.getLanguage();
            if (l != null) {
                if (label.getLanguage().equals(language)) result.add(label);
            }
        }
        
        return result;
    }

    /**
     * @see org.xbrlapi.Fragment#getLabelsWithLanguageAndRole(Networks, String, String)
     */
    public FragmentList<LabelResource> getLabelsWithLanguageAndRole(Networks networks, String language, String role) throws XBRLException {
        
        FragmentList<LabelResource> labels = new FragmentListImpl<LabelResource>();
        for (Network network: networks.getNetworks(Constants.LabelArcRole)) {
            List<Relationship> relationships = network.getActiveRelationshipsFrom(this.getIndex());
            for (Relationship relationship: relationships) {
                LabelResource label = (LabelResource) relationship.getTarget();
                String l = label.getLanguage();
                String r = label.getResourceRole();
                if (l != null && r != null) {
                    if (l.equals(language) && r.equals(role)) labels.add(label);
                }
            }
        }
        for (Network network: networks.getNetworks(Constants.GenericLabelArcRole)) {
            List<Relationship> relationships = network.getActiveRelationshipsFrom(this.getIndex());
            for (Relationship relationship: relationships) {
                LabelResource label = (LabelResource) relationship.getTarget();
                String l = label.getLanguage();
                String r = label.getResourceRole();
                if (l != null && r != null) {
                    if (l.equals(language) && r.equals(role)) labels.add(label);
                }
            }
        }
        return labels;

    }
    
    /**
     * @see org.xbrlapi.Fragment#getLabelsWithLanguageAndRole(String, String)
     */
    public FragmentList<LabelResource> getLabelsWithLanguageAndRole(String language, String role) throws XBRLException {
        
        Networks networks = this.getLabelNetworks();
        FragmentList<LabelResource> result = new FragmentListImpl<LabelResource>();
        FragmentList<LabelResource> labels = networks.<LabelResource>getTargetFragments(this.getIndex(),Constants.LabelArcRole);
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
     * @return a set of networks of relationships from the fragment to labels.
     * @throws XBRLException
     */
    private Networks getLabelNetworks() throws XBRLException {

        Relationship relationship = null;
        Networks networks;
        if (getStore().hasStoredNetworks()) networks = getStore().getStoredNetworks();
        else networks = new NetworksImpl(getStore());

        // If we have a resource, it could be related directly via arcs to relatives.
        if (this.isa("org.xbrlapi.impl.ResourceImpl")) {
            FragmentList<Arc> arcs = ((ArcEnd) this).getArcsFromWithArcrole(Constants.LabelArcRole);
            arcs.addAll(((ArcEnd) this).getArcsFromWithArcrole(Constants.GenericLabelArcRole));
            
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

        }

        // Next get the locators for the fragment to find indirect relatives
        HashMap<String,ExtendedLink> links = new HashMap<String,ExtendedLink>();
        FragmentList<Locator> locators = this.getReferencingLocators();
        LOCATOR: for (Locator locator: locators) {
            ExtendedLink link;
            String pIndex = locator.getParentIndex();
            if (links.containsKey(locator.getParentIndex())) {
                link = links.get(pIndex);
            } else {
                link = locator.getExtendedLink();
                links.put(pIndex,link);
            }
            if (! (link.getLocalname().equals("labelLink") || link.getLocalname().equals("link"))) continue LOCATOR; 
            FragmentList<Arc> arcs = ((ArcEnd) locator).getArcsFromWithArcrole(Constants.LabelArcRole);
            arcs.addAll(((ArcEnd) locator).getArcsFromWithArcrole(Constants.GenericLabelArcRole));
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
            
        }

        return networks;
    }    
    

    
    /**
     * TODO Add methods to get references based on language and role.
     * @see org.xbrlapi.Fragment#getReferences()
     */
    public FragmentList<ReferenceResource> getReferences() throws XBRLException {
    	Networks networks = this.getNetworks();
        FragmentList<ReferenceResource> references = networks.<ReferenceResource>getTargetFragments(this.getIndex(),Constants.ReferenceArcRole);
        references.addAll(networks.<ReferenceResource>getTargetFragments(this.getIndex(),Constants.GenericReferenceArcRole));
        return references;
    }
    
    /**
     * @see org.xbrlapi.Fragment#getNetworks()
     */
    public Networks getNetworks() throws XBRLException {
    	
    	logger.debug("Getting networks for fragment " + getIndex());
        Networks networks;
        if (getStore().hasStoredNetworks()) networks = getStore().getStoredNetworks();
        else networks = new NetworksImpl(getStore());
		Relationship relationship = null;
    	
    	// If we have a resource, it could be related directly via arcs to relatives.
    	if (this.isa("org.xbrlapi.impl.ResourceImpl")) {
    		FragmentList<Arc> arcs = ((ArcEnd) this).getArcsFrom();
    		logger.debug("Fragment " + this.getIndex() + " is a XLink resource with " + arcs.getLength() + " direct arcs FROM it.");
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
    		logger.debug("Fragment " + this.getIndex() + " is an XLink resource with " + arcs.getLength() + " direct arcs TO it.");
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
		logger.debug("Fragment " + this.getIndex() + " has " + locators.getLength() + " locators locating it.");
    	for (Locator locator: locators) {        	
    		FragmentList<Arc> arcs = locator.getArcsFrom();
    		logger.debug("Locator " + locator.getIndex() + " has " + arcs.getLength() + " direct arcs FROM it.");    		
        	for (Arc arc: arcs) {
        		FragmentList<ArcEnd> targets = arc.getTargetFragments();
        		logger.debug("Arc " + arc.getIndex() + " has " + targets.getLength() + " targets.");    		
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
    		logger.debug("Locator " + locator.getIndex() + " has " + arcs.getLength() + " direct arcs TO it.");    		
        	for (Arc arc: arcs) {
        		FragmentList<ArcEnd> sources = arc.getSourceFragments();
        		logger.debug("Arc " + arc.getIndex() + " has " + sources.getLength() + " sources.");    		
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
     * @see org.xbrlapi.Fragment#getNetworksWithArcrole(String)
     */
    public Networks getNetworksWithArcrole(String arcrole) throws XBRLException {
        logger.debug("Getting relationships to and from fragment " + getIndex() + " with arcrole " + arcrole);
        Networks networks;
        if (getStore().hasStoredNetworks()) networks = getStore().getStoredNetworks();
        else networks = new NetworksImpl(getStore());
        Relationship relationship = null;

        // If we have a resource, it could be related directly via arcs to relatives.
        if (this.isa("org.xbrlapi.impl.ResourceImpl")) {
            FragmentList<Arc> arcs = ((ArcEnd) this).getArcsFromWithArcrole(arcrole);

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
            
            arcs = ((ArcEnd) this).getArcsToWithArcrole(arcrole);
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
        for (Locator locator: locators) {
            FragmentList<Arc> arcs = locator.getArcsFromWithArcrole(arcrole);
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
            
            arcs = locator.getArcsToWithArcrole(arcrole);
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

        return networks;
    }
    
    /**
     * @see org.xbrlapi.Fragment#getNetworksFromWithArcrole(String)
     */
    public Networks getNetworksFromWithArcrole(String arcrole) throws XBRLException {
        logger.debug("Getting relationships from fragment " + getIndex() + " with arcrole " + arcrole);
        Networks networks;
        if (getStore().hasStoredNetworks()) networks = getStore().getStoredNetworks();
        else networks = new NetworksImpl(getStore());
        Relationship relationship = null;

        // If we have a resource, it could be related directly via arcs to relatives.
        if (this.isa("org.xbrlapi.impl.ResourceImpl")) {
            FragmentList<Arc> arcs = ((ArcEnd) this).getArcsFromWithArcrole(arcrole);

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
            
        }

        // Next get the locators for the fragment to find indirect relatives
        FragmentList<Locator> locators = this.getReferencingLocators();
        for (Locator locator: locators) {
            FragmentList<Arc> arcs = locator.getArcsFromWithArcrole(arcrole);
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
        }

        return networks;
    }
    
    /**
     * @see org.xbrlapi.Fragment#getNetworksFromWithRoleAndArcrole(String, String)
     */
    public Networks getNetworksFromWithRoleAndArcrole(String linkrole, String arcrole) throws XBRLException {
        Networks networks;
        if (getStore().hasStoredNetworks()) networks = getStore().getStoredNetworks();
        else networks = new NetworksImpl(getStore());
        Relationship relationship = null;

        // If we have a resource, it could be related directly via arcs to relatives.
        if (this.isa("org.xbrlapi.impl.ResourceImpl")) {
            if (((Resource) this).getExtendedLink().getLinkRole().equals(linkrole)) {
                FragmentList<Arc> arcs = ((ArcEnd) this).getArcsFromWithArcrole(arcrole);

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
            }
        }

        HashMap<String,ExtendedLink> links = new HashMap<String,ExtendedLink>();
        
        // Next get the locators for the fragment to find indirect relatives
        FragmentList<Locator> locators = this.getReferencingLocators();
        for (Locator locator: locators) {
            ExtendedLink link = null;
            String parentIndex = locator.getParentIndex();
            if (links.containsKey(parentIndex)) {
                link = links.get(parentIndex);
            } else {
                link = locator.getExtendedLink();
                links.put(parentIndex,link);
            }
            if (! link.getLinkRole().equals(linkrole)) continue;
            FragmentList<Arc> arcs = locator.getArcsFromWithArcrole(arcrole);
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
        }

        return networks;        
    }

    /**
     * Map of extended links.
     */
    private Map<String,ExtendedLink> links = new HashMap<String,ExtendedLink>();
    
    /**
     * Convenience method for checking link roles on arcs.
     * TODO migrate linkrole checking to the arc class and expose the map as a static field.
     * @param arc The arc to check
     * @param linkrole The required link role
     * @return true if the arc is in an extended link with the given link role.
     * @throws XBRLException
     */
    private boolean arcHasLinkRole(Arc arc, String linkrole) throws XBRLException {
        String linkIndex = arc.getParentIndex();
        ExtendedLink link = null;
        if (links.containsKey(linkIndex)) {
            link = links.get(linkIndex);
        } else {
            link = (ExtendedLink) arc.getParent();
            links.put(link.getIndex(),link);
        }
        return (link.getLinkRole().equals(linkrole));
    }

    /**
     * @see org.xbrlapi.Fragment#getRelatives(String, String, String, String, boolean)
     */
    public <F extends Fragment> FragmentList<F> getRelatives(String arcrole, String linkrole, String resourcerole, String language, boolean getTargets) throws XBRLException {

        Networks networks;
        if (getStore().hasStoredNetworks()) networks = getStore().getStoredNetworks();
        else networks = new NetworksImpl(getStore());
        Relationship relationship = null;
        
        // First get any direct arcs if this fragment is an XLink resource.
        if (this.isa("org.xbrlapi.impl.ResourceImpl")) {
            
            if (getTargets) { // We want the relatives that are targets of relationships from this fragment.
                FragmentList<Arc> arcs = ((ArcEnd) this).getArcsFromWithArcrole(arcrole);
                for (Arc arc: arcs) {
                    if ((linkrole != null) && ! arcHasLinkRole(arc,linkrole)) continue;
                    FragmentList<ArcEnd> targets = arc.getTargetFragments();
                    for (ArcEnd end: targets) {
                        Fragment target = null;
                        if (end.getType().equals("org.xbrlapi.impl.LocatorImpl")) {
                            target = ((Locator) end).getTargetFragment();
                        } else {
                            target = end;
                        }
                        if (target.isa("org.xbrlapi.impl.ResourceImpl")) {
                            if ((resourcerole != null) && ! ((Resource) target).getResourceRole().equals(resourcerole)) continue;
                            if ((language != null) && ! ((Resource) target).getLanguage().equals(language)) continue;
                        }
                        relationship = new RelationshipImpl(arc,this,target);
                        networks.addRelationship(relationship);
                    }
                }

            } else { // We want the relatives that are sources of relationships to this fragment.
                FragmentList<Arc> arcs = ((ArcEnd) this).getArcsToWithArcrole(arcrole);
                for (Arc arc: arcs) {
                    if ((linkrole != null) && ! arcHasLinkRole(arc,linkrole)) continue;
                    FragmentList<ArcEnd> sources = arc.getSourceFragments();
                    for (ArcEnd end: sources) {
                        Fragment source = null;
                        if (end.getType().equals("org.xbrlapi.impl.LocatorImpl")) {
                            source = ((Locator) end).getTargetFragment();
                        } else {
                            source = end;
                        }
                        if (source.isa("org.xbrlapi.impl.ResourceImpl")) {
                            if ((resourcerole != null) && ! ((Resource) source).getResourceRole().equals(resourcerole)) continue;
                            if ((language != null) && ! ((Resource) source).getLanguage().equals(language)) continue;                            
                        }
                        relationship = new RelationshipImpl(arc,source,this);
                        networks.addRelationship(relationship);
                    }
                }
            }

        }

        // Next get the locators for the fragment to find indirect relatives
        FragmentList<Locator> locators = this.getReferencingLocators();
        for (Locator locator: locators) {
            
            if (getTargets) {
            
                FragmentList<Arc> arcs = locator.getArcsFromWithArcrole(arcrole);
                for (Arc arc: arcs) {
                    if ((linkrole != null) && ! arcHasLinkRole(arc,linkrole)) continue;
                    FragmentList<ArcEnd> targets = arc.getTargetFragments();
                    for (ArcEnd end: targets) {
                        Fragment target = null;
                        if (end.getType().equals("org.xbrlapi.impl.LocatorImpl")) {
                            target = ((Locator) end).getTargetFragment();
                        } else {
                            target = end;
                        }
                        if (target.isa("org.xbrlapi.impl.ResourceImpl")) {
                            if ((resourcerole != null) && ! ((Resource) target).getResourceRole().equals(resourcerole)) continue;
                            if ((language != null) && ! ((Resource) target).getLanguage().equals(language)) continue;                            
                        }
                        relationship = new RelationshipImpl(arc,this,target);
                        networks.addRelationship(relationship);
                    }
                }

            } else {
                FragmentList<Arc> arcs = locator.getArcsToWithArcrole(arcrole);
                for (Arc arc: arcs) {
                    if ((linkrole != null) && ! arcHasLinkRole(arc,linkrole)) continue;
                    FragmentList<ArcEnd> sources = arc.getSourceFragments();
                    for (ArcEnd end: sources) {
                        Fragment source = null;
                        if (end.getType().equals("org.xbrlapi.impl.LocatorImpl")) {
                            source = ((Locator) end).getTargetFragment();
                        } else {
                            source = end;
                        }
                        if (source.isa("org.xbrlapi.impl.ResourceImpl")) {
                            if ((resourcerole != null) && ! ((Resource) source).getResourceRole().equals(resourcerole)) continue;
                            if ((language != null) && ! ((Resource) source).getLanguage().equals(language)) continue;                            
                        }
                        relationship = new RelationshipImpl(arc,source,this);
                        networks.addRelationship(relationship);
                    }
                }
            }
        }

        // Make sure that the map of extended links does not get too large.
        this.links = new HashMap<String,ExtendedLink>();
        
        if (getTargets) {
            if (linkrole == null) return networks.<F>getTargetFragments(this.getIndex(),arcrole);
            return networks.<F>getTargetFragments(this.getIndex(),arcrole,linkrole);
        }
        if (linkrole == null) return networks.<F>getSourceFragments(this.getIndex(),arcrole);
        return networks.<F>getSourceFragments(this.getIndex(),arcrole,linkrole);
    
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
            }
            return getNamespaceFromQName(qname, element.getParentNode());
            
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
    	String localname = qname;
		int delimiterIndex = qname.indexOf(':');
    	if (delimiterIndex > 0) {
    		localname = qname.substring(delimiterIndex+1,qname.length());
    	}
    	return localname;
    }
    
    /**
     * @see org.xbrlapi.Fragment#getRelationshipsToWithArcrole(String)
     */
    public List<Relationship> getRelationshipsToWithArcrole(String arcrole) throws XBRLException {
        logger.debug("Getting relationships to fragment " + getIndex() + " with arcrole " + arcrole);
        List<Relationship> relationships = new Vector<Relationship>();
    
        // If we have a resource, it could be related directly via arcs to relatives.
        if (this.isa("org.xbrlapi.impl.ResourceImpl")) {
            FragmentList<Arc> arcs = ((ArcEnd) this).getArcsToWithArcrole(arcrole);
    
            for (Arc arc: arcs) {
                FragmentList<ArcEnd> sources = arc.getSourceFragments();
                for (ArcEnd source: sources) {
                    if (source.getType().equals("org.xbrlapi.impl.LocatorImpl")) {
                        Fragment s = ((Locator) source).getTargetFragment();
                        relationships.add(new RelationshipImpl(arc,s,this));
                    } else {
                        relationships.add(new RelationshipImpl(arc,source,this));
                    }
                }
            }
            
        }
    
        // Next get the locators for the fragment to find indirect relatives
        FragmentList<Locator> locators = this.getReferencingLocators();
        for (Locator locator: locators) {
            FragmentList<Arc> arcs = locator.getArcsToWithArcrole(arcrole);
            for (Arc arc: arcs) {
                FragmentList<ArcEnd> sources = arc.getSourceFragments();
                for (ArcEnd source: sources) {
                    if (source.getType().equals("org.xbrlapi.impl.LocatorImpl")) {
                        Fragment s = ((Locator) source).getTargetFragment();
                        relationships.add(new RelationshipImpl(arc,s,this));
                    } else {
                        relationships.add(new RelationshipImpl(arc,source,this));
                    }
                }
            }
        }
    
        return relationships;
    }


}
