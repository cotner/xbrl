package org.xbrlapi.data.bdbxml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xbrlapi.XML;
import org.xbrlapi.data.BaseStoreImpl;
import org.xbrlapi.data.Store;
import org.xbrlapi.impl.FragmentFactory;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.utilities.XMLDOMBuilder;

import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.Environment;
import com.sleepycat.db.EnvironmentConfig;
import com.sleepycat.dbxml.XmlContainer;
import com.sleepycat.dbxml.XmlContainerConfig;
import com.sleepycat.dbxml.XmlDocument;
import com.sleepycat.dbxml.XmlDocumentConfig;
import com.sleepycat.dbxml.XmlException;
import com.sleepycat.dbxml.XmlIndexDeclaration;
import com.sleepycat.dbxml.XmlIndexSpecification;
import com.sleepycat.dbxml.XmlManager;
import com.sleepycat.dbxml.XmlManagerConfig;
import com.sleepycat.dbxml.XmlQueryContext;
import com.sleepycat.dbxml.XmlQueryExpression;
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

public class StoreImpl extends BaseStoreImpl implements Store {

    private final static Logger logger = Logger.getLogger(StoreImpl.class); 
    
    private String computerIdentity;
    private String locationName;
    private String containerName;
	transient private Environment environment;
	transient public XmlManager dataManager;
	transient public XmlContainer dataContainer;
	
    /**
     * @param location The location of the database (The path to where the database exists)
     * @param container The name of the container to hold the data in the store.
     * @throws XBRLException
     */
    public StoreImpl(String location, String container) throws XBRLException {
        super();
        initialize(location,container);
    }
    
    private void initialize(String location, String container) throws XBRLException {
        
        lastSync = 0;
        
        try {
            InetAddress addr = InetAddress.getLocalHost();
            byte[] ipAddr = addr.getAddress();
            computerIdentity = addr.getHostName();
            for (byte b: ipAddr) {
                int i = new Integer(b).intValue();
                computerIdentity += "." + i;
            }
            
        } catch (UnknownHostException e) {
            throw new XBRLException("The computer identity could not be obtained.", e);
        }

        
        if (location != null) this.locationName = location;
        else throw new XBRLException("The Berkeley DB XML database location must be specified.");
        
        if (container != null)  this.containerName = container;
        else throw new XBRLException("The Berkeley DB XML database container must be specified.");
        
        try {
            XmlManager.setLogLevel(XmlManager.LEVEL_ALL, false);
            XmlManager.setLogCategory(XmlManager.CATEGORY_ALL, false);     
        } catch (XmlException e) {
            throw new XBRLException("The BDB XML log levels could not be initialised.", e);
        }
        
        initContainer();
        
        //this.logIndexes();
        
    }

    /**
     * Log information about all the indexes in the data store.
     */
    public void logIndexes() {
        try {
            XmlIndexSpecification xis = dataContainer.getIndexSpecification();
            int count = 0;
            XmlIndexDeclaration idxDecl = null;
            while((idxDecl = (xis.next())) != null) {
                logger.info("For node '" + idxDecl.uri + ":" + idxDecl.name + "', found index: '" + idxDecl.index + "'.");
                count ++;
            }
            System.out.println(count + " indices found.");
        } catch (XmlException e) {
            ;
        }        
    }
    
    /**
     * @see org.xbrlapi.data.Store#persist(XML)
     */
    public synchronized int getSize() throws XBRLException {
        try {
            return this.dataContainer.getNumDocuments();
        } catch (XmlException e) {
            throw new XBRLException("Failed to get the number of fragments in the data store.",e);
        }
    }
    
