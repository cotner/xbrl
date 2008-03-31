package org.xbrlapi.xlink.handler;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.xbrlapi.Arc;
import org.xbrlapi.ExtendedLink;
import org.xbrlapi.Locator;
import org.xbrlapi.Resource;
import org.xbrlapi.SimpleLink;
import org.xbrlapi.Title;
import org.xbrlapi.SAXHandlers.ElementState;
import org.xbrlapi.impl.ArcImpl;
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
import org.xbrlapi.xlink.XLinkException;
import org.xbrlapi.xlink.XLinkHandlerDefaultImpl;
import org.xbrlapi.xmlbase.BaseURLSAXResolver;
import org.xbrlapi.xmlbase.XMLBaseException;
import org.xbrlapi.xpointer.resolver.PointerResolver;
import org.xml.sax.Attributes;
/**
 * XBRL XLink Handler
 * 
 * This class provides a real world example of an XLink handler for XBRL.
 * 
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
*/

public class XBRLXLinkHandlerImpl extends XLinkHandlerDefaultImpl {

	protected static Logger logger = Logger.getLogger(XBRLXLinkHandlerImpl.class);	
	
	/**
	 * The XBRL DTS loader that is using this XLink handler
	 */
	private Loader loader;
	
	/**
	 * The XPointer Resolver implementation to be used by the
	 * XLink handler
	 */
	private PointerResolver xptrResolver;

	/**
	 * The base URL resolver used by the XLink handler
	 */
	private BaseURLSAXResolver baseURLResolver;
		
	/**
	 * The depth of the element being examined for XLink 
	 * characteristics in the containing tree.  This is updated
	 * by the content handler each time a new element is started
	 * by the content handler.
	 */
	private long depth;
	
    /**
     * Data required to track the element scheme XPointer 
     * expressions that can be used to identify XBRL fragments.
     */
	private ElementState state;
	
