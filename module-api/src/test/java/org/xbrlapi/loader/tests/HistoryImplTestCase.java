package org.xbrlapi.loader.tests;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.xbrlapi.data.dom.tests.BaseTestCase;
import org.xbrlapi.loader.History;
import org.xbrlapi.loader.HistoryImpl;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class HistoryImplTestCase extends BaseTestCase {
	
	private final String STARTING_POINT = "test.data.small.schema";
	
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public HistoryImplTestCase(String arg0) {
		super(arg0);
	}

	public void testHistoryImpl_LogsDocumentIdentifiers() throws Exception {
		try {
		    loader.setHistory(new HistoryImpl());
		    loader.discover(this.getURI(STARTING_POINT));
		    assertEquals(100,store.getSize());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
    public void testHistoryImpl_ReloadUsingSameIdentifiers() throws Exception {
        try {
            MapHistory history = new MapHistory();
            loader.setHistory(history);
            loader.discover(this.getURI(STARTING_POINT));
            int size = store.getDocumentURIs().size();
            Set<URI> uris = history.getURIs();
            for (URI uri: uris) {
                store.deleteDocument(uri);
            }
            assertEquals(0,store.getDocumentURIs().size());
            MapHistory newHistory = new MapHistory();
            loader.setHistory(newHistory);
            for (URI uri: uris) {
                logger.warn(uri + " = " + history.getIdentifier(uri));
                loader.rediscover(uri,history.getIdentifier(uri));
            }            
            assertEquals(size,store.getDocumentURIs().size());
            for (URI uri: newHistory.getURIs()) {
                assertEquals(newHistory.getIdentifier(uri),history.getIdentifier(uri));
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }	
	
	private class MapHistory extends HistoryImpl implements History {
	
	    private Map<URI, String> map = new HashMap<URI, String>();
	    
	    /**
	     * @see History#addRecord(URI, String)
	     */
	    @Override
	    public void addRecord(URI uri, String identifier) {
	        map.put(uri,identifier);
	    }	    

	    public Set<URI> getURIs() {
	        return map.keySet();
	    }
	    
        public String getIdentifier(URI uri) {
            return map.get(uri);
        }
	    
	}
	
}
