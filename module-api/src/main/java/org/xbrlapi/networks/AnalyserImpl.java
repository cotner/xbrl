package org.xbrlapi.networks;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.xbrlapi.Arc;
import org.xbrlapi.ArcEnd;
import org.xbrlapi.Fragment;
import org.xbrlapi.PersistedRelationship;
import org.xbrlapi.data.Store;
import org.xbrlapi.impl.PersistedRelationshipOrderComparator;
import org.xbrlapi.utilities.XBRLException;

public class AnalyserImpl implements Analyser {

    protected static Logger logger = Logger.getLogger(AnalyserImpl.class);
    
    /**
     * @see Analyser#hasAllRelationships(URI)
     */
    public boolean hasAllRelationships(URI document) throws XBRLException {
        
        Set<String> indices = getStore().getFragmentIndicesFromDocument(document,"Arc");
        for (String index: indices) {
            Arc arc = getStore().<Arc>getFragment(index);
            List<ArcEnd> sources = arc.getSourceFragments();
            List<ArcEnd> targets = arc.getTargetFragments();
            long relationships = sources.size() * targets.size();
            String query = "#roots#[@type='org.xbrlapi.impl.PersistedRelationshipImpl' and @arcIndex='" + index + "']";
            long count = store.queryCount(query);
            if (count != relationships) return false;
        }
        return true;
    }
    