	/**
	 * Initialises the database environment.
	 * @throws XBRLException if the environment files are not found or 
	 * there is a database problem.
	 */
	private void initEnvironment() throws XBRLException {
        try {
            EnvironmentConfig environmentConfiguration = new EnvironmentConfig();
            environmentConfiguration.setThreaded(true);
            environmentConfiguration.setAllowCreate(true);         // If the environment does not exist, create it.
            environmentConfiguration.setInitializeLocking(true);   // Turn on the locking subsystem.
            environmentConfiguration.setErrorStream(System.err);   // Capture error information in more detail.
            environmentConfiguration.setInitializeCache(true);
            environmentConfiguration.setCacheSize(1024 * 1024 * 500);
            environmentConfiguration.setInitializeLogging(true);   // Turn off the logging subsystem.
            environmentConfiguration.setTransactional(true);       // Turn on the transactional subsystem.
            environment = new Environment(new File(locationName), environmentConfiguration);
            environment.trickleCacheWrite(20);
            logger.debug("Initialised the environment.");
        } catch (FileNotFoundException e) {
            throw new XBRLException("The physical location of the BDB XML database could not be found.", e);
        } catch (DatabaseException e) {
            throw new XBRLException("The BDB XML database environment could not be set up.", e);
        }	    
	}
	
    /**
     * Initialises the database manager
     * @throws XBRLException
     */
	private void initManager() throws XBRLException {
	    if (environment == null) initEnvironment();
        try {
            XmlManagerConfig managerConfiguration = new XmlManagerConfig();
            managerConfiguration.setAdoptEnvironment(true);
            managerConfiguration.setAllowExternalAccess(true);
            dataManager = new XmlManager(environment, managerConfiguration);
            logger.debug("Initialised the data manager.");
        } catch (XmlException e) {
            throw new XBRLException("The Berkeley XML database manager could not be set up.", e);
        }	    
	}
	
    /**
     * Initialises the database container.
     * @throws XBRLException
     */
    private void initContainer() throws XBRLException {
        if (dataManager == null) initManager();
        try {
            if (dataManager.existsContainer(containerName) != 0) {
                dataContainer = dataManager.openContainer(containerName);
            } else {
                createContainer();
            }
            logger.debug("Initialised the data container.");
        } catch (XmlException e) {
            throw new XBRLException("The database container, " + containerName + ", could not be opened.");
        }
    }
    
