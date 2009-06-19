package org.xbrlapi.data.exist;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xbrlapi.XML;
import org.xbrlapi.data.BaseStoreImpl;
import org.xbrlapi.data.Store;
import org.xbrlapi.impl.FragmentFactory;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XPathQueryService;

/**
 * Implementation of the XBRL data store using eXist
 * as the underlying database.
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class StoreImpl extends BaseStoreImpl implements Store {

	/**
	 * The database connection used by the data store.
	 */
	private DBConnectionImpl connection = null;
	
	/**
	 * The collection that holds the data in the data store.
	 */
	private Collection collection = null;
	
	/**
	 * The collection manager that operates on the data store collection (for indexing etc.).
	 */
	private CollectionManagementService manager = null;

	/**
	 * The XPath query service for the data collection.
	 */
    private XPathQueryService xpathService = null;
    	
	/**
	 * Initialise the database connection.
	 * 
	 * @param connection The live connection to the Xindice database.
	 * @param storeParentPath The full path to the container collection, which is the
	 * collection that will hold the data collection when it are created.
	 * @param dataCollectionName The name of the data collection.
	 * @throws XBRLException
	 */
	public StoreImpl(
			DBConnectionImpl connection,
			String storeParentPath,
			String dataCollectionName
			) throws XBRLException {
		
	    super();
	    
		storeParentPath = (storeParentPath.charAt(storeParentPath.length()-1) != '/') ? storeParentPath + "/" : storeParentPath;

		if (connection == null) throw new XBRLException("The Exist database connection is null so no data store can be established.");
		this.connection = connection;
		
		Collection parentCollection = connection.getCollection(storeParentPath);
		if (parentCollection == null) throw new XBRLException("The collection to contain the data store does not exist.");
				
		boolean storeAlreadyExisted = false;
		if (connection.hasCollection(dataCollectionName,parentCollection)) {
			storeAlreadyExisted = true;
			collection = connection.getCollection(dataCollectionName, parentCollection);
		} else {
			collection = connection.createCollection(dataCollectionName,parentCollection);
		}
		
		if (collection == null) throw new XBRLException("Collection " + dataCollectionName + " could not be created in collection " + storeParentPath + ".");

		try {
			manager = (CollectionManagementService) collection.getService("CollectionManagementService", "1.0");
		} catch (XMLDBException e) {
			throw new XBRLException("The collection manager could not be instantiated.",e);
		}
				
		if (! storeAlreadyExisted) {
			
			// Add the collection configuration (indexing information) if the store uses a new collection.
			Collection configParentCollection = connection.getCollection("system/config");
			Collection dataConfigCollection = null;
			try {
				dataConfigCollection = configParentCollection.getChildCollection(dataCollectionName); 
			if (dataConfigCollection == null)
				dataConfigCollection = connection.createCollection(dataCollectionName, configParentCollection);
			} catch (XMLDBException e) {
				throw new XBRLException("The data configuration collection could not be instantiated.",e);
			}
			
			String dataXconf = 
				"<collection xmlns=\"http://exist-db.org/collection-config/1.0\">\n"
				+ "<index xmlns:xlink=\"" + Constants.XLinkNamespace + "\" >\n"
				+ "    <fulltext default=\"none\" attributes=\"false\" alphanum=\"false\" />\n"
		        + "    <create path=\"/"+ Constants.XBRLAPIPrefix + ":" + "fragment/"+ Constants.XBRLAPIPrefix + ":" + "data/*/@xlink:label\" type=\"xs:string\"/>\n"
		        + "    <create path=\"/"+ Constants.XBRLAPIPrefix + ":" + "fragment/"+ Constants.XBRLAPIPrefix + ":" + "data/*/@xlink:type\" type=\"xs:string\"/>\n"				
		        + "    <create path=\"/"+ Constants.XBRLAPIPrefix + ":" + "fragment/@type\" type=\"xs:string\"/>\n"
		        + "    <create path=\"/"+ Constants.XBRLAPIPrefix + ":" + "fragment/@id\" type=\"xs:string\"/>\n"
		        + "    <create path=\"/"+ Constants.XBRLAPIPrefix + ":" + "fragment/@targetDocumentURI\" type=\"xs:string\"/>\n"
		        + "    <create path=\"/"+ Constants.XBRLAPIPrefix + ":" + "fragment/@targetPointerValue\" type=\"xs:string\"/>\n"
		        + "    <create path=\"/"+ Constants.XBRLAPIPrefix + ":" + "fragment/@parentIndex\" type=\"xs:string\"/>\n"
		        + "    <create path=\"/"+ Constants.XBRLAPIPrefix + ":" + "fragment/@uri\" type=\"xs:string\"/>\n"
		        + "    <create path=\"/"+ Constants.XBRLAPIPrefix + ":" + "fragment/@absoluteHref\" type=\"xs:string\"/>\n"
                + "    <create path=\"/"+ Constants.XBRLAPIPrefix + ":" + "fragment/@resourceURI\" type=\"xs:string\"/>\n"
                + "    <create path=\"/"+ Constants.XBRLAPIPrefix + ":" + "fragment/@value\" type=\"xs:string\"/>\n"
                + "    <create path=\"/"+ Constants.XBRLAPIPrefix + ":" + "fragment/"+ Constants.XBRLAPIPrefix + ":" + "xptr/@value\" type=\"xs:string\"/>\n"
		        + "</index>\n"
		        + "</collection>";
			
			try {
				
				XMLResource dataXconfResource = (XMLResource) dataConfigCollection.createResource("indexes.xconf", XMLResource.RESOURCE_TYPE);
				dataXconfResource.setContent(dataXconf);
				dataConfigCollection.storeResource(dataXconfResource);
				
			} catch (XMLDBException e) {
				throw new XBRLException("The system index configuration resource could not be added.",e);
			}

		}

		try {
			xpathService = (XPathQueryService) collection.getService("XPathQueryService","1.0");
			// Not allowed to bind the xml prefix: xpathService.setNamespace(Constants.XMLPrefix, Constants.XMLNamespace);
			xpathService.setNamespace(Constants.XLinkPrefix, Constants.XLinkNamespace);
			xpathService.setNamespace(Constants.XMLSchemaPrefix, Constants.XMLSchemaNamespace);
			xpathService.setNamespace(Constants.XBRL21Prefix, Constants.XBRL21Namespace);
			xpathService.setNamespace(Constants.XBRL21LinkPrefix, Constants.XBRL21LinkNamespace);
			xpathService.setNamespace(Constants.XBRLAPIPrefix, Constants.XBRLAPINamespace);
			xpathService.setNamespace(Constants.XBRLAPILanguagesPrefix, Constants.XBRLAPILanguagesNamespace);
			// TODO add means for users to add their own namespace declarations to the data store xpath services
			
		} catch (XMLDBException e) {
			throw new XBRLException("The XPath services could not be initialised.",e);
		}		
		
	}
    
	/**
	 * Close the data store.
	 * Throws XBRLException if the data store cannot be closed. 
	 */
	public synchronized void close() throws XBRLException {
		try {
			collection.close();
			connection = null;
		} catch (XMLDBException e) {
			throw new XBRLException("The Xindice DTS collection could not be closed.",e);
		}
	}
	
	/**
	 * @see org.xbrlapi.data.Store#delete()
	 */
	public synchronized void delete() throws XBRLException {
		try {
			String collectionName = collection.getName();
			collection.close();
			if (connection.hasCollection(collectionName)) {
				manager.removeCollection(collectionName);
			} else {
				logger.debug("The collection is not in the eXist database so it is not deleted.");
			}
			connection = null;
		} catch (XMLDBException e) {
			throw new XBRLException("The data collection could not be deleted.",e);
		}
	}	
	
    /**
     * @see org.xbrlapi.data.Store#persist(XML)
     */
    public synchronized int getSize() throws XBRLException {
        try {
            return this.collection.getResourceCount();
        } catch (XMLDBException e) {
            throw new XBRLException("Failed to get the number of fragments in the data store.",e);
        }
    }	

    /**
	 * Add a fragment to the DTS.  Sets the fragment builder to null because it
	 * is no longer required, stores the fragment data and metadata in the 
	 * data store, sets the store property in the fragment.
	 * @param xml The fragment to be added to the DTS store.
	 * @throws XBRLException if the fragment cannot be added to the store 
	 * (eg: because one with the same index is already in the store).
	 */
    public synchronized void persist(XML xml) throws XBRLException {

		if (xml == null) throw new XBRLException("The fragment is null so it cannot be added.");
		String index = xml.getIndex();
		
		if (hasXML(index)) {
            this.remove(index);
        }

        if (xml.getStore() != null) {
	    	try {
	    		XMLResource resource = (XMLResource) collection.createResource(index, XMLResource.RESOURCE_TYPE);
		        resource.setContent(DOM2String(xml.getMetadataRootElement()));
		        collection.storeResource(resource);
	        } catch (XMLDBException e) {
	        	throw new XBRLException("The fragment data could not be added to the eXist data store.", e);
	        }			
			return;
		}
		
		try {
			XMLResource resource = (XMLResource) collection.createResource(index, XMLResource.RESOURCE_TYPE);
            resource.setContent(DOM2String(xml.getBuilder().getMetadata()));
	        collection.storeResource(resource);
        } catch (XMLDBException e) {
        	throw new XBRLException("The fragment data could not be added to the eXist data store.", e);
        }

        // Finalise the fragment, ready for use
        xml.setResource(xml.getBuilder().getMetadata());
        xml.setStore(this);

	}
	
    /**
     * Test if a store contains a specific fragment, as identified by
     * its index.
     * @param index The index of the fragment to test for.
     * @return true iff the store contains a fragment with the specified 
     * fragment index.
     * @throws XBRLException If the test cannot be conducted.
     */
	public synchronized boolean hasXML(String index) throws XBRLException {
    	if (getFragment(index) == null) {
    		return false;
    	}
    	return true;
    }
	
    /**
     * Retrieves a fragment from an XBRL API data store.
     * The fragment will be created as a fragment of the original 
     * fragment type but will be returned as a straight fragment.
     * @param index The index of the fragment.
     * @return The fragment corresponding to the specified index or null if 
     * the fragment is not in the store.
     * @throws XBRLException if the fragment cannot be retrieved.
     */
    public synchronized <F extends XML> F getFragment(String index) throws XBRLException {
    	try {
    		XMLResource resource = (XMLResource) collection.getResource(index);
    		if (resource == null) return null;
    		Element root = getResourceRootElement(resource);
    		return FragmentFactory.<F>newFragment(this, root);
    	} catch (XMLDBException e) {
    		throw new XBRLException("The fragment with index " + index + " could not be retrieved.",e);
    	}
    }
    
    /**
     * @param resource The resource to get the root element from.
     * @return the root element of the resource.
     * @throws XMLDBException
     */
    private Element getResourceRootElement(XMLResource resource) throws XMLDBException {
		Node node = resource.getContentAsDOM();
    	if (node.getNodeType() == Element.DOCUMENT_NODE) {
    		return ((Document) node).getDocumentElement();
    	}
		return (Element) node;
    }
    


	/**
	 * Remove a fragment from the DTS.  
	 * If a fragment with the same ID does not already exist in the 
	 * DTs then no action is required.
	 * @param index The index of the fragment to be removed from the DTS store.
	 * @throws XBRLException if the fragment cannot be removed from the store.
	 */
    public synchronized void remove(String index) throws XBRLException {
        try {
            if (!hasXML(index)) {
            	return;
            }

            Resource document = collection.getResource(index);
            collection.removeResource(document);
        
        }
        catch (XMLDBException e) {
        	throw new XBRLException("The fragment removal failed.", e);
        }
	}
	



	

	
	/**
	 * @see org.xbrlapi.data.Store#query(String)
	 */
    @SuppressWarnings(value = "unchecked")
	public synchronized <F extends XML> List<F> query(String query) throws XBRLException {
		
        String roots = "/*" + this.getURIFilteringPredicate();
        query = query.replaceAll("#roots#",roots);
        
		ResourceSet resources = null;
		try {
	        for (URI namespace: this.namespaceBindings.keySet()) 
	            xpathService.setNamespace(this.namespaceBindings.get(namespace), namespace.toString());
			resources = xpathService.query(query);
		} catch (XMLDBException e) {
			throw new XBRLException("The query service failed.", e);
		}

		List<F> fragments = new Vector<F>();
		try {
			ResourceIterator iterator = resources.getIterator();
			while (iterator.hasMoreResources()) {
				Element root = getResourceRootElement((XMLResource) iterator.nextResource());
				fragments.add((F) FragmentFactory.newFragment(this, root));
			}
		} catch (XMLDBException e) {
			throw new XBRLException("The query failed.", e);
		}
		return fragments;
	}
    
    /**
     * @see org.xbrlapi.data.Store#queryCount(String)
     */
    public synchronized long queryCount(String query) throws XBRLException {
        
        String roots = "/*" + this.getURIFilteringPredicate();
        query = query.replaceAll("#roots#",roots);
        
        ResourceSet resources = null;
        try {
            for (URI namespace: this.namespaceBindings.keySet()) 
                xpathService.setNamespace(this.namespaceBindings.get(namespace), namespace.toString());
            resources = xpathService.query(query);
            return resources.getSize();
        } catch (XMLDBException e) {
            throw new XBRLException("The query service failed.", e);
        }

    }    
    
    /**
     * @see org.xbrlapi.data.Store#queryForIndices(String)
     */
    public synchronized Set<String> queryForIndices(String query) throws XBRLException {

        String roots = "/*" + this.getURIFilteringPredicate();
        query = query.replaceAll("#roots#",roots);
        
        ResourceSet resources = null;
        try {
            for (URI namespace: this.namespaceBindings.keySet()) 
                xpathService.setNamespace(this.namespaceBindings.get(namespace), namespace.toString());
            resources = xpathService.query(query);
        } catch (XMLDBException e) {
            throw new XBRLException("The XPath query service failed.", e);
        }

        Set<String> indices = new TreeSet<String>();
        try {
            ResourceIterator iterator = resources.getIterator();
            String regex = "<xbrlapi:fragment.*? index=\"(\\w+)\".*?>";
            Pattern pattern = Pattern.compile(regex,Pattern.DOTALL);
            while (iterator.hasMoreResources()) {
                XMLResource resource = (XMLResource) iterator.nextResource();
                String string = ((String) resource.getContent()).replaceAll("\n"," ");
                Matcher matcher = pattern.matcher(string);
                matcher.matches();
                String index = matcher.group(1);
                indices.add(index);
            }
        } catch (XMLDBException e) {
            throw new XBRLException("The query failed.", e);
        }
        return indices;
    }
    
    /**
     * @see org.xbrlapi.data.Store#queryForStrings(String)
     */
    public synchronized Set<String> queryForStrings(String query) throws XBRLException {

        String roots = "/*" + this.getURIFilteringPredicate();
        query = query.replaceAll("#roots#",roots);
        
        ResourceSet resources = null;
        try {
            for (URI namespace: this.namespaceBindings.keySet()) 
                xpathService.setNamespace(this.namespaceBindings.get(namespace), namespace.toString());
            resources = xpathService.query(query + "/string()");
        } catch (XMLDBException e) {
            throw new XBRLException("The XPath query service failed.", e);
        }

        Set<String> strings = new TreeSet<String>();
        try {
            ResourceIterator iterator = resources.getIterator();
            while (iterator.hasMoreResources()) {
                XMLResource resource = (XMLResource) iterator.nextResource();
                strings.add((String) resource.getContent());
            }
        } catch (XMLDBException e) {
            throw new XBRLException("The query failed.", e);
        }
        return strings;

    }    
	
	/**
	 * Run a query against the collection of all fragments in the store.
	 * @param query The XPath query to run against the set of fragments.
	 * @param fragments The list of fragments to be populated with the query results.
	 * @return a list of matching fragments or the empty list if no matching fragments
	 * exist.
	 * @throws XBRLException if the query cannot be executed.
	 */
