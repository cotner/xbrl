package org.xbrlapi.impl;

import org.xbrlapi.ArcroleType;
import org.xbrlapi.Concept;
import org.xbrlapi.ExtendedLink;
import org.xbrlapi.FragmentList;
import org.xbrlapi.Linkbase;
import org.xbrlapi.ReferencePartDeclaration;
import org.xbrlapi.RoleType;
import org.xbrlapi.Schema;
import org.xbrlapi.SimpleLink;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class SchemaImpl extends SchemaContentImpl implements Schema {
		
    /**
     * Get the schema target namespace URI.  
     * @return the target namespace of the schema or null if the schema
     * is anonymous.
     * @throws XBRLException
     * @see org.xbrlapi.Schema#getTargetNamespaceURI()
     */
    public String getTargetNamespaceURI() throws XBRLException {
    	if (getDataRootElement().hasAttribute("targetNamespace")) {
    		return getDataRootElement().getAttribute("targetNamespace");
    	}
    	return null;
    }
    
    /**
     * Set the target namespace URI of the schema.
     * @param namespaceURI
     * @throws XBRLException
     * @see org.xbrlapi.Schema#setTargetNamespaceURI(String)
     */
    public void setTargetNamespaceURI(String namespaceURI) throws XBRLException {
    	throw new XBRLException("Data update methods are not yet implemented.");
    }

    /**
     * Checks if the element form is qualified.
     * @return true if the element form is qualified and false otherwise.
     * @throws XBRLException
     * @see org.xbrlapi.Schema#isElementFormQualified()
     */
    public boolean isElementFormQualified() throws XBRLException {
    	if (getDataRootElement().getAttribute("elementFormDefault").equals("qualified")) {
    		return true;
    	}
    	return false;
    }

    /**
     * @see org.xbrlapi.Schema#getImports()
     */
    public FragmentList<SimpleLink> getImports() throws XBRLException {
    	String query = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@parentIndex='" + this.getFragmentIndex() + "' and @type='org.xbrlapi.impl.SimpleLinkImpl']";
    	FragmentList<SimpleLink> links = getStore().<SimpleLink>query(query);
    	FragmentList<SimpleLink> imports = new FragmentListImpl<SimpleLink>();
    	for (SimpleLink link: links) {
        	if (link.getLocalname().equals("import") && link.getNamespaceURI().equals(Constants.XMLSchemaNamespace)) {
        		imports.add(link);
        	}
    	}
    	return imports;
    }
    
    /**
     * @see org.xbrlapi.Schema#getIncludes()
     */
    public FragmentList<SimpleLink> getIncludes() throws XBRLException {
    	String query = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@parentIndex='" + this.getFragmentIndex() + "' and @type='org.xbrlapi.impl.SimpleLinkImpl']";
    	FragmentList<SimpleLink> links = getStore().<SimpleLink>query(query);
    	FragmentList<SimpleLink> includes = new FragmentListImpl<SimpleLink>();
    	for (SimpleLink link: links) {
        	if (link.getLocalname().equals("include") && link.getNamespaceURI().equals(Constants.XMLSchemaNamespace)) {
        		includes.add(link);
        	}
    	}
    	return includes;
    }
    
    /**
     * @see org.xbrlapi.Schema#getExtendedLinks()
     */
    public FragmentList<ExtendedLink> getExtendedLinks() throws XBRLException {
    	FragmentList<Linkbase> linkbases = getStore().<Linkbase>getChildFragments("Linkbase",getFragmentIndex());
    	logger.debug("The schema contains " + linkbases.getLength() + " linkbases.");
    	FragmentList<ExtendedLink> links = new FragmentListImpl<ExtendedLink>();
    	for (Linkbase linkbase: linkbases) {
        	links.addAll(getStore().<ExtendedLink>getChildFragments("ExtendedLink",linkbase.getFragmentIndex()));
    	}
    	return links;
    }
    
    /**
     * Get the fragment list of concepts in the schema.
     * @return the list of concepts in the schema.
     * @throws XBRLException.
     * @see org.xbrlapi.Schema#getConcepts()
     */
    public FragmentList<Concept> getConcepts() throws XBRLException {
    	return this.<Concept>getChildren("org.xbrlapi.impl.ConceptImpl");
    }

    /**
     * Get a specific concept by its name.
     * return the chosen concept or null if the concept does not exist.
     * @param name The name of the concept
     * @throws XBRLException
     * @see org.xbrlapi.Schema#getConceptByName(String)
     */
    public Concept getConceptByName(String name) throws XBRLException {
    	String query = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@type='org.xbrlapi.impl.ConceptImpl' and @parentIndex='" + getFragmentIndex() + "' and "+ Constants.XBRLAPIPrefix+ ":" + "data/*/@name='" + name + "']";
    	FragmentList<Concept> concepts = getStore().<Concept>query(query);
    	if (concepts.getLength() == 0) return null;
    	if (concepts.getLength() > 1) throw new XBRLException("The concept name is not unique to the schema.");
    	return concepts.getFragment(0);
    }

    /**
     * Get a list of concepts based on their type.
     * Returns null if no concepts match the selection criteria.
     * @param namespaceURI The namespaceURI of the concept type
     * @param localName The local name of the concept type
     * @return A list of concept fragments in the containing schema that
     * match the specified element type.
     * @throws XBRLException
     * @see org.xbrlapi.Schema#getConceptsByType(String, String)
     */
    public FragmentList<Concept> getConceptsByType(String namespaceURI, String localName) throws XBRLException {
    	FragmentList<Concept> matches = new FragmentListImpl<Concept>();
    	FragmentList<Concept> concepts = getConcepts();
    	for (Concept concept: concepts) {
			if (concept.getTypeNamespace().equals(namespaceURI) && 
				concept.getTypeLocalname().equals(localName)) {
				matches.add(concept);
			}
		}
		return matches;
	}
    
    /**
     * Get a list concepts based on their substitution group.
     * Returns null if no concepts match the selection criteria.
     * @param namespaceURI The namespaceURI of the concept type
     * @param localName The local name of the concept type
     * @return a list of concepts in the schema that match the specified
     * substitution group
     * @throws XBRLException
     * @see org.xbrlapi.Schema#getConceptsBySubstitutionGroup(String, String)
     */
    public FragmentList<Concept> getConceptsBySubstitutionGroup(String namespaceURI, String localName) throws XBRLException {
    	FragmentListImpl<Concept> matches = new FragmentListImpl<Concept>();
    	FragmentList<Concept> concepts = getConcepts();
    	for (Concept concept: concepts) {
			if (concept.getSubstitutionGroupNamespace().equals(namespaceURI) && 
					concept.getSubstitutionGroupLocalname().equals(localName)) {
					matches.add(concept);
			}
    	}
		return matches;
    }

    /**
     * Remove a specific concept identified by its name.
     * Throws an exception if the concept does not exist.
     * @param name The name of the concept
     * @throws XBRLException
     * @see org.xbrlapi.Schema#removeConceptByName(String)
     */
    public void removeConceptByName(String name) throws XBRLException {
    	throw new XBRLException("Data update methods have not yet been implemented.");
    }

    /**
     * Add a concept to a schema.
     * Throws an exception if the schema already contains a 
     * concept with the same name or id. 
     * @param concept The concept to be added to the schema.
     * @throws XBRLException
     * @see org.xbrlapi.Schema#addConcept(Concept)
     */
    public void addConcept(Concept concept) throws XBRLException {
    	throw new XBRLException("Data update methods have not yet been implemented.");
    }	

    /**
     * Get a reference part declaration in a schema.
     * Returns null if the reference part does not exist in the schema.
     * @param name The name attribute value of the reference part to be retrieved.
     * @throws XBRLException
     * @see org.xbrlapi.Schema#getReferencePartDeclaration(String)
     */
    public ReferencePartDeclaration getReferencePartDeclaration(String name) throws XBRLException {
    	for (int i=0; i<getReferencePartDeclarations().getLength(); i++) {
    		if (getReferencePartDeclarations().getFragment(i).getLocalname().equals(name))
				return getReferencePartDeclarations().getFragment(i);
    	}
    	return null;
    }
    
    /**
     * Get a list of the reference part declarations in a schema.
     * @return a list of reference part declarations in the schema.
     * @throws XBRLException
     * @see org.xbrlapi.Schema#getReferencePartDeclarations()
     */
    public FragmentList<ReferencePartDeclaration> getReferencePartDeclarations() throws XBRLException {
    	return this.getChildren("org.xbrlapi.impl.ReferencePartDeclarationImpl");
    }
    
    /**
     * Add a reference part declaration to a schema.
     * @param referencePartDeclaration The concept to be added to the schema.
     * @throws XBRLException if the reference part already exists in the schema.
     * @see org.xbrlapi.Schema#addReferencePartDeclaration(ReferencePartDeclaration)
     */
    public void addReferencePartDeclaration(ReferencePartDeclaration referencePartDeclaration) throws XBRLException {
    	throw new XBRLException("Data update methods have not yet been implemented.");
    }    

    /**
     * Remove a reference part declaration from a schema.
     * @param referencePartDeclaration The reference part declaration to be removed.
     * @throws XBRLException
     * @see org.xbrlapi.Schema#removeReferencePartDeclaration(ReferencePartDeclaration)
     */
    public void removeReferencePartDeclaration(ReferencePartDeclaration referencePartDeclaration) throws XBRLException {
    	throw new XBRLException("Data update methods have not yet been implemented.");
    }

    /**
     * Adds an linkbaseRef to a schema.  More methods are required to
     * give greater control over document order.
     * @param linkbaseRef The linkbaseRef to be added to the schema.
     * @throws XBRLException
     * @see org.xbrlapi.Schema#addLinkbaseRef(SimpleLink)
     */
    public void addLinkbaseRef(SimpleLink linkbaseRef) throws XBRLException {
    	throw new XBRLException("Data update methods are not yet implemented.");
    }

    
    /**
     * Remove a linkbaseRef from a schema.
     * @param linkbaseRef The linkbaseRef to be removed.
     * @throws XBRLException if the linkbaseRef is not contained in the schema.
     * @see org.xbrlapi.Schema#removeLinkbaseRef(SimpleLink)
     */
    public void removeLinkbaseRef(SimpleLink linkbaseRef) throws XBRLException {
    	throw new XBRLException("Data update methods have not yet been implemented.");
    }

    /**
     * Adds a roleType to a schema.
     * @param roleType The roleType to be added to the schema.
     * @throws XBRLException
     * @see org.xbrlapi.Schema#addRoleType(RoleType)
     */
    public void addRoleType(RoleType roleType) throws XBRLException {
    	throw new XBRLException("Data update methods have not yet been implemented.");
    }
    
    /**
     * Remove a roleType from a schema.
     * @param roleType The roleType to be removed.
     * @throws XBRLException  if the roleType is not contained in the schema.
     * @see org.xbrlapi.Schema#removeRoleType(RoleType)
     */
    public void removeRoleType(RoleType roleType) throws XBRLException {
    	throw new XBRLException("Data update methods have not yet been implemented.");
    }

    /**
     * Adds an arcroleType to a schema.
     * @param arcroleType The arcroleType to be added to the schema.
     * @throws XBRLException
     * @see org.xbrlapi.Schema#addArcroleType(ArcroleType)
     */
    public void addArcroleType(ArcroleType arcroleType) throws XBRLException {
    	throw new XBRLException("Data update methods have not yet been implemented.");
    }

    
    /**
     * Remove a arcroleType from a schema.
     * @param arcroleType The arcroleType to be removed.
     * @throws XBRLException if the arcroleType is not contained in the schema.
     * @see org.xbrlapi.Schema#removeArcroleType(ArcroleType)
     */
    public void removeArcroleType(ArcroleType arcroleType) throws XBRLException {
    	throw new XBRLException("Data update methods have not yet been implemented.");
    }

}