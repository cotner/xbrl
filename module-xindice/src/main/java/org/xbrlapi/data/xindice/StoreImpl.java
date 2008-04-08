package org.xbrlapi.data.xindice;

import org.apache.xindice.client.xmldb.services.CollectionManager;
import org.apache.xindice.xml.dom.DocumentImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.data.XBRLStore;
import org.xbrlapi.data.XBRLStoreImpl;
import org.xbrlapi.impl.FragmentFactory;
import org.xbrlapi.impl.FragmentListImpl;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XPathQueryService;

/**
 * Implementation of the Xindice based data store for the XBRLAPI.
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
*/
public class StoreImpl extends XBRLStoreImpl implements XBRLStore {
    
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
	private CollectionManager manager = null;
	
	/**
	 * The XPath query service for the data collection.
	 */
    XPathQueryService xpathService = null;
    
	/**
	 * Initialise the database connection.
	 * 
	 * @param connection The live connection to the Xindice database.
	 * @param storeParentPath The full path to the container collection, which is the
	 * collection that will hold the data collection when it are created.
	 * @param dataCollectionName The name of the data collection.
	 * @throws XBRLException if the database connection is null or if the data or metadata collections
	 * could not be created or accessed or the indexes could not be created or the XPath services
	 * could not be established or if the storeParentPath does not end in a delimiter: /.
	 */
	public StoreImpl(
			DBConnectionImpl connection,
			String storeParentPath,
			String dataCollectionName
			) throws XBRLException {

		this.connection = connection;
		
		if (storeParentPath.charAt(storeParentPath.length()-1) != '/') {
			storeParentPath = storeParentPath + "/";
		}

		if (connection == null) {
			throw new XBRLException("The Xindice database connection is null so no data store can be established.");
		}

		Collection parentCollection = connection.getCollection(storeParentPath);
		
		if (parentCollection == null) {
			throw new XBRLException("The collection to contain the data store does not exist.");
		}		
		
		boolean storeExists = false;
		if (connection.hasCollection(dataCollectionName,parentCollection)) {
			storeExists = true;
			collection = connection.getCollection(dataCollectionName, parentCollection);
		} else {
			collection = connection.createCollection(dataCollectionName,parentCollection);
		}
		
		if (collection == null) {
			throw new XBRLException("The data collection " + storeParentPath + "/" + dataCollectionName + " could not be created.");
		}
		
		try {
			manager = (CollectionManager) collection.getService("CollectionManager", "1.0");
		} catch (XMLDBException e) {
			throw new XBRLException("The collection manager could not be instantiated.",e);
		}

        try {
            xpathService = (XPathQueryService) collection.getService("XPathQueryService","1.0");
            xpathService.setNamespace(Constants.XMLPrefix, Constants.XMLNamespace);
            xpathService.setNamespace(Constants.XBRLAPIPrefix, Constants.XBRLAPINamespace);
            xpathService.setNamespace(Constants.XLinkPrefix, Constants.XLinkNamespace);
            xpathService.setNamespace(Constants.XMLSchemaPrefix, Constants.XMLSchemaNamespace);
            xpathService.setNamespace(Constants.XBRL21Prefix, Constants.XBRL21Namespace);
            xpathService.setNamespace(Constants.XBRL21LinkPrefix, Constants.XBRL21LinkNamespace);
            xpathService.setNamespace(Constants.XBRLAPILanguagesPrefix, Constants.XBRLAPILanguagesNamespace);
            // TODO add means for users to add their own namespace declarations to the data store xpath services
            
        } catch (XMLDBException e) {
            throw new XBRLException("The XPath services could not be initialised.",e);
        }

        if (! storeExists) {
            this.addIndex("stub","value","@stub");
            this.addIndex("name","value","@name");
            this.addIndex("type","value","@type");
            this.addIndex("id","value","@id");
            this.addIndex("url","value","@url");
            this.addIndex("xpointerExpression","value","@expression");
            this.addIndex("parentIndex","value","@parentIndex");
        }   
		
		
	}
    
	/**
	 * Close the data store by closing the data and metadata collections.
	 * Throws XBRLException if the collections cannot be closed.
	 */
	public void close() throws XBRLException {
		try {
			collection.close();
		} catch (XMLDBException e) {
			throw new XBRLException("The data collection could not be closed.",e);
		}
	}
	