    private void createContainer() throws XBRLException {
        if (dataManager == null) initManager();
        try {
            XmlContainerConfig config = new XmlContainerConfig();
            config.setStatisticsEnabled(true);
            dataContainer = dataManager.createContainer(containerName,config);
            
        } catch (XmlException e) {
            throw new XBRLException("The data container could not be created.", e);
        } 
        
        XmlIndexSpecification xmlIndexSpecification = null;
        XmlUpdateContext xmlUpdateContext = null;

        try {

            xmlIndexSpecification = dataContainer.getIndexSpecification();

            xmlIndexSpecification.replaceDefaultIndex("node-element-presence");

            // TODO Remove these indices - they are redundant given the default index.
            xmlIndexSpecification.addIndex(Constants.XBRLAPINamespace.toString(),"fragment","node-element-presence");
            xmlIndexSpecification.addIndex(Constants.XBRLAPINamespace.toString(),"data","node-element-presence");
            xmlIndexSpecification.addIndex(Constants.XBRLAPINamespace.toString(),"xptr","node-element-presence");
/*            xmlIndexSpecification.addIndex(Constants.XBRLAPINamespace.toString(),"match","node-element-presence");
*/
            xmlIndexSpecification.addIndex("","stub","node-attribute-presence");

            xmlIndexSpecification.addIndex("","index", "unique-node-attribute-equality-string");

            xmlIndexSpecification.addIndex(Constants.XBRL21LinkNamespace.toString(),"label","node-element-substring-string");
            xmlIndexSpecification.addIndex(Constants.GenericLabelNamespace.toString(),"label","node-element-substring-string");
            xmlIndexSpecification.addIndex("","name","node-attribute-substring-string");
            
            xmlIndexSpecification.addIndex("","signature", "node-attribute-equality-string");

            xmlIndexSpecification.addIndex("","arcIndex", "node-attribute-equality-string");
            xmlIndexSpecification.addIndex("","arcName", "node-attribute-equality-string");
            xmlIndexSpecification.addIndex("","arcNamespace", "node-attribute-equality-string");
            xmlIndexSpecification.addIndex("","arcRole", "node-attribute-equality-string");
            xmlIndexSpecification.addIndex("","arcOrder", "node-attribute-equality-string");
            xmlIndexSpecification.addIndex("","arcPriority", "node-attribute-equality-string");
            xmlIndexSpecification.addIndex("","use", "node-attribute-equality-string");

            xmlIndexSpecification.addIndex("","linkName", "node-attribute-equality-string");
            xmlIndexSpecification.addIndex("","linkNamespace", "node-attribute-equality-string");
            xmlIndexSpecification.addIndex("","linkRole", "node-attribute-equality-string");
            
            xmlIndexSpecification.addIndex("","sourceIndex", "node-attribute-equality-string");
            xmlIndexSpecification.addIndex("","sourceType", "node-attribute-equality-string");
            xmlIndexSpecification.addIndex("","sourceName", "node-attribute-equality-string");
            xmlIndexSpecification.addIndex("","sourceNamespace", "node-attribute-equality-string");
            xmlIndexSpecification.addIndex("","sourceRole", "node-attribute-equality-string");
            xmlIndexSpecification.addIndex("","sourceLanguage", "node-attribute-equality-string");

            xmlIndexSpecification.addIndex("","targetIndex", "node-attribute-equality-string");
            xmlIndexSpecification.addIndex("","targetType", "node-attribute-equality-string");
            xmlIndexSpecification.addIndex("","targetName", "node-attribute-equality-string");
            xmlIndexSpecification.addIndex("","targetNamespace", "node-attribute-equality-string");
            xmlIndexSpecification.addIndex("","targetRole", "node-attribute-equality-string");
            xmlIndexSpecification.addIndex("","targetLanguage", "node-attribute-equality-string");
            
            xmlIndexSpecification.addIndex("","label", "node-attribute-presence");
            xmlIndexSpecification.addIndex("","reference", "node-attribute-presence");
            xmlIndexSpecification.addIndex("","use", "node-attribute-presence");
            xmlIndexSpecification.addIndex("","use", "node-attribute-equality-string");

            xmlIndexSpecification.addIndex("","parentIndex", "node-attribute-equality-string");

            xmlIndexSpecification.addIndex("","uri", "node-attribute-equality-string");
            xmlIndexSpecification.addIndex("","uri", "node-attribute-presence");

            xmlIndexSpecification.addIndex("","type", "node-attribute-equality-string");
            xmlIndexSpecification.addIndex("","targetDocumentURI", "node-attribute-equality-string");
            xmlIndexSpecification.addIndex("","targetPointerValue", "node-attribute-equality-string");
            xmlIndexSpecification.addIndex("","absoluteHref", "node-attribute-equality-string");
            xmlIndexSpecification.addIndex("","id","node-attribute-equality-string");
            xmlIndexSpecification.addIndex("","value", "node-attribute-equality-string");
            xmlIndexSpecification.addIndex("","arcroleURI", "node-attribute-equality-string");
            xmlIndexSpecification.addIndex("","roleURI", "node-attribute-equality-string");
            xmlIndexSpecification.addIndex("","name", "node-attribute-equality-string");
            xmlIndexSpecification.addIndex("","targetNamespace", "node-attribute-equality-string");

            xmlIndexSpecification.addIndex("","contextRef", "node-attribute-equality-string");
            xmlIndexSpecification.addIndex("","unitRef", "node-attribute-equality-string");
            
            // Entity identification indexes
            xmlIndexSpecification.addIndex("","scheme", "node-attribute-equality-string");
            xmlIndexSpecification.addIndex("","value", "node-attribute-equality-string");
            xmlIndexSpecification.addIndex(Constants.XBRL21Namespace.toString(),"identifier", "node-element-equality-string");            
            
            xmlIndexSpecification.addIndex(Constants.XMLNamespace.toString(),"lang","node-attribute-equality-string");

            xmlIndexSpecification.addIndex(Constants.XLinkNamespace.toString(),"label","node-attribute-equality-string");
            xmlIndexSpecification.addIndex(Constants.XLinkNamespace.toString(),"from","node-attribute-equality-string");
            xmlIndexSpecification.addIndex(Constants.XLinkNamespace.toString(),"to","node-attribute-equality-string");
            xmlIndexSpecification.addIndex(Constants.XLinkNamespace.toString(),"type","node-attribute-equality-string");
            xmlIndexSpecification.addIndex(Constants.XLinkNamespace.toString(),"arcrole","node-attribute-equality-string");
            xmlIndexSpecification.addIndex(Constants.XLinkNamespace.toString(),"role","node-attribute-equality-string");
            xmlIndexSpecification.addIndex(Constants.XLinkNamespace.toString(),"label","node-attribute-equality-string");

            xmlIndexSpecification.addIndex(Constants.XBRLAPILanguagesNamespace.toString(),"language","node-element-presence");
            xmlIndexSpecification.addIndex(Constants.XBRLAPILanguagesNamespace.toString(),"code","node-element-equality-string");
            xmlIndexSpecification.addIndex(Constants.XBRLAPILanguagesNamespace.toString(),"value","node-element-equality-string");
            xmlIndexSpecification.addIndex(Constants.XBRLAPILanguagesNamespace.toString(),"encoding","node-element-equality-string");

            xmlUpdateContext = dataManager.createUpdateContext();
            dataContainer.setIndexSpecification(xmlIndexSpecification,xmlUpdateContext);
            
        } catch (XmlException e) {
            throw new XBRLException("The indexes could not be configured.", e);
        } finally {
            //if (xmlUpdateContext != null) xmlUpdateContext.delete();
            if (xmlIndexSpecification != null) xmlIndexSpecification.delete();
        }
        
    }        

	

