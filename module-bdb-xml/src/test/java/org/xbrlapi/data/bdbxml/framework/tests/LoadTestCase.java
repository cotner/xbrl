package org.xbrlapi.data.bdbxml.framework.tests;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import junit.framework.TestCase;

import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.Environment;
import com.sleepycat.db.EnvironmentConfig;
import com.sleepycat.dbxml.XmlContainer;
import com.sleepycat.dbxml.XmlDocumentConfig;
import com.sleepycat.dbxml.XmlException;
import com.sleepycat.dbxml.XmlInputStream;
import com.sleepycat.dbxml.XmlManager;
import com.sleepycat.dbxml.XmlManagerConfig;
import com.sleepycat.dbxml.XmlUpdateContext;

/**
 * Designed to simplify the analysis of the SIGSEGV crash
 * occurring on multiple processor computers.
 */
public class LoadTestCase extends TestCase {
    
    private final int iterations = 100;
    private String container = "container";
    private String location = "/home/geoff/data/bdbxml";
    private String prefix = "<a_node>";
    private String suffix = "</a_node>";
    private SimpleStore store = null;
        
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        store = new SimpleStore(location, container);
        //XmlManager.setLogLevel(XmlManager.LEVEL_ALL, true);
        //XmlManager.setLogCategory(XmlManager.CATEGORY_ALL, true);
    }

    @Override
    protected void tearDown() throws Exception {
        store.delete();
    }

    public LoadTestCase(String arg0) {
        super(arg0);
    }

    /**
     * Test the creation and registration of an XMLDB database object
     */
    public final void testRepeatedDocumentAddition() {
        
        try {
            for (int i=1; i<=iterations; i++) {
                store.putDocument(prefix + "Document: " + i + suffix);
                //Thread.sleep(1);
            }
        
        } catch (Exception e) {
            e.printStackTrace();
            fail("The database insertions failed.");
        }
    }

    private class SimpleStore {

        private Environment environment = null;
        private XmlManagerConfig managerConfiguration = null;
        private XmlManager dataManager = null;
        private XmlUpdateContext xmlUpdateContext = null;
        private XmlContainer dataContainer = null;
        private XmlDocumentConfig documentConfiguration = null;

        public SimpleStore(String location, String container) 
        throws FileNotFoundException, DatabaseException, XmlException {
            EnvironmentConfig environmentConfiguration = new EnvironmentConfig();
            environmentConfiguration.setAllowCreate(true);         // If the environment does not exist, create it.
            environmentConfiguration.setInitializeCache(true);     // Turn on the shared memory region.
            environmentConfiguration.setInitializeLocking(true);   // Turn on the locking subsystem.
            environmentConfiguration.setInitializeLogging(true);   // Turn on the logging subsystem.
            environmentConfiguration.setTransactional(true);       // Turn on the transactional subsystem.
            environmentConfiguration.setErrorStream(System.err);   // Capture error information in more detail.
            
            environment = new Environment(new File(location), environmentConfiguration);
            
            managerConfiguration = new XmlManagerConfig();
            managerConfiguration.setAdoptEnvironment(true);
            managerConfiguration.setAllowExternalAccess(true);
        
            dataManager = new XmlManager(environment, managerConfiguration);
            
            if (dataManager.existsContainer(container) != 0) {
                dataContainer = dataManager.openContainer(container);
            } else {
                dataContainer = dataManager.createContainer(container);
            }

            xmlUpdateContext = dataManager.createUpdateContext();
            documentConfiguration = new XmlDocumentConfig();
            documentConfiguration.setWellFormedOnly(true);

        }

        public void close() throws XmlException {
            if (dataContainer != null) {
                dataContainer.sync();
                dataContainer.close();
                dataContainer.delete();
            }
            if (dataManager != null) {
                dataManager.delete();
            }
        }
        
        public void delete() throws XmlException, Exception {

            String dataContainerName = null;
            if (dataContainer != null) dataContainerName = dataContainer.getName();
            else throw new Exception("The data container to be deleted is null.");
        
            if (dataContainer != null) {
                dataContainer.sync();
                dataContainer.close();
                dataContainer.delete();
                dataContainer = null;
            }

            if (dataManager != null) {
                if (dataManager.existsContainer(dataContainerName) != 0) {
                    dataManager.removeContainer(dataContainerName);
                }
            } else {
                throw new Exception("The data manager was null when an attempt to delete a data store was attempted.");
            }

            // Close the data store.
            this.close();
        }
        
        private int index = 0;
        public void putDocument(String document) throws XmlException {
            InputStream inputStream = new ByteArrayInputStream(document.getBytes());
            XmlInputStream xmlInputStream = dataManager.createInputStream(inputStream);
            System.out.println("Adding document " + index++);
            dataContainer.putDocument(""+ index, xmlInputStream, xmlUpdateContext, documentConfiguration);
        }

    }

}

