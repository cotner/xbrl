package org.xbrlapi.networks;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.xbrlapi.Fragment;
import org.xbrlapi.PersistedRelationship;
import org.xbrlapi.utilities.XBRLException;

/**
 * Classes implementing this interface support analysis of
 * persisted relationship information in a data store.  This 
 * can be much more efficient than direct analysis of the 
 * network information based upon the original data.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public interface Analyser {
    
    /**
     * @return a list of all extended link roles for persisted relationships.
     * @throws XBRLException
     */
    public Set<URI> getLinkRoles() throws XBRLException;

    /**
     * @return a list of all arcroles for persisted relationships.
     * @throws XBRLException
     */
    public Set<URI> getArcroles() throws XBRLException;

    /**
     * @param arcrole the constraining arcrole value.
     * @return a list of all link roles for extended links that 
     * contain arcs defining persisted relationships with the
     * given arcrole.
     * @throws XBRLException
     */
    public Set<URI> getLinkRoles(URI arcrole) throws XBRLException;        

    /**
     * @param linkRole the constraining link role value.
     * @return a list of all arcroles for persisted relationships defined
     * in extended links with the given link role.
     * @throws XBRLException
     */
    public Set<URI> getArcroles(URI linkRole) throws XBRLException;    

    /**
     * @param linkRole The link role for the extended links that
     * are allowed to contain the arcs defining the active relationships
     * to be returned.
     * @param arcroles The list of arcroles used to select the
     * returned list of active relationships that have been
     * persisted in the data store.
     * @return A list of all active relationships that involve 
     * any one of the specified arcroles and that are in networks
     * with the given link role.
     * @throws XBRLException
     */
    public List<PersistedRelationship> getRelationships(URI linkRole, Set<URI> arcroles) throws XBRLException;
    
    /**
     * @param arcroles The list of arcroles used to select the
     * returned list of active relationships that have been
     * persisted in the data store.
     * @return A list of all active relationships that involve 
     * any one of the specified arcroles.
     * @throws XBRLException
     */
    public List<PersistedRelationship> getRelationships(Set<URI> arcroles) throws XBRLException;

    /**
     * @param arcrole The arcrole used to select the
     * returned list of active relationships that have been
     * persisted in the data store.
     * @return A list of all active relationships that involve 
     * the specified arcrole.
     * @throws XBRLException
     */
    public List<PersistedRelationship> getRelationships(URI arcrole) throws XBRLException;
    

    

    
    
    

    /**
     * @param linkRole The link role constraining the set of
     * returned active relationships.
     * @param arcrole The arcrole used to select the
     * returned list of active relationships that have been
     * persisted in the data store.
     * @return A list of all active relationships that involve 
     * the specified arcrole and that have the specified link role.
     * @throws XBRLException
     */
    public List<PersistedRelationship> getRelationships(URI linkRole, URI arcrole) throws XBRLException;

    /**
     * @param sourceIndex The index of the fragment to get relationships from.
     * @param linkRole The link role constraining the set of
     * returned active relationships.
     * @param arcrole The arcrole used to select the
     * returned list of active relationships that have been
     * persisted in the data store.
     * @return A sorted set of all active relationships that involve 
     * the specified arcrole and that have the specified link role
     * and that run from the specified source fragment, ordered by the arc order
     * attribute value.
     * @throws XBRLException
     */
    public SortedSet<PersistedRelationship> getRelationshipsFrom(String sourceIndex, URI linkRole, URI arcrole) throws XBRLException;

    /**
     * @param targetIndex The index of the fragment to get relationships to.
     * @param linkRole The link role constraining the set of
     * returned active relationships.
     * @param arcrole The arcrole used to select the
     * returned list of active relationships that have been
     * persisted in the data store.
     * @return A sorted set of all active relationships that involve 
     * the specified arcrole and that have the specified link role
     * and that run to the specified target fragment.  The relationships
     * are sorted by the order attributes on the arcs expressing them.
     * @throws XBRLException
     */
    public SortedSet<PersistedRelationship> getRelationshipsTo(String targetIndex, URI linkRole, URI arcrole) throws XBRLException;


    /**
     * @param sourceIndex The index of the fragment to get relationships from.
     * @param arcrole The arcrole used to select the
     * returned list of active relationships that have been
     * persisted in the data store.
     * @return A list of all active relationships that involve 
     * the specified arcrole and that run from the specified source fragment.
     * @throws XBRLException
     */
    public List<PersistedRelationship> getRelationshipsFrom(String sourceIndex, URI arcrole) throws XBRLException;

    /**
     * @param targetIndex The index of the fragment to get relationships to.
     * @param arcrole The arcrole used to select the
     * returned list of active relationships that have been
     * persisted in the data store.
     * @return A list of all active relationships that involve 
     * the specified arcrole and that run to the specified target fragment.
     * @throws XBRLException
     */
    public List<PersistedRelationship> getRelationshipsTo(String targetIndex, URI arcrole) throws XBRLException;

    /**
     * @param sourceIndex The index of the fragment to get relationships from.
     * @param linkRole The link role constraining the set of
     * returned active relationships.
     * @param arcroles The arcroles used to select the
     * returned list of active relationships that have been
     * persisted in the data store.
     * @return A list of all active relationships that involve 
     * any of the specified arcroles and that have the specified link role
     * and that run from the specified source fragment.
     * @throws XBRLException
     */
    public List<PersistedRelationship> getRelationshipsFrom(String sourceIndex, URI linkRole, Set<URI> arcroles) throws XBRLException;

    /**
     * @param targetIndex The index of the fragment to get relationships to.
     * @param linkRole The link role constraining the set of
     * returned active relationships.
     * @param arcrole The arcroles used to select the
     * returned list of active relationships that have been
     * persisted in the data store.
     * @return A list of all active relationships that involve 
     * any of the specified arcroles and that have the specified link role
     * and that run to the specified target fragment.
     * @throws XBRLException
     */
    public List<PersistedRelationship> getRelationshipsTo(String targetIndex, URI linkRole, Set<URI> arcroles) throws XBRLException;


    /**
     * @param sourceIndex The index of the fragment to get relationships from.
     * @param arcroles The arcroles used to select the
     * returned list of active relationships that have been
     * persisted in the data store.
     * @return A list of all active relationships that involve 
     * any of the specified arcroles and that run from the specified source fragment.
     * @throws XBRLException
     */
    public List<PersistedRelationship> getRelationshipsFrom(String sourceIndex, Set<URI> arcroles) throws XBRLException;

    /**
     * @param targetIndex The index of the fragment to get relationships to.
     * @param arcrole The arcroles used to select the
     * returned list of active relationships that have been
     * persisted in the data store.
     * @return A list of all active relationships that involve 
     * any of the specified arcroles and that run to the specified target fragment.
     * @throws XBRLException
     */
    public List<PersistedRelationship> getRelationshipsTo(String targetIndex, Set<URI> arcroles) throws XBRLException;

    /**
     * @param linkRole The link role of the network.
     * @param arcrole The arcrole of the network.
     * @return the relationships that run from root fragments in the 
     * network with the specified link role and arcrole.
     * @throws XBRLException
     */
    public List<PersistedRelationship> getRootRelationships(URI linkRole, URI arcrole) throws XBRLException;
    
    /**
     * @param <F> The fragment type
     * @param linkRole The link role URI
     * @param arcrole The arcrole URI
     * @return the list of fragments that are roots of networks with the given
     * link role and arcrole.
     * @throws XBRLException
     */
    public <F extends Fragment> Set<F> getRoots(URI linkRole, URI arcrole) throws XBRLException;    
    
    /**
     * @param arcrole The arcrole of the network.
     * @return the relationships that run from root fragments in the 
     * networks with the specified arcrole.
     * @throws XBRLException
     */
    public List<PersistedRelationship> getRootRelationships(URI arcrole) throws XBRLException;
    
    /**
     * @param <F> The fragment type
     * @param arcrole The arcrole URI
     * @return the list of fragments that are roots of networks with the given
     * arcrole.
     * @throws XBRLException
     */
    public <F extends Fragment> Set<F> getRoots(URI arcrole) throws XBRLException;

    /**
     * @param sourceIndex The index of the fragment to get the labels for.
     * @return a list of active relationships to labels from the 
     * fragment identified by the specified index.
     * @throws XBRLException
     */
    public List<PersistedRelationship> getLabelRelationships(String sourceIndex) throws XBRLException;

    /**
     * @param sourceIndex The index of the fragment to get the labels for.
     * @param language The XML language code identifying the language of the label.
     * @return a list of active relationships to labels from the 
     * fragment identified by the specified index and with the specified language.
     * The list is empty if no relationships match the specified selection criteria.
     * @throws XBRLException
     */
    public List<PersistedRelationship> getLabelRelationshipsByLanguage(String sourceIndex, String language) throws XBRLException;

    /**
     * @param sourceIndex The index of the fragment to get the labels for.
     * @param languages The list of XML language code identifying the language of the label,
     * in order of preference with the first value being the most preferred.
     * A null value in the list implies that the full set of labels that match the 
     * other criteria will be selected.
     * @return a list of active relationships to labels that match the selection criteria.
     * The list is empty if no relationships match the specified selection criteria.
     * @throws XBRLException
     */
    public List<PersistedRelationship> getLabelRelationshipsByLanguages(String sourceIndex, List<String> languages) throws XBRLException;
    
    /**
     * @param sourceIndex The index of the fragment to get the labels for.
     * @param role A required resource role for the labels.
     * @return a list of active relationships to labels that match the selection criteria.
     * @throws XBRLException
     */
    public List<PersistedRelationship> getLabelRelationshipsByRole(String sourceIndex, URI role) throws XBRLException;
    
    /**
     * @param sourceIndex The index of the fragment to get the labels for.
     * @param roles A list of resource roles for the labels in order of preference with the 
     * first role in the list being the most preferred XLink role attribute value.
     * A null value in the list implies that the full set of labels that match the 
     * other criteria will be selected.
     * @return a list of active relationships to labels that match the selection criteria.
     * @throws XBRLException
     */
    public List<PersistedRelationship> getLabelRelationshipsByRoles(String sourceIndex, List<URI> roles) throws XBRLException;    
 
    /**
     * @param sourceIndex The index of the fragment to get the labels for.
     * @param language The XML language code identifying the language of the label.
     * @param role A required resource role for the labels.
     * @return a list of active relationships to labels that match the selection criteria.
     * @throws XBRLException
     */
    public List<PersistedRelationship> getLabelRelationshipsByLanguageAndRole(String sourceIndex, String language, URI role) throws XBRLException;
    
    /**
     * @param sourceIndex The index of the fragment to get the labels for.
     * @param languages The list of XML language code identifying the language of the label,
     * in order of preference with the first value being the most preferred.
     * A null value in the list implies that the full set of labels that match the 
     * other criteria will be selected.
     * @param roles A list of resource roles for the labels in order of preference with the 
     * first role in the list being the most preferred XLink role attribute value.
     * A null value in the list implies that the full set of labels that match the 
     * other criteria will be selected.
     * @return a list of active relationships to labels that match the selection criteria.  Note that
     * label language preferences get precedence over label role preferences.
     * @throws XBRLException
     */
    public List<PersistedRelationship> getLabelRelationshipsByRoles(String sourceIndex, List<String> languages, List<URI> roles) throws XBRLException;

    /**
     * @param sourceIndex The index of the fragment to get the references for.
     * @return a list of active relationships to references from the 
     * fragment identified by the specified index.
     * @throws XBRLException
     */
    public List<PersistedRelationship> getReferenceRelationships(String sourceIndex) throws XBRLException;

    /**
     * @param sourceIndex The index of the fragment to get the references for.
     * @param language The XML language code identifying the language of the reference.
     * @return a list of active relationships to references from the 
     * fragment identified by the specified index and with the specified language.
     * The list is empty if no relationships match the specified selection criteria.
     * @throws XBRLException
     */
    public List<PersistedRelationship> getReferenceRelationshipsByLanguage(String sourceIndex, String language) throws XBRLException;

    /**
     * @param sourceIndex The index of the fragment to get the references for.
     * @param languages The list of XML language code identifying the language of the reference,
     * in order of preference with the first value being the most preferred.
     * A null value in the list implies that the full set of references that match the 
     * other criteria will be selected.
     * @return a list of active relationships to references that match the selection criteria.
     * The list is empty if no relationships match the specified selection criteria.
     * @throws XBRLException
     */
    public List<PersistedRelationship> getReferenceRelationshipsByLanguages(String sourceIndex, List<String> languages) throws XBRLException;
    
    /**
     * @param sourceIndex The index of the fragment to get the references for.
     * @param role A required resource role for the references.
     * @return a list of active relationships to references that match the selection criteria.
     * @throws XBRLException
     */
    public List<PersistedRelationship> getReferenceRelationshipsByRole(String sourceIndex, URI role) throws XBRLException;
    
    /**
     * @param sourceIndex The index of the fragment to get the references for.
     * @param roles A list of resource roles for the references in order of preference with the 
     * first role in the list being the most preferred XLink role attribute value.
     * A null value in the list implies that the full set of references that match the 
     * other criteria will be selected.
     * @return a list of active relationships to references that match the selection criteria.
     * @throws XBRLException
     */
    public List<PersistedRelationship> getReferenceRelationshipsByRoles(String sourceIndex, List<URI> roles) throws XBRLException;    
 
    /**
     * @param sourceIndex The index of the fragment to get the references for.
     * @param language The XML language code identifying the language of the reference.
     * @param role A required resource role for the references.
     * @return a list of active relationships to references that match the selection criteria.
     * @throws XBRLException
     */
    public List<PersistedRelationship> getReferenceRelationshipsByLanguageAndRole(String sourceIndex, String language, URI role) throws XBRLException;
    
    /**
     * @param sourceIndex The index of the fragment to get the references for.
     * @param languages The list of XML language code identifying the language of the reference,
     * in order of preference with the first value being the most preferred.
     * A null value in the list implies that the full set of references that match the 
     * other criteria will be selected.
     * @param roles A list of resource roles for the references in order of preference with the 
     * first role in the list being the most preferred XLink role attribute value.
     * A null value in the list implies that the full set of references that match the 
     * other criteria will be selected.
     * @return a list of active relationships to references that match the selection criteria.
     * @throws XBRLException
     */
    public List<PersistedRelationship> getReferenceRelationshipsByRoles(String sourceIndex, List<String> languages, List<URI> roles) throws XBRLException;    
    
    /**
     * @param sourceIndex The source index to match.
     * @param targetIndex The target index to match.
     * @param linkRole The link role to match.
     * @param arcrole The arcrole to match.
     * @return a list of persisted relationships matching the specified
     * criteria.
     * @throws XBRLException
     */
    public List<PersistedRelationship> getRelationships(String sourceIndex, String targetIndex, URI linkRole, URI arcrole) throws XBRLException;

}
