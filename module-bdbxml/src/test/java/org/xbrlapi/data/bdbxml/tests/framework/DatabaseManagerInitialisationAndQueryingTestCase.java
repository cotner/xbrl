package org.xbrlapi.data.bdbxml.tests.framework;

import java.io.File;

import org.xbrlapi.utilities.BaseTestCase;

import com.sleepycat.db.Environment;
import com.sleepycat.db.EnvironmentConfig;
import com.sleepycat.dbxml.XmlContainer;
import com.sleepycat.dbxml.XmlDocument;
import com.sleepycat.dbxml.XmlException;
import com.sleepycat.dbxml.XmlManager;
import com.sleepycat.dbxml.XmlManagerConfig;
import com.sleepycat.dbxml.XmlQueryContext;
import com.sleepycat.dbxml.XmlQueryExpression;
import com.sleepycat.dbxml.XmlResults;
import com.sleepycat.dbxml.XmlUpdateContext;
import com.sleepycat.dbxml.XmlValue;

/**
 * basic testing that the Berkeley DB XML implementation is working OK.
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
*/
public class DatabaseManagerInitialisationAndQueryingTestCase extends BaseTestCase {

	private String docString = "<a_node><b_node>Some text</b_node></a_node>";
	private String docName = "testDocumentName";	

	private String containerName;
	private File environmentHome;
	
	private EnvironmentConfig environmentConfiguration = null;
	private Environment environment = null;
	private XmlManagerConfig managerConfiguration = null;
	private XmlManager myManager = null;
	private XmlQueryContext xmlQueryContext = null;
    private XmlQueryExpression queryExpression = null;
    private XmlResults xmlResults = null;
	private XmlUpdateContext xmlUpdateContext = null;
	private XmlContainer xmlContainer = null;
		
	protected void setUp() throws Exception {
		super.setUp();
		
		try {
		    containerName = configuration.getProperty("bdbxml.container.name");
		    environmentHome = new File(configuration.getProperty("bdbxml.store.location"));
		    environmentConfiguration = new EnvironmentConfig();
            environmentConfiguration.setThreaded(true);
            environmentConfiguration.setAllowCreate(true);         // If the environment does not exist, create it.
            environmentConfiguration.setInitializeLocking(true);   // Turn on the locking subsystem.
            environmentConfiguration.setErrorStream(System.err);   // Capture error information in more detail.
            environmentConfiguration.setInitializeCache(true);
            environmentConfiguration.setCacheSize(1024 * 1024 * 500);
            environmentConfiguration.setInitializeLogging(true);   // Turn off the logging subsystem.
            environmentConfiguration.setTransactional(true);       // Turn on the transactional subsystem.
		    environment = new Environment(environmentHome, environmentConfiguration);

		    managerConfiguration = new XmlManagerConfig();
            managerConfiguration.setAdoptEnvironment(true);
            managerConfiguration.setAllowExternalAccess(true);
		    myManager = new XmlManager(environment, managerConfiguration);
		    
		    if (myManager.existsContainer(containerName) > 0) {
			    myManager.removeContainer(containerName);
		    }
			xmlContainer = myManager.createContainer(containerName);
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("The BDB XML manager setup failed.");
		}
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		
		try {
			
			if (queryExpression != null) {
				queryExpression.delete();
			}

			if (xmlResults != null) {
				xmlResults.delete();
			}

			if (xmlQueryContext != null) {
				// xmlQueryContext.delete();
			}
			
			if (xmlUpdateContext != null) {

			}
			
			if (xmlContainer != null)  {
				xmlContainer.close();
			    if (myManager.existsContainer(containerName) != 0) {
			    	myManager.removeContainer(containerName);
			    }
			}

	        if (myManager != null) {
	        	myManager.close();
	        }
	        
		} catch (Exception e) {
			e.printStackTrace();
			fail("The database failed to clean up.");
		}
	}

	public DatabaseManagerInitialisationAndQueryingTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test the creation and registration of an XMLDB database object
	 */
	public final void testDatabaseManagerInitialisationAndQuerying() {

		try {

			xmlUpdateContext = myManager.createUpdateContext();
		    xmlContainer.putDocument(docName,docString,xmlUpdateContext,null);
		    
		    String myQuery = "collection('" + containerName + "')/a_node";
		    xmlQueryContext = myManager.createQueryContext();		    
		    xmlQueryContext.setReturnType(XmlQueryContext.LiveValues);
		    
		    queryExpression = myManager.prepare(myQuery, xmlQueryContext);
		    xmlResults = queryExpression.execute(xmlQueryContext);
		    XmlValue value = xmlResults.next();
		    while (value != null) {
		        XmlDocument theDoc = value.asDocument();
		        String docName = theDoc.getName();
		        assertNotNull(docName);
		        String message = "Document ";
		        message += theDoc.getName() + ":\n";
		        message += value.asString();
		        message += "\n===============================\n";
		        value = xmlResults.next();
		     }		    
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("The database operations failed.");
		}       
	
	}

    /**
     * Test document retrieval
     */
    public final void testDocumentRetrieval() {

        try {

            xmlUpdateContext = myManager.createUpdateContext();
            xmlContainer.putDocument(docName,docString,xmlUpdateContext,null);

            @SuppressWarnings("unused")
            XmlDocument doc = null;
            
            doc = xmlContainer.getDocument(docName);

            try {
                doc = xmlContainer.getDocument("garbage");
            } catch (XmlException e) {
                ;//Expected exception
            } catch (Exception e) {
                fail("Unexpected exception.");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("The database operations failed.");
        }       
    
    }
	
	
}

           
