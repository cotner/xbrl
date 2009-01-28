package org.xbrlapi.data;

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.xbrlapi.Arc;
import org.xbrlapi.ArcEnd;
import org.xbrlapi.ArcroleType;
import org.xbrlapi.Concept;
import org.xbrlapi.ExtendedLink;
import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.Instance;
import org.xbrlapi.Item;
import org.xbrlapi.Locator;
import org.xbrlapi.Resource;
import org.xbrlapi.RoleType;
import org.xbrlapi.SchemaDeclaration;
import org.xbrlapi.Tuple;
import org.xbrlapi.impl.FragmentListImpl;
import org.xbrlapi.networks.Network;
import org.xbrlapi.networks.Networks;
import org.xbrlapi.networks.NetworksImpl;
import org.xbrlapi.networks.Relationship;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * Abstract implementation of the XBRL data store.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public abstract class XBRLStoreImpl extends BaseStoreImpl implements XBRLStore {
	
	public XBRLStoreImpl() {
		super();
	}
    
    /**
     * @return a list of all of the root-level facts in the data store (those facts
     * that are children of the root element of an XBRL instance).  Returns an empty list 
     * if no facts are found.
     * @throws XBRLException
     */
    public FragmentList<Fact> getFacts() throws XBRLException {
    	FragmentList<Instance> instances = this.<Instance>getFragments("Instance");
    	return getFactsFromInstances(instances);
    }
    
    /**
     * This method is provided as a helper method for the getFact methods.
     * @param instances The list of instance fragments to extract facts from.
     * @return The list of facts in the instances.
     * @throws XBRLException
     */
    private FragmentList<Fact> getFactsFromInstances(FragmentList<Instance> instances) throws XBRLException {
    	FragmentList<Fact> facts = new FragmentListImpl<Fact>();
    	for (Instance instance: instances) {
    		facts.addAll(instance.getFacts());
    	}
    	return facts;
    }
    
    /**
     * Helper method for common code in the getItem methods.
     * @param instances The instances to retrieve items for.
     * @return a list of root items in the instances.
     * @throws XBRLException
     */
    private FragmentList<Item> getItemsFromInstances(FragmentList<Instance> instances) throws XBRLException {
    	FragmentList<Fact> facts = getFactsFromInstances(instances);
    	FragmentList<Item> items = new FragmentListImpl<Item>();
    	for (Fact fact: facts) {
    		if (! fact.getType().equals("org.xbrlapi.org.impl.TupleImpl"))
    			items.addFragment((Item) fact);
    	}
    	return items;
    }
    
    /**
     * Helper method for common code in the getTuple methods.
     * @param instances The instances to retrieve tuples for.
     * @return a list of root tuples in the instances.
     * @throws XBRLException
     */
    private FragmentList<Tuple> getTuplesFromInstances(FragmentList<Instance> instances) throws XBRLException {
    	FragmentList<Fact> facts = getFactsFromInstances(instances);
    	FragmentList<Tuple> tuples = new FragmentListImpl<Tuple>();
    	for (Fact fact: facts) {
    		if (fact.getType().equals("org.xbrlapi.org.impl.TupleImpl"))
    			tuples.addFragment((Tuple) fact);
    	}
    	return tuples;
    }    
    
    /**
     * @return a list of all of the root-level items in the data store(those items
     * that are children of the root element of an XBRL instance).
     * TODO eliminate the redundant retrieval of tuples from the getItems methods.
     * @throws XBRLException
     */
    public FragmentList<Item> getItems() throws XBRLException {
    	FragmentList<Instance> instances = this.<Instance>getFragments("Instance");
    	return getItemsFromInstances(instances);
    }
    
    /**
     * @return a list of all of the tuples in the data store.
     * @throws XBRLException
     */
    public FragmentList<Tuple> getTuples() throws XBRLException {
    	FragmentList<Instance> instances = this.<Instance>getFragments("Instance");
    	return this.getTuplesFromInstances(instances);
    }

    /**
     * @param uri The URI of the document to get the facts from.
     * @return a list of all of the root-level facts in the specified document.
     * @throws XBRLException
     */
    public FragmentList<Fact> getFacts(URI uri) throws XBRLException {
    	FragmentList<Instance> instances = this.<Instance>getFragmentsFromDocument(uri,"Instance");
    	return this.getFactsFromInstances(instances);
    }
    
    /**
     * @param uri The URI of the document to get the items from.
     * @return a list of all of the root-level items in the data store.
     * @throws XBRLException
     */
    public FragmentList<Item> getItems(URI uri) throws XBRLException {
    	FragmentList<Instance> instances = this.<Instance>getFragmentsFromDocument(uri,"Instance");
    	return this.getItemsFromInstances(instances);
    }
    
    /**
     * @param uri The URI of the document to get the facts from.
     * @return a list of all of the root-level tuples in the specified document.
     * @throws XBRLException
     */
    public FragmentList<Tuple> getTuples(URI uri) throws XBRLException {
    	FragmentList<Instance> instances = this.<Instance>getFragmentsFromDocument(uri,"Instance");
    	return this.getTuplesFromInstances(instances);
    }

    /**
     * @see XBRLStore#getNetworkRoots(String, String, String, String, String, String)
     */
    @SuppressWarnings("unchecked")
    public <F extends Fragment> FragmentList<F> getNetworkRoots(String linkNamespace, String linkName, String linkRole, String arcNamespace, String arcName, String arcRole) throws XBRLException {
    	
    	// Get the links that contain the network declaring arcs.
    	String linkQuery = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@type='org.xbrlapi.impl.ExtendedLinkImpl' and "+ Constants.XBRLAPIPrefix+ ":" + "data/*[namespace-uri()='" + linkNamespace + "' and local-name()='" + linkName + "' and @xlink:role='" + linkRole + "']]";
    	FragmentList<ExtendedLink> links = this.<ExtendedLink>query(linkQuery);
    	
    	// Get the arcs that declare the relationships in the network.
    	// For each arc map the ids of the fragments at their sources and targets.
    	HashMap<String,String> sourceIds = new HashMap<String,String>();
    	HashMap<String,String> targetIds = new HashMap<String,String>();
    	for (int i=0; i<links.getLength(); i++) {
    		ExtendedLink link = links.getFragment(i);
    		FragmentList<Arc> arcs = link.getArcs();
    		for (Arc arc: arcs) {
    			if (arc.getNamespaceURI().equals(arcNamespace))
    				if (arc.getLocalname().equals(arcName))
    					if (arc.getArcrole().equals(arcRole)) {
    			    		FragmentList<ArcEnd> sources = arc.getSourceFragments();
    						FragmentList<ArcEnd> targets = arc.getTargetFragments();
    						for (int k=0; k<sources.getLength(); k++) {
    							sourceIds.put(sources.getFragment(k).getFragmentIndex(),"");
    						}
    						for (int k=0; k<sources.getLength(); k++) {
    							targetIds.put(targets.getFragment(k).getFragmentIndex(),"");
    						}
    					}
    		}
    	}
    	
    	// Get the root resources in the network
    	FragmentList<F> roots = new FragmentListImpl<F>();
    	Iterator<String> iterator = sourceIds.keySet().iterator();
    	while (iterator.hasNext()) {
    		String id = iterator.next();
    		if (! targetIds.containsKey(id)) {
    		    Fragment target = this.getFragment(id);
    		    if (! target.isa("org.xbrlapi.impl.LocatorImpl"))
    		        roots.addFragment((F) target);
    		    else {
                    roots.addFragment((F) ((Locator) target).getTargetFragment());
    		    }
    		}
    	}
    	return roots;
    }

    /**
     * @see XBRLStore#getNetworkRoots(String, String)
     */
    public FragmentList<Fragment> getNetworkRoots(String linkRole, String arcRole) throws XBRLException {
    	
    	// Get the links that contain the network declaring arcs.
    	String linkQuery = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@type='org.xbrlapi.impl.ExtendedLinkImpl' and "+ Constants.XBRLAPIPrefix+ ":" + "data/*[@xlink:role='" + linkRole + "']]";
    	FragmentList<ExtendedLink> links = this.<ExtendedLink>query(linkQuery);
    	
    	// Get the arcs that declare the relationships in the network.
    	// For each arc map the ids of the fragments at their sources and targets.
    	HashMap<String,String> sourceIds = new HashMap<String,String>();
    	HashMap<String,String> targetIds = new HashMap<String,String>();
    	for (int i=0; i<links.getLength(); i++) {
    		ExtendedLink link = links.getFragment(i);
    		FragmentList<Arc> arcs = link.getArcs();
    		for (Arc arc: arcs) {
				if (arc.getArcrole().equals(arcRole)) {
		    		FragmentList<ArcEnd> sources = arc.getSourceFragments();
					FragmentList<ArcEnd> targets = arc.getTargetFragments();
					for (int k=0; k<sources.getLength(); k++) {
						sourceIds.put(sources.getFragment(k).getFragmentIndex(),"");
					}
					for (int k=0; k<sources.getLength(); k++) {
						targetIds.put(targets.getFragment(k).getFragmentIndex(),"");
					}
				}
    		}
    	}
    	
    	// Get the root resources in the network
    	FragmentList<Fragment> roots = new FragmentListImpl<Fragment>();
    	Iterator<String> iterator = sourceIds.keySet().iterator();
    	while (iterator.hasNext()) {
    		String id = iterator.next();
    		if (! targetIds.containsKey(id)) {
    			roots.addFragment(this.getFragment(id));
    		}
    	}
    	return roots;
    }    
    
    
    /**
     * @param namespace The namespace for the concept.
     * @param name The local name for the concept.
     * @return the concept fragment for the specified namespace and name.
     * @throws XBRLException if more than one matching concept is found in the data store
     * or if no matching concepts are found in the data store.
     */
    public Concept getConcept(String namespace, String name) throws XBRLException {
    	
    	FragmentList<SchemaDeclaration> candidates = this.<SchemaDeclaration>query("/*[*/xsd:element[@name='" + name + "']]");
    	FragmentList<Concept> matches = new FragmentListImpl<Concept>();
    	for (SchemaDeclaration candidate: candidates) {
    		if (candidate.getTargetNamespaceURI().equals(namespace)) {
    		    try {
                    matches.addFragment((Concept) candidate);
    		    } catch (Exception e) {
    		        ;// Casting to a concept failed so it is not a concept.
    		    }
    		}
    	}
    	
    	if (matches.getLength() == 0) 
    		throw new XBRLException("No matching concepts were found for " + namespace + ":" + name + ".");
    	
    	if (matches.getLength() > 1) 
    		throw new XBRLException(new Integer(matches.getLength()) + "matching concepts were found for " + namespace + ":" + name + ".");
    	
    	return matches.getFragment(0);
    }

    /**
     * @see org.xbrlapi.data.XBRLStore#getLinkRoles()
     */
    public HashMap<String,String> getLinkRoles() throws XBRLException {
    	HashMap<String,String> roles = new HashMap<String,String>();
    	FragmentList<RoleType> types = this.getRoleTypes();
    	for (RoleType type: types) {
    		String role = type.getCustomURI();
    		FragmentList<ExtendedLink> all = this.<ExtendedLink>getFragments("ExtendedLink");
    		logger.debug("# links = " + all.getLength());
    		String query = "/*[@type='org.xbrlapi.impl.ExtendedLinkImpl' and */*/@xlink:role='" + role + "']";
        	FragmentList<ExtendedLink> links = this.<ExtendedLink>query(query);
        	logger.debug("# links with given role = " + links.getLength());
    		if (links.getLength() > 0) {
    			roles.put(role,"");
    		}
    	}
    	return roles;
    }
    

    
    /**
     * @see org.xbrlapi.data.XBRLStore#getArcRoles()
     */
    public HashMap<String,String> getArcRoles() throws XBRLException {
    	// TODO Simplify getArcRoles method of the XBRLStore to eliminate need to get all arcs in the data store.
    	HashMap<String,String> roles = new HashMap<String,String>();
    	FragmentList<ArcroleType> types = this.getArcroleTypes();
    	for (ArcroleType type: types) {
    		String role = type.getCustomURI();
    		String query = "/"+ Constants.XBRLAPIPrefix + ":" + "fragment["+ Constants.XBRLAPIPrefix+ ":" + "data/*[@xlink:type='arc' and @xlink:arcrole='" + role + "']]";
        	FragmentList<Arc> arcs = this.<Arc>query(query);
    		if (arcs.getLength() > 0) {
    			roles.put(role,"");
    		}
    	}

    	return roles;
    }
    
    /**
     * @see org.xbrlapi.data.XBRLStore#getLinkRoles(String)
     */
    public HashMap<String,String> getLinkRoles(String arcrole) throws XBRLException {
    	HashMap<String,String> roles = new HashMap<String,String>();
    	HashMap<String,Fragment> links = new HashMap<String,Fragment>();
		String query = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment["+ Constants.XBRLAPIPrefix+ ":" + "data/*[@xlink:type='arc' and @xlink:arcrole='" + arcrole + "']]";
    	FragmentList<Arc> arcs = this.<Arc>query(query);
    	for (Arc arc: arcs) {
    		if (! links.containsKey(arc.getParentIndex())) {
    			ExtendedLink link = arc.getExtendedLink();
    			links.put(link.getFragmentIndex(),link);
    		}
    	}
    	
    	for (Fragment l: links.values()) {
    		ExtendedLink link = (ExtendedLink) l;
    		if (! roles.containsKey(link.getLinkRole())) {
    			roles.put(link.getLinkRole(),"");
    		}
    	}
    	
    	return roles;
    	
    }
    
    /**
     * @return a list of roleType fragments
     * @throws XBRLException
     */
    public FragmentList<RoleType> getRoleTypes() throws XBRLException {
    	return this.<RoleType>getFragments("RoleType");
    }
    
    /**
     * @see org.xbrlapi.data.XBRLStore#getRoleTypes(String)
     */
    public FragmentList<RoleType> getRoleTypes(String uri) throws XBRLException {
    	String query = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment["+ Constants.XBRLAPIPrefix+ ":" + "data/link:roleType/@roleURI='" + uri + "']";
    	return this.<RoleType>query(query);
    }    
    
    /**
     * @return a list of ArcroleType fragments
     * @throws XBRLException
     */
    public FragmentList<ArcroleType> getArcroleTypes() throws XBRLException {
    	return this.<ArcroleType>getFragments("ArcroleType");
    }
    
    /**
     * @return a list of arcroleType fragments that define a given arcrole.
     * @throws XBRLException
     */
    public FragmentList<ArcroleType> getArcroleTypes(String uri) throws XBRLException {
    	String query = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment["+ Constants.XBRLAPIPrefix+ ":" + "data/link:arcroleType/@arcroleURI='" + uri + "']";
    	return this.<ArcroleType>query(query);
    }
    
    /**
     * @see org.xbrlapi.data.XBRLStore#getResourceRoles()
     */
    public HashMap<String,String> getResourceRoles() throws XBRLException {
    	HashMap<String,String> roles = new HashMap<String,String>();
    	FragmentList<Resource> resources = this.<Resource>query("/"+ Constants.XBRLAPIPrefix+ ":" + "fragment["+ Constants.XBRLAPIPrefix+ ":" + "data/*/@xlink:type='resource']");
    	for (Resource resource: resources) {
    		String role = resource.getResourceRole();
    		if (! roles.containsKey(role)) roles.put(role,"");
    	}
    	return roles;
    }    


    /**
     * @see org.xbrlapi.data.XBRLStore#getMinimumDocumentSet(URI)
     */
    public List<URI> getMinimumDocumentSet(URI uri) throws XBRLException {
        List<URI> starters = new Vector<URI>();
        starters.add(uri);
        return this.getMinimumDocumentSet(starters);
    }
    
    /**
     * @see org.xbrlapi.data.XBRLStore#getMinimumDocumentSet(List)
     */
    public List<URI> getMinimumDocumentSet(List<URI> starters) throws XBRLException {
        
        List<URI> allDocuments = new Vector<URI>();        
        List<URI> documentsToCheck = new Vector<URI>();        
        Map<URI,String> foundDocuments = new HashMap<URI,String>();

        for (URI starter: starters) {
            if (! foundDocuments.containsKey(starter)) {
                foundDocuments.put(starter,"");
                documentsToCheck.add(starter);
            }
        }
        
        while (documentsToCheck.size() > 0) {
            URI doc = documentsToCheck.get(0);
            allDocuments.add(doc);
            List<URI> newDocuments = this.getReferencedDocuments(doc);
            for (URI newDocument: newDocuments) {
                if (! foundDocuments.containsKey(newDocument)) {
                    foundDocuments.put(newDocument,"");
                    documentsToCheck.add(newDocument);
                }
            }
            documentsToCheck.remove(0);
        }

        return allDocuments;

    }
 
    /**
     * @see XBRLStore#getExtendedLinksWithRole(String)
     */
    public FragmentList<ExtendedLink> getExtendedLinksWithRole(String linkrole) throws XBRLException {
        String query = "/*[*/*[@xlink:type='extended' and @xlink:role='" + linkrole + "']]";
        FragmentList<ExtendedLink> links = this.<ExtendedLink>query(query);
        return links;
    }

    /**
     * Tracks the fragments that have been processed to get minimal networks with a given arcrole
     */
    private HashMap<String,Fragment> processedFragments = new HashMap<String,Fragment>();


    /**
     * @see XBRLStore#getMinimalNetworksWithArcrole(Fragment, String)
     */
    public Networks getMinimalNetworksWithArcrole(Fragment fragment, String arcrole) throws XBRLException {
        return this.getMinimalNetworksWithArcrole(new FragmentListImpl<Fragment>(fragment),arcrole);
    }    
    
    /**
     * @see XBRLStore#getMinimalNetworksWithArcrole(FragmentList, String)
     */
    public Networks getMinimalNetworksWithArcrole(FragmentList<Fragment> fragments, String arcrole) throws XBRLException {
        
        Networks networks;
        if (this.hasStoredNetworks()) networks = this.getStoredNetworks();
        else networks = new NetworksImpl(this);

        processedFragments = new HashMap<String,Fragment>();
        
        for (Fragment fragment: fragments) {
            networks = augmentNetworksForFragment(fragment,arcrole,networks);
        }
        return networks;
    }
    
    /**
     * This method is recursive.
     * @param fragment The fragment to use as the target for the relationships to be added to the networks
     * @param arcrole The arcrole for the networks to augment.
     * @param networks The networks system to augment.
     * @return The networks after augmentation.
     * @throws XBRLException
     */
    private Networks augmentNetworksForFragment(Fragment fragment, String arcrole, Networks networks) throws XBRLException {
        if (processedFragments.containsKey(fragment.getFragmentIndex())) {
            return networks;
        }
        for (Relationship relationship: fragment.getRelationshipsToWithArcrole(arcrole)) {
            networks.addRelationship(relationship);
        }
        for (Network network: networks.getNetworks(arcrole)) {
            List<Relationship> activeRelationships = network.getActiveRelationshipsTo(fragment.getFragmentIndex());
            logger.debug(fragment.getFragmentIndex() + " has " + activeRelationships.size() + " parent fragments.");
            for (Relationship activeRelationship: activeRelationships) {
                Fragment source = activeRelationship.getSource();
                networks = augmentNetworksForFragment(source,arcrole,networks);
            }
        }
        return networks;
    }
    
    
    
    
}
