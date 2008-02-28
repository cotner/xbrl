package org.xbrlapi.data.bdbxml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;

import org.custommonkey.xmlunit.XMLUnit;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xbrlapi.Constants;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.XBRLException;
import org.xbrlapi.data.XBRLStore;
import org.xbrlapi.data.XBRLStoreImpl;
import org.xbrlapi.impl.FragmentFactory;
import org.xbrlapi.impl.FragmentListImpl;
import org.xbrlapi.utilities.XMLDOMBuilder;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.Environment;
import com.sleepycat.db.EnvironmentConfig;
import com.sleepycat.dbxml.XmlContainer;
import com.sleepycat.dbxml.XmlDocument;
import com.sleepycat.dbxml.XmlDocumentConfig;
import com.sleepycat.dbxml.XmlException;
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
	
	/**
	 * The next fragmentId.
	 */
	String nextId = "1";

	/**
	 * Initialise the BDB XML database data store.
	 * @param location The location of the database (The path to where the database exists)
	 * @param container The name of the container to hold the data in the store.
	 * @throws XBRLException
	 */
	public StoreImpl(String location, String container) throws XBRLException {
		logger.info("Creating a store with name " + container);
	    EnvironmentConfig environmentConfiguration = new EnvironmentConfig();
	    environmentConfiguration.setAllowCreate(true);         // If the environment does not exist, create it.
	    environmentConfiguration.setInitializeCache(true);     // Turn on the shared memory region.
	    environmentConfiguration.setInitializeLocking(true);   // Turn on the locking subsystem.
	    environmentConfiguration.setInitializeLogging(true);   // Turn on the logging subsystem.
	    environmentConfiguration.setTransactional(true);       // Turn on the transactional subsystem.
	    environmentConfiguration.setErrorStream(System.err);   // Capture error information in more detail.
		
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
*/	    } catch (XmlException e) {
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
	    } catch (XmlException e) {
			throw new XBRLException("The BDB XML database container could not be opened.", e);
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
	 * Deletes the data in the data store as well as the container for the data.
	 * Also closes the data store.
	 * @throws XBRLException
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
		
		logger.info("Deleting a store with name " + dataContainerName);
		// Close the data container before removing it.

		try {
			if (dataContainer != null) {
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
        	throw new XBRLException("A fragment with index " + index + " already exists.");
        }

		try {

			XmlUpdateContext xmlUpdateContext = dataManager.createUpdateContext();
			XmlDocumentConfig documentConfiguration = new XmlDocumentConfig();
			documentConfiguration.setWellFormedOnly(true);
		
			String xmlString = serializeToString(fragment.getMetadataRootElement());
			InputStream inputStream = new ByteArrayInputStream(xmlString.getBytes());			
			XmlInputStream xmlInputStream = dataManager.createInputStream(inputStream);
			dataContainer.putDocument(index, xmlInputStream, xmlUpdateContext, documentConfiguration);
			
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
			document = builder.newDocument(xmlDocument.getContentAsInputStream());
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
			XmlUpdateContext updateContext = dataManager.createUpdateContext();
			dataContainer.deleteDocument(index,updateContext);
			updateContext.delete();
		} catch (XmlException e) {
			e.printStackTrace();
			throw new XBRLException("The fragment removal failed.", e);
		}	
	}

	/**
	 * @see org.xbrlapi.data.Store#updateFragment(String, String)
	 */
	public long updateFragment(String index, String updateDeclaration) throws XBRLException {
		throw new XBRLException("Update functionality is not implemented.");
	}

	/**
	 * @see org.xbrlapi.data.Store#updateFragment(String)
	 */
	public long updateFragments(String updateDeclaration) throws XBRLException {
		throw new XBRLException("Update functionality is not implemented.");
	}
	




	/**
	 * @see org.xbrlapi.data.Store#query(String)
	 */
    @SuppressWarnings(value = "unchecked")
	public <F extends Fragment> FragmentList<F> query(String myQuery) throws XBRLException {
		try {
			
		    XmlQueryContext xmlQueryContext = dataManager.createQueryContext();
		    xmlQueryContext.setReturnType(XmlQueryContext.DeadValues);
		    xmlQueryContext.setNamespace(Constants.XLinkPrefix, Constants.XLinkNamespace);
		    xmlQueryContext.setNamespace(Constants.XMLSchemaPrefix, Constants.XMLSchemaNamespace);
		    xmlQueryContext.setNamespace(Constants.XBRL21Prefix, Constants.XBRL21Namespace);
		    xmlQueryContext.setNamespace(Constants.XBRL21LinkPrefix, Constants.XBRL21LinkNamespace);
		    xmlQueryContext.setNamespace(Constants.XBRLAPIPrefix, Constants.XBRLAPINamespace);
		    
		    String query = "collection('" + dataContainer.getName() + "')" + myQuery;
		    XmlResults results = dataManager.query(query,xmlQueryContext);
		    XmlValue value = results.next();
			FragmentList<F> fragments = new FragmentListImpl<F>();
		    while (value != null) {
		        XmlDocument theDoc = value.asDocument();
		        // TODO determine why the BDB XML query result cannot be accessed as an input stream.
		        //Document document = builder.newDocument(theDoc.getContentAsInputStream());
		        Document document = XMLUnit.buildTestDocument(new InputSource(new StringReader(value.asString())));
		        Element root = document.getDocumentElement();
				fragments.addFragment((F) FragmentFactory.newFragment(this, root));
		        value = results.next();
		     }
	    	
			// Clean up
			xmlQueryContext.delete();
			results.delete();
			return fragments;

		} catch (IOException e) {
			throw new XBRLException("An IO exception occurred for : " + myQuery,e);			
		} catch (SAXException e) {
			throw new XBRLException("A SAX exception occurred for : " + myQuery,e);			
		} catch (ParserConfigurationException e) {
			throw new XBRLException("A parser configuration exception occurred for : " + myQuery,e);			
		} catch (XmlException e) {
			throw new XBRLException("Failed query: " + myQuery,e);
		}
	}
	


	/**
	 * @see org.xbrlapi.data.Store#storeNextFragmentId(String)
	 */
	public void storeNextFragmentId(String id) throws XBRLException {
		try {

			XmlUpdateContext xmlUpdateContext = dataManager.createUpdateContext();
		
			String xmlString = "<summary maximumFragmentId='" + id + "'/>";
			
			try {
				XmlDocument xmlDocument = dataContainer.getDocument("summary");
				xmlDocument.setContent(xmlString);
				dataContainer.updateDocument(xmlDocument, xmlUpdateContext);
				xmlDocument.delete();
			} catch (XmlException documentNotFoundException) {
				XmlDocumentConfig documentConfiguration = new XmlDocumentConfig();
				documentConfiguration.setWellFormedOnly(true);
				InputStream inputStream = new ByteArrayInputStream(xmlString.getBytes());			
				XmlInputStream xmlInputStream = dataManager.createInputStream(inputStream);
				dataContainer.putDocument("summary", xmlInputStream, xmlUpdateContext, documentConfiguration);
			}
					
		} catch (XmlException e) {
    		throw new XBRLException("The next fragment ID could not be stored.",e);
        }
	}

	/**
	 * @see org.xbrlapi.data.Store#getNextFragmentId()
	 */
	public String getNextFragmentId() throws XBRLException {
		try {
			XmlDocument xmlDocument = null;
			try {
				xmlDocument = dataContainer.getDocument("summary");
			} catch (XmlException noDocumentException) {
				return "1";
			}
			Document document = builder.newDocument(xmlDocument.getContentAsInputStream());
			xmlDocument.delete();
			Element root = document.getDocumentElement();
			String maxId = root.getAttribute("maximumFragmentId");
			if (maxId.equals("")) return "1";
			return maxId;
		} catch (XmlException e) {
			throw new XBRLException("The summary document could not be retrieved from the store.",e);
		}
	}

}