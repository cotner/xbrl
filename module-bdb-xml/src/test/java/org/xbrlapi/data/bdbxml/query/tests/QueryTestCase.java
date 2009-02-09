package org.xbrlapi.data.bdbxml.query.tests;


import java.net.URI;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.data.Store;
import org.xbrlapi.data.bdbxml.StoreImpl;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class QueryTestCase extends TestCase {
    
    protected static Logger logger = Logger.getLogger(QueryTestCase.class);  
    
    private final String CONTAINER = "browser";
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

