package org.xbrlapi.networks;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.xbrlapi.Arc;
import org.xbrlapi.ArcEnd;
import org.xbrlapi.Fragment;
import org.xbrlapi.Locator;
import org.xbrlapi.PersistedRelationship;
import org.xbrlapi.data.Store;
import org.xbrlapi.impl.PersistedRelationshipImpl;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class StorerImpl implements Storer {

    protected static Logger logger = Logger.getLogger(StorerImpl.class);   
    
    private Store store;
    
    public StorerImpl(Store store) throws XBRLException {
        super();
        setStore(store);
    }

    /**
     * @return The data store in which the relationships are
     * to be persisted.
     * @throws XBRLException if the data store is null.
     */
    private Store getStore() {
        return store;
    }
    
    /**
     * @param store The data store in which the relationships are
     * to be persisted.
     * @throws XBRLException if the data store is null.
     */
    private void setStore(Store store) throws XBRLException {
        if (store == null) throw new XBRLException("The store must not be null.");
        this.store = store;
    }

    /**
     * @see org.xbrlapi.networks.Storer#storeRelationship(org.xbrlapi.networks.Relationship)
     */
    public void storeRelationship(Relationship relationship)
            throws XBRLException {
        
        PersistedRelationship persistedRelationship = new PersistedRelationshipImpl(relationship);
        if (! getStore().hasXML(persistedRelationship.getIndex())) getStore().persist(persistedRelationship);
    }

    /**
     * @see org.xbrlapi.networks.Storer#storeRelationships(org.xbrlapi.networks.Network)
     */
    public void storeRelationships(Network network) throws XBRLException {
        for (Relationship relationship: network.getAllActiveRelationships()) {
            storeRelationship(relationship);
        }
    }

    /**
     * @see org.xbrlapi.networks.Storer#storeRelationships(org.xbrlapi.networks.Networks)
     */
    public void storeRelationships(Networks networks) throws XBRLException {
        for (Network network: networks) {
            storeRelationships(network);
        }
    }

    /**
     * @see org.xbrlapi.networks.Storer#StoreAllNetworks()
     */
    public void StoreAllNetworks() throws XBRLException {
        
        Set<URI> arcroles = getStore().getArcroles();
        logger.info("# of arcroles = " + arcroles.size());
        for (URI arcrole: arcroles) {
            Set<URI> linkRoles = getStore().getLinkRoles(arcrole);
            logger.info(linkRoles.size() + " linkroles for arcrole "+ arcrole);
            for (URI linkRole: linkRoles) {
                this.deleteRelationships(linkRole,arcrole);
                this.storeRelationships(getStore().getNetworks(linkRole,arcrole));
            }
        }
    }

    /**
     * @see org.xbrlapi.networks.Storer#deleteRelationships(java.net.URI, java.net.URI)
     */
    public void deleteRelationships(URI linkRole, URI arcrole) throws XBRLException {
        Store store = getStore();
        Set<String> indices = store.queryForIndices("/*[@type='org.xbrlapi.impl.PersistedRelationshipImpl' and @arcRole='"+arcrole+"' and @linkRole='"+linkRole+"']");
        for (String index: indices) {
            store.remove(index);
        }
    }

    /**
     * @see org.xbrlapi.networks.Storer#deleteRelationships()
     */
    public void deleteRelationships() throws XBRLException {
        Store store = getStore();
        Set<String> indices = store.queryForIndices("/*[@type='org.xbrlapi.impl.PersistedRelationshipImpl']");
        for (String index: indices) {
            store.remove(index);
        }
    }

    /**
     * @see org.xbrlapi.networks.Storer#storeRelationships(List<URI>)
     */
    public void storeRelationships(Set<URI> documents) throws XBRLException {

        // Get all of the arcs
        List<Arc> arcs = new Vector<Arc>();
        for (URI document: documents) {
            String query = "/*[@uri='"+ document +"' and */*/@xlink:type='arc']";
            List<Arc> newArcs = getStore().<Arc>query(query);
            logger.info(document + " contains " + newArcs.size() + " arcs.");
            arcs.addAll(newArcs);
        }
        
        logger.info("Got " + arcs.size() + " arcs to persist.");
        
        Networks networks = new NetworksImpl(getStore());
        
        for (Arc arc: arcs) {
            List<ArcEnd> sources = arc.getSourceFragments();
            List<ArcEnd> targets = arc.getTargetFragments();
            
            for (ArcEnd source: sources) {
                for (ArcEnd target: targets) {
                    Fragment s = source;
                    Fragment t = target;
                    if (source.getType().equals("org.xbrlapi.impl.LocatorImpl")) s = ((Locator) source).getTarget();
                    if (target.getType().equals("org.xbrlapi.impl.LocatorImpl")) t = ((Locator) target).getTarget();
                    Relationship relationship = new RelationshipImpl(arc,s,t);
                    networks.addRelationship(relationship);
                }
            }
        }
        
        networks.complete();
        
        // Add in the new relationships
        Analyser analyser = new AnalyserImpl(getStore());
        for (Network network: networks) {
            for (Relationship relationship: network.getAllActiveRelationships()) {
                this.storeRelationship(relationship);
                List<PersistedRelationship> persistedRelationships = 
                    analyser.getRelationships(
                            relationship.getSourceIndex(),
                            relationship.getTargetIndex(),
                            relationship.getLinkRole(),
                            relationship.getArcrole());
                if (persistedRelationships.size() > 1) {
                    HashMap<String,Relationship> relationshipMap = new HashMap<String,Relationship>();
                    for (PersistedRelationship pr: persistedRelationships) {
                        Relationship candidateRelationship = new RelationshipImpl(pr);
                        String key = candidateRelationship.getSemanticKey();
                        if (relationshipMap.containsKey(key)) {
                            if (candidateRelationship.getPriority().intValue() > relationshipMap.get(key).getPriority().intValue()) {
                                Relationship or = relationshipMap.get(key);
                                PersistedRelationship opr = new PersistedRelationshipImpl(or);
                                getStore().remove(opr.getIndex());
                                relationshipMap.put(key,candidateRelationship);
                            }
                        }
                    }
                }
            }
        }

    }

}
