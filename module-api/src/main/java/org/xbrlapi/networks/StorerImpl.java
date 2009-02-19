package org.xbrlapi.networks;

import java.net.URI;
import java.util.List;

import org.apache.log4j.Logger;
import org.xbrlapi.ActiveRelationship;
import org.xbrlapi.data.XBRLStore;
import org.xbrlapi.impl.ActiveRelationshipImpl;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class StorerImpl implements Storer {

    protected static Logger logger = Logger.getLogger(StorerImpl.class);   
    
    private XBRLStore store;
    
    public StorerImpl(XBRLStore store) throws XBRLException {
        super();
        setStore(store);
    }

    /**
     * @return The data store in which the relationships are
     * to be persisted.
     * @throws XBRLException if the data store is null.
     */
    private XBRLStore getStore() {
        return store;
    }
    
    /**
     * @param store The data store in which the relationships are
     * to be persisted.
     * @throws XBRLException if the data store is null.
     */
    private void setStore(XBRLStore store) throws XBRLException {
        if (store == null) throw new XBRLException("The store must not be null.");
        this.store = store;
    }

    /**
     * @see org.xbrlapi.networks.Storer#storeRelationship(org.xbrlapi.networks.Relationship)
     */
    public void storeRelationship(Relationship relationship)
            throws XBRLException {
        ActiveRelationship xml = new ActiveRelationshipImpl(relationship);
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
        
        List<URI> arcroles = getStore().getArcroles();
        for (URI arcrole: arcroles) {
            List<URI> linkRoles = getStore().getLinkRoles(arcrole);
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
        XBRLStore store = getStore();
        List<String> indices = store.queryForIndices("/*[@type='org.xbrlapi.impl.ActiveRelationshipImpl' and @arcRole='"+arcrole+"' and @linkRole='"+linkRole+"']");
        for (String index: indices) {
            store.remove(index);
        }
    }

    /**
     * @see org.xbrlapi.networks.Storer#deleteRelationships()
     */
    public void deleteRelationships() throws XBRLException {
        XBRLStore store = getStore();
        List<String> indices = store.queryForIndices("/*[@type='org.xbrlapi.impl.ActiveRelationshipImpl']");
        for (String index: indices) {
            store.remove(index);
        }
    }
    
    
    
    

    

}
