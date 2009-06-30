package org.xbrlapi.loader.tests;

import java.net.URI;
import java.util.Set;

import org.xbrlapi.data.dom.tests.BaseTestCase;

/**
 * Test the loader handling of partial loading situations.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class LoadingDuplicateDocumentsTestCase extends BaseTestCase {

	private final String VALID_URI = "test.data.local.xbrl.presentation.simple";

    private final String DUPLICATE_INSTANCE_URI = "test.data.local.xbrl.duplicate.instance";

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public LoadingDuplicateDocumentsTestCase(String arg0) {
		super(arg0);
	}

	public void testLoaderHandlingOfDuplicateInstances() throws Exception {
		try {
            loader.stashURI(getURI(this.VALID_URI));
            
            logger.info(getURI(this.VALID_URI));
            
            loader.discover();
            Set<URI> uris = store.getDocumentURIs();
            int first = uris.size();
	        loader.stashURI(getURI(this.DUPLICATE_INSTANCE_URI));
            loader.discover();
            uris = store.getDocumentURIs();
            int second = uris.size();
            
            for (URI uri: uris) {
                logger.info(uri);
            }
            
            assertTrue(first == second);
            
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}


	
	


}
