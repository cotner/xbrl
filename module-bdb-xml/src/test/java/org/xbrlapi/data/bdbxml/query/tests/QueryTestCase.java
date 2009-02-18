package org.xbrlapi.data.bdbxml.query.tests;


import java.net.URI;
import java.util.List;
import java.util.TreeMap;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.data.Store;
import org.xbrlapi.data.bdbxml.StoreImpl;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public abstract class QueryTestCase extends TestCase {
    
    protected static Logger logger = Logger.getLogger(QueryTestCase.class);  
    
    private final String CONTAINER = "xbrlapiContainer";
    private final String LOCATION = "/home/geoff/Data/bdbxml";

    private Store store = null;
    
    private int maxDocuments = 10;

    public QueryTestCase(String arg0) {
        super(arg0);
    }

    @Override
    protected void setUp() throws Exception {
        store = new StoreImpl(LOCATION, CONTAINER);
    }

    @Override
    protected void tearDown() throws Exception {
        store.close();
    }

    public final void testStubsRetrieval() throws Exception {

        String query = "/*[@stub]";
        FragmentList<Fragment> fragments = store.<Fragment>query(query);
        for (Fragment stub: fragments) {
            FragmentList<Fragment> referrers = store.<Fragment>query("/*[@targetDocumentURI='"+stub.getURI()+"']");
            TreeMap<URI,String> map = new TreeMap<URI,String>();
            for (Fragment referrer: referrers) {
                if (! map.containsKey(referrer.getURI())) {
                    map.put(referrer.getURI(),"");
                }
            }
            logger.info("---------------------------------------------");
            logger.info(stub.getMetadataRootElement().getAttribute("reason") + ": " + stub.getURI());
            logger.info("This document is referred to by:");
            for (URI uri: map.keySet()) {
                FragmentList<Fragment> sources = store.<Fragment>query("/*[@uri='"+ uri +"' and @targetDocumentURI='"+stub.getURI()+"']");
                logger.info(uri + " contains " + sources.getLength() + " references.");
                //if (sources.getLength() > 0) store.serialize(sources.get(0));
            }
        }
    }    
    
    public final void testWildcardQueryWithSingleResult() {
        try {
            iterateURIs("/*[@uri='","' and @parentIndex='none']");
        } catch (Exception e) {
            e.printStackTrace();    
            fail(e.getMessage());
        }  
    }

    public final void testSpecificNameWithSingleResult() {
        try {
            iterateURIs("/xbrlapi:fragment[@uri='","' and @parentIndex='none']");
        } catch (Exception e) {
            e.printStackTrace();    
            fail(e.getMessage());
        }  
    }

    private void iterateURIs(String prefix, String suffix) throws Exception {
        List<URI> uris = store.getStoredURIs();
        int count = 1;
        long cumulativeDuration = 0;
        URI_LOOP: for (URI uri : uris) {
            count++;
            if (count > maxDocuments) {
                break URI_LOOP;
            }
            long duration = runQuery(prefix + uri + suffix);
            logger.info(uri + " took " + duration);
            cumulativeDuration += duration;
        }
        logger.info("Average duration = " + (cumulativeDuration / count) + " milliseconds.");
        
    }      

    private long runQuery(String query) throws Exception {
        long start = System.currentTimeMillis();
        logger.info(query);
        FragmentList<Fragment> fragments = store.<Fragment>query(query);
        long result = (System.currentTimeMillis() - start);
        assertTrue(fragments.getLength() == 1);
        return result;
    }    
    

}