	/**
	 * The closure operation entails closing the XML container and the XML data manager
	 * that are used by the store.
	 * @see org.xbrlapi.data.Store#close()
	 */
    public synchronized void close() throws XBRLException {
	    super.close();
	    closeContainer();
	    closeManager();
	}
	

	
	private void closeContainer() throws XBRLException {
        if (dataContainer == null) return;
	    if (dataManager == null) initManager();
        dataContainer.delete();
        dataContainer = null;
	}
	
    private void closeManager() {
        if (dataManager == null) return;
        dataManager.delete();
    }	
	
    private void deleteContainer() throws XBRLException {
        if (dataManager == null) initManager();
        closeContainer();
        try {
            if (dataManager.existsContainer(containerName) != 0) {
                dataManager.removeContainer(containerName);
            }
        } catch (XmlException e) {
            throw new XBRLException("The BDB XML database container could not be deleted.");
        }
    }	
	
	/**
	 * @see org.xbrlapi.data.Store#delete()
	 */
    public synchronized void delete() throws XBRLException {
	    deleteContainer();
	    closeManager();
	}
	
	/**
	 * @see org.xbrlapi.data.Store#persist(XML)
	 */
    synchronized public void persist(XML xml) throws XBRLException {

        XmlUpdateContext xmlUpdateContext = null;
	    try {
	        
            String index = xml.getIndex();
            if (hasXMLResource(index)) remove(index);

            String content = serializeToString(xml.getMetadataRootElement());
            xmlUpdateContext = dataManager.createUpdateContext();
            XmlDocumentConfig documentConfiguration = new XmlDocumentConfig();
            documentConfiguration.setWellFormedOnly(true);
            dataContainer.putDocument(index, content, xmlUpdateContext, null);

            if (xml.getStore() == null) {
                xml.setResource(xml.getBuilder().getMetadata());
                xml.setStore(this);
	        }
	        	        
        } catch (XmlException e) {
            throw new XBRLException("The fragment could not be added to the BDB XML data store.", e);
	    } finally {
            //if (xmlUpdateContext != null) xmlUpdateContext.delete();
	    }
	}

