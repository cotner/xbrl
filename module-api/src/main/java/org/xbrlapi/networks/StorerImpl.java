package org.xbrlapi.networks;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.xbrlapi.Arc;
import org.xbrlapi.ArcEnd;
import org.xbrlapi.Fragment;
import org.xbrlapi.Locator;
import org.xbrlapi.PersistedRelationship;
import org.xbrlapi.data.Store;
import org.xbrlapi.impl.PersistedRelationshipImpl;
import org.xbrlapi.impl.PersistedRelationshipPriorityComparator;
import org.xbrlapi.utilities.Constants;
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
        if (! getStore().hasXMLResource(persistedRelationship.getIndex())) getStore().persist(persistedRelationship);
    }

    /**
     * @see org.xbrlapi.networks.Storer#storeRelationship(Arc, Fragment, Fragment)
     */
    public void storeRelationship(Arc arc, Fragment source, Fragment target) throws XBRLException {
        PersistedRelationship persistedRelationship = new PersistedRelationshipImpl(arc, source, target);
        if (! getStore().hasXMLResource(persistedRelationship.getIndex())) getStore().persist(persistedRelationship);
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
        this.storeRelationships(getStore().getDocumentURIs());
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
     * @see org.xbrlapi.networks.Storer#storeRelationships(Collection)
     */
    public void storeRelationships(Collection<URI> documents) throws XBRLException {
        for (URI document: documents) {
            storeRelationships(document);
        }
    }
    
    /**
     * This implementation uses just 3 database queries for the document.
     * @see org.xbrlapi.networks.Storer#storeRelationships(URI)
     */
    public void storeRelationships(URI document) throws XBRLException {

        Store store = getStore();

        // Get indices of all arcs in the document.
        Set<String> arcIndices = store.getFragmentIndicesFromDocument(document,"Arc");

        if (arcIndices.size() > 0) {
            logger.info("# arcs = " + arcIndices.size() + " in " + document);
            long start = System.currentTimeMillis();

            // Get indices of arc ends in the document.
            Map<String,List<String>> endIndices = new HashMap<String,List<String>>();
            String query = "for $fragment in #roots#[@uri='" + document + "' and */*[@xlink:type='resource' or @xlink:type='locator']] return concat($fragment/@index,' ',$fragment/@parentIndex,$fragment/*/*/@xlink:label)";
            Set<String> pairs = getStore().queryForStrings(query);
            for (String pair: pairs) {
                int split = pair.indexOf(" ");
                String index = pair.substring(0,split);
                String label = pair.substring(split+1);
                if (endIndices.containsKey(label)) {
                    endIndices.get(label).add(index);
                } else {
                    List<String> list = new Vector<String>();
                    list.add(index);
                    endIndices.put(label,list);
                }
            }
    
            // Get indices of locator target fragments
            Map<String,String> locatorTargets = new HashMap<String,String>();
            query = "for $locator in #roots#[@uri='" + document + "' and */*/@xlink:type='locator'] return concat($locator/@index,' ',#roots#[@uri=$locator/@targetDocumentURI and $locator/@targetPointerValue=" + Constants.XBRLAPIPrefix + ":xptr/@value]/@index)";
            pairs = getStore().queryForStrings(query);
            for (String pair: pairs) {
                int split = pair.indexOf(" ");
                String locatorIndex = pair.substring(0,split);
                String targetIndex = pair.substring(split+1);
                locatorTargets.put(locatorIndex,targetIndex);
            }
            
            // Iterate arcs, storing relationships defined by each
            for (String index: arcIndices) {
                Arc arc = getStore().<Arc>getXMLResource(index);
                String parentIndex = arc.getParentIndex();
                String from = parentIndex + arc.getFrom();
                String to = parentIndex + arc.getTo();
                if (endIndices.containsKey(from) && endIndices.containsKey(to)) {
                    for (String sourceIndex: endIndices.get(from)) {
                        for (String targetIndex: endIndices.get(to)) {
                            Fragment source = null;
                            if (locatorTargets.containsKey(sourceIndex))
                                source = store.getXMLResource(locatorTargets.get(sourceIndex));
                            else 
                                source = store.getXMLResource(sourceIndex);
                            Fragment target = null;
                            if (locatorTargets.containsKey(targetIndex))
                                target = store.getXMLResource(locatorTargets.get(targetIndex));
                            else 
                                target = store.getXMLResource(targetIndex);
                            this.storeRelationship(arc,source,target);
                        }
                    }
                }
            }
            logger.info("Average ms to persist " + arcIndices.size() + " arcs = " + (System.currentTimeMillis() - start)/arcIndices.size());
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
     * @param linkRole The link role of the relationships to mark.
     * @param arcrole the arcrole  of the relationships to mark.
     * @throws XBRLException
     */
    private void markActiveRelationships(URI linkRole, URI arcrole)
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
        List<PersistedRelationship> relationships = this.getStore().<PersistedRelationship>queryForXMLResources(query);
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
