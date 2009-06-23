package org.xbrlapi.networks;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.xbrlapi.Arc;
import org.xbrlapi.ArcEnd;
import org.xbrlapi.ExtendedLink;
import org.xbrlapi.Fragment;
import org.xbrlapi.Locator;
import org.xbrlapi.PersistedRelationship;
import org.xbrlapi.data.Store;
import org.xbrlapi.impl.PersistedRelationshipImpl;
import org.xbrlapi.impl.PersistedRelationshipPriorityComparator;
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
        if (! store.isUsingPersistedNetworks()) {
            store.setAnalyser(new AnalyserImpl(store));
        }
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
     * @see org.xbrlapi.networks.Storer#storeRelationship(Arc, Fragment, Fragment)
     */
    public void storeRelationship(Arc arc, Fragment source, Fragment target) throws XBRLException {
        PersistedRelationship persistedRelationship = new PersistedRelationshipImpl(arc, source, target);
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

    /**+++
     * @see org.xbrlapi.networks.Storer#storeAllRelationships()
     */
    public void storeAllRelationships() throws XBRLException {
        this.storeRelationships(getStore().getStoredURIs());
    }

    /**
     * @see org.xbrlapi.networks.Storer#deleteRelationships(java.net.URI, java.net.URI)
     */
    public void deleteRelationships(URI linkRole, URI arcrole) throws XBRLException {
        Store store = getStore();
        Set<String> indices = store.queryForIndices("#roots#[@type='org.xbrlapi.impl.PersistedRelationshipImpl' and @arcRole='"+arcrole+"' and @linkRole='"+linkRole+"']");
        for (String index: indices) {
            store.remove(index);
        }
    }

    /**
     * @see org.xbrlapi.networks.Storer#deleteRelationships()
     */
    public void deleteRelationships() throws XBRLException {
        Store store = getStore();
        Set<String> indices = store.queryForIndices("#roots#[@type='org.xbrlapi.impl.PersistedRelationshipImpl']");
        for (String index: indices) {
            store.remove(index);
        }
    }

    /**
     * @see org.xbrlapi.networks.Storer#storeRelationships(List<URI>)
     */
    public void storeRelationships(Collection<URI> documents) throws XBRLException {
        for (URI document: documents) {
            storeRelationships(document);
        }
    }
    
    /**
     * @see org.xbrlapi.networks.Storer#storeRelationships(URI)
     */
    public void storeRelationships(URI document) throws XBRLException {

        Store store = getStore();

        Set<String> linkIndices = store.getFragmentIndicesFromDocument(document,"ExtendedLink");
        for (String linkIndex: linkIndices) {
            ExtendedLink link = (ExtendedLink) store.getFragment(linkIndex);
            Map<String,List<String>> endIndices = link.getArcEndIndicesByLabel();
            Set<String> arcIndices = link.getChildrenIndices("org.xbrlapi.impl.ArcImpl");

            if (arcIndices.size() > 0) {
                long start = System.currentTimeMillis();
                logger.info("Storing relationships for " + arcIndices.size() + " arcs in extended link.");
                int count = 0;

                for (String index: arcIndices) {
                    Arc arc = getStore().<Arc>getFragment(index);
                    String from = arc.getFrom();
                    String to = arc.getTo();
                    if (endIndices.containsKey(from) && endIndices.containsKey(to)) {
                        for (String sourceIndex: endIndices.get(from)) {
                            for (String targetIndex: endIndices.get(to)) {
                                this.storeRelationship(arc,(Fragment) store.getFragment(sourceIndex),(Fragment) store.getFragment(targetIndex));
                            }
                        }
                    }
                    if (count > 10) break;
                    count++;
                    logger.info("MS to persist arc = " + (System.currentTimeMillis() - start));
                    start = System.currentTimeMillis();
                }
                
            }
            
        }
        
/*        Set<String> arcIndices = getStore().getFragmentIndicesFromDocument(document,"Arc");
        if (arcIndices.size() > 0) {
            logger.info("Storing relationships for " + arcIndices.size() + " arcs in " + document);
            long start = System.currentTimeMillis();
            int count = 0;
            for (String index: arcIndices) {
                storeRelationships(getStore().<Arc>getFragment(index));
                logger.info("MS to persist arc = " + (System.currentTimeMillis() - start));
                start = System.currentTimeMillis();
                if (count > 10) break;
                count++;
            }
        }
*/
        getStore().sync();

    }    
    
    /**
     * @param arc The arc to store relationships for.
     * @throws XBRLException
     */
    private void storeRelationships(Arc arc) throws XBRLException {

        long start = System.currentTimeMillis();

        try {
            List<ArcEnd> sources = arc.getSourceFragments();
            List<ArcEnd> targets = arc.getTargetFragments();
            for (ArcEnd source: sources) {
                for (ArcEnd target: targets) {
                    Fragment s = source;
                    Fragment t = target;
                    if (source.getType().equals("org.xbrlapi.impl.LocatorImpl")) s = ((Locator) source).getTarget();
                    if (target.getType().equals("org.xbrlapi.impl.LocatorImpl")) t = ((Locator) target).getTarget();
                    storeRelationship(arc,s,t);
                }
            }
            
            logger.debug("" + (System.currentTimeMillis() - start) + " ms to store relationships for arc " + arc.getIndex());
            
        } catch (XBRLException e) {
            logger.error("The relationship expressed by arc " + arc.getIndex() + " could not be persisted. " + e.getMessage());
        }

        
    }

    /**
     * @see Storer#deleteInactiveRelationships()
     */
    public void deleteInactiveRelationships() throws XBRLException {

        logger.info("Deleting inactive persisted relationships.");
        Analyser analyser = new AnalyserImpl(getStore());
        
        Set<URI> arcroles = analyser.getArcroles();
        for (URI arcrole: arcroles) {
            Set<URI> linkRoles = analyser.getLinkRoles(arcrole);
            for (URI linkRole: linkRoles) {
                this.deleteInactiveRelationships(linkRole,arcrole);
            }
        }
    }

    /**
     * @see Storer#deleteInactiveRelationships(URI, URI)
     */
    public void deleteInactiveRelationships(URI linkRole, URI arcrole)
            throws XBRLException {

        logger.info("Deleting inactive persisted relationships for linkRole: " + linkRole + " and arcrole " + arcrole);

        String query = "#roots#[@linkRole='"+linkRole+"' and @arcRole='"+arcrole+"']/@sourceIndex";
        Set<String> sourceIndices = getStore().queryForStrings(query);
        logger.info("# sources = " + sourceIndices.size());
        for (String sourceIndex: sourceIndices) {
            query = "#roots#[@linkRole='"+linkRole+"' and @arcRole='"+arcrole+"' and @sourceIndex='"+sourceIndex+"']/@targetIndex";
            Set<String> targetIndices = getStore().queryForStrings(query);
            for (String targetIndex: targetIndices) {
                Map<String,SortedSet<PersistedRelationship>> map = getEquivalentRelationships(linkRole,arcrole,sourceIndex,targetIndex);
                for (String key: map.keySet()) {
                    SortedSet<PersistedRelationship> equivalents = map.get(key);
                    PersistedRelationship active = equivalents.first();
                    for (PersistedRelationship equivalent: equivalents) {
                        if (equivalent != active) {
                            // getStore().remove(equivalent);
                            logger.info("removing " + equivalent.getArc().getURI() + " " + equivalent.getIndex());
                        }
                    }
                    //if (active.getArcUse().equals("prohibited") getStore().remove(active);
                }
            }
        }
        
    }    

    /**
     * @see Storer#markActiveRelationships(URI, URI)
     */
    public void markActiveRelationships(URI linkRole, URI arcrole)
            throws XBRLException {
        
        String query = "#roots#[@linkRole='"+linkRole+"' and @arcRole='"+arcrole+"']/@sourceIndex";
        Set<String> sourceIndices = getStore().queryForStrings(query);
        logger.info("# sources = " + sourceIndices.size());
        for (String sourceIndex: sourceIndices) {
            query = "#roots#[@linkRole='"+linkRole+"' and @arcRole='"+arcrole+"' and @sourceIndex='"+sourceIndex+"']/@targetIndex";
            Set<String> targetIndices = getStore().queryForStrings(query);
            for (String targetIndex: targetIndices) {
                Map<String,SortedSet<PersistedRelationship>> map = getEquivalentRelationships(linkRole,arcrole,sourceIndex,targetIndex);
                for (String key: map.keySet()) {
                    map.get(key).first().setMetaAttribute("active","");
                }
            }
        }
        
    }

    /**
     * @param linkRole The network link role
     * @param arcrole The network arcrole
     * @return a map of sorted sets of equivalent relationships in the network.
     * @throws XBRLException
     */
    private Map<String,SortedSet<PersistedRelationship>> getEquivalentRelationships(URI linkRole, URI arcrole, String sourceIndex, String targetIndex)
            throws XBRLException {
        
        Map<String,SortedSet<PersistedRelationship>> map = new HashMap<String,SortedSet<PersistedRelationship>>();
        String query = "#roots#[@linkRole='"+linkRole+"' and @arcRole='"+arcrole+"' and @sourceIndex='"+sourceIndex+"' and @targetIndex='"+targetIndex+"']";
        List<PersistedRelationship> relationships = this.getStore().<PersistedRelationship>queryForFragments(query);
        for (PersistedRelationship relationship: relationships) {
            String key = relationship.getSourceIndex() + relationship.getTargetIndex() + relationship.getLinkRole() + relationship.getArcrole() + relationship.getSignature();
            if (map.containsKey(key)) {
                map.get(key).add(relationship);
            } else {
                SortedSet<PersistedRelationship> set = new TreeSet<PersistedRelationship>(new PersistedRelationshipPriorityComparator());
                set.add(relationship);
                map.put(key,set);
            }
        }

        return map;
    }
    
}
