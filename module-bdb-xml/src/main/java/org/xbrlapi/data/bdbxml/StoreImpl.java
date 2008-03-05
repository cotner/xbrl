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
	 * Initialise the BDB XML database data store.
	 * @param location The location of the database (The path to where the database exists)
	 * @param container The name of the container to hold the data in the store.
	 * @throws XBRLException
	 */
	public StoreImpl(String location, String container) throws XBRLException {

        // Set the log level for the database manager
        try {
            XmlManager.setLogLevel(XmlManager.LEVEL_ALL, false);
            XmlManager.setLogCategory(XmlManager.CATEGORY_ALL, false);          
        } catch (XmlException e) {
            throw new XBRLException("The BDB XML log levels could not be initialised.", e);
        }

	    // Generate the database environment
	    try {
	        EnvironmentConfig environmentConfiguration = new EnvironmentConfig();
	        environmentConfiguration.setAllowCreate(true);         // If the environment does not exist, create it.
	        environmentConfiguration.setInitializeLocking(true);   // Turn on the locking subsystem.
	        environmentConfiguration.setErrorStream(System.err);   // Capture error information in more detail.
	        environmentConfiguration.setInitializeCache(true);
	        environmentConfiguration.setCacheSize(1024 * 1024 * 50);
	        environmentConfiguration.setInitializeLogging(true);   // Turn off the logging subsystem.
	        environmentConfiguration.setTransactional(true);       // Turn on the transactional subsystem.
		    environment = new Environment(new File(location), environmentConfiguration);
		} catch (FileNotFoundException e) {
			throw new XBRLException("The physical location of the BDB XML database could not be found.", e);
		} catch (DatabaseException e) {
			throw new XBRLException("The BDB XML database environment could not be set up.", e);
		} 
		
		// Generate the database manager
	    try {

            // Generate the database manager configuration
	        managerConfiguration = new XmlManagerConfig();
	        managerConfiguration.setAdoptEnvironment(true);
	        managerConfiguration.setAllowExternalAccess(true);
	        
	        dataManager = new XmlManager(environment, managerConfiguration);

	    } catch (XmlException e) {
            throw new XBRLException("The BDB XML database manager could not be set up.", e);
        }

        // Set up the XML Update context required for updating XML in the database.
	    try {
	        xmlUpdateContext = dataManager.createUpdateContext();
        } catch (XmlException e) {
            throw new XBRLException("The XML update context could not be set up.", e);
        }

        // Define the document configuration to use for documents stored in the database
        documentConfiguration = new XmlDocumentConfig();
        documentConfiguration.setWellFormedOnly(true);

        // Define the checkpointing configuration (check points force the log to be written into the database)
        checkpointConfig = new CheckpointConfig();
        checkpointConfig.setKBytes(1000); // Do a checkpoint if more than 1000 KB have been logged.
	    
        // Generate the XQuery context (defining NS prefixes and specifying the return type)
        try {
			xmlQueryContext = dataManager.createQueryContext();
			xmlQueryContext.setReturnType(XmlQueryContext.DeadValues);
			xmlQueryContext.setNamespace(Constants.XLinkPrefix, Constants.XLinkNamespace);
			xmlQueryContext.setNamespace(Constants.XMLSchemaPrefix, Constants.XMLSchemaNamespace);
			xmlQueryContext.setNamespace(Constants.XBRL21Prefix, Constants.XBRL21Namespace);
			xmlQueryContext.setNamespace(Constants.XBRL21LinkPrefix, Constants.XBRL21LinkNamespace);
			xmlQueryContext.setNamespace(Constants.XBRLAPIPrefix, Constants.XBRLAPINamespace);
			xmlQueryContext.setNamespace(Constants.XBRLAPILanguagesPrefix, Constants.XBRLAPILanguagesNamespace);
	    } catch (XmlException e) {
			throw new XBRLException("The BDB XML XQuery context could not be defined.", e);
		}

    	// Get the data container and check its storage approach is node based (not document based).
	    try {
			if (dataManager.existsContainer(container) != 0) {
				dataContainer = dataManager.openContainer(container);
		    	if (dataContainer.getContainerType() != XmlContainer.NodeContainer)
		    		throw new XBRLException("The container must be use the node format for data storage.");
			} else {
				dataContainer = dataManager.createContainer(container);
				
		        // Set the indexes for the new container.
		        try {
		            XmlIndexSpecification indexSpecification = dataContainer.getIndexSpecification();
		            indexSpecification.addDefaultIndex("node-element-presence-none");
		            indexSpecification.addIndex("node-element-presence-none", Constants.XBRLAPIPrefix,"data");
		            indexSpecification.addIndex("node-element-presence-none", Constants.XBRLAPIPrefix,"fragment");
		            indexSpecification.addIndex("node-element-presence-none", Constants.XBRLAPIPrefix,"xptr");
		            indexSpecification.addIndex("node-attribute-equality-string", "","parentIndex");
		            indexSpecification.addIndex("node-attribute-equality-string", "","url");
		            indexSpecification.addIndex("node-attribute-equality-string", "","type");
		            indexSpecification.addIndex("node-attribute-equality-string", "","id");
		            indexSpecification.addIndex("node-attribute-equality-string", "","targetDocumentURL");
		            indexSpecification.addIndex("node-attribute-equality-string", "","targetPointerValue");
		            indexSpecification.addIndex("node-attribute-equality-string", "","absoluteHref");
		            indexSpecification.addIndex("node-attribute-equality-string", "","value");
		            indexSpecification.addIndex("node-attribute-equality-string", "","arcroleURI");
		            indexSpecification.addIndex("node-attribute-equality-string", "","roleURI");
		            indexSpecification.addIndex("node-attribute-equality-string", "","name");
		            indexSpecification.addIndex("node-attribute-equality-string", "","targetNamespace");
		            indexSpecification.addIndex("node-attribute-equality-string", Constants.XMLNamespace,"lang");
		            indexSpecification.addIndex("node-attribute-equality-string", Constants.XLinkNamespace,"label");
		            indexSpecification.addIndex("node-attribute-equality-string", Constants.XLinkNamespace,"from");
		            indexSpecification.addIndex("node-attribute-equality-string", Constants.XLinkNamespace,"to");
		            indexSpecification.addIndex("node-attribute-equality-string", Constants.XLinkNamespace,"type");
		            indexSpecification.addIndex("node-attribute-equality-string", Constants.XLinkNamespace,"arcrole");
		            indexSpecification.addIndex("node-attribute-equality-string", Constants.XLinkNamespace,"role");
		            indexSpecification.addIndex("node-element-presence-none", Constants.XBRLAPILanguagesNamespace,"language");
		            indexSpecification.addIndex("node-element-equality-string", Constants.XBRLAPILanguagesNamespace,"code");
		            indexSpecification.addIndex("node-element-equality-string", Constants.XBRLAPILanguagesNamespace,"value");
		            indexSpecification.addIndex("node-element-equality-string", Constants.XBRLAPILanguagesNamespace,"encoding");
		            // TODO: Should the indexSpecification be deleted here?
		        } catch (XmlException e) {
		            throw new XBRLException("The default index could not be set.", e);
		        }
		        
			}
        } catch (XmlException e) {
            throw new XBRLException("The BDB XML database container could not be created or opened.", e);
        }
		
        try {
            this.getStoreState();
        } catch (XBRLException e) {
            this.storeLoaderState("0",new LinkedList<String>());
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
				logger.info("Closing a store with name " + dataContainer.getName());
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

		try {
		    environment.close();
		} catch (DatabaseException e) {
		    throw new XBRLException("The database environment could not be closed.",e);
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
			return; // The fragment is already in the store.
		}
		
		String index = fragment.getFragmentIndex();
		
		if (hasFragment(index)) {
		    removeFragment(index);
		}

		XmlInputStream xmlInputStream = null;
		try {

		    // Store the XML 
			String xmlString = serializeToString(fragment.getMetadataRootElement());
			InputStream inputStream = new ByteArrayInputStream(xmlString.getBytes());			
			xmlInputStream = dataManager.createInputStream(inputStream);
			dataContainer.putDocument(index, xmlInputStream, xmlUpdateContext, documentConfiguration);

			// Do a checkpoint if enough data has been logged.
            environment.checkpoint(checkpointConfig);

            // Finalise the fragment, ready for use
            fragment.setResource(fragment.getBuilder().getMetadata());
            fragment.setStore(this);
		
        } catch (XmlException e) {
            throw new XBRLException("The fragment could not be added to the BDB XML data store.", e);
        } catch (DatabaseException e) {
            throw new XBRLException("The database checkpoint operation failed.", e);
        } finally {
            if (xmlInputStream != null) xmlInputStream.delete();
        }

	}

	/**
	 * @see org.xbrlapi.data.Store#hasFragment(String)
	 */
	public boolean hasFragment(String index) throws XBRLException {
        XmlDocument xmlDocument = null;
		try {
			xmlDocument = dataContainer.getDocument(index);
			return true;
        } catch (XmlException e) {
            return false;
        } finally {
            if (xmlDocument != null) xmlDocument.delete();
        }
	}
	
    /**
     * Overrides the base implementation to avoid the overhead of creating the
     * various fragment objects.
     * @see org.xbrlapi.data.Store#hasDocument(String)
     */
    public boolean hasDocument(String url) throws XBRLException {
        XmlResults results = null;
        try {
            results = performQuery("/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@url='" + url + "' and @parentIndex='none']");
    		boolean result = (results.size() == 1); 
        	return  result;
    	} catch (XmlException e) {
    		throw new XBRLException("The size of the result set for the query could not be determined.", e);
    	} finally {
            if (results != null) results.delete();
    	}
    }    	

	/**
	 * @see org.xbrlapi.data.Store#getFragment(String)
	 */
	public Fragment getFragment(String index) throws XBRLException {
		
        XmlDocument xmlDocument = null;
        Document document = null;
	    try {
	        // Get the document from the container.
	        try {
	            xmlDocument = dataContainer.getDocument(index);
	        } catch (XmlException e) { // Thrown if the document is not found
	            throw new XBRLException("The fragment " + index + " could not be retrieved from the store.",e);
	        }
	        
	        // Generate the DOM representation of the fragment, setting the root to the root element thereof.
	        try {
	            document = XMLDOMBuilder.newDocument(xmlDocument.getContentAsInputStream());
	        } catch (XmlException e) {
	            throw new XBRLException("The fragment content was not available as an input stream from the store.",e);
	        }
		} finally {
	        if (xmlDocument != null) xmlDocument.delete();
		}

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
	 * @see org.xbrlapi.data.Store@removeFragment(String)
	 */
	public void removeFragment(String index) throws XBRLException {
		try {
			dataContainer.deleteDocument(index,xmlUpdateContext);
		} catch (XmlException e) {
			throw new XBRLException("The fragment removal failed.", e);
		}	
	}





	/**
     * TODO Make sure that queries finding a node within a fragment return the fragment itself.         
	 * @see org.xbrlapi.data.Store#query(String)
	 */
    @SuppressWarnings(value = "unchecked")
	public <F extends Fragment> FragmentList<F> query(String myQuery) throws XBRLException {

        XmlResults xmlResults = null;
        XmlValue xmlValue = null;

        try {
			double startTime = System.currentTimeMillis();

			xmlResults = performQuery(myQuery);

			Double time = new Double((System.currentTimeMillis()-startTime));
			logger.debug(myQuery);
			logger.debug("Average time for query = " + time + " milliseconds");

            xmlValue = xmlResults.next();
			FragmentList<F> fragments = new FragmentListImpl<F>();
		    while (xmlValue != null) {
		        Document document = XMLDOMBuilder.newDocument(xmlValue.asString());
		        xmlValue.delete();
		        Element root = document.getDocumentElement();
				fragments.addFragment((F) FragmentFactory.newFragment(this, root));
		        xmlValue = xmlResults.next();
		    }
	    	
			return fragments;

		} catch (XmlException e) {
			throw new XBRLException("Failed query: " + myQuery,e);
		} finally {
		    if (xmlValue != null) xmlValue.delete();
            if (xmlResults != null) xmlResults.delete();
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
        // TODO provide a means of adding additional namespaces for querying.
        // TODO provide a means of investigating namespace bindings for the query configuration.
	    
	    XmlResults results = null;
		try {
			
		    String query = "collection('" + dataContainer.getName() + "')" + myQuery;
		    results = dataManager.query(query,xmlQueryContext);
			return results;

		} catch (XmlException e) {
		    if (results != null) results.delete();
			throw new XBRLException("Failed query: " + myQuery,e);
		}
	}
	






}