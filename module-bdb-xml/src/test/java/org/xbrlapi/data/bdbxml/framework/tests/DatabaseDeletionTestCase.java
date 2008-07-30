package org.xbrlapi.data.bdbxml.framework.tests;

import java.io.File;

import org.xbrlapi.utilities.BaseTestCase;

import com.sleepycat.db.Environment;
import com.sleepycat.db.EnvironmentConfig;
import com.sleepycat.dbxml.XmlContainer;
import com.sleepycat.dbxml.XmlManager;
import com.sleepycat.dbxml.XmlManagerConfig;
import com.sleepycat.dbxml.XmlUpdateContext;

/**
 * basic testing that the Berkeley DB XML implementation is working OK.
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
*/
public class DatabaseDeletionTestCase extends BaseTestCase {

	private String docString = "<a_node><b_node>Some text</b_node></a_node>";
	//private String docName = "testDocumentName";	
	private String containerName = configuration.getProperty("bdbxml.container.name");
	private File environmentHome = new File(configuration.getProperty("bdbxml.store.location"));
	
	private EnvironmentConfig environmentConfiguration = null;
	private Environment environment = null;
	private XmlManagerConfig managerConfiguration = null;
	private XmlManager myManager = null;
	private XmlContainer xmlContainer = null;
		
	protected void setUp() throws Exception {
		super.setUp();
		
		try {
		    environmentConfiguration = new EnvironmentConfig();
		    environmentConfiguration.setAllowCreate(true);         // If the environment does not exist, create it.
		    environmentConfiguration.setInitializeCache(true);     // Turn on the shared memory region.
		    environmentConfiguration.setInitializeLocking(true);   // Turn on the locking subsystem.
		    environmentConfiguration.setInitializeLogging(true);   // Turn on the logging subsystem.
		    environmentConfiguration.setTransactional(true);       // Turn on the transactional subsystem.
		    environmentConfiguration.setErrorStream(System.err);   // Capture error information in more detail.
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

    protected void closeUp() throws Exception {
        
        try {
            
            if (xmlContainer != null)  {
                xmlContainer.close();
            }

            if (myManager != null) {
                myManager.close();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("The database failed to clean up.");
        }
    }	
	

    protected void insertData(String id) {
        
        XmlUpdateContext xmlUpdateContext = null;
        try {
            xmlUpdateContext = myManager.createUpdateContext();
            xmlContainer.putDocument(id,docString,xmlUpdateContext,null);
        } catch (Exception e) {
            e.printStackTrace();
            fail("The database operations failed.");
        } finally  {
            xmlUpdateContext.delete();
        }
    
    }    
    
	public DatabaseDeletionTestCase(String arg0) {
		super(arg0);
	}

	public final void testDeletionAfterRepeatedInstantiation() {

	    try {
	        
	        for (int i=0; i<10; i++) {
        	    this.closeUp();
        	    this.setUp();
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        fail("Unexpected exception");
	    }
	
	}

    public final void testDeletionAfterRepeatedInstantiationAndOperations() {

        try {
            
            for (int i=0; i<10; i++) {
                logger.info(i);
                this.closeUp();
                this.setUp();
                this.insertData((new Integer(i)).toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception");
        }
    
    }
	
	
}

           
