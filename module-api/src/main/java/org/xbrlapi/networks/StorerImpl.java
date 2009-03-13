package org.xbrlapi.networks;

import java.net.URI;
import java.util.Set;

import org.apache.log4j.Logger;
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
        PersistedRelationship xml = new PersistedRelationshipImpl(relationship);
        getStore().persist(xml);
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
        for (URI arcrole: arcroles) {
            Set<URI> linkRoles = getStore().getLinkRoles(arcrole);
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
    
    
    
    

    

}