	/**
	 * @see org.xbrlapi.data.Store#delete()
	 */
	public void delete() throws XBRLException {
		try {
			Collection parent = collection.getParentCollection();
			String collectionName = collection.getName();
			connection.deleteCollection(collectionName,parent);
			collection.close();
			connection = null;
		} catch (XMLDBException e) {
			throw new XBRLException("The data collection could not be deleted.",e);
		}
	}
	
	/**
	 * Add a fragment to the data store.
	 * 
	 * The addition of a fragment to the data store involves:
	 * 1. Creating resources to hold the XML data and metadata
	 * 2. Serialising the data and the metadata from the fragment builder.
	 * 3. Storing the data and metadata in their respective resources.
	 * 4. Adding the data and metadata resources to the data store.
	 * 5. Getting the index to use for the new fragment being added.
	 * 6. Replaces the builder in the fragment with the new data and metadata resources.
	 * 7. Informs the fragment being stored that it is stored in this data store.
	 * 
	 * TODO Modify Xindice StoreImpl so that existing fragments in the data store can be replaced.
	 * 
	 * @param fragment The fragment to be added to the DTS store.
	 * @throws XBRLException if the fragment is in the store already or
	 * the process of inserting its data and metadata fails or if the fragment
	 * properties cannot be updated as required to reflect that its data has been
	 * stored.
	 */
	public void storeFragment(Fragment fragment) throws XBRLException {

		if (fragment == null) throw new XBRLException("The fragment is null so it cannot be added.");
		String index = fragment.getFragmentIndex();

		if (hasFragment(index)) {
            this.removeFragment(index);
        }

        if (fragment.getStore() != null) {
	    	try {
	    		XMLResource resource = (XMLResource) collection.createResource(index, XMLResource.RESOURCE_TYPE);
		        resource.setContent(DOM2String(fragment.getMetadataRootElement()));
		        collection.storeResource(resource);
	        } catch (XMLDBException e) {
	        	throw new XBRLException("The fragment data could not be added to the eXist data store.", e);
	        }			
			return;
		}
		
		try {
			XMLResource resource = (XMLResource) collection.createResource(index, XMLResource.RESOURCE_TYPE);
            resource.setContent(DOM2String(fragment.getBuilder().getMetadata()));
	        collection.storeResource(resource);
        } catch (XMLDBException e) {
        	throw new XBRLException("The fragment data could not be added to the eXist data store.", e);
        }

        // Finalise the fragment, ready for use
        fragment.setResource(fragment.getBuilder().getMetadata());
        fragment.setStore(this);

	}
	
