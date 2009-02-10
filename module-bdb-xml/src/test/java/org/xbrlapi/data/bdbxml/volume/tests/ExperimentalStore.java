package org.xbrlapi.data.bdbxml.volume.tests;

import java.io.File;

import com.sleepycat.db.Environment;
import com.sleepycat.db.EnvironmentConfig;
import com.sleepycat.dbxml.XmlContainer;
import com.sleepycat.dbxml.XmlContainerConfig;
import com.sleepycat.dbxml.XmlIndexSpecification;
import com.sleepycat.dbxml.XmlManager;
import com.sleepycat.dbxml.XmlManagerConfig;
import com.sleepycat.dbxml.XmlUpdateContext;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ExperimentalStore {

    public long counter = 0;
    private String prefix = "<root xmlns='http://test.namespace/' attribute='";
    private String suffix = "'>Value<hello/><something else='nothing much'>dfasd;fjasdfas;ldkj asdfl;hjasdfjksjdf;lasfl;adfhdsfahasdfh;alsdfhakslfjhalskjdljflkdalkhksd;lahdf;lsdflsn;kjkjniasvnoawe;oasinv;asno;an;osnvd;odf;sv;aivnaoas;ova;sovasodn</something></root>";
    
    XmlIndexSpecification xmlIndexSpecification = null;
    
    private String container = null;
    private String location = null;
	private Environment environment = null;
	private XmlManager dataManager = null;
	private XmlContainer dataContainer = null;
	
    /**
     * @param location The file system location of the database
     * @param container The name of the container for the data being stored
     */
    public ExperimentalStore(String location, String container) throws Exception {
        super();
        if (location != null) this.location = location;
        else throw new Exception("The Berkeley DB XML database location must be specified.");
        
        if (container != null)  this.container = container;
        else throw new Exception("The Berkeley DB XML database container must be specified.");
                
        initContainer();

    }

	private void initEnvironment() throws Exception {
        EnvironmentConfig environmentConfiguration = new EnvironmentConfig();
        environmentConfiguration.setThreaded(true);
        environmentConfiguration.setAllowCreate(true);         // If the environment does not exist, create it.
        environmentConfiguration.setInitializeLocking(true);   // Turn on the locking subsystem.
        environmentConfiguration.setErrorStream(System.err);   // Capture error information in more detail.
        environmentConfiguration.setInitializeCache(true);
        environmentConfiguration.setCacheSize(1024 * 1024 * 500);
        environmentConfiguration.setInitializeLogging(true);   // Turn off the logging subsystem.
        environmentConfiguration.setTransactional(false);       // Turn on the transactional subsystem.        
        environment = new Environment(new File(location), environmentConfiguration);
        environment.trickleCacheWrite(20);
	}
	
	private void initManager() throws Exception {
	    if (environment == null) initEnvironment();
        XmlManagerConfig managerConfiguration = new XmlManagerConfig();
        managerConfiguration.setAdoptEnvironment(true);
        managerConfiguration.setAllowExternalAccess(true);
        dataManager = new XmlManager(environment, managerConfiguration);
	}
	
    private void initContainer() throws Exception {
        if (dataManager == null) initManager();
        if (dataManager.existsContainer(container) != 0) {
            dataContainer = dataManager.openContainer(container);
        } else {
            createContainer();
        }
    }
    
    private void createContainer() throws Exception {

        try {
            if (dataManager == null) initManager();
            XmlContainerConfig config = new XmlContainerConfig();
            config.setStatisticsEnabled(true);
            dataContainer = dataManager.createContainer(container,config);
            
            XmlUpdateContext xmlUpdateContext = null;
    
            xmlIndexSpecification = dataContainer.getIndexSpecification();
            xmlIndexSpecification.replaceDefaultIndex("node-element-presence");
            xmlIndexSpecification.addIndex("http://test.namespace/","root","node-element-presence");
    
            xmlUpdateContext = dataManager.createUpdateContext();
            dataContainer.setIndexSpecification(xmlIndexSpecification,xmlUpdateContext);
            
        } finally {
            if (xmlIndexSpecification != null) xmlIndexSpecification.delete();
        }
        
    }

    public void close() throws Exception {
	    closeContainer();
	    closeManager();
	}
    
    public void sync() throws Exception {
        this.dataContainer.sync();
    }    
	
	private void closeContainer() throws Exception {
        if (dataContainer == null) return;
	    if (dataManager == null) initManager();
        long start = System.currentTimeMillis();
        dataContainer.close();
        this.reportDuration("Container closing",start,System.currentTimeMillis());
	}
	
    private void closeManager() throws Exception {
        if (dataManager == null) return;
        long start = System.currentTimeMillis();
        dataManager.close();
        this.reportDuration("Manager closing",start,System.currentTimeMillis());
        start = System.currentTimeMillis();
        dataManager.delete();
        this.reportDuration("Manager deletion",start,System.currentTimeMillis());
    }	
	
    public void deleteContainer() throws Exception {
        long start = System.currentTimeMillis();
        if (dataManager == null) initManager();
        this.reportDuration("System initialisation",start,System.currentTimeMillis());
        closeContainer();
        if (dataManager.existsContainer(container) != 0) {
            start = System.currentTimeMillis();
            dataManager.removeContainer(container);
            this.reportDuration("Container removal",start,System.currentTimeMillis());
        }
    }
    
    private void reportDuration(String action, long start, long end) throws Exception {
        System.out.println(action + " took " + (end-start) + " ms.");
    }    
	
    public void delete() throws Exception {
	    deleteContainer();
	    closeManager();
	}
	
    public void storeDocument() throws Exception {

        XmlUpdateContext xmlUpdateContext = null;
        xmlUpdateContext = dataManager.createUpdateContext();
        String documentId = "" + (counter++);
        String xml = prefix + counter + suffix;
        dataContainer.putDocument(documentId, xml, xmlUpdateContext, null);
	}

}