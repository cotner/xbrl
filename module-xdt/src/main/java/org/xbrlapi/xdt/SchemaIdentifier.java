package org.xbrlapi.xdt;

import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSNamespaceItemList;
import org.xbrlapi.Fragment;
import org.xbrlapi.impl.ConceptImpl;
import org.xbrlapi.impl.ElementDeclarationImpl;
import org.xbrlapi.impl.ReferencePartDeclarationImpl;
import org.xbrlapi.impl.SchemaImpl;
import org.xbrlapi.sax.ContentHandler;
import org.xbrlapi.sax.identifiers.Identifier;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;
import org.xml.sax.Attributes;

/**
 * Identifies XDT Schema Fragments.
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class SchemaIdentifier extends org.xbrlapi.sax.identifiers.SchemaIdentifier implements Identifier {

    /**
     * @see org.xbrlapi.sax.identifiers.BaseIdentifier#BaseFragmentIdentifierImpl(ContentHandler)
     */
    public SchemaIdentifier(ContentHandler contentHandler) throws XBRLException {
        super(contentHandler);
    }

    /**
     * Finds Sschema fragments including XDT-specific fragments
     * 
     * @see org.xbrlapi.sax.identifiers.BaseIdentifier#startElement(String,String,String,Attributes)
     */
    public void startElement(
            String namespaceURI, 
            String lName, 
            String qName,
            Attributes attrs) throws XBRLException {

        Fragment fragment = null;

        if (namespaceURI.equals(Constants.XMLSchemaNamespace)) {
            
            if (lName.equals("schema")) {
                
                fragment = new SchemaImpl();

                setXSModel(constructXSModel());
                setTargetNamespace(attrs.getValue("targetNamespace"));
                
            } else if (lName.equals("element")) {
                
                String elementName = attrs.getValue("name");
                
                if (getXSModel() == null) {
                    throw new XBRLException("An XML Schema element was found outside of an XML Schema.");
                }

                if (getTargetNamespace() == null) {
                    throw new XBRLException("An XML Schema element was found where the target namespace was not initialised.");
                }

                // Find the XS model element declaration for the element that has been started - if one can be found
                XSElementDeclaration declaration = null;
                
                // Handle anonymous schemas first - these are the tough case
                if (getTargetNamespace().equals("")) {
                    
                    // Get the list of namespaces declared in the model
                    XSNamespaceItemList nsItemList = getXSModel().getNamespaceItems();
                    
                    // For each namespace ...
                    for (int i=0; i<nsItemList.getLength(); i++) {
                        XSNamespaceItem nsItem = nsItemList.item(i);
                        
                        // Get a candidate element declaration if one exists
                        XSElementDeclaration candidateDeclaration = nsItem.getElementDeclaration(elementName);
                        if (candidateDeclaration != null) {
                            
                            // Get the URIs of the documents that were used to create elements in this namespace
                            StringList locations = nsItem.getDocumentLocations();
                            
                            // Check to see if the current document URL is one of those documents and if so, the candidate could be good
                            for (int j=0; j<locations.getLength(); j++) {
                                String location = locations.item(j);
                                if (location.equals(getContentHandler().getURL().toString())) {
                                    // Throw an exception if we find two feasible candidate element declarations in the Schema model
                                    if (declaration != null) throw new XBRLException("Potentially ambiguous anonymous Schema problem.");
                                    declaration = candidateDeclaration;
                                }
                            }
                        }
                    }
                    
                    // TODO Handle anonymous schemas without throwing an exception.
                    if (declaration == null) throw new XBRLException("An anonymous XML Schema was found that could not be handled.");
                    
                // Handle the easy case where the schema specifies its target namespace
                } else if (elementName != null) {
                        declaration = getXSModel().getElementDeclaration(elementName, getTargetNamespace());
                }
                
                // Determine what substitution groups the element is in - if any.
                if (declaration != null) {
                    XSElementDeclaration sgDeclaration = declaration.getSubstitutionGroupAffiliation();
                    while (sgDeclaration != null) {
                        if (sgDeclaration.getNamespace().equals(XDTConstants.XBRLDTNamespace)) {
                            if (sgDeclaration.getName().equals(XDTConstants.hypercube)) {
                                fragment = new HypercubeImpl();
                                break;
                            } else if (sgDeclaration.getName().equals(XDTConstants.dimension)) {
                                if (attrs.getValue(XDTConstants.XBRLDTNamespace,"typedDomainRef") == null) {
                                    fragment = new ExplicitDimensionImpl();
                                } else {
                                    fragment = new TypedDimensionImpl();
                                    logger.info(attrs.getValue(XDTConstants.XBRLDTNamespace,"typedDomainRef"));
                                }
                                break;
                            }
                        }
                        
                        if (sgDeclaration.getNamespace().equals(Constants.XBRL21Namespace)) {
                            if (sgDeclaration.getName().equals("item")) {
                                fragment = new ConceptImpl();
                                break;
                            } else if (sgDeclaration.getName().equals("tuple")) {
                                fragment = new ConceptImpl();
                                break;
                            }
                        }

                        if (sgDeclaration.getNamespace().equals(Constants.XBRL21LinkNamespace)) {
                            if (sgDeclaration.getName().equals("part"))
                                fragment = new ReferencePartDeclarationImpl();
                                break;
                        }
                        
                        sgDeclaration = sgDeclaration.getSubstitutionGroupAffiliation();
                    }
                }
                
                if ((fragment == null) && (elementName != null)) {
                    fragment = new ElementDeclarationImpl();
                }                   
                
            }
            
            if (fragment != null) {
                this.processFragment(fragment,attrs);
            }
        }
            
    }
    
    
    
}
