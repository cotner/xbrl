package org.xbrlapi.data.bdbxml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.data.XBRLStore;
import org.xbrlapi.data.XBRLStoreImpl;
import org.xbrlapi.impl.FragmentFactory;
import org.xbrlapi.impl.FragmentListImpl;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.utilities.XMLDOMBuilder;

import com.sleepycat.db.CheckpointConfig;
import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.Environment;
import com.sleepycat.db.EnvironmentConfig;
import com.sleepycat.dbxml.XmlContainer;
import com.sleepycat.dbxml.XmlDocument;
import com.sleepycat.dbxml.XmlDocumentConfig;
import com.sleepycat.dbxml.XmlException;
import com.sleepycat.dbxml.XmlIndexSpecification;
import com.sleepycat.dbxml.XmlInputStream;
import com.sleepycat.dbxml.XmlManager;
import com.sleepycat.dbxml.XmlManagerConfig;
import com.sleepycat.dbxml.XmlQueryContext;
import com.sleepycat.dbxml.XmlResults;
import com.sleepycat.dbxml.XmlUpdateContext;
import com.sleepycat.dbxml.XmlValue;

/**
 * Implementation of the data store using the Oracle 
 * Berkeley Database.  Note that this store implementation
 * does not use the XML:DB API and so does not require
 * a DBConnection implementation.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class StoreImpl extends XBRLStoreImpl implements XBRLStore {

	private Environment environment = null;
	private XmlManagerConfig managerConfiguration = null;
	private XmlManager dataManager = null;
	private XMLDOMBuilder builder = new XMLDOMBuilder();
	private XmlContainer dataContainer = null;
    private XmlUpdateContext xmlUpdateContext = null;
    private XmlDocumentConfig documentConfiguration = null;
	private XmlQueryContext xmlQueryContext = null;
	private CheckpointConfig checkpointConfig = null;
	
	/**
	 * The next fragmentId.
	 */
	String nextId = "1";

    /**
     * Fragment storage counter
     */
    private int inserts = 0;
    private int MAX_INSERTS = 10000;
	
	/**
	 * Initialise the BDB XML database data store.
	 * @param location The location of the database (The path to where the database exists)
	 * @param container The name of the container to hold the data in the store.
	 * @throws XBRLException
	 */
	public StoreImpl(String location, String container) throws XBRLException {
		
	    EnvironmentConfig environmentConfiguration = new EnvironmentConfig();
	    environmentConfiguration.setAllowCreate(true);         // If the environment does not exist, create it.
        environmentConfiguration.setInitializeLocking(true);   // Turn on the locking subsystem.
        environmentConfiguration.setErrorStream(System.err);   // Capture error information in more detail.
        environmentConfiguration.setInitializeCache(true);
        environmentConfiguration.setCacheSize(1024 * 1024 * 50);
        environmentConfiguration.setInitializeLogging(true);   // Turn off the logging subsystem.
	    environmentConfiguration.setTransactional(true);       // Turn on the transactional subsystem.
	    //environmentConfiguration.setPrivate(true);
	    //environmentConfiguration.setSystemMemory(true);

	    try {
		    environment = new Environment(new File(location), environmentConfiguration);
		} catch (FileNotFoundException e) {
			throw new XBRLException("The physical location of the BDB XML database could not be found.", e);
		} catch (DatabaseException e) {
			throw new XBRLException("The BDB XML database environment could not be set up.", e);
		} 
		
		managerConfiguration = new XmlManagerConfig();
	    managerConfiguration.setAdoptEnvironment(true);
	    managerConfiguration.setAllowExternalAccess(true);
		
	    try {
	    	dataManager = new XmlManager(environment, managerConfiguration);
/*		    XmlManager.setLogLevel(XmlManager.LEVEL_ALL, true);
		    XmlManager.setLogCategory(XmlManager.CATEGORY_ALL, true);		    
*/	    
			// Set up the database document write configuration for the store
			xmlUpdateContext = dataManager.createUpdateContext();
			documentConfiguration = new XmlDocumentConfig();
			documentConfiguration.setWellFormedOnly(true);
			checkpointConfig = new CheckpointConfig();
			
			xmlQueryContext = dataManager.createQueryContext();
			xmlQueryContext.setReturnType(XmlQueryContext.DeadValues);
			xmlQueryContext.setNamespace(Constants.XLinkPrefix, Constants.XLinkNamespace);
			xmlQueryContext.setNamespace(Constants.XMLSchemaPrefix, Constants.XMLSchemaNamespace);
			xmlQueryContext.setNamespace(Constants.XBRL21Prefix, Constants.XBRL21Namespace);
			xmlQueryContext.setNamespace(Constants.XBRL21LinkPrefix, Constants.XBRL21LinkNamespace);
			xmlQueryContext.setNamespace(Constants.XBRLAPIPrefix, Constants.XBRLAPINamespace);
			xmlQueryContext.setNamespace(Constants.XBRLAPILanguagesPrefix, Constants.XBRLAPILanguagesNamespace);

	    } catch (XmlException e) {
			throw new XBRLException("The BDB XML database manager could not be set up.", e);
		}

    	// Create or open the data container and check its storage approach is node based (not document based).
	    try {
			if (dataManager.existsContainer(container) != 0) {
				dataContainer = dataManager.openContainer(container);
		    	if (dataContainer.getContainerType() != XmlContainer.NodeContainer)
		    		throw new XBRLException("The container must be use the node format for data storage.");
			} else {
				dataContainer = dataManager.createContainer(container);
				
			}

		    // Configure the indexes for the data store if not done already.
			addIndex("node-element-presence-none", Constants.XBRLAPIPrefix,"data");
			addIndex("node-element-presence-none", Constants.XBRLAPIPrefix,"fragment");
            addIndex("node-element-presence-none", Constants.XBRLAPIPrefix,"xptr");
			addIndex("node-attribute-equality-string", "","parentIndex");
			addIndex("node-attribute-equality-string", "","url");
			addIndex("node-attribute-equality-string", "","type");
			addIndex("node-attribute-equality-string", "","id");
			addIndex("node-attribute-equality-string", "","targetDocumentURL");
			addIndex("node-attribute-equality-string", "","targetPointerValue");
			addIndex("node-attribute-equality-string", "","absoluteHref");
			addIndex("node-attribute-equality-string", "","value");
			addIndex("node-attribute-equality-string", "","arcroleURI");
			addIndex("node-attribute-equality-string", "","roleURI");
			addIndex("node-attribute-equality-string", "","name");
			addIndex("node-attribute-equality-string", "","targetNamespace");
			addIndex("node-attribute-equality-string", Constants.XLinkNamespace,"label");
			addIndex("node-attribute-equality-string", Constants.XLinkNamespace,"from");
			addIndex("node-attribute-equality-string", Constants.XLinkNamespace,"to");
			addIndex("node-attribute-equality-string", Constants.XLinkNamespace,"type");
			addIndex("node-attribute-equality-string", Constants.XLinkNamespace,"arcrole");
			addIndex("node-attribute-equality-string", Constants.XLinkNamespace,"role");

			addIndex("node-element-presence-none", Constants.XBRLAPILanguagesNamespace,"language");
			addIndex("node-element-equality-string", Constants.XBRLAPILanguagesNamespace,"code");
			addIndex("node-element-equality-string", Constants.XBRLAPILanguagesNamespace,"value");
			addIndex("node-element-equality-string", Constants.XBRLAPILanguagesNamespace,"encoding");

			// Set the default element presence index.
			try {
				XmlIndexSpecification indexSpecification = dataContainer.getIndexSpecification();
				indexSpecification.addDefaultIndex("node-element-presence-none");			
				XmlUpdateContext xmlUpdateContext = dataManager.createUpdateContext();
			    dataContainer.setIndexSpecification(indexSpecification, xmlUpdateContext);
			    indexSpecification.delete();
			    xmlUpdateContext.delete();
			} catch (XmlException e) {
				throw new XBRLException("The default index could not be set.", e);
			}
			
            try {
                this.getStoreState();
            } catch (XBRLException e) {
                this.storeLoaderState("0",new LinkedList<String>());
            }			
			
	    } catch (XmlException e) {
			throw new XBRLException("The BDB XML database container could not be opened.", e);
	    }
	    
	}
	
	private void addIndex(int indexType, int syntaxType, String namespace, String localname) throws XBRLException {
		try {
			XmlIndexSpecification indexSpecification = dataContainer.getIndexSpecification();
			indexSpecification.addIndex(namespace, localname, indexType, syntaxType);
		    dataContainer.setIndexSpecification(indexSpecification, xmlUpdateContext);
		    indexSpecification.delete();
		} catch (XmlException e) {
			throw new XBRLException("The index could not be set.", e);
		}
	}
	
	private void addIndex(String type, String namespace, String localname) throws XBRLException {
		try {
			XmlIndexSpecification indexSpecification = dataContainer.getIndexSpecification();
			indexSpecification.addIndex(namespace, localname, type);
		    dataContainer.setIndexSpecification(indexSpecification, xmlUpdateContext);
		    indexSpecification.delete();
		} catch (XmlException e) {
			throw new XBRLException("The index could not be set.", e);
		}
	}	

	/**
	 * The closure operation entails closing the XML container and the XML data manager
	 * that are used by the store.
	 * @see org.xbrlapi.data.Store#close()
	 */
	public void close() throws XBRLException {
		super.close();
		try {
			if (dataContainer != null) {
				logger.debug("Closing a store with name " + dataContainer.getName());
	            xmlQueryContext.delete();
	            xmlUpdateContext.delete();
				dataContainer.sync();
				dataContainer.close();
				dataContainer.delete();
			}
		} catch (XmlException e) {
			throw new XBRLException("The BDB XML database container could not be closed cleanly.",e);
		}
		try {
			if (dataManager != null) {
				dataManager.close();
				dataManager.delete();
			}
		} catch (XmlException e) {
			throw new XBRLException("The BDB XML database manager could not be closed cleanly.",e);
		}
	}
	
	/**
	 * TODO Provide unit tests for BDBXML data collection deletion.
	 * @see org.xbrlapi.data.Store#delete()
	 */
	public void delete() throws XBRLException {

		// Get the name of the data container to be removed.
		String dataContainerName = null;
		try {
			if (dataContainer != null) dataContainerName = dataContainer.getName();
			else throw new XBRLException("The data container to be deleted is null.");
		} catch (XmlException e) {
			throw new XBRLException("The BDB XML database container name could not be obtained.",e);
		}
		
		// Close the data container before removing it.

		try {
			if (dataContainer != null) {
	            xmlQueryContext.delete();
	            xmlUpdateContext.delete();
				dataContainer.sync();
				dataContainer.close();
				dataContainer.delete();
				dataContainer = null;
			}
		} catch (XmlException e) {
			throw new XBRLException("The BDB XML database container could not be closed cleanly.",e);
		}

		// Remove the data container, thus destroying its data.
		try {
			if (dataManager != null) {
				if (dataManager.existsContainer(dataContainerName) != 0) {
			    	dataManager.removeContainer(dataContainerName);
				}
			} else {
				throw new XBRLException("The data manager was null when an attempt to delete a data store was attempted.");
			}
		} catch (XmlException e) {
			throw new XBRLException("The BDB XML database manager could not be closed cleanly.",e);
		}

		// Close the data store.
		this.close();
	}
	
	/**
	 * @see org.xbrlapi.data.Store#storeFragment(Fragment)
	 */
	public void storeFragment(Fragment fragment) throws XBRLException {
		
		if (fragment.getStore() != null) {
			return;
		}
		
		String index = fragment.getFragmentIndex();
		
		if (hasFragment(index)) {
		    this.removeFragment(index);
		}

		try {

			String xmlString = serializeToString(fragment.getMetadataRootElement());
			InputStream inputStream = new ByteArrayInputStream(xmlString.getBytes());			
			XmlInputStream xmlInputStream = dataManager.createInputStream(inputStream);
			dataContainer.putDocument(index, xmlInputStream, xmlUpdateContext, documentConfiguration);
			xmlInputStream.delete();
			this.inserts++;
			if (inserts > this.MAX_INSERTS) {
			    try {
			        this.environment.checkpoint(checkpointConfig);
			    } catch (DatabaseException e) {
		            throw new XBRLException("The database checkpoint operation failed.", e);
			    }
			}
			
            // Finalise the fragment, ready for use
            fragment.setResource(fragment.getBuilder().getMetadata());
            fragment.setStore(this);
		
		} catch (XmlException e) {
        	throw new XBRLException("The fragment could not be added to the BDB XML data store.", e);
        }

	}

	/**
	 * @see org.xbrlapi.data.Store#hasFragment(String)
	 */
	public boolean hasFragment(String index) throws XBRLException {
		try {
			XmlDocument xmlDocument = dataContainer.getDocument(index);
			if (xmlDocument != null) {
				xmlDocument.delete();
				return true;
			}
			xmlDocument.delete();
			return false;
		} catch (XmlException e) {
			return false;
		}
	}
	
    /**
     * Overrides the base implementation to avoid the overhead of creating the
     * various fragment objects.
     * @see org.xbrlapi.data.Store#hasDocument(String)
     */
    public boolean hasDocument(String url) throws XBRLException {
    	XmlResults results = performQuery("/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@url='" + url + "']");
    	
    	try {
    		boolean result = (results.size() > 0); 
    		results.delete();
        	return  result;
    	} catch (XmlException e) {
    		throw new XBRLException("The size of the result set for the query could not be determined.", e);
    	} finally {
    	}
    }    	

	/**
	 * @see org.xbrlapi.data.Store#getFragment(String)
	 */
	public Fragment getFragment(String index) throws XBRLException {
		
		// Get the Document from the container.
		XmlDocument xmlDocument = null;
		try {
			xmlDocument = dataContainer.getDocument(index);
		} catch (XmlException e) {
			throw new XBRLException("The fragment " + index + " could not be retrieved from the store.",e);
		}
		if (xmlDocument == null) throw new XBRLException("The fragment " + index + " could not be retrieved from the store.");
		
		// Generate the DOM representation of the fragment, setting the root to the root element thereof.
		Document document = null;
		try {
			document = XMLDOMBuilder.newDocument(xmlDocument.getContentAsInputStream());
		} catch (XmlException e) {
			xmlDocument.delete();
			throw new XBRLException("The fragment content was not available as an input stream from the store.",e);
		}
		xmlDocument.delete();

		// Build the fragment using the fragment factory
		return FragmentFactory.newFragment(this, document.getDocumentElement());
	}

	/**
	 * @see org.xbrlapi.data.Store#removeFragment(String)
	 */
	public void removeFragment(Fragment fragment) throws XBRLException {
		removeFragment(fragment.getFragmentIndex());
	}

	/**
	 * Remove a fragment from the store.
	 * 
	 * @param index
	 *            The index of the fragment to be removed from the store.
	 * @throws XBRLException
	 *             if the fragment exists but cannot be removed from the store.
	 */
	public void removeFragment(String index) throws XBRLException {
		try {
			dataContainer.deleteDocument(index,xmlUpdateContext);
		} catch (XmlException e) {
			e.printStackTrace();
			throw new XBRLException("The fragment removal failed.", e);
		}	
	}





	/**
	 * @see org.xbrlapi.data.Store#query(String)
	 */
    @SuppressWarnings(value = "unchecked")
	public <F extends Fragment> FragmentList<F> query(String myQuery) throws XBRLException {
		try {
			
			double startTime = System.currentTimeMillis();

			// TODO Make sure that queries finding a node within a fragment return the fragment itself.
			
			XmlResults results = performQuery(myQuery);
			
			Double time = new Double((System.currentTimeMillis()-startTime));
			logger.debug(myQuery);
			logger.debug("Average time for query = " + time + " milliseconds");

			XmlValue value = results.next();
			FragmentList<F> fragments = new FragmentListImpl<F>();
		    while (value != null) {
		        XmlDocument theDoc = value.asDocument();
		        // TODO determine why the BDB XML query result cannot be accessed as an input stream.
		        //Document document = builder.newDocument(theDoc.getContentAsInputStream());
		        Document document = XMLDOMBuilder.newDocument(value.asString());
		        Element root = document.getDocumentElement();
				fragments.addFragment((F) FragmentFactory.newFragment(this, root));
		        value = results.next();
		     }
	    	
			// Clean up
			results.delete();
			return fragments;

		} catch (XmlException e) {
			throw new XBRLException("Failed query: " + myQuery,e);
		}
	}
    
    /**
     * Provides direct access to the query mechanism so that we can use
     * queries without constructing fragments for each query result.
     * @param myQuery The query to be performed.
     * @return the results of the query.
     * @throws XBRLException if the query fails to execute.
     */
	private XmlResults performQuery(String myQuery) throws XBRLException {
		
		try {
			
		    String query = "collection('" + dataContainer.getName() + "')" + myQuery;
		    // TODO provide a means of adding additional namespaces for querying.
		    // TODO provide a means of investigating namespace bindings for the query configuration.
			
		    long startTime = System.currentTimeMillis();
		    
		    XmlResults results = dataManager.query(query,xmlQueryContext);
		    
		    long endTime = System.currentTimeMillis();
		    
		    logger.debug((endTime-startTime) + " milliseconds to run: " + query);

			return results;

		} catch (XmlException e) {
			throw new XBRLException("Failed query: " + myQuery,e);
		}
	}
	






}