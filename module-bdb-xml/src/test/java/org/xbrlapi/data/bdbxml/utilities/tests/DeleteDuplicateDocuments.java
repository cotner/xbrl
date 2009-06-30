package org.xbrlapi.data.bdbxml.utilities.tests;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.xbrlapi.XML;
import org.xbrlapi.data.bdbxml.tests.BaseTestCase;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.utilities.XBRLException;

/**
 * Use this unit test to find XLink locators with multiple targets.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class DeleteDuplicateDocuments extends BaseTestCase {
    
    public DeleteDuplicateDocuments(String arg0) {
        super(arg0);
    }

    private List<URI> resources = new Vector<URI>();
    
    Loader secondLoader = null;
    
    protected void setUp() throws Exception {
        super.setUp();

    }

    protected void tearDown() throws Exception {
        // Close but DO NOT delete the data stores.
        for (int i=0; i<stores.size(); i++) {
            stores.get(i).close();
        }
    }   
    
    public void testDeleteDuplicateDocuments() {
        try {

            Map<String,String> map = new HashMap<String,String>();
            Set<String> pairs = store.queryForStrings("for $root in #roots#[@parentIndex=''] return concat($root/@index,' ',$root/@uri)");
            int count = 0;
            for (String pair: pairs) {
                int split = pair.indexOf(" ");
                String index = pair.substring(0,split);
                String documentId = index.substring(0,index.indexOf("_")-1);
                String uri = pair.substring(split+1);
                if (map.containsKey(uri)) {
                    logger.info("Deleting duplicate document stored under index " + index);
                    XML fragment = store.getXMLResource(index);
                    int i = 2;
                    while (fragment != null) {
                        store.remove(fragment);
                        index = documentId + "_" + i;
                        try {
                            fragment = store.getXMLResource(index);
                        } catch (XBRLException e) {
                            fragment = null;
                        }
                        i++;
                    }
                    count++;
                } else {
                    map.put(uri,index);
                }
            }
            logger.info("# of duplicated documents = " + count);
            
            for (int i=10000;i>0;i--) {
                try {
                    String idx = "uqMF4V_" + new Integer(i).toString();
                    store.remove(idx);
                    logger.info("removed fragment " + idx);
                } catch (XBRLException e) {
                    ;
                }
            }            
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("An unexpected exception was thrown.");
        }
    }
    
}