    /**
     * Test if a store contains a specific fragment, as identified by
     * its index.
     * @param index The index of the fragment to test for.
     * @return true iff the store contains a fragment with the specified 
     * fragment index.
     * @throws XBRLException If the test cannot be conducted.
     */
    public boolean hasFragment(String index) throws XBRLException {
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
    public Fragment getFragment(String index) throws XBRLException {
    	try {
    		XMLResource resource = (XMLResource) collection.getResource(index);
    		if (resource == null) return null;
    		Element root = getResourceRootElement(resource);
    		
    		return FragmentFactory.newFragment(this, root);
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
    	} else {
    		return (Element) node;
    	}
    }    
    


	/**
	 * Remove a fragment from the DTS.  
	 * If a fragment with the same ID does not already exist in the 
	 * DTs then no action is required.
	 * @param index The index of the fragment to be removed from the DTS store.
	 * @throws XBRLException if the fragment cannot be removed from the store.
	 */
	public void removeFragment(String index) throws XBRLException {
        try {
            if (! hasFragment(index)) {
            	return;
            }

            collection.removeResource(collection.getResource(index));

        } catch (XMLDBException e) {
        	throw new XBRLException("The fragment removal from the underlying Xindice store failed.", e);
        }
	}

	/**
	 * Run a query against the collection of all fragments in the DTS.
	 * @param query The XPath query to run against the set of fragments in the
	 * DTS.
	 * @return a fragment list that contains each matching fragment.
	 * @throws XBRLException if the query cannot be executed.
	 */
    @SuppressWarnings(value = "unchecked")
	public <F extends Fragment> FragmentList<F> query(String query) throws XBRLException {
        
		ResourceSet resources = null;
		try {
            for (String namespace: this.namespaceBindings.keySet()) 
                xpathService.setNamespace(this.namespaceBindings.get(namespace), namespace);
			resources = xpathService.query(query);
		} catch (XMLDBException e) {
			throw new XBRLException("The XPath query service failed.", e);
		}

		FragmentList<F> fragments = new FragmentListImpl<F>();
		try {
			ResourceIterator iterator = resources.getIterator();
			while (iterator.hasMoreResources()) {
				Element root = getResourceRootElement((XMLResource) iterator.nextResource());
				// TODO Figure out how to eliminate this warning.
				fragments.addFragment((F) FragmentFactory.newFragment(this, root));
			}
		} catch (XMLDBException e) {
			throw new XBRLException("The XPath query of the DTS failed.", e);
		}
		return fragments;
    	
	}
	
	/**
 	 * Adds an index to the specified collection (used my data and metadata).
	 * @param m The collection manager to be used to add the index.
	 * @param name The name of the index.
	 * @param type The type of the index ('name' or 'value').
	 * @param pattern The pattern for the index (a bit like the match attribute value in XSLT template elements.).
	 * @throws XBRLException if the index cannot be added.
	 */
	private void addIndex(
			CollectionManager m,
			String name,
			String type,
			String pattern
			) throws XBRLException {
		try {

            // Create the document that defines the index to be added
            Document document = new DocumentImpl();
            Element element = document.createElement("index");
            document.appendChild(element);

            // TODO throw an error if a store index addition overwrites an existing index
            
            // Specify the index name
            if (name == null) {
            	throw new XBRLException("The index name was null.");
            } else {
            	element.setAttribute("name", name);
            }
            
            // Specify the type of index
            if (type.equalsIgnoreCase("name")) {
                element.setAttribute("class", "org.apache.xindice.core.indexer.NameIndexer");
            } else if (type.equalsIgnoreCase("value")) {
                element.setAttribute("class", "org.apache.xindice.core.indexer.ValueIndexer");
            } else {
            	throw new XBRLException("The index type must be one of 'name' or 'value' instead of " + type + ".");
            }
            
            // Specify the index pattern
            if (pattern == null) {
            	throw new XBRLException("The index pattern was null.");
            } else {
            	element.setAttribute("pattern", pattern);
            }

	        // Create the indexer for this collection manager
            if (m == null) {
            	throw new XBRLException("The collection manager is inappropriately null.");
            }
	        m.createIndexer(document);

	    } catch (XMLDBException e) {
	    	throw new XBRLException("The index could not be added.",e);
	    }
	}
	
	/**
	 * Adds an index to the collection containing the DTS.
	 * @param name The name of the index.
	 * @param type The type of the index ('name' or 'value').
	 * @param pattern The pattern for the index (like the match attribute value in XSLT template elements.).
	 * @throws XBRLException if the index cannot be added.
	 */
	public void addIndex(String name, String type, String pattern) throws XBRLException {
		addIndex(manager,name,type,pattern);	
	}

	/**
	 * Deletes an index from a data or metadata collection in the data store.
	 * @param m The collection manager to be used to delete the index.
	 * @param name The name of the index to be deleted.
	 * @throws XBRLException if the index cannot be added.
	 */
	private void deleteIndex(
			CollectionManager m,
			String name) throws XBRLException {
		try {
	        
			// TODO check that deletion of a non-existing index from a DTS data store is handled correctly.
			
			if (name == null) {
	        	throw new XBRLException("The name of the index to be deleted must be specified but was null.");
	        }	        
	        m.dropIndexer(name);
	        
		} catch (XMLDBException e) {
			throw new XBRLException("The index could not be deleted.",e);
		}
	}
	
	/**
	 * Deletes an index to the collection containing the DTS.
	 * @param name The name of the index to be deleted.
	 * @throws XBRLException if the index cannot be added.
	 */
	public void deleteIndex(String name) throws XBRLException {
		deleteIndex(manager,name);
	}

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
    	} else {
    		return ((Element) node).getOwnerDocument();  // May return a null value!
    	}
	}
    
}