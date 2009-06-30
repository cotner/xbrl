package org.xbrlapi.fragment.tests;

import java.util.List;

import org.xbrlapi.Arc;
import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.utilities.Constants;

/**
 * Tests the implementation of the org.xbrlapi.Arc interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ArcSemanticKeyTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.local.xbrl.arc.semantic.key";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public ArcSemanticKeyTestCase(String arg0) {
		super(arg0);
	}
	
	public void testGetSemanticKeyIgnoringNSDeclaration() {	

		try {
			List<Arc> fragments = store.<Arc>getXMLs("Arc");
			Arc fragment = fragments.get(0);
			String key = fragment.getSemanticKey();
			logger.info(key);
			assertFalse(key.contains("xmlns"));
            assertFalse(key.contains(Constants.XLinkNamespace));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	
}