	/**
	 * XBRL XLink handler constructor
	 */
	public XBRLXLinkHandlerImpl() {
		super();
		this.baseURLResolver = null;
		this.xptrResolver = null;
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
	 * Set the base URL resolver for the XBRL XLink handler.
	 * @param resolver the base URL resolver used by the XLink handler.
	 */
	public void setBaseURLSAXResolver(BaseURLSAXResolver resolver) {
		baseURLResolver = resolver;
	}
	
	/**
	 * Set the XPointer resolver for the XBRL XLink handler.
	 * @param resolver the XPointer resolver used by the XLink handler.
	 */
	public void setXPointerResolver(PointerResolver resolver) {
		xptrResolver = resolver;
	}
	
	/**
	 * Handle the XML Base attribute discovery
	 * @param value the Value of the XML Base attribute
	 */
	public void xmlBaseStart(String value) throws XLinkException {
		try {
			baseURLResolver.addBaseURL(value);
		} catch (XMLBaseException e) {
			throw new XLinkException("The Base URL Resolver could not update the base URL",e);
		}
	}

	/**
	 * Creates and stores an XLink title fragment.
	 */
	public void startTitle(String namespaceURI, String lName, String qName,
			Attributes attrs) throws XLinkException {
		Title title = null;
		try {
			Loader loader = this.getLoader();
			title = new TitleImpl();
			title.setFragmentIndex(getLoader().getNextFragmentId());

			loader.addFragment(title,depth, state);
		} catch (XBRLException e) {
			throw new XLinkException("The title could not be created and stored.",e);
		}
	}
	
	/**
	 * Handle the change of XML Base scope as you step back up the tree
	 */
	public void xmlBaseEnd() throws XLinkException {
			try {
				 baseURLResolver.removeBaseURL();
			} catch (XMLBaseException e) {
				throw new XLinkException("The Base URL Resolver could not revert to the previous base URL",e);
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
		
		// Create the extended link fragment
		try {
			ExtendedLink link = new ExtendedLinkImpl();
			link.setFragmentIndex(getLoader().getNextFragmentId());
			if (attrs.getValue("id") != null) {
				link.appendID(attrs.getValue("id"));
				state.setId(attrs.getValue("id"));
			}
			getLoader().addFragment(link,depth, state);
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
	 * TODO Avoid using classes to differentiate XLink resources.
	 * Create the resource and add it to the map of arc anchors ready to be processed
	 * once the end of the containing extended link has been found.
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
			Resource resource = null;
			if (namespaceURI.equals(Constants.XBRL21LinkNamespace)) {
				if (lName.equals("label")) {
					resource = new LabelResourceImpl();
				} else if (lName.equals("reference")) {
					resource = new ReferenceResourceImpl();
				} else if (lName.equals("footnote")) {
					resource = new FootnoteResourceImpl();
				} else {
					resource = new ResourceImpl();				
				}
			} else if (namespaceURI.equals(Constants.GenericLabelNamespace)) {
				if (lName.equals("label")) {
					resource = new LabelResourceImpl();			
				} else {
	                resource = new ResourceImpl();
	            }
			} else if (namespaceURI.equals(Constants.GenericReferenceNamespace)) {
				if (lName.equals("reference")) {
					resource = new ReferenceResourceImpl();				
				} else {
	                resource = new ResourceImpl();
	            }
            } else if (namespaceURI.equals(Constants.GalexyEntitiesNamespace)) {
                if (lName.equals("entity")) {
                    resource = new EntityResourceImpl();             
                } else {
                    resource = new ResourceImpl();
                }
			} else {
				resource = new ResourceImpl();
			}
			resource.setFragmentIndex(getLoader().getNextFragmentId());

			if (attrs.getValue("id") != null) {
				resource.appendID(attrs.getValue("id"));
				state.setId(attrs.getValue("id"));
			}

			getLoader().addFragment(resource,depth, state);
		} catch (XBRLException e) {
			throw new XLinkException("The resource could not be created.",e);
		}
	}

	/**
	 * Handle the end of the resource.
	 */
	public void endResource(String namespaceURI, String sName, String qName) throws XLinkException {
		;// Do Nothing.
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
			URL url = new URL(baseURLResolver.getBaseURL(),href);
			Loader loader = getLoader();
			Locator locator = new LocatorImpl();
			locator.setFragmentIndex(loader.getNextFragmentId());
			locator.setTarget(url);
			loader.addFragment(locator,depth, state);
			loader.stashURL(url);
		} catch (MalformedURLException e) {
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
			Arc arc = new ArcImpl();				
    		arc.setFragmentIndex(getLoader().getNextFragmentId());
			getLoader().addFragment(arc,depth, state);
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
			URL url = new URL(baseURLResolver.getBaseURL(),href);
			Loader loader = getLoader();
			SimpleLink link = new SimpleLinkImpl();
			link.setFragmentIndex(getLoader().getNextFragmentId());
			link.setTarget(url);
			loader.addFragment(link,depth, state);
			loader.stashURL(url);
			
		} catch (MalformedURLException e) {
			throw new XLinkException("The URL on a simple link was malformed.",e);
		} catch (XBRLException e) {
			throw new XLinkException("The URL on a simple link could not be queued up for exploration in DTS discovery.",e);
		} catch (XMLBaseException e) {
			throw new XLinkException("The URL on a simple link could not be queued up for exploration in DTS discovery.",e);
		}
		
	}
	
	/**
	 * Handle the end of the simple link
	 */
	public void endSimpleLink(String namespaceURI, String sName, String qName) throws XLinkException {
		;// Do nothing
	}
		
	/**
	 * Returns the XBRL DTS loader that is using this XLink handler.
	 * @return The XBRL DTS loader that is using this XLink handler.
	 * @throws XBRLException if the XLink handler has no loader to work with.
	 */
	private Loader getLoader() throws XBRLException {
		if (loader == null)
			throw new XBRLException("The XLink Handler has no XBRL DTS loader to work with.");
		return loader;
	}

	public void setDepth(long depth) {
		this.depth = depth; 
	}
	
	private long getDepth() {
		return depth; 
	}
	
	public void setState(ElementState state) {
		this.state = state; 
	}
	
	private ElementState getState() {
		return state; 
	}	
	
}