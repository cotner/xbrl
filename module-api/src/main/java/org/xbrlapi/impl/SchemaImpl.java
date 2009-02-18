package org.xbrlapi.impl;

import java.net.URI;
import java.net.URISyntaxException;

import org.xbrlapi.Concept;
import org.xbrlapi.ExtendedLink;
import org.xbrlapi.FragmentList;
import org.xbrlapi.Linkbase;
import org.xbrlapi.ReferencePartDeclaration;
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
     * @see org.xbrlapi.Schema#getTargetNamespace()
     */
    public URI getTargetNamespace() throws XBRLException {
    	if (getDataRootElement().hasAttribute("targetNamespace")) {
    		try {
    		    return new URI(getDataRootElement().getAttribute("targetNamespace"));
    		} catch (URISyntaxException e) {
    		    throw new XBRLException("The target namespace is not a valid URI.",e);
    		}
    	}
    	return null;
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
    	String query = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@parentIndex='" + this.getIndex() + "' and @type='org.xbrlapi.impl.SimpleLinkImpl']";
    	FragmentList<SimpleLink> links = getStore().<SimpleLink>query(query);
    	FragmentList<SimpleLink> imports = new FragmentListImpl<SimpleLink>();
    	for (SimpleLink link: links) {
        	if (link.getLocalname().equals("import") && link.getNamespace().equals(Constants.XMLSchemaNamespace)) {
        		imports.add(link);
        	}
    	}
    	return imports;
    }
    
    /**
     * @see org.xbrlapi.Schema#getIncludes()
     */
    public FragmentList<SimpleLink> getIncludes() throws XBRLException {
    	String query = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@parentIndex='" + this.getIndex() + "' and @type='org.xbrlapi.impl.SimpleLinkImpl']";
    	FragmentList<SimpleLink> links = getStore().<SimpleLink>query(query);
    	FragmentList<SimpleLink> includes = new FragmentListImpl<SimpleLink>();
    	for (SimpleLink link: links) {
        	if (link.getLocalname().equals("include") && link.getNamespace().equals(Constants.XMLSchemaNamespace)) {
        		includes.add(link);
        	}
    	}
    	return includes;
    }
    
    /**
     * @see org.xbrlapi.Schema#getExtendedLinks()
     */
    public FragmentList<ExtendedLink> getExtendedLinks() throws XBRLException {
    	FragmentList<Linkbase> linkbases = getStore().<Linkbase>getChildFragments("Linkbase",getIndex());
    	logger.debug("The schema contains " + linkbases.getLength() + " linkbases.");
    	FragmentList<ExtendedLink> links = new FragmentListImpl<ExtendedLink>();
    	for (Linkbase linkbase: linkbases) {
        	links.addAll(getStore().<ExtendedLink>getChildFragments("ExtendedLink",linkbase.getIndex()));
    	}
    	return links;
    }
    
    /**
     * @see org.xbrlapi.Schema#getConcepts()
     */
    public FragmentList<Concept> getConcepts() throws XBRLException {
    	return this.<Concept>getChildren("org.xbrlapi.impl.ConceptImpl");
    }
    
    /**
     * @see org.xbrlapi.Schema#getOtherElementDeclarations()
     */
    public FragmentList<Concept> getOtherElementDeclarations() throws XBRLException {
        return this.<Concept>getChildren("org.xbrlapi.impl.ElementDeclarationImpl");
    }
    

    /**
     * Get a specific concept by its name.
     * return the chosen concept or null if the concept does not exist.
     * @param name The name of the concept
     * @throws XBRLException
     * @see org.xbrlapi.Schema#getConceptByName(String)
     */
    public Concept getConceptByName(String name) throws XBRLException {
    	String query = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@type='org.xbrlapi.impl.ConceptImpl' and @parentIndex='" + getIndex() + "' and "+ Constants.XBRLAPIPrefix+ ":" + "data/*/@name='" + name + "']";
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
    
    





    



    




    


}