/*	public <F extends Fragment> void query(String query) throws XBRLException {
		ResourceSet resources = null;
		try {
			resources = xpathService.query(query);
		} catch (XMLDBException e) {
			throw new XBRLException("The XPath query service failed.", e);
		}
		try {
			ResourceIterator iterator = resources.getIterator();
			while (iterator.hasMoreResources()) {
				XMLResource resource = (XMLResource) iterator.nextResource();
				fragments.add((F) FragmentFactory.newFragment(this, resource));
			}
		} catch (XMLDBException e) {
			throw new XBRLException("The XPath query of the DTS failed.", e);
		}
		return fragments;		
	}*/
	
    
	/**
	 * @param resource The XMLResource to be used to get the DOM document node.
	 * @return the org.w3.Document node from an XMLResource returned by the
	 * data store or null if no document node is available.
	 * @throws XBRLException if the resource content is not available as a DOM object.
	 */
	private Document getDocumentNode(XMLResource resource) throws XBRLException {
		Node node = null;
		try {
			node = resource.getContentAsDOM();
		} catch (XMLDBException e) {
			throw new XBRLException("The resource content was not available as a DOM object.", e);
		}
		
    	if (node.getNodeType() == Element.DOCUMENT_NODE) {
    		return (Document) node;
    	}
		return ((Element) node).getOwnerDocument();  // May return a null value!
	}    
 
    
    
}