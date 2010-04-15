package org.xbrlapi.sax.identifiers;

import java.io.IOException;

import org.apache.xerces.parsers.XMLGrammarPreparser;
import org.apache.xerces.util.XMLResourceIdentifierImpl;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XSGrammar;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSNamespaceItemList;
import org.xbrlapi.Fragment;
import org.xbrlapi.impl.AttributeDeclarationImpl;
import org.xbrlapi.impl.AttributeGroupDeclarationImpl;
import org.xbrlapi.impl.ComplexTypeDeclarationImpl;
import org.xbrlapi.impl.ConceptImpl;
import org.xbrlapi.impl.ElementDeclarationImpl;
import org.xbrlapi.impl.ReferencePartDeclarationImpl;
import org.xbrlapi.impl.SchemaAllCompositorImpl;
import org.xbrlapi.impl.SchemaChoiceCompositorImpl;
import org.xbrlapi.impl.SchemaGroupCompositorImpl;
import org.xbrlapi.impl.SchemaImpl;
import org.xbrlapi.impl.SchemaSequenceCompositorImpl;
import org.xbrlapi.impl.SimpleTypeDeclarationImpl;
import org.xbrlapi.sax.ContentHandler;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;
import org.xml.sax.Attributes;

/**
 * Identifies XML Schema fragments.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class SchemaIdentifier extends BaseIdentifier implements Identifier {

    /**
     * @see org.xbrlapi.sax.identifiers.BaseIdentifier#BaseIdentifier(ContentHandler)
     */
    public SchemaIdentifier(ContentHandler contentHandler) throws XBRLException {
        super(contentHandler);
    }

    /**
     * Find fragments with root elements in the XML Schema namespace.
     * @see org.xbrlapi.sax.identifiers.BaseIdentifier#startElement(String,String,String,Attributes)
     */
    public void startElement(
            String namespaceURI, 
            String lName, 
            String qName,
            Attributes attrs) throws XBRLException {

        Fragment fragment = null;

        if (namespaceURI.equals(Constants.XMLSchemaNamespace.toString())) {

            if (lName.equals("group")) {
                
                fragment = new SchemaGroupCompositorImpl();
            
            } else if (lName.equals("all")) {
                    
                fragment = new SchemaAllCompositorImpl();
            
            } else if (lName.equals("choice")) {
                
                fragment = new SchemaChoiceCompositorImpl();
        
            } else if (lName.equals("sequence")) {
                
                fragment = new SchemaSequenceCompositorImpl();
        
            } else if (lName.equals("complexType")) {
                
                fragment = new ComplexTypeDeclarationImpl();
            
            } else if (lName.equals("simpleType")) {
                
                fragment = new SimpleTypeDeclarationImpl();
            
            } else if (lName.equals("attribute")) {
                
                fragment = new AttributeDeclarationImpl();

            } else if (lName.equals("attributeGroup")) {
                    
                    fragment = new AttributeGroupDeclarationImpl();

            } else if (lName.equals("schema")) {
                
                fragment = new SchemaImpl();
                setXSModel(constructXSModel());
                setTargetNamespace(attrs.getValue("targetNamespace"));
                
            } else if (lName.equals("element")) {
                
                String elementName = attrs.getValue("name");
                
                if (getXSModel() == null) {
                    throw new XBRLException("An XML Schema element declaration was found outside of an XML Schema.");
                }

                String targetNamespace = getTargetNamespace(); 
                if (targetNamespace == null) {
                    throw new XBRLException("An XML Schema element was found without a target namespace.");
                }

                // Find the XS model element declaration for the element that has been started - if one can be found
                XSElementDeclaration declaration = null;

                // Handle anonymous schemas first - these are the tough case
                if (targetNamespace.equals("")) {
                    
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
                            
                            // Check to see if the current document URI is one of those documents and if so, the candidate could be good
                            for (int j=0; j<locations.getLength(); j++) {
                                String location = locations.item(j);
                                if (location.equals(getContentHandler().getURI().toString())) {
                                    // Throw an exception if we find two feasible candidate element declarations in the Schema model
                                    if (declaration != null) throw new XBRLException("Potentially ambiguous anonymous Schema problem.");
                                    declaration = candidateDeclaration;
                                }
                            }
                        }
                    }

                    if (declaration == null) throw new XBRLException("An element declaration was found that could not be handled.");
                    
                // Handle the easy case where the schema specifies its target namespace
                } else if (elementName != null) {
                        declaration = getXSModel().getElementDeclaration(elementName, getTargetNamespace());
                }
                
                // Determine what substitution groups the element is in - if any.
                if (declaration != null) {
                    XSElementDeclaration sgDeclaration = declaration.getSubstitutionGroupAffiliation();
                    while (sgDeclaration != null) {
                        
                        if (sgDeclaration.getNamespace().equals(Constants.XBRL21Namespace.toString())) {
                            if (sgDeclaration.getName().equals("item")) {
                                fragment = new ConceptImpl();
                                break;
                            } else if (sgDeclaration.getName().equals("tuple")) {
                                fragment = new ConceptImpl();
                                break;
                            }
                        }

                        if (sgDeclaration.getNamespace().equals(Constants.XBRL21LinkNamespace.toString())) {
                            if (sgDeclaration.getName().equals("part"))
                                fragment = new ReferencePartDeclarationImpl();
                                break;
                        }
                        
                        sgDeclaration = sgDeclaration.getSubstitutionGroupAffiliation();
                    }
                }
                
                if ((fragment == null)) {
                    fragment = new ElementDeclarationImpl();
                }

            }
            
            if (fragment != null) {
                this.processFragment(fragment,attrs);
            }
        }
    }
    
    /**
     * The implementation assumes that XML schemas do not nest XML schemas.
     * 
     * Set the target namespace to null once the schema element is ended.
     * 
     * @see Identifier#endElement(String, String, String, Attributes)
     */
    public void endElement(
            String namespaceURI, 
            String lName, 
            String qName,
            Attributes attrs) throws XBRLException {
        
        // This assumes that XML schemas do not nest XML schemas
        if (lName.equals("schema"))
            if (namespaceURI.equals(Constants.XMLSchemaNamespace.toString())) {
                setTargetNamespace(null);
            }

    }    
    
    /**
     * The XML Schema model if parsing an XML Schema.
     */
    private XSModel model = null;    
    
    /**
     * @return the XML Schema grammar model.
     */
    protected XSModel getXSModel() {
        return this.model;
    }
    
    /**
     * @param model The XML Schema grammar model.
     * @throws XBRLException if the model is null.
     */
    protected void setXSModel(XSModel model) throws XBRLException {
        if (model == null) throw new XBRLException("The XML Schema model must not be null.");
        this.model = model;
    }
    
    /**
     * The target namespace of the XML Schema being parsed or null if the
     * XML Schema being parsed has no target namespace.
     */
    private String targetNamespace = null;
    
    /**
     * @return the target namespace.
     */
    protected String getTargetNamespace() {
        return this.targetNamespace;
    }    
    
    /**
     * @param targetNamespace The target namespace of the schema.
     */
    protected void setTargetNamespace(String targetNamespace) {
        this.targetNamespace = targetNamespace;
    }    
    
    /**
     * (Commented out!) Modified on 13 February, 2007 by Howard Ungar to use the static grammar pool provided by the GrammarCache implementation.
     * TODO Determine why a static grammar pool is needed to use included anonymous schemas.
     * @return the XML Schema grammar model for the XML Schema being parsed.
     * @throws XBRLException
     */
    protected XSModel constructXSModel() throws XBRLException {
        try {

            XMLGrammarPreparser preparser = new XMLGrammarPreparser();
            preparser.registerPreparser(XMLGrammarDescription.XML_SCHEMA, null);
            //preparser.setProperty("http://apache.org/xml/properties/internal/grammar-pool", GrammarCacheImpl.getGrammarPool());
            //preparser.setGrammarPool(GrammarCacheImpl.getGrammarPool());
            preparser.setFeature("http://xml.org/sax/features/namespaces", true);
            preparser.setFeature("http://xml.org/sax/features/validation", true);
            preparser.setFeature("http://apache.org/xml/features/validation/schema", true);
            preparser.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);

            XMLEntityResolver entityResolver = this.getLoader().getEntityResolver();
            preparser.setEntityResolver(entityResolver);

            // TODO make sure that this XML Resource Identifier is being initialised correctly.
            String uri = this.getContentHandler().getURI().toString();
            XMLResourceIdentifier xri = new XMLResourceIdentifierImpl("", uri, uri, uri);
            
            XMLInputSource xmlInputSource = entityResolver.resolveEntity(xri);
            
            XSGrammar grammar = (XSGrammar) preparser.preparseGrammar(XMLGrammarDescription.XML_SCHEMA, xmlInputSource);

            return grammar.toXSModel();         

        } catch (IOException e) {
            throw new XBRLException("Grammar model construction for schema at URI: " + getContentHandler().getURI() + " failed.",e);
        }
    }    
    
}
