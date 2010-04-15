package org.xbrlapi.loader.tests;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.xbrlapi.data.dom.tests.BaseTestCase;
import org.xbrlapi.loader.History;
import org.xbrlapi.loader.HistoryImpl;
import org.xbrlapi.loader.Loader;

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
		    assertEquals(382,store.getSize());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
    public void testHistoryImpl_ReloadUsingSameIdentifiers() throws Exception {
        try {
            History history = new MapHistory();
            loader.setHistory(history);
            loader.discover(this.getURI(STARTING_POINT));
            int size = store.getDocumentURIs().size();
            assertEquals(6, size);
            history = loader.getHistory();
            Set<URI> uris = loader.getHistory().getURIs();
            assertEquals(6, uris.size());
            for (URI uri: uris) {
                store.deleteDocument(uri);
            }
            assertEquals(0,store.getDocumentURIs().size());
            Loader newLoader = createLoader(store);
            newLoader.setHistory(history);
            for (URI uri: uris) {
                newLoader.discover(uri);
            }
            assertEquals(size,store.getDocumentURIs().size());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }	
	
	private class MapHistory extends HistoryImpl implements History {
	
	    /**
         * 
         */
        private static final long serialVersionUID = 2536817003874223302L;
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
