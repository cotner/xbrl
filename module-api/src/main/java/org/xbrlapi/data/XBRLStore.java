package org.xbrlapi.data;

import java.net.URI;
import java.util.HashMap;
import java.util.List;

import org.xbrlapi.ArcroleType;
import org.xbrlapi.Concept;
import org.xbrlapi.ExtendedLink;
import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.Item;
import org.xbrlapi.RoleType;
import org.xbrlapi.Tuple;
import org.xbrlapi.networks.Networks;
import org.xbrlapi.utilities.XBRLException;

/**
 * The XBRL store interface extends the base data store to define 
 * a range of utility methods that provide XBRL related functionality 
 * to the data store.
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface XBRLStore extends Store {

    /**
     * @return a list of all of the root-level facts in the data store (those facts
     * that are children of the root element of an XBRL instance).  Returns an empty list 
     * if no facts are found.
     * @throws XBRLException
     */
    public FragmentList<Fact> getFacts() throws XBRLException;
    
    /**
     * @return a list of all of the items in the data store.
     * @throws XBRLException
     */
    public FragmentList<Item> getItems() throws XBRLException;
    
    /**
     * @return a list of all of the tuples in the data store.
     * @throws XBRLException
     */
    public FragmentList<Tuple> getTuples() throws XBRLException;

    /**
     * @param uri The URI of the document to get the facts from.
     * @return a list of all of the root-level facts in the specified document.
     * @throws XBRLException
     */
    public FragmentList<Fact> getFacts(URI uri) throws XBRLException;
    
    /**
     * @param uri The URI of the document to get the items from.
     * @return a list of all of the root-level items in the data store.
     * @throws XBRLException
     */
    public FragmentList<Item> getItems(URI uri) throws XBRLException;
    
    /**
     * @param uri The URI of the document to get the facts from.
     * @return a list of all of the root-level tuples in the specified document.
     * @throws XBRLException
     */
    public FragmentList<Tuple> getTuples(URI uri) throws XBRLException;
    
    /**
     * This implementation is not as strict as the XBRL 2.1 specification
     * requires but it is generally faster and delivers sensible results.
     * It will only fail if people use the same link role and arc role but
     * rely on arc or link element differences to distinguish networks.<br/><br/>
     * 
     * Implementation strategy is:<br/>
     * 1. Get all extended link elements with the given link role.<br/>
     * 2. Get all arcs with the given arc role.<br/>
     * 3. Get all resources at the source of the arcs.<br/>
     * 4. Return only those source resources that that are not target resources also.<br/>
     * 
     * @param linkRole the role on the extended links that contain the network arcs.
     * @param arcRole the arcrole on the arcs describing the network.
     * @return The list of fragments for each of the resources that is identified as a root
     * of the specified network (noting that a root resource is defined as a resource that is
     * at the source of one or more relationships in the network and that is not at the target 
     * of any relationships in the network).
     * @throws XBRLException
     */
    public FragmentList<Fragment> getNetworkRoots(String linkRole, String arcRole) throws XBRLException;
   
    /**
     * @param namespace The namespace for the concept.
     * @param name The local name for the concept.
     * @return the concept fragment for the specified namespace and name.
     * @throws XBRLException if more than one matching concept is found in the data store
     * or if no matching concepts are found in the data store.
     */
    public Concept getConcept(URI namespace, String name) throws XBRLException;

    /**
     * @return a hash map indexed by link roles that are used in extended links in the data store.
     * @throws XBRLException
     */
    public HashMap<String,String> getLinkRoles() throws XBRLException;
    
    /**
     * @param arcrole The arcrole determining the extended links that are to be examined for
     * linkroles that are used on links containing arcs with the required arcrole.
     * @return a hashmap of link roles, with one entry for each link role that is used on an
     * extended link that contains an arc with the required arcrole.
     * @throws XBRLException
     */
    public HashMap<String,String> getLinkRoles(String arcrole) throws XBRLException;
    
    /**
     * @return a hash map indexed by arc roles that are used in extended links in the data store.
     * @throws XBRLException
     */
    public HashMap<String,String> getArcRoles() throws XBRLException;
    
    /**
     * @return a list of arcroleType fragments
     * @throws XBRLException
     */
    public FragmentList<ArcroleType> getArcroleTypes() throws XBRLException;
    
    /**
     * @return a list of arcroleType fragments with a given arcrole
     * @throws XBRLException
     */
    public FragmentList<ArcroleType> getArcroleTypes(String uri) throws XBRLException;
    
    /**
     * @return a list of roleType fragments
     * @throws XBRLException
     */
    public FragmentList<RoleType> getRoleTypes() throws XBRLException;
    

    
    /**
     * @return a list of RoleType fragments with a given role
     * @throws XBRLException
     */
    public FragmentList<RoleType> getRoleTypes(String uri) throws XBRLException;    
    
    /**
     * @return a hash map indexed by resource roles that are used in extended links in the data store.
     * @throws XBRLException
     */
    public HashMap<String,String> getResourceRoles() throws XBRLException;    
    
    /**
     * @param starters The list of URIs of the documents to use as 
     * starting points for analysis.
     * @return list of URIs for the documents in the data store
     * that are referenced, directly or indirectly, by any of the documents
     * identified by the supplied list of document URIs.  Each entry in the list is a String.
     * @throws XBRLException if some of the referenced documents are not in
     * the data store.
     */
    public List<URI> getMinimumDocumentSet(List<URI> starters) throws XBRLException;
    
    
    /**
     * This is just a convenience method.
     * @param uri The single document URI to use as 
     * starting points for analysis.
     * @return list of URIs for the documents in the data store
     * that are referenced, directly or indirectly, by the document
     * identified by the supplied URI.  Each entry in the list is a String.
     * @throws XBRLException if some of the referenced documents are not in
     * the data store.
     */
    public List<URI> getMinimumDocumentSet(URI uri) throws XBRLException;

    /**
     * Implementation strategy is:<br/>
     * <ol>
     * <li>Get all extended link elements matching network requirements.</li>
     * <li>Get all arcs defining relationships in the network.</li>
     * <li>Get all resources at the source of the arcs.</li>
     * <li>Return only those source resources that that are not target resources also.</li>
     * </ol>
     * @param linkNamespace The namespace of the link element.
     * @param linkName The name of the link element.
     * @param linkRole the role on the extended links that contain the network arcs.
     * @param arcNamespace The namespace of the arc element.
     * @param arcName The name of the arc element.
     * @param arcRole the arcrole on the arcs describing the network.
     * @return The list of fragments for each of the resources that is identified as a root
     * of the specified network (noting that a root resource is defined as a resource that is
     * at the source of one or more relationships in the network and that is not at the target 
     * of any relationships in the network).
     * @throws XBRLException
     */
    public <F extends Fragment> FragmentList<F> getNetworkRoots(String linkNamespace, String linkName, String linkRole, String arcNamespace, String arcName, String arcRole) throws XBRLException;
 
    /**
     * @param linkrole The required linkrole value.
     * @return the list of extended links with the specified linkrole.
     * @throws XBRLException
     */
    public FragmentList<ExtendedLink> getExtendedLinksWithRole(String linkrole) throws XBRLException;

    /**
     * Get the networks that, at a minimum, contain the relationships
     * from each of the given fragments working back through ancestor relationships
     * as far as possible.
     * @param fragments The fragments to analyse.
     * @param arcrole The required arcrole.
     * @return The networks containing the relationships.
     * @throws XBRLException
     */
    public Networks getMinimalNetworksWithArcrole(FragmentList<Fragment> fragments, String arcrole) throws XBRLException;
    
    /**
     * Convenience method for a single fragment.
     * @see XBRLStore#getMinimalNetworksWithArcrole(FragmentList,String)
     */
    public Networks getMinimalNetworksWithArcrole(Fragment fragment, String arcrole) throws XBRLException;    
    
}
