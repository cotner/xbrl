package org.xbrlapi.xdt;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSNamespaceItemList;
import org.xbrlapi.Fragment;
import org.xbrlapi.SAXHandlers.ContentHandlerImpl;
import org.xbrlapi.SAXHandlers.ElementState;
import org.xbrlapi.impl.ArcroleTypeImpl;
import org.xbrlapi.impl.ConceptImpl;
import org.xbrlapi.impl.ContextImpl;
import org.xbrlapi.impl.ElementDeclarationImpl;
import org.xbrlapi.impl.EntityImpl;
import org.xbrlapi.impl.FragmentImpl;
import org.xbrlapi.impl.InstanceImpl;
import org.xbrlapi.impl.LanguageImpl;
import org.xbrlapi.impl.LinkbaseImpl;
import org.xbrlapi.impl.NonNumericItemImpl;
import org.xbrlapi.impl.PeriodImpl;
import org.xbrlapi.impl.ReferencePartDeclarationImpl;
import org.xbrlapi.impl.ReferencePartImpl;
import org.xbrlapi.impl.RoleTypeImpl;
import org.xbrlapi.impl.ScenarioImpl;
import org.xbrlapi.impl.SchemaImpl;
import org.xbrlapi.impl.SegmentImpl;
import org.xbrlapi.impl.SimpleNumericItemImpl;
import org.xbrlapi.impl.TupleImpl;
import org.xbrlapi.impl.UnitImpl;
import org.xbrlapi.impl.UsedOnImpl;
import org.xbrlapi.impl.XlinkDocumentationImpl;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xlink.XLinkException;
import org.xbrlapi.xmlbase.XMLBaseException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class XDTContentHandlerImpl extends ContentHandlerImpl {

	protected static Logger logger = Logger.getLogger(XDTContentHandlerImpl.class);		
    
    /**
     * @see org.xbrlapi.SaxHandlers.ContentHandlerImpl#ContentHandlerImpl(Loader, URL)
     */
    public XDTContentHandlerImpl(Loader loader, URL url) throws XBRLException {
        super(loader,url);
    }
    	
    /**
     * @see org.xbrlapi.SaxHandlers.ContentHandlerImpl#ContentHandlerImpl(Loader, URL, String)
     */
    public XDTContentHandlerImpl(Loader loader, URL url, String xml) throws XBRLException {
        super(loader, url, xml);
    }    

    /**
     * @see org.xbrlapi.SaxHandlers.ContentHandlerImpl#startElement(String,String,String,Attributes)
     */
    @SuppressWarnings("unchecked")
    public void startElement( String namespaceURI, String lName, String qName, Attributes attrs) throws SAXException 
    {

        // Update element depth and child count.
        setDepth(getDepth()+1);
        
        try {
            getLoader().incrementChildren();
        } catch (XBRLException e) {
            throw new SAXException("Could not record a new child for the fragment being parsed.",e);
        }
        
        // Update the information about the state of the current element
        setState(new ElementState(getState()));
        
        // Stash new URLs in xsi:schemaLocation attributes if desired
        if (getLoader().useSchemaLocationAttributes()) {
            String schemaLocations = attrs.getValue(Constants.XMLSchemaInstanceNamespace,"schemaLocation");
            if (schemaLocations != null) {
                logger.debug("Processing schema locations: " + schemaLocations);
                String[] fields = schemaLocations.trim().split("\\s+");
                for (int i=1; i<fields.length; i=i+2) {
                    try {
                        URL url = new URL(getBaseURLSAXResolver().getBaseURL(),fields[i]);
                        logger.debug("Working on: " + url);
                        getLoader().stashURL(url);
                    } catch (MalformedURLException e) {
                        logger.warn("Ignoring malformed XSI schemaLocation URL in: " + schemaLocations);
                    } catch (XBRLException e) {
                        logger.warn("A problem occurred when stashing the schemaLocation URL: " + fields[i]);
                    } catch (XMLBaseException e) {
                        logger.warn("A problem occurred when getting the base URL so schemaLocation URLs were not stashed from: " + schemaLocations);
                    }
                }
            }
        }
        
        // Handle XLink fragments
        try {

            // First let the XLink handler know the depth of the element being processed in case it makes a fragment
            getXLinkHandler().setDepth(getDepth());
            getXLinkHandler().setState(getState());
            
            // Next pass control to the XLink processor so it can recognise and respond to XLink elements
            getLoader().getXlinkProcessor().startElement(namespaceURI,lName,qName,attrs);
            
        } catch (XLinkException e) {
            throw new SAXException("XLink processing of the start of an element failed.",e);
        }

        // Handle XML Schema fragments
        try {
            Fragment schemaFragment = null;
            String fragmentID = null; // The ID attribute value for the fragment if it exists

            if (namespaceURI.equals(Constants.XMLSchemaNamespace)) {
                
                if (lName.equals("schema")) {
                    schemaFragment = new SchemaImpl();

                    setXSModel(constructXSModel());
                    setTargetNamespace(attrs.getValue("targetNamespace"));
                    
                } else if (lName.equals("element")) {
                    
                    fragmentID = attrs.getValue("id");
                    
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
                                    if (location.equals(getURL().toString())) {
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
                    boolean isItemConcept = false;
                    boolean isTupleConcept = false;
                    boolean isReferencePartDeclaration = false;
                    if (declaration != null) {
                        XSElementDeclaration sgDeclaration = declaration.getSubstitutionGroupAffiliation();
                        while (sgDeclaration != null) {
                            
                            if (sgDeclaration.getNamespace().equals(Constants.XBRL21Namespace)) {
                                if (sgDeclaration.getName().equals("item"))
                                    isItemConcept = true;
                                else if (sgDeclaration.getName().equals("tuple"))
                                    isTupleConcept = true;
                            }
                            if (sgDeclaration.getNamespace().equals(Constants.XBRL21LinkNamespace)) {
                                if (sgDeclaration.getName().equals("part"))
                                    isReferencePartDeclaration = true;
                            }
                            sgDeclaration = sgDeclaration.getSubstitutionGroupAffiliation();
                        }
                    }
                    // TODO Decide if the element declarations need to keep track of their whole substitution group stack.
                    
                    // If the element has a periodType attribute it is an XBRL concept
                    if (isItemConcept || isTupleConcept) {
                        schemaFragment = new ConceptImpl();
                        
                    } else if (isReferencePartDeclaration) {
                        schemaFragment = new ReferencePartDeclarationImpl();
                    } else if (elementName != null) {
                        schemaFragment = new ElementDeclarationImpl();
                    }                   
                    
                }
                
                if (schemaFragment != null) {
                    schemaFragment.setFragmentIndex(getLoader().getNextFragmentId());
                    if (fragmentID != null) {
                        schemaFragment.appendID(fragmentID);
                        getState().setId(fragmentID);
                    }
                    getLoader().addFragment(schemaFragment,getDepth(),getState());
                }
            }
            
        } catch (XBRLException e) {
            throw new SAXException("The XML Schema fragment could not be processed.",e);
        }
        
        // Handle XBRL fragments
        Fragment xbrlFragment = null;
        try {

            if (namespaceURI.equals(Constants.XBRL21Namespace)) {

                if (lName.equals("xbrl")) {
                    xbrlFragment = new InstanceImpl();
                    this.parsingAnXBRLInstance = true;
                    this.canBeATuple = true;
                } else if (lName.equals("period")) {
                    xbrlFragment = new PeriodImpl();
                } else if (lName.equals("entity")) {
                    xbrlFragment = new EntityImpl();
                } else if (lName.equals("segment")) {
                    xbrlFragment = new SegmentImpl();
                } else if (lName.equals("scenario")) {
                    xbrlFragment = new ScenarioImpl();
                } else if (lName.equals("context")) {
                    xbrlFragment = new ContextImpl();
                    this.canBeATuple = false;
                } else if (lName.equals("unit")) {
                    xbrlFragment = new UnitImpl();
                    this.canBeATuple = false;
                }
                
                if (xbrlFragment != null) {
                    xbrlFragment.setFragmentIndex(getLoader().getNextFragmentId());
                    if (attrs.getValue("id") != null) {
                        xbrlFragment.appendID(attrs.getValue("id"));
                        getState().setId(attrs.getValue("id"));
                    }
                    getLoader().addFragment(xbrlFragment,getDepth(),getState());
                }
            }
        } catch (XBRLException e) {
            e.printStackTrace();
            throw new SAXException("The XBRL fragment could not be processed.",e);
        }

        // Handle reference part fragments
        Fragment referencePartFragment = null;
        try {
            
            if (parsingAReferenceResource) {
                referencePartFragment = new ReferencePartImpl();
            }

            if (referencePartFragment != null) {
                referencePartFragment.setFragmentIndex(getLoader().getNextFragmentId());
                getLoader().addFragment(referencePartFragment,getDepth(),getState());
            }
            
        } catch (XBRLException e) {
            throw new SAXException("The XBRL fragment could not be processed.",e);
        }
        
        // Handle XBRL Link fragments
        try {
            Fragment xbrlLinkFragment = null;
            if (namespaceURI.equals(Constants.XBRL21LinkNamespace)) {
                if (lName.equals("roleType")) {
                    xbrlLinkFragment = new RoleTypeImpl();
                } else if (lName.equals("arcroleType")) {
                    xbrlLinkFragment = new ArcroleTypeImpl();
                } else if (lName.equals("usedOn")) {
                    xbrlLinkFragment = new UsedOnImpl();
                } else if (lName.equals("linkbase")) {
                    xbrlLinkFragment = new LinkbaseImpl();
                } else if (lName.equals("documentation")) {
                    xbrlLinkFragment = new XlinkDocumentationImpl();
                } else if (lName.equals("footnoteLink")) {
                    // Fragment already created by XLink processor
                    this.canBeATuple = false;
                } else if (lName.equals("schemaRef")) {
                    // Fragment already created by XLink processor
                    this.canBeATuple = false;
                } else if (lName.equals("linkbaseRef")) {
                    // Fragment already created by XLink processor
                    this.canBeATuple = false;
                } else if (lName.equals("roleRef")) {
                    // Fragment already created by XLink processor
                    this.canBeATuple = false;
                } else if (lName.equals("arcroleRef")) {
                    // Fragment already created by XLink processor
                    this.canBeATuple = false;
                } else if (lName.equals("reference")) {
                    // Fragment already created by XLink processor
                    this.parsingAReferenceResource = true;
                }
                
                if (xbrlLinkFragment != null) {
                    xbrlLinkFragment.setFragmentIndex(getLoader().getNextFragmentId());
                    if (attrs.getValue("id") != null) {
                        xbrlLinkFragment.appendID(attrs.getValue("id"));
                        getState().setId(attrs.getValue("id"));
                    }
                    getLoader().addFragment(xbrlLinkFragment,getDepth(),getState());          
                }
            }
        } catch (XBRLException e) {
            throw new SAXException("The XBRL Link fragment could not be processed.",e);
        }

        // Handle XBRL facts
        if ((xbrlFragment == null) && (referencePartFragment == null) && this.parsingAnXBRLInstance) {
            try {
                Fragment factFragment = null;
                
                // First handle items
                String contextRef = attrs.getValue("contextRef");
                if (contextRef != null) {
                    String unitRef = attrs.getValue("unitRef");
                    if (unitRef != null) {
                        // TODO Handle recognition of fraction numeric items - may require reading ahead in SAX - ouch
                        factFragment = new SimpleNumericItemImpl();
                    } else {
                        factFragment = new NonNumericItemImpl();
                    }
                }
                
                // Next handle XBRL instance tuples
                if ((factFragment == null) && this.canBeATuple) {
                    factFragment = new TupleImpl();
                }
                
                if (factFragment != null) {
                    factFragment.setFragmentIndex(getLoader().getNextFragmentId());
                    if (attrs.getValue("id") != null) {
                        factFragment.appendID(attrs.getValue("id"));
                        getState().setId(attrs.getValue("id"));
                    }
                    getLoader().addFragment(factFragment,getDepth(),getState());
                }

            } catch (XBRLException e) {
                throw new SAXException("The XBRL Link fragment could not be processed.",e);
            }
        }
        
        // Handle language fragments
        Fragment languageFragment = null;
        try {
            if (namespaceURI.equals(Constants.XBRLAPILanguagesNamespace)) {
                if (lName.equals("language")) {
                    languageFragment = new LanguageImpl();
                }
            }

            if (languageFragment != null) {
                languageFragment.setFragmentIndex(getLoader().getNextFragmentId());
                getLoader().addFragment(languageFragment,getDepth(),getState());
            }
            
        } catch (XBRLException e) {
            throw new SAXException("The languages fragment could not be processed.",e);
        }
        
        // Add a generic fragment for a document root element if we have not already done so
        if (! getLoader().addedAFragment()) {
            if (getDepth() == 1) {
                try {
                    Fragment root = new FragmentImpl();
                    root.setFragmentIndex(getLoader().getNextFragmentId());
                    getLoader().addFragment(root,1,getState());
                } catch (XBRLException e) {
                    throw new SAXException("The default root element fragment could not be created.",e);
                }
            }
        }
        
        // Extend the child count for an new element if we have not started a new fragment
        try {
            if (! getLoader().getFragment().isNewFragment()) {
                getLoader().extendChildren();       
            }
        } catch (XBRLException e) {
            throw new SAXException("Could not handle children tracking at the fragment level.",e);
        }
        
        // Update the namespace data structure and insert namespace mappings into metadata.
        HashMap<String,String> inheritedMap = getNSStack().peek();
        HashMap<String,String> myMap = (HashMap<String,String>) inheritedMap.clone();
        for (int i = 0; i < attrs.getLength(); i++) {
            if ((attrs.getQName(i).equals("xmlns") || attrs.getQName(i).startsWith("xmlns:"))) {
                myMap.put(attrs.getValue(i),attrs.getQName(i));
            }
        }
        getNSStack().push(myMap);

        try {
            if (getLoader().getFragment().isNewFragment()) {
                Fragment f = getLoader().getFragment();
                for (String key: inheritedMap.keySet()) {
                    f.setMetaAttribute(inheritedMap.get(key),key);
                }
            }
        } catch (XBRLException e) {
           throw new SAXException("The loader is not building a fragment so something is badly amiss.",e);
        }
        
        // Insert the element into the fragment being built
        try {
            getLoader().getFragment().getBuilder().appendElement(namespaceURI, lName, qName, attrs);
        } catch (XBRLException e) {
            throw new SAXException("The element could not be appended to the fragment.",e);
        }
        
    }
    
    /**
     * @see org.xbrlapi.SAXHandlers.ContentHandlerImpl#endElement(String,String,String)
     */
    public void endElement(
            String namespaceURI, 
            String sName, 
            String qName) throws SAXException {
        
        // Handle the ending of an element in the fragment builder
        try {
            getLoader().getFragment().getBuilder().endElement(namespaceURI, sName, qName);
        } catch (XBRLException e) {
            throw new SAXException("The XBRLAPI fragment endElement failed.",e);
        }

        // Handle the ending of an element in the XLink processor
        try {
            getLoader().getXlinkProcessor().endElement(namespaceURI, sName, qName);
        } catch (XLinkException e) {
            throw new SAXException("The XLink processor endElement failed.",e);
        }

        // Give the loader a chance to update its state
        try {
            getLoader().updateState(getDepth());
        } catch (XBRLException e) {
            throw new SAXException("The state of the loader could not be updated at the end of element " + namespaceURI + ":" + sName + "." + e.getMessage(),e);
        }
        
        // Identify if an XBRL instance has been finished.
        if (namespaceURI.equals(Constants.XBRL21Namespace) && sName.equals("xbrl")) {
                this.parsingAnXBRLInstance = false;
        } else if (namespaceURI.equals(Constants.XBRL21LinkNamespace)) {
            if (sName.equals("footnoteLink")) {
                this.canBeATuple = true;
            } else if (sName.equals("schemaRef")) {
                this.canBeATuple = true;
            } else if (sName.equals("linkbaseRef")) {
                this.canBeATuple = true;
            } else if (sName.equals("arcroleRef")) {
                this.canBeATuple = true;
            } else if (sName.equals("roleRef")) {
                this.canBeATuple = true;
            } else if (sName.equals("reference")) {
                this.parsingAReferenceResource = false;
            }           
        } else if (namespaceURI.equals(Constants.XBRL21Namespace)) {
            if (sName.equals("context")) {
                this.canBeATuple = true;    
            } else if (sName.equals("true")) {
                this.canBeATuple = false;   
            }
        }
        
        // We have finished with an element so move one step up the document tree, reducing the depth of the current element by 1
        setDepth(getDepth() - 1);

        // Update the information about the state of the current element
        setState(getState().getParent());
        
        // Revert to Namespace declarations of the parent element.
        getNSStack().pop();

    }    
    
}