	/**
	 * @see org.xbrlapi.data.Store#hasXMLResource(String)
	 */
    public synchronized boolean hasXMLResource(String index) throws XBRLException {

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
	 * @see org.xbrlapi.data.Store#getXMLResource(String)
	 */
     public synchronized <F extends XML> F getXMLResource(String index) throws XBRLException {
        XmlDocument xmlDocument = null;
        Document document = null;
	    try {

	        try {
	            xmlDocument = dataContainer.getDocument(index);
	        } catch (XmlException e) { // Thrown if the document is not found
	            throw new XBRLException("The fragment " + index + " could not be retrieved from the store.",e);
	        }

	        try {
	            document = (new XMLDOMBuilder()).newDocument(xmlDocument.getContentAsInputStream());
	        } catch (XmlException e) {
	            throw new XBRLException("The fragment content is not available as an input stream.",e);
	        }

    		return FragmentFactory.<F>newFragment(this, document.getDocumentElement());

	    } finally {
            if (xmlDocument != null) xmlDocument.delete();
	    }
	}

	/**
	 * @see org.xbrlapi.data.Store#remove(String)
	 */
     public synchronized void remove(XML fragment) throws XBRLException {
		remove(fragment.getIndex());
	}

	/**
	 * @see org.xbrlapi.data.Store#remove(String)
	 */
     public synchronized void remove(String index) throws XBRLException {

        XmlUpdateContext xmlUpdateContext = null;
        try {
            xmlUpdateContext = dataManager.createUpdateContext();
            dataContainer.deleteDocument(index,xmlUpdateContext);
        } catch (XmlException e) {
            throw new XBRLException("The fragment removal failed.", e);
        } finally {
            //if (xmlUpdateContext != null) xmlUpdateContext.delete();
        }
	}

	/**
     * TODO Make sure that queries finding a node within a fragment return the fragment itself.
	 * @see org.xbrlapi.data.Store#queryForXMLResources(String)
	 */
    @SuppressWarnings(value = "unchecked")
	public synchronized <F extends XML> List<F> queryForXMLResources(String query) throws XBRLException {

        XmlResults xmlResults = null;
        XmlValue xmlValue = null;
        try {
    
            try {
    			xmlResults = runQuery(query);

    			double startTime = System.currentTimeMillis();

                xmlValue = xmlResults.next();
    			List<F> fragments = new Vector<F>();
    			XMLDOMBuilder builder = new XMLDOMBuilder();
    		    while (xmlValue != null) {
                    XmlDocument doc = xmlValue.asDocument();
    		        Document document = builder.newDocument(doc.getContentAsInputStream());
    		        doc.delete();
    		        xmlValue.delete();
    		        Element root = document.getDocumentElement();
    				fragments.add((F) FragmentFactory.newFragment(this, root));
    		        xmlValue = xmlResults.next();
    		    }
    	    	
    	        Double time = new Double((System.currentTimeMillis()-startTime));
    	        logger.debug(time + " milliseconds to create fragment list from" + query);
    			return fragments;
    
    		} catch (XmlException e) {
    			throw new XBRLException("Failed query: " + query,e);
    		}
    		
        } finally {
            if (xmlValue != null) xmlValue.delete();
            if (xmlResults != null) xmlResults.delete();
        }
	}
    
    /**
     * @see org.xbrlapi.data.Store#queryForIndices(String)
     */
    public synchronized Set<String> queryForIndices(String query) throws XBRLException {

        query = "for $fragment in " + query + " return string($fragment/@index)";
        
        XmlResults xmlResults = null;
        XmlValue xmlValue = null;
        try {
    
            try {
                xmlResults = runQuery(query);
                xmlValue = xmlResults.next();
                Set<String> indices = new HashSet<String>();
/*                String regex = "<xbrlapi:fragment.*? index=\"(\\w+)\".*?>";
                Pattern pattern = Pattern.compile(regex,Pattern.DOTALL);
*/                
                while (xmlValue != null) {
/*                  Matcher matcher = pattern.matcher(xmlValue.asString());
                    matcher.matches();
                    String index = matcher.group(1);
*/
                    indices.add(xmlValue.asString());
/*                    indices.put(index,null);
*/                    xmlValue.delete();
                    xmlValue = xmlResults.next();
                }
                return indices;
    
            } catch (XmlException e) {
                throw new XBRLException("Failed query: " + query,e);
            } catch (IllegalStateException e) {
                throw new XBRLException("Failed query: " + query,e);
            }
            
        } finally {
            if (xmlValue != null) xmlValue.delete();
            if (xmlResults != null) xmlResults.delete();
        }
        
    }
    
    /**
     * @see org.xbrlapi.data.Store#queryForStrings(String)
     */
    public synchronized Set<String> queryForStrings(String query) throws XBRLException {
                
        XmlResults xmlResults = null;
        XmlValue xmlValue = null;
        try {
    
            try {
                xmlResults = runQuery(query);
                double startTime = System.currentTimeMillis();
                xmlValue = xmlResults.next();
                Set<String> strings = new TreeSet<String>();
                while (xmlValue != null) {
                    if (xmlValue.isNode())
                        strings.add(xmlValue.getNodeValue());
                    else if (xmlValue.isString())
                        strings.add(xmlValue.asString());
                    xmlValue.delete();
                    xmlValue = xmlResults.next();
                }
                
                Double time = new Double((System.currentTimeMillis()-startTime));
                logger.debug(time + " milliseconds to create String list from" + query);
                return strings;
    
            } catch (XmlException e) {
                throw new XBRLException("Failed query: " + query,e);
            }
            
        } finally {
            if (xmlValue != null) xmlValue.delete();
            if (xmlResults != null) xmlResults.delete();
        }        
    }
    
    /**
     * @see Store#queryCount(String)
     */
    public synchronized long queryCount(String query) throws XBRLException {

        XmlResults xmlResults = null;
        try {
            xmlResults = runQuery(query);        
            return xmlResults.size();
        } catch (XmlException e) {
            throw new XBRLException("Failed query: " + query,e);
        } finally {
            if (xmlResults != null) xmlResults.delete();
        }
    }    
    
    
    /**
     * TODO provide a means of investigating namespace bindings for the query configuration.
     * Provides direct access to the query mechanism so that we can use
     * queries without constructing fragments for each query result.
     * @param myQuery The query to be performed.
     * @return the results of the query.
     * @throws XBRLException if the query fails to execute.
     */
	private XmlResults runQuery(String myQuery) throws XBRLException {
	    
	    XmlQueryContext xmlQueryContext = null;
	    XmlQueryExpression xmlQueryExpression = null;
	    try {
	        String roots = "collection('" + dataContainer.getName() + "')/*" + this.getURIFilteringPredicate();
	        myQuery = myQuery.replaceAll("#roots#",roots);
            xmlQueryContext = createQueryContext();
            xmlQueryExpression = dataManager.prepare(myQuery,xmlQueryContext);
            logger.debug(xmlQueryExpression.getQueryPlan());
            double startTime = System.currentTimeMillis();
            XmlResults xmlResults = xmlQueryExpression.execute(xmlQueryContext);
            Double time = new Double((System.currentTimeMillis()-startTime));
            logger.debug(time + " milliseconds to evaluate " + myQuery);
			return xmlResults;

		} catch (XmlException e) {
			throw new XBRLException("Failed query: " + myQuery,e);
		} finally {
            if (xmlQueryContext != null) xmlQueryContext.delete();
            if (xmlQueryExpression != null) xmlQueryExpression.delete();
		}
    		
	}
	
    /**
     * Performs a lazy query evaluation
     * @param myQuery The query to be performed.
     * @return the results of the query.
     * @throws XBRLException if the query fails to execute.
     */
    private XmlResults performLazyQuery(String myQuery) throws XBRLException {
        // TODO provide a means of adding additional namespaces for querying.
        // TODO provide a means of investigating namespace bindings for the query configuration.
        
        XmlQueryContext xmlQueryContext = null;
        XmlQueryExpression xmlQueryExpression = null;
        try {
            String query = "collection('" + dataContainer.getName() + "')" + myQuery;
            xmlQueryContext = createQueryContext();
            xmlQueryContext.setEvaluationType(XmlQueryContext.Lazy);
            xmlQueryExpression = dataManager.prepare(query,xmlQueryContext);
            double startTime = System.currentTimeMillis();
            XmlResults xmlResults = xmlQueryExpression.execute(xmlQueryContext);
            Double time = new Double((System.currentTimeMillis()-startTime));
            logger.debug(time + " milliseconds to evaluate " + myQuery);
            return xmlResults;

        } catch (XmlException e) {
            throw new XBRLException("Failed query: " + myQuery,e);
        } finally {
            if (xmlQueryContext != null) xmlQueryContext.delete();
            if (xmlQueryExpression != null) xmlQueryExpression.delete();
        }
            
    }	
	
	/**
	 * @return a XQuery context, prepared with namespace declarations etc.
	 * @throws XBRLException
	 */
	private XmlQueryContext createQueryContext() throws XBRLException {
        XmlQueryContext xmlQueryContext = null;
        try {
            xmlQueryContext = dataManager.createQueryContext();
            xmlQueryContext.setReturnType(XmlQueryContext.LiveValues);
            xmlQueryContext.setNamespace(Constants.XLinkPrefix, Constants.XLinkNamespace.toString());
            xmlQueryContext.setNamespace(Constants.XMLSchemaPrefix, Constants.XMLSchemaNamespace.toString());
            xmlQueryContext.setNamespace(Constants.XBRL21Prefix, Constants.XBRL21Namespace.toString());
            xmlQueryContext.setNamespace(Constants.XBRL21LinkPrefix, Constants.XBRL21LinkNamespace.toString());
            xmlQueryContext.setNamespace(Constants.XBRLAPIPrefix, Constants.XBRLAPINamespace.toString());
            xmlQueryContext.setNamespace(Constants.XBRLAPILanguagesPrefix, Constants.XBRLAPILanguagesNamespace.toString());
            for (URI namespace: this.namespaceBindings.keySet()) 
                xmlQueryContext.setNamespace(this.namespaceBindings.get(namespace),namespace.toString());
            // xmlQueryContext.setEvaluationType(XmlQueryContext.Lazy); 
            return xmlQueryContext;
        } catch (XmlException e) {
            throw new XBRLException("Failed to create query context.",e);
        }
	}
	
    /**
     * Ensures that the database container is flushed to disk.
     * @see Store#sync()
     */
    transient private long lastSync;
    public synchronized void sync() throws XBRLException {
        if ((System.currentTimeMillis() - lastSync) < 10000) return;
        if (this.dataContainer == null) throw new XBRLException("The database container cannot be synced because it is null.");
        try {
            this.dataContainer.sync();
            lastSync = System.currentTimeMillis();
        } catch (XmlException e) {
            throw new XBRLException("The database updates could not be flushed to disk using the sync method.",e);
        }
    }

	


    /**
     * @param namespace The namespace of the node to index or null if the node does not have a namespace.
     * @param name The local name of the node to index.
     * @param type The index type.  See the Oracle Berkeley DB documentation for details on what strings to use.
     * @throws XBRLException
     */
    public void addIndex(URI namespace, String name, String type) throws XBRLException {

        XmlIndexSpecification xmlIndexSpecification = null;
        XmlUpdateContext xmlUpdateContext = null;

        try {
            xmlIndexSpecification = dataContainer.getIndexSpecification();
            String ns = "";
            if (namespace != null) ns = namespace.toString();
            xmlIndexSpecification.addIndex(ns,name,type);
            logger.info("Adding index for " + ns + ":" + name + " " + type);
            xmlUpdateContext = dataManager.createUpdateContext();
            dataContainer.setIndexSpecification(xmlIndexSpecification,xmlUpdateContext);
        } catch (XmlException e) {
            throw new XBRLException("The new index could not be configured.", e);
        } finally {
            if (xmlIndexSpecification != null) xmlIndexSpecification.delete();
        }        
    }
    
    /**
     * Handles object inflation.
     * @param in The input object stream used to access the object's serialization.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        try {
            String location = (String) in.readObject();
            String container = (String) in.readObject();
            String id = (String) in.readObject();
            initialize(location, container);
            if (! this.computerIdentity.equals(id)) {
                throw new IOException("The data store is being deserialized on the wrong computer.");
            }
        } catch (XBRLException e) {
            throw new IOException("The data store could not be deserialized.",e);
        }
    }
    
    /**
     * Handles object serialization
     * @param out The input object stream used to store the serialization of the object.
     * @throws IOException
     */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject( );
        out.writeObject(locationName);
        out.writeObject(containerName);
        out.writeObject(computerIdentity);
        logger.info("Done writing out the data store.");
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((computerIdentity == null) ? 0 : computerIdentity.hashCode());
        result = prime * result + ((containerName == null) ? 0 : containerName.hashCode());
        result = prime * result + ((locationName == null) ? 0 : locationName.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        StoreImpl other = (StoreImpl) obj;
        if (computerIdentity == null) {
            if (other.computerIdentity != null)
                return false;
        } else if (!computerIdentity.equals(other.computerIdentity))
            return false;
        if (containerName == null) {
            if (other.containerName != null)
                return false;
        } else if (!containerName.equals(other.containerName))
            return false;
        if (locationName == null) {
            if (other.locationName != null)
                return false;
        } else if (!locationName.equals(other.locationName))
            return false;
        return true;
    }    
    
}