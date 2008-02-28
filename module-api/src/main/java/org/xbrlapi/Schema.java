package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Schema extends SchemaContent {
    
    /**
     * Set the target namespace URI of the schema.
     *
     * @param namespaceURI The new target namespace for the schema.
     * 
     * @throws XBRLException
     */
    public void setTargetNamespaceURI(String namespaceURI) throws XBRLException;

    /**
     * Checks if the element form is qualified.
     * @return true if the element form is qualified and false otherwise.
     * @throws XBRLException
     */
    public boolean isElementFormQualified() throws XBRLException;

    /**
     * @return a list of SimpleLink fragments, one per XML Schema 
     * import used by this schema.
     * @throws XBRLException
     */
    public FragmentList<SimpleLink> getImports() throws XBRLException;
    
    /**
     * @return a list of SimpleLink fragments, one per XML Schema 
     * include used by this schema.
     * @throws XBRLException
     */
    public FragmentList<SimpleLink> getIncludes() throws XBRLException;
    
    /**
     * @return a list of the extended links contained by the schema.
     * @throws XBRLException
     */
    public FragmentList<ExtendedLink> getExtendedLinks() throws XBRLException;    
    
    /**
     * Get the fragment list of concepts in the schema.
     * @return the list of concepts in the schema.
     * @throws XBRLException.
     */
    public FragmentList<Concept> getConcepts() throws XBRLException;

    /**
     * Get a specific concept by its name.
     * return the chosen concept or null if the concept does not exist.
     * @param name The name of the concept
     * @throws XBRLException
     */
    public Concept getConceptByName(String name) throws XBRLException;    

    /**
     * Get a list of concepts based on their type.
     * Returns null if no concepts match the selection criteria.
     *
     * @param namespaceURI The namespaceURI of the concept type
     * @param localName The local name of the concept type
     * @return A list of concept fragments in the containing schema that
     * match the specified element type.
     * 
     * @throws XBRLException
     */
    public FragmentList<Concept> getConceptsByType(String namespaceURI, String localName) throws XBRLException;
    
    /**
     * Get a list concepts based on their substitution group.
     * Returns null if no concepts match the selection criteria.
     *
     * @param namespaceURI The namespaceURI of the concept type
     * @param localName The local name of the concept type
     * 
     * @return a list of concepts in the schema that match the specified
     * substitution group
     * 
     * @throws XBRLException
     */
    public FragmentList<Concept> getConceptsBySubstitutionGroup(String namespaceURI, String localName) throws XBRLException;

    /**
     * Remove a specific concept identified by its name.
     * Throws an exception if the concept does not exist.
     *
     * @param name The name of the concept
     * @throws XBRLException
     */
    public void removeConceptByName(String name) throws XBRLException; 		

    /**
     * Add a concept to a schema.
     * Throws an exception if the schema already contains a 
     * concept with the same name or id. 
     *
     * @param concept The concept to be added to the schema.
     * @throws XBRLException
     */
    public void addConcept(Concept concept) throws XBRLException;

    /**
     * Get a reference part declaration in a schema.
     * Returns null if the reference part does not exist in the schema.
     *
     * @param name The name attribute value of the reference part to be retrieved.
     * @throws XBRLException
     */
    public ReferencePartDeclaration getReferencePartDeclaration(String name) throws XBRLException;
    
    /**
     * Get a list of the reference part declarations in a schema.
     * @return a list of reference part declarations in the schema.
     * @throws XBRLException
     */
    public FragmentList<ReferencePartDeclaration> getReferencePartDeclarations() throws XBRLException;    
    
    /**
     * Add a reference part declaration to a schema.
     * Throws an exception if the reference part already exists in the schema.
     *
     * @param referencePartDeclaration The concept to be added to the schema.
     * @throws XBRLException
     */
    public void addReferencePartDeclaration(ReferencePartDeclaration referencePartDeclaration) throws XBRLException;
    
    /**
     * Remove a reference part declaration from a schema.
     *
     * @param referencePartDeclaration The reference part declaration to be removed.
     * @throws XBRLException
     */
    public void removeReferencePartDeclaration(ReferencePartDeclaration referencePartDeclaration) throws XBRLException;

    /**
     * Adds an linkbaseRef to a schema.  More methods are required to
     * give greater control over document order.
     *
     * @param linkbaseRef The linkbaseRef to be added to the schema.
     * @throws XBRLException
     */
    public void addLinkbaseRef(SimpleLink linkbaseRef) throws XBRLException;
    
    /**
     * Remove a linkbaseRef from a schema.
     * Throws an error if the linkbaseRef is not contained in the schema.
     *
     * @param linkbaseRef The linkbaseRef to be removed.
     * @throws XBRLException
     */
    public void removeLinkbaseRef(SimpleLink linkbaseRef) throws XBRLException;

    /**
     * Adds a roleType to a schema.
     *
     * @param roleType The roleType to be added to the schema.
     * @throws XBRLException
     */
    public void addRoleType(RoleType roleType) throws XBRLException;
    
    /**
     * Remove a roleType from a schema.
     *
     * @param roleType The roleType to be removed.
     * @throws XBRLException  if the roleType is not contained in the schema.
     */
    public void removeRoleType(RoleType roleType) throws XBRLException;

    /**
     * Adds an arcroleType to a schema.
     *
     * @param arcroleType The arcroleType to be added to the schema.
     * @throws XBRLException
     */
    public void addArcroleType(ArcroleType arcroleType) throws XBRLException;
    
    /**
     * Remove a arcroleType from a schema.
     *
     * @param arcroleType The arcroleType to be removed.
     * @throws XBRLException if the arcroleType is not contained in the schema.
     */
    public void removeArcroleType(ArcroleType arcroleType) throws XBRLException;

}
