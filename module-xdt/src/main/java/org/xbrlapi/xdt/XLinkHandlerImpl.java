package org.xbrlapi.xdt;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.xbrlapi.Fragment;
import org.xbrlapi.Locator;
import org.xbrlapi.Resource;
import org.xbrlapi.SimpleLink;
import org.xbrlapi.builder.BuilderImpl;
import org.xbrlapi.impl.ArcImpl;
import org.xbrlapi.impl.EntityResourceImpl;
import org.xbrlapi.impl.ExtendedLinkImpl;
import org.xbrlapi.impl.FootnoteResourceImpl;
import org.xbrlapi.impl.LabelResourceImpl;
import org.xbrlapi.impl.LocatorImpl;
import org.xbrlapi.impl.ReferenceResourceImpl;
import org.xbrlapi.impl.ResourceImpl;
import org.xbrlapi.impl.SimpleLinkImpl;
import org.xbrlapi.impl.TitleImpl;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xlink.ElementState;
import org.xbrlapi.xlink.XLinkException;
import org.xbrlapi.xlink.XLinkHandler;
import org.xbrlapi.xlink.handler.XBRLXLinkHandlerImpl;
import org.xbrlapi.xmlbase.BaseURISAXResolver;
import org.xbrlapi.xmlbase.XMLBaseException;
import org.xml.sax.Attributes;

/**
 * XBRL XLink Handler
 * This class provides a real world example of an XLink handler for XBRL.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
*/
public class XLinkHandlerImpl extends XBRLXLinkHandlerImpl {

	/**
     * 
     */
    private static final long serialVersionUID = -4285395768000453609L;
    private static final Logger logger = Logger.getLogger(XLinkHandlerImpl.class);	
	
	/**
	 * XBRL XLink handler constructor
	 */
	public XLinkHandlerImpl() {
		super();
		this.baseURIResolver = null;
		this.loader = null;
	}

	/**
	 * Set the XBRL DTS loader
	 * @param loader The XBRL DTS loader
	 */
	public void setLoader(Loader loader) {
		this.loader = loader;
	}
	
	/**
	 * Set the base URI resolver for the XBRL XLink handler.
	 * @param resolver the base URI resolver used by the XLink handler.
	 */
	public void setBaseURISAXResolver(BaseURISAXResolver resolver) {
		baseURIResolver = resolver;
	}
	

	
	/**
	 * Handle the XML Base attribute discovery
	 * @param value the Value of the XML Base attribute
	 */
	public void xmlBaseStart(String value) throws XLinkException {
		try {
			baseURIResolver.addBaseURI(value);
		} catch (XMLBaseException e) {
			throw new XLinkException("The Base URI Resolver could not update the base URI",e);
		}
	}

	/**
	 * Creates and stores an XLink title fragment.
	 */
	public void startTitle(String namespaceURI, String lName, String qName,
			Attributes attrs) throws XLinkException {
		try {
			processFragment(new TitleImpl(),attrs);
		} catch (XBRLException e) {
			throw new XLinkException("The title could not be created and stored.",e);
		}
	}

	/**
	 * Handle the change of XML Base scope as you step back up the tree
	 */
	public void xmlBaseEnd() throws XLinkException {
			try {
				 baseURIResolver.removeBaseURI();
			} catch (XMLBaseException e) {
				throw new XLinkException("The Base URI Resolver could not revert to the previous base URI",e);
			}
	}

	/**
	 * The extended link processing algorithm, central to this implementation, 
	 * operates as follows:
	 * <ul>
	 * <li>Store all locators and resources that are found in the extended link 
	 * in a map of lists where the map is indexed by XLink label attribute values
	 * and each list is made up of the locators and resources that have been found
	 * in the extended link that carry the same XLink label.
	 * </li>
	 * <li> 
	 * Preprocess the XLink href attribute values on locators to determine those
	 * locators linking to XML resources that have already been discovered and 
	 * parsed and those linking to XML resources that are yet to be parsed.  For 
	 * locators linking to parsed XML resources, add a relationship element to the
	 * locator containing the DTS index of the resource and add a relationship
	 * element to the resource containing the DTS index of the locator (The DTS
	 * index is the index attribute value unique within the DTS that is given to 
	 * various elements during the discovery process.)  For locators linked to 
	 * unparsed XML resources, place them in map of unprocessed locators, indexed
	 * by the href attribute value of the locator so that a resource can quickly 
	 * find all locators that point to them using the map and knowing their location.
	 * </li>
	 * <li>
	 * Store all arcs that are found in the extended link in a stack ready for 
	 * processing once the end of the extended link has been found.
	 * </li>
	 * </ul>
	 */
	public void startExtendedLink(
			String namespaceURI, 
			String lName,
			String qName, 
			Attributes attrs, 
			String role, 
			String title)
			throws XLinkException {
		
		try {
            processFragment(new ExtendedLinkImpl(),attrs);
		} catch (XBRLException e) {
			throw new XLinkException("The extended link could not be created.",e);
		}
	}