    /**
     * This method provides a place to filter relationships after retrieving them
     * to eliminate overridden and prohibited relationships as desired.
     * @param query The query to run to get the relationships.
     * @return the list of relationships returned by the query.
     * @throws XBRLException
     */
    private List<PersistedRelationship> getRelationships(String query) throws XBRLException {
        List<PersistedRelationship> allRelationships = getStore().<PersistedRelationship>queryForFragments(query);
        return allRelationships;
/*        List<PersistedRelationship> result = new Vector<PersistedRelationship>();
        for (PersistedRelationship relationship: allRelationships) {
            
        }
        return result;
*/    }
    
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
    private Store getStore() {
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
    public List<PersistedRelationship> getRelationships(Set<URI> arcroles) throws XBRLException {
        List<PersistedRelationship> relationships = new Vector<PersistedRelationship>();
        for (URI arcrole: arcroles) {
            relationships.addAll(this.getRelationships(arcrole));
        }
        return relationships;
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getRelationships(URI, Set)
     */
    public List<PersistedRelationship> getRelationships(URI linkRole, Set<URI> arcroles) throws XBRLException {
        List<PersistedRelationship> relationships = new Vector<PersistedRelationship>();
        for (URI arcrole: arcroles) {
            relationships.addAll(this.getRelationships(linkRole, arcrole));
        }
        return relationships;
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getRelationships(java.net.URI, java.net.URI)
     */
    public List<PersistedRelationship> getRelationships(URI linkRole, URI arcrole) throws XBRLException {
        String query = "#roots#[not(@use) and @arcRole='"+ arcrole +"' and @linkRole='"+ linkRole +"']";
        return this.getRelationships(query);
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getRelationships(java.net.URI)
     */
    public List<PersistedRelationship> getRelationships(URI arcrole) throws XBRLException {
        Set<URI> linkRoles = this.getLinkRoles(arcrole);
        List<PersistedRelationship> relationships = new Vector<PersistedRelationship>();
        for (URI linkRole: linkRoles) {
            relationships.addAll(this.getRelationships(linkRole, arcrole));
        }
        return relationships;
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getRelationshipsFrom(java.lang.String, Set)
     */
    public List<PersistedRelationship> getRelationshipsFrom(String sourceIndex, Set<URI> arcroles) throws XBRLException {
        List<PersistedRelationship> relationships = new Vector<PersistedRelationship>();
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
    public List<PersistedRelationship> getRelationshipsFrom(String sourceIndex, URI linkRole, Set<URI> arcroles) throws XBRLException {
        List<PersistedRelationship> relationships = new Vector<PersistedRelationship>();
        for (URI arcrole: arcroles) {
            relationships.addAll(this.getRelationshipsFrom(sourceIndex, linkRole, arcrole));
        }
        return relationships;
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getRelationshipsFrom(java.lang.String, java.net.URI, java.net.URI)
     */
    public SortedSet<PersistedRelationship> getRelationshipsFrom(String sourceIndex, URI linkRole, URI arcrole) throws XBRLException {
        String query = "#roots#[not(@use) and @arcRole='"+ arcrole +"' and @linkRole='"+ linkRole +"' and @sourceIndex='"+ sourceIndex +"']";
        List<PersistedRelationship> list = this.getRelationships(query);
        SortedSet<PersistedRelationship> sortedSet = new TreeSet<PersistedRelationship>(new PersistedRelationshipOrderComparator());
        sortedSet.addAll(list);
        return sortedSet;
    }
    

    /**
     * @see org.xbrlapi.networks.Analyser#getRelationshipsFrom(java.lang.String, java.net.URI)
     */
    public List<PersistedRelationship> getRelationshipsFrom(String sourceIndex, URI arcrole) throws XBRLException {
        List<PersistedRelationship> relationships = new Vector<PersistedRelationship>();
        Set<URI> linkRoles = this.getLinkRoles(arcrole);
        for (URI linkRole: linkRoles) {
            relationships.addAll(this.getRelationshipsFrom(sourceIndex,linkRole, arcrole));
        }
        return relationships;
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getRelationshipsTo(java.lang.String, Set)
     */
    public List<PersistedRelationship> getRelationshipsTo(String targetIndex, Set<URI> arcroles) throws XBRLException {
        List<PersistedRelationship> relationships = new Vector<PersistedRelationship>();
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
    public List<PersistedRelationship> getRelationshipsTo(String targetIndex, URI linkRole, Set<URI> arcroles) throws XBRLException {
        List<PersistedRelationship> relationships = new Vector<PersistedRelationship>();
        for (URI arcrole: arcroles) {
            relationships.addAll(this.getRelationshipsTo(targetIndex, linkRole, arcrole));
        }
        return relationships;
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getRelationshipsTo(java.lang.String, java.net.URI, java.net.URI)
     */
    public SortedSet<PersistedRelationship> getRelationshipsTo(String targetIndex, URI linkRole, URI arcrole) throws XBRLException {
        String query = "#roots#[not(@use) and @arcRole='"+ arcrole +"' and @linkRole='"+ linkRole +"' and @targetIndex='"+ targetIndex +"']";
        List<PersistedRelationship> list = this.getRelationships(query);
        SortedSet<PersistedRelationship> sortedSet = new TreeSet<PersistedRelationship>(new PersistedRelationshipOrderComparator());
        sortedSet.addAll(list);
        return sortedSet;
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getRelationshipsTo(java.lang.String, java.net.URI)
     */
    public List<PersistedRelationship> getRelationshipsTo(String targetIndex, URI arcrole) throws XBRLException {
        List<PersistedRelationship> relationships = new Vector<PersistedRelationship>();
        Set<URI> linkRoles = this.getLinkRoles(arcrole);
        for (URI linkRole: linkRoles) {
            relationships.addAll(this.getRelationshipsTo(targetIndex,linkRole, arcrole));
        }
        return relationships;
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getRootRelationships(java.net.URI, java.net.URI)
     */
    public List<PersistedRelationship> getRootRelationships(URI linkRole, URI arcrole) throws XBRLException {
        Set<String> rootIndices = this.getRootIndices(linkRole,arcrole);
        List<PersistedRelationship> relationships = new Vector<PersistedRelationship>();
        for (String index: rootIndices) {
            String query = "#roots#[not(@use) and @sourceIndex='"+ index + "' and @arcRole='"+ arcrole + "' and @linkRole='"+ linkRole + "']";
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
            roots.add(getStore().<F>getFragment(index));
        }
        return roots;
    }
    
    /**
     * @see Analyser#getRootIndices(java.net.URI, java.net.URI)
     */
    public Set<String> getRootIndices(URI linkRole, URI arcrole) throws XBRLException {
        String sourcesQuery = "#roots#[not(@use) and @arcRole='"+ arcrole + "' and @linkRole='"+ linkRole + "']/@sourceIndex";
        Set<String> sourceIndices = this.getStore().queryForStrings(sourcesQuery);
        String targetsQuery = "#roots#[not(@use) and @arcRole='"+ arcrole + "' and @linkRole='"+ linkRole + "']/@targetIndex";
        Set<String> targetIndices = this.getStore().queryForStrings(targetsQuery);
        sourceIndices.removeAll(targetIndices);
        return sourceIndices;
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getLabelRelationships(java.lang.String)
     */
    public List<PersistedRelationship> getLabelRelationships(String sourceIndex) throws XBRLException {
        String query = "#roots#[not(@use) and @label and @sourceIndex='"+ sourceIndex+"']";
        return this.getStore().<PersistedRelationship>queryForFragments(query);
    }
    
    /**
     * @see org.xbrlapi.networks.Analyser#getLabelRelationships(java.lang.String,java.lang.String)
     */
    public List<PersistedRelationship> getLabelRelationships(String sourceIndex, String language) throws XBRLException {
        String query = "#roots#[not(@use) and @label and @sourceIndex='"+ sourceIndex+"']";
        return this.getStore().<PersistedRelationship>queryForFragments(query);
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getLabelRelationshipsByLanguage(java.lang.String, java.lang.String)
     */
    public List<PersistedRelationship> getLabelRelationshipsByLanguage( 
            String sourceIndex, String language) 
            throws XBRLException {
        String query = "#roots#[not(@use) and @label and @sourceIndex='"+ sourceIndex+"' and targetLanguage='"+ language +"']";
        return this.getStore().<PersistedRelationship>queryForFragments(query);
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getLabelRelationshipsByLanguageAndRole(java.lang.String, java.lang.String, java.net.URI)
     */
    public List<PersistedRelationship> getLabelRelationshipsByLanguageAndRole(
            String sourceIndex, String language, URI role) throws XBRLException {
        String query = "#roots#[not(@use) and @label and @sourceIndex='"+ sourceIndex+"' and targetLanguage='"+ language +"' and targetRole='"+ role +"']";
        return this.getStore().<PersistedRelationship>queryForFragments(query);
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getLabelRelationshipsByLanguages(java.lang.String, java.util.List)
     */
    public List<PersistedRelationship> getLabelRelationshipsByLanguages(
            String sourceIndex, List<String> languages) throws XBRLException {
        for (String language: languages) {
            List<PersistedRelationship> relationships;
            if (language == null) { 
                relationships = this.getLabelRelationships(sourceIndex);
            } else {
                String query = "#roots#[not(@use) and @label and @sourceIndex='"+ sourceIndex+"' and targetLanguage='"+ language +"']";
                relationships = this.getStore().<PersistedRelationship>queryForFragments(query);
            }
            if (! relationships.isEmpty()) return relationships;
        }
        return new Vector<PersistedRelationship>();
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getLabelRelationshipsByRole(java.lang.String, java.net.URI)
     */
    public List<PersistedRelationship> getLabelRelationshipsByRole(
            String sourceIndex, URI role) throws XBRLException {
        String query = "#roots#[not(@use) and @label and @sourceIndex='"+ sourceIndex+"' targetRole='"+ role +"']";
        return this.getStore().<PersistedRelationship>queryForFragments(query);
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getLabelRelationshipsByRoles(java.lang.String, java.util.List, java.util.List)
     */
    public List<PersistedRelationship> getLabelRelationshipsByRoles(
            String sourceIndex, List<String> languages, List<URI> roles)
            throws XBRLException {
        for (String language: languages) {
            for (URI role: roles) {
                List<PersistedRelationship> relationships;
                String languagePhrase = "";
                if (language != null) languagePhrase = " and targetLanguage='"+ language +"'";
                String rolePhrase = "";
                if (role != null) rolePhrase = " and targetRole='"+ role +"'";
                String query = "#roots#[not(@use) and @label and @sourceIndex='"+ sourceIndex+"'" + languagePhrase + rolePhrase + "]";
                relationships = this.getStore().<PersistedRelationship>queryForFragments(query);
                if (! relationships.isEmpty()) return relationships;
            }
        }
        return new Vector<PersistedRelationship>();
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getLabelRelationshipsByRoles(java.lang.String, java.util.List)
     */
    public List<PersistedRelationship> getLabelRelationshipsByRoles(
            String sourceIndex, List<URI> roles) throws XBRLException {
        for (URI role: roles) {
            List<PersistedRelationship> relationships;
            if (role == null) { 
                relationships = this.getLabelRelationships(sourceIndex);
            } else {
                String query = "#roots#[not(@use) and @label and @sourceIndex='"+ sourceIndex+"' and targetRole='"+ role +"']";
                relationships = this.getStore().<PersistedRelationship>queryForFragments(query);
            }
            if (! relationships.isEmpty()) return relationships;
        }
        return new Vector<PersistedRelationship>();
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getReferenceRelationships(java.lang.String)
     */
    public List<PersistedRelationship> getReferenceRelationships(String sourceIndex) throws XBRLException {
        String query = "#roots#[not(@use) and @reference and @sourceIndex='"+ sourceIndex+"']";
        return this.getStore().<PersistedRelationship>queryForFragments(query);
    }
    
    /**
     * @see org.xbrlapi.networks.Analyser#getReferenceRelationships(java.lang.String,java.lang.String)
     */
    public List<PersistedRelationship> getReferenceRelationships(String sourceIndex, String language) throws XBRLException {
        String query = "#roots#[not(@use) and @reference and @sourceIndex='"+ sourceIndex+"']";
        return this.getStore().<PersistedRelationship>queryForFragments(query);
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getReferenceRelationshipsByLanguage(java.lang.String, java.lang.String)
     */
    public List<PersistedRelationship> getReferenceRelationshipsByLanguage( 
            String sourceIndex, String language) 
            throws XBRLException {
        String query = "#roots#[not(@use) and @reference and @sourceIndex='"+ sourceIndex+"' and targetLanguage='"+ language +"']";
        return this.getStore().<PersistedRelationship>queryForFragments(query);
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getReferenceRelationshipsByLanguageAndRole(java.lang.String, java.lang.String, java.net.URI)
     */
    public List<PersistedRelationship> getReferenceRelationshipsByLanguageAndRole(
            String sourceIndex, String language, URI role) throws XBRLException {
        String query = "#roots#[not(@use) and @reference and @sourceIndex='"+ sourceIndex+"' and targetLanguage='"+ language +"' and targetRole='"+ role +"']";
        return this.getStore().<PersistedRelationship>queryForFragments(query);
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getReferenceRelationshipsByLanguages(java.lang.String, java.util.List)
     */
    public List<PersistedRelationship> getReferenceRelationshipsByLanguages(
            String sourceIndex, List<String> languages) throws XBRLException {
        for (String language: languages) {
            List<PersistedRelationship> relationships;
            if (language == null) { 
                relationships = this.getReferenceRelationships(sourceIndex);
            } else {
                String query = "#roots#[not(@use) and @reference and @sourceIndex='"+ sourceIndex+"' and targetLanguage='"+ language +"']";
                relationships = this.getStore().<PersistedRelationship>queryForFragments(query);
            }
            if (! relationships.isEmpty()) return relationships;
        }
        return new Vector<PersistedRelationship>();
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getReferenceRelationshipsByRole(java.lang.String, java.net.URI)
     */
    public List<PersistedRelationship> getReferenceRelationshipsByRole(
            String sourceIndex, URI role) throws XBRLException {
        String query = "#roots#[not(@use) and @reference and @sourceIndex='"+ sourceIndex+"' targetRole='"+ role +"']";
        return this.getStore().<PersistedRelationship>queryForFragments(query);
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getReferenceRelationshipsByRoles(java.lang.String, java.util.List, java.util.List)
     */
    public List<PersistedRelationship> getReferenceRelationshipsByRoles(
            String sourceIndex, List<String> languages, List<URI> roles)
            throws XBRLException {
        for (String language: languages) {
            for (URI role: roles) {
                List<PersistedRelationship> relationships;
                String languagePhrase = "";
                if (language != null) languagePhrase = " and targetLanguage='"+ language +"'";
                String rolePhrase = "";
                if (role != null) rolePhrase = " and targetRole='"+ role +"'";
                String query = "#roots#[not(@use) and @reference and @sourceIndex='"+ sourceIndex+"'" + languagePhrase + rolePhrase + "]";
                relationships = this.getStore().<PersistedRelationship>queryForFragments(query);
                if (! relationships.isEmpty()) return relationships;
            }
        }
        return new Vector<PersistedRelationship>();
    }

    /**
     * @see org.xbrlapi.networks.Analyser#getReferenceRelationshipsByRoles(java.lang.String, java.util.List)
     */
    public List<PersistedRelationship> getReferenceRelationshipsByRoles(
            String sourceIndex, List<URI> roles) throws XBRLException {
        for (URI role: roles) {
            List<PersistedRelationship> relationships;
            if (role == null) { 
                relationships = this.getReferenceRelationships(sourceIndex);
            } else {
                String query = "#roots#[not(@use) and @reference and @sourceIndex='"+ sourceIndex+"' and targetRole='"+ role +"']";
                relationships = this.getStore().<PersistedRelationship>queryForFragments(query);
            }
            if (! relationships.isEmpty()) return relationships;
        }
        return new Vector<PersistedRelationship>();
    }

    /**
     * @see Analyser#getRelationships(String, String, URI, URI)
     */
    public List<PersistedRelationship> getRelationships(String sourceIndex, String targetIndex, URI linkRole, URI arcrole) throws XBRLException {
        String query = "#roots#[not(@use) and @arcRole='" + arcrole + "' and @linkRole='"+linkRole+"' and @sourceIndex='"+sourceIndex+"' and @targetIndex='"+targetIndex+"']";
        return getStore().<PersistedRelationship>queryForFragments(query);
    }
    
}
