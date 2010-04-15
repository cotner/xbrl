package org.xbrlapi.networks;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.xbrlapi.Fragment;
import org.xbrlapi.Relationship;
import org.xbrlapi.data.Store;
import org.xbrlapi.impl.RelationshipOrderComparator;
import org.xbrlapi.utilities.XBRLException;

/**
 * This Analyser returns information based upon all
 * <em>active</em> relationships.
 * Set this as the analyser used by a store when you want that
 * store to produce query results where only active relationships
 * are considered.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class AnalyserImpl implements Analyser {

    /**
     * 
     */
    private static final long serialVersionUID = -3842008045278037807L;
    
    protected final static Logger logger = Logger.getLogger(AnalyserImpl.class);
    
    /**
     * Algorithm involves:
     * <ol>
     *  <li>Getting all persisted relationships in the document</li>
     *  <li>Getting all arc+arcend+arcsource combinations in the document</li>
     *  <li>Matching each arc to its persisted relationships</li>
     *  <li>Returning false if any arc does not have persisted relationships</li>
     * </ol>
     * @see Analyser#hasAllRelationships(URI)
     */
    public boolean hasAllRelationships(URI document) throws XBRLException {
        String query = 
            "for $arc in #roots#[@uri='"+document+"' and */*/@xlink:type='arc'], " +
            "$start in #roots#[@parentIndex=$arc/@parentIndex and */*[@xlink:type='resource' or @xlink:type='locator']], " +
            "$end in #roots#[@parentIndex=$arc/@parentIndex and */*[@xlink:type='resource' or @xlink:type='locator']] where " +
            "$arc/*/*/@xlink:from=$start/*/*/@xlink:label " +
            "and $arc/*/*/@xlink:to=$end/*/*/@xlink:label " +
            "return concat($arc/@index,' ',$start/@index,' ',$end/@index)";
        long tripleCount = getStore().queryCount(query);
        logger.debug("#Triples =" + tripleCount);
        query = "for $relationship in #roots# where $relationship/@type='org.xbrlapi.impl.RelationshipImpl' and $relationship/@arcURI='" + document + "' return $relationship";
        long relationshipCount = store.queryCount(query);
        logger.debug("#Relationships =" + relationshipCount);
        if (tripleCount != relationshipCount) return false;
        return true;

    }
    
    /**
     * This method provides a place to filter relationships after retrieving them
     * to eliminate overridden and prohibited relationships as desired.
     * @param query The query to run to get the relationships.
     * @return the list of relationships returned by the query.
     * @throws XBRLException
     */
    protected List<Relationship> getRelationships(String query) throws XBRLException {
        List<Relationship> relationships = getStore().<Relationship>queryForXMLResources(query);
        Networks networks = new NetworksImpl(getStore());
        networks.addRelationships(relationships);
        return networks.getActiveRelationships();
    }
    
    /**
     * @see Analyser#getAllRelationships()
     */
    public List<Relationship> getAllRelationships() throws XBRLException {
        return getRelationships("#roots#[@type='org.xbrlapi.impl.RelationshipImpl']");
    }    
    
    /**
     * @param store The data store to use.
     * @throws XBRLException if the store is null.
     */
    public AnalyserImpl(Store store) throws XBRLException {
        super();
        setStore(store);
    }
    
    private Store store = null;
    
    /**
     * @param store The store that the active relationships are persisted in.
     * @throws XBRLException if the store is null.
     */
    private void setStore(Store store) throws XBRLException {
        if (store == null) throw new XBRLException("The store must not be null.");
        this.store = store;
    }
    
    /**
     * @return the store that the active relationships are persisted in.
     */
    protected Store getStore() {
        return this.store;
    }
    
    /**
     * @see org.xbrlapi.networks.Analyser#getArcroles()
     */
    public Set<URI> getArcroles() throws XBRLException {
        return store.getArcroles();
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getArcroles(java.net.URI)
     */
    public Set<URI> getArcroles(URI linkRole) throws XBRLException {
        return store.getArcroles(linkRole);
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getLinkRoles()
     */
    public Set<URI> getLinkRoles() throws XBRLException {
        return store.getLinkRoles();
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getLinkRoles(URI)
     */
    public Set<URI> getLinkRoles(URI arcrole) throws XBRLException {
        return store.getLinkRoles(arcrole);
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getRelationships(Set)
     */
    public List<Relationship> getRelationships(Set<URI> arcroles) throws XBRLException {
        List<Relationship> relationships = new Vector<Relationship>();
        for (URI arcrole: arcroles) {
            relationships.addAll(this.getRelationships(arcrole));
        }
        return relationships;
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getRelationships(URI, Set)
     */
    public List<Relationship> getRelationships(URI linkRole, Set<URI> arcroles) throws XBRLException {
        List<Relationship> relationships = new Vector<Relationship>();
        for (URI arcrole: arcroles) {
            relationships.addAll(this.getRelationships(linkRole, arcrole));
        }
        return relationships;
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getRelationships(java.net.URI, java.net.URI)
     */
    public List<Relationship> getRelationships(URI linkRole, URI arcrole) throws XBRLException {
        String query = "#roots#[@arcRole='"+ arcrole +"' and @linkRole='"+ linkRole +"']";
        return this.getRelationships(query);
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getRelationships(java.net.URI)
     */
    public List<Relationship> getRelationships(URI arcrole) throws XBRLException {
        Set<URI> linkRoles = this.getLinkRoles(arcrole);
        List<Relationship> relationships = new Vector<Relationship>();
        for (URI linkRole: linkRoles) {
            relationships.addAll(this.getRelationships(linkRole, arcrole));
        }
        return relationships;
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getRelationshipsFrom(java.lang.String, Set)
     */
    public List<Relationship> getRelationshipsFrom(String sourceIndex, Set<URI> arcroles) throws XBRLException {
        List<Relationship> relationships = new Vector<Relationship>();
        for (URI arcrole: arcroles) {
            Set<URI> linkRoles = this.getLinkRoles(arcrole);
            for (URI linkRole: linkRoles) {
                relationships.addAll(this.getRelationshipsFrom(sourceIndex,linkRole, arcrole));
            }
        }
        return relationships;
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getRelationshipsFrom(java.lang.String, java.net.URI, Set)
     */
    public List<Relationship> getRelationshipsFrom(String sourceIndex, URI linkRole, Set<URI> arcroles) throws XBRLException {
        List<Relationship> relationships = new Vector<Relationship>();
        for (URI arcrole: arcroles) {
            relationships.addAll(this.getRelationshipsFrom(sourceIndex, linkRole, arcrole));
        }
        return relationships;
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getRelationshipsFrom(java.lang.String, java.net.URI, java.net.URI)
     */
    public SortedSet<Relationship> getRelationshipsFrom(String sourceIndex, URI linkRole, URI arcrole) throws XBRLException {
        String lr = "";
        String ar = "";
        if (linkRole != null) {
            lr = " and @linkRole='"+ linkRole +"'";            
        }
        if (arcrole != null) {
            ar = " and @arcRole='"+ arcrole +"'";            
        }
        String query = "#roots#[@sourceIndex='"+ sourceIndex +"'" + ar + lr + "]";
        List<Relationship> list = this.getRelationships(query);
        SortedSet<Relationship> sortedSet = new TreeSet<Relationship>(new RelationshipOrderComparator());
        sortedSet.addAll(list);
        return sortedSet;
    }
    
    
    

    /**
     * @see org.xbrlapi.networks.Analyser#getRelationshipsFrom(java.lang.String, java.net.URI)
     */
    public List<Relationship> getRelationshipsFrom(String sourceIndex, URI arcrole) throws XBRLException {
        List<Relationship> relationships = new Vector<Relationship>();
        Set<URI> linkRoles = this.getLinkRoles(arcrole);
        for (URI linkRole: linkRoles) {
            relationships.addAll(this.getRelationshipsFrom(sourceIndex,linkRole, arcrole));
        }
        return relationships;
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getRelationshipsTo(java.lang.String, Set)
     */
    public List<Relationship> getRelationshipsTo(String targetIndex, Set<URI> arcroles) throws XBRLException {
        List<Relationship> relationships = new Vector<Relationship>();
        for (URI arcrole: arcroles) {
            Set<URI> linkRoles = this.getLinkRoles(arcrole);
            for (URI linkRole: linkRoles) {
                relationships.addAll(this.getRelationshipsTo(targetIndex,linkRole, arcrole));
            }
        }
        return relationships;
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getRelationshipsTo(java.lang.String, java.net.URI, Set)
     */
    public List<Relationship> getRelationshipsTo(String targetIndex, URI linkRole, Set<URI> arcroles) throws XBRLException {
        List<Relationship> relationships = new Vector<Relationship>();
        for (URI arcrole: arcroles) {
            relationships.addAll(this.getRelationshipsTo(targetIndex, linkRole, arcrole));
        }
        return relationships;
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getRelationshipsTo(java.lang.String, java.net.URI, java.net.URI)
     */
    public SortedSet<Relationship> getRelationshipsTo(String targetIndex, URI linkRole, URI arcrole) throws XBRLException {
        String lr = "";
        String ar = "";
        if (linkRole != null) {
            lr = " and @linkRole='"+ linkRole +"'";            
        }
        if (arcrole != null) {
            ar = " and @arcRole='"+ arcrole +"'";            
        }
        String query = "#roots#[@targetIndex='"+ targetIndex +"'" + ar + lr + "]";
        List<Relationship> list = this.getRelationships(query);
        SortedSet<Relationship> sortedSet = new TreeSet<Relationship>(new RelationshipOrderComparator());
        sortedSet.addAll(list);
        return sortedSet;
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getRelationshipsTo(java.lang.String, java.net.URI)
     */
    public List<Relationship> getRelationshipsTo(String targetIndex, URI arcrole) throws XBRLException {
        List<Relationship> relationships = new Vector<Relationship>();
        Set<URI> linkRoles = this.getLinkRoles(arcrole);
        for (URI linkRole: linkRoles) {
            relationships.addAll(this.getRelationshipsTo(targetIndex,linkRole, arcrole));
        }
        return relationships;
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getRootRelationships(java.net.URI, java.net.URI)
     */
    public List<Relationship> getRootRelationships(URI linkRole, URI arcrole) throws XBRLException {
        String lr = "";
        String ar = "";
        if (linkRole != null) {
            lr = " and @linkRole='"+ linkRole +"'";            
        }
        if (arcrole != null) {
            ar = " and @arcRole='"+ arcrole +"'";
        }
        Set<String> rootIndices = this.getRootIndices(linkRole,arcrole);
        List<Relationship> relationships = new Vector<Relationship>();
        for (String index: rootIndices) {
            String query = "#roots#[@sourceIndex='"+ index + "'" + ar + lr + "]";
            relationships.addAll(this.getRelationships(query));
        }
        return relationships;
    }
    
    /**
     * @see Analyser#getRoots(java.net.URI, java.net.URI)
     */
    public <F extends Fragment> Set<F> getRoots(URI linkRole, URI arcrole) throws XBRLException {
        Set<String> rootIndices = this.getRootIndices(linkRole,arcrole);
        Set<F> roots = new TreeSet<F>();
        for (String index: rootIndices) {
            roots.add(getStore().<F>getXMLResource(index));
        }
        return roots;
    }
    
    /**
     * @see Analyser#getRootIndices(java.net.URI, java.net.URI)
     */
    public Set<String> getRootIndices(URI linkRole, URI arcrole) throws XBRLException {
        String lr = "";
        String ar = "";
        if (linkRole != null) {
            lr = " and @linkRole='"+ linkRole +"'";            
        }
        if (arcrole != null) {
            ar = " and @arcRole='"+ arcrole +"'";
        }
        String sourcesQuery = "for $root in #roots#[@sourceIndex " + ar + lr + "] return string($root/@sourceIndex)";
        Set<String> sourceIndices = this.getStore().queryForStrings(sourcesQuery);
        String targetsQuery = "for $root in #roots#[@targetIndex " + ar + lr + "] return string($root/@targetIndex)";
        Set<String> targetIndices = this.getStore().queryForStrings(targetsQuery);
        sourceIndices.removeAll(targetIndices);
        return sourceIndices;
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getLabelRelationships(java.lang.String)
     */
    public List<Relationship> getLabelRelationships(String sourceIndex) throws XBRLException {
        String query = "#roots#[@label and @sourceIndex='"+ sourceIndex+"']";
        return this.getStore().<Relationship>queryForXMLResources(query);
    }
    


    /**
     * @see org.xbrlapi.networks.Analyser#getLabelRelationshipsByLanguage(java.lang.String, java.lang.String)
     */
    public List<Relationship> getLabelRelationshipsByLanguage( 
            String sourceIndex, String language) 
            throws XBRLException {
        String query = "#roots#[@label and @sourceIndex='"+ sourceIndex+"' and targetLanguage='"+ language +"']";
        return this.getStore().<Relationship>queryForXMLResources(query);
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getLabelRelationshipsByLanguageAndRole(java.lang.String, java.lang.String, java.net.URI)
     */
    public List<Relationship> getLabelRelationshipsByLanguageAndRole(
            String sourceIndex, String language, URI role) throws XBRLException {
        String query = "#roots#[@label and @sourceIndex='"+ sourceIndex+"' and targetLanguage='"+ language +"' and targetRole='"+ role +"']";
        return this.getStore().<Relationship>queryForXMLResources(query);
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getLabelRelationshipsByLanguages(java.lang.String, java.util.List)
     */
    public List<Relationship> getLabelRelationshipsByLanguages(
            String sourceIndex, List<String> languages) throws XBRLException {
        for (String language: languages) {
            List<Relationship> relationships;
            if (language == null) { 
                relationships = this.getLabelRelationships(sourceIndex);
            } else {
                String query = "#roots#[@label and @sourceIndex='"+ sourceIndex+"' and targetLanguage='"+ language +"']";
                relationships = this.getStore().<Relationship>queryForXMLResources(query);
            }
            if (! relationships.isEmpty()) return relationships;
        }
        return new Vector<Relationship>();
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getLabelRelationshipsByRole(java.lang.String, java.net.URI)
     */
    public List<Relationship> getLabelRelationshipsByRole(
            String sourceIndex, URI role) throws XBRLException {
        String query = "#roots#[@label and @sourceIndex='"+ sourceIndex+"' targetRole='"+ role +"']";
        return this.getStore().<Relationship>queryForXMLResources(query);
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getLabelRelationshipsByRoles(java.lang.String, java.util.List, java.util.List)
     */
    public List<Relationship> getLabelRelationshipsByRoles(
            String sourceIndex, List<String> languages, List<URI> roles)
            throws XBRLException {
        for (String language: languages) {
            for (URI role: roles) {
                List<Relationship> relationships;
                String languagePhrase = "";
                if (language != null) languagePhrase = " and targetLanguage='"+ language +"'";
                String rolePhrase = "";
                if (role != null) rolePhrase = " and targetRole='"+ role +"'";
                String query = "#roots#[@label and @sourceIndex='"+ sourceIndex+"'" + languagePhrase + rolePhrase + "]";
                relationships = this.getStore().<Relationship>queryForXMLResources(query);
                if (! relationships.isEmpty()) return relationships;
            }
        }
        return new Vector<Relationship>();
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getLabelRelationshipsByRoles(java.lang.String, java.util.List)
     */
    public List<Relationship> getLabelRelationshipsByRoles(
            String sourceIndex, List<URI> roles) throws XBRLException {
        for (URI role: roles) {
            List<Relationship> relationships;
            if (role == null) { 
                relationships = this.getLabelRelationships(sourceIndex);
            } else {
                String query = "#roots#[@label and @sourceIndex='"+ sourceIndex+"' and targetRole='"+ role +"']";
                relationships = this.getStore().<Relationship>queryForXMLResources(query);
            }
            if (! relationships.isEmpty()) return relationships;
        }
        return new Vector<Relationship>();
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getReferenceRelationships(java.lang.String)
     */
    public List<Relationship> getReferenceRelationships(String sourceIndex) throws XBRLException {
        String query = "#roots#[@reference and @sourceIndex='"+ sourceIndex+"']";
        return this.getStore().<Relationship>queryForXMLResources(query);
    }
    


    /**
     * @see org.xbrlapi.networks.Analyser#getReferenceRelationshipsByLanguage(java.lang.String, java.lang.String)
     */
    public List<Relationship> getReferenceRelationshipsByLanguage( 
            String sourceIndex, String language) 
            throws XBRLException {
        String query = "#roots#[@reference and @sourceIndex='"+ sourceIndex+"' and targetLanguage='"+ language +"']";
        return this.getStore().<Relationship>queryForXMLResources(query);
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getReferenceRelationshipsByLanguageAndRole(java.lang.String, java.lang.String, java.net.URI)
     */
    public List<Relationship> getReferenceRelationshipsByLanguageAndRole(
            String sourceIndex, String language, URI role) throws XBRLException {
        String query = "#roots#[@reference and @sourceIndex='"+ sourceIndex+"' and targetLanguage='"+ language +"' and targetRole='"+ role +"']";
        return this.getStore().<Relationship>queryForXMLResources(query);
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getReferenceRelationshipsByLanguages(java.lang.String, java.util.List)
     */
    public List<Relationship> getReferenceRelationshipsByLanguages(
            String sourceIndex, List<String> languages) throws XBRLException {
        for (String language: languages) {
            List<Relationship> relationships;
            if (language == null) { 
                relationships = this.getReferenceRelationships(sourceIndex);
            } else {
                String query = "#roots#[@reference and @sourceIndex='"+ sourceIndex+"' and targetLanguage='"+ language +"']";
                relationships = this.getStore().<Relationship>queryForXMLResources(query);
            }
            if (! relationships.isEmpty()) return relationships;
        }
        return new Vector<Relationship>();
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getReferenceRelationshipsByRole(java.lang.String, java.net.URI)
     */
    public List<Relationship> getReferenceRelationshipsByRole(
            String sourceIndex, URI role) throws XBRLException {
        String query = "#roots#[@reference and @sourceIndex='"+ sourceIndex+"' targetRole='"+ role +"']";
        return this.getStore().<Relationship>queryForXMLResources(query);
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getReferenceRelationshipsByRoles(java.lang.String, java.util.List, java.util.List)
     */
    public List<Relationship> getReferenceRelationshipsByRoles(
            String sourceIndex, List<String> languages, List<URI> roles)
            throws XBRLException {
        for (String language: languages) {
            for (URI role: roles) {
                List<Relationship> relationships;
                String languagePhrase = "";
                if (language != null) languagePhrase = " and targetLanguage='"+ language +"'";
                String rolePhrase = "";
                if (role != null) rolePhrase = " and targetRole='"+ role +"'";
                String query = "#roots#[@reference and @sourceIndex='"+ sourceIndex+"'" + languagePhrase + rolePhrase + "]";
                relationships = this.getStore().<Relationship>queryForXMLResources(query);
                if (! relationships.isEmpty()) return relationships;
            }
        }
        return new Vector<Relationship>();
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getReferenceRelationshipsByRoles(java.lang.String, java.util.List)
     */
    public List<Relationship> getReferenceRelationshipsByRoles(
            String sourceIndex, List<URI> roles) throws XBRLException {
        for (URI role: roles) {
            List<Relationship> relationships;
            if (role == null) { 
                relationships = this.getReferenceRelationships(sourceIndex);
            } else {
                String query = "#roots#[@reference and @sourceIndex='"+ sourceIndex+"' and targetRole='"+ role +"']";
                relationships = this.getStore().<Relationship>queryForXMLResources(query);
            }
            if (! relationships.isEmpty()) return relationships;
        }
        return new Vector<Relationship>();
    }

    /**
     * @see Analyser#getRelationships(String, String, URI, URI)
     */
    public List<Relationship> getRelationships(String sourceIndex, String targetIndex, URI linkRole, URI arcrole) throws XBRLException {
        String lr = "";
        String ar = "";
        if (linkRole != null) {
            lr = " and @linkRole='"+ linkRole +"'";            
        }
        if (arcrole != null) {
            ar = " and @arcRole='"+ arcrole +"'";            
        }
        String query = "#roots#[@sourceIndex='"+sourceIndex+"' and @targetIndex='"+targetIndex+"'" + ar + lr + "]";
        return getStore().<Relationship>queryForXMLResources(query);
    }
    
}