	/**
	 * Nothing needs to be done at the end of the extended link discovery.
	 */
	public void endExtendedLink(String namespaceURI, String sName, String qName)
			throws XLinkException {
		;
	}
	
    /**
     * Create the resource and add it to the map of arc anchors ready to be processed
     * once the end of the containing extended link has been found.
     * @see XLinkHandler#startResource(String, String, String, Attributes, String, String, String)
     */
	public void startResource(
			String namespaceURI, 
			String lName, 
			String qName,
			Attributes attrs, 
			String role, 
			String title, 
			String label)
			throws XLinkException {
		try {

		    Resource fragment = null;
			if (namespaceURI.equals(Constants.XBRL21LinkNamespace.toString())) {
				if (lName.equals("label")) {
					fragment = new LabelResourceImpl();
				} else if (lName.equals("reference")) {
					fragment = new ReferenceResourceImpl();
				} else if (lName.equals("footnote")) {
					fragment = new FootnoteResourceImpl();
				} else {
					fragment = new ResourceImpl();				
				}
			} else if (namespaceURI.equals(Constants.GenericLabelNamespace.toString())) {
				if (lName.equals("label")) {
					fragment = new LabelResourceImpl();			
				} else {
	                fragment = new ResourceImpl();
	            }
			} else if (namespaceURI.equals(Constants.GenericReferenceNamespace.toString())) {
				if (lName.equals("reference")) {
					fragment = new ReferenceResourceImpl();				
				} else {
	                fragment = new ResourceImpl();
	            }
            } else if (namespaceURI.equals(Constants.XBRLAPIEntitiesNamespace.toString())) {
                if (lName.equals("identifier")) {
                    fragment = new EntityResourceImpl();             
                } else {
                    fragment = new ResourceImpl();
                }
			} else {
				fragment = new ResourceImpl();
			}
			processFragment(fragment,attrs);
		} catch (XBRLException e) {
			throw new XLinkException("The resource could not be created.",e);
		}
	}

	/**
	 * Handle the end of the resource.
	 */
	public void endResource(String namespaceURI, String sName, String qName) throws XLinkException {
		;
	}
	
	/**
	 * Create the locator fragment and then add it to the map of arc anchors.
	 * Finally queue up the locator href value in the exploration queue.
	 */
	public void startLocator(
			String namespaceURI, 
			String lName, 
			String qName,
			Attributes attrs, 
			String href, 
			String role, 
			String title,
			String label) 
	throws XLinkException {
		try {
            Locator fragment = new LocatorImpl();
            processFragment(fragment,attrs);            

            Loader loader = getLoader();
            URI uri = baseURIResolver.getBaseURI().resolve(new URI(href));
            fragment.setTarget(uri);
            loader.stashURI(uri);

		} catch (URISyntaxException e) {
			throw new XLinkException("The locator href is malformed.",e);
		} catch (XMLBaseException e) {
			throw new XLinkException("The locator href could not be queued up for processing.",e);
		} catch (XBRLException e) {
			throw new XLinkException("The locator href could not be queued up for processing.",e);
		}
	}

	/**
	 * Handle the end of the locator.
	 */
	public void endLocator(String namespaceURI, String sName, String qName) throws XLinkException {
		;
	}
	
	/**
	 * Create the arc fragment first. Then add the arc to the 
	 * stack of arcs to be processed.
	 */
	public void startArc(
			String namespaceURI, 
			String lName, 
			String qName,
			Attributes attrs, 
			String from, 
			String to, 
			String arcrole,
			String title, 
			String show, 
			String actuate) throws XLinkException {
		try {
		    processFragment(new ArcImpl(),attrs);
		} catch (XBRLException e) {
			throw new XLinkException("The arc could not be created.",e);
		}
	}
	
	/**
	 * Handle the end of the arc.
	 */
	public void endArc(String namespaceURI, String sName, String qName) throws XLinkException {
		;
	}
	
	/**
	 * Add the href to the set of XML documents to be explored and 
	 * create the fragment for the simple link.
	 * TODO Should simple links generate relationship metadata?
	 */
	public void startSimpleLink(
			String namespaceURI, 
			String lName,
			String qName, 
			Attributes attrs, 
			String href, 
			String role,
			String arcrole, 
			String title, 
			String show, 
			String actuate)
			throws XLinkException {
		
		try {
		    SimpleLink fragment = null;
		    if (attrs.getValue(XDTConstants.XBRLDTNamespace.toString(),"typedDomainRef") != null) {
                fragment = new TypedDimensionImpl();
		    } else {
	            fragment = new SimpleLinkImpl();
		    }
            processFragment(fragment,attrs);

            URI uri = baseURIResolver.getBaseURI().resolve(new URI(href));
			Loader loader = getLoader();
            fragment.setTarget(uri);
            loader.stashURI(uri);
			
        } catch (URISyntaxException e) {
            throw new XLinkException("The URI on a simple link was malformed.",e);
		} catch (XBRLException e) {
			throw new XLinkException("The URI on a simple link could not be queued up for exploration in DTS discovery.",e);
		} catch (XMLBaseException e) {
			throw new XLinkException("The URI on a simple link could not be queued up for exploration in DTS discovery.",e);
		}
		
	}
	
	/**
	 * Handle the end of the simple link
	 */
	public void endSimpleLink(String namespaceURI, String sName, String qName) throws XLinkException {
		;
	}
		
	/**
	 * @return The XBRL DTS loader that is using this XLink handler.
	 * @throws XBRLException if the XLink handler has no loader to work with.
	 */
	private Loader getLoader() throws XBRLException {
		if (loader == null)
			throw new XBRLException("The XLink Handler has no XBRL DTS loader to work with.");
		return loader;
	}

	/**
	 * @param elementState The state for the current element.
	 */
	public void setElementState(ElementState elementState) {
		this.elementState = elementState; 
	}
	
	private ElementState getElementState() {
		return elementState; 
	}	
	
    /**
     * Walter Hamscher has identified documents in the XBRL community that violate the constraint
     * that attributes not defined in the XLink specification must not be in the XLink namespace.
     * To accommodate this imperfection, we catch that kind of error and make it a warning.
     * 
     * @see org.xbrlapi.xlink.XLinkHandler#warning(java.lang.String,java.lang.String, java.lang.String, org.xml.sax.Attributes, java.lang.String)
     */
    public void error(String namespaceURI, String lName, String qName,
            Attributes attrs,String message) throws XLinkException {

        if (message.endsWith(" is not defined in the XLink namespace.")) {
            this.warning(namespaceURI, lName, qName, attrs, message);
            return;
        }

        throw new XLinkException(message);
    }
    
    /**
     * Log a warning message
     * @see org.xbrlapi.xlink.XLinkHandler#warning(java.lang.String,java.lang.String, java.lang.String, org.xml.sax.Attributes, java.lang.String)
     */
    public void warning(String namespaceURI, String lName, String qName,
            Attributes attrs,String message) throws XLinkException {
        logger.warn(message);
    }
    
    /**
     * Set up the fragment and add it to the loader.
     * @param fragment The newly identified fragment.
     * @param attrs The attributes of the root element of the fragment.
     * @throws XBRLException
     */
    private void processFragment(Fragment fragment,Attributes attrs) throws XBRLException {
        Loader loader = this.getLoader();
        fragment.setBuilder(new BuilderImpl(loader.getBuilderDOM()));
        fragment.setIndex(getLoader().getNextFragmentId());
        if (attrs.getValue("id") != null) {
            fragment.appendID(attrs.getValue("id"));
            this.getElementState().setId(attrs.getValue("id"));
        }
        loader.add(fragment,getElementState());
    }
    
    
	
}