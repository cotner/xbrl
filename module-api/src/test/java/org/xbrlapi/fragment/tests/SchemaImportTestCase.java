package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.SimpleLink;
import org.xbrlapi.utilities.XBRLException;

/**
 * Tests the features of XML Schema Import Handling as a simple link.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class SchemaImportTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.import.custom.link";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURL(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public SchemaImportTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test get target fragment
	 */
	public void testGetTargetFragment() {	

		try {
			FragmentList<SimpleLink> links = store.<SimpleLink>getFragments("SimpleLink");
			for (SimpleLink link: links) {
				try {
					Fragment targetFragment = link.getTargetFragment();
					assertNotNull(targetFragment);
				} catch (XBRLException e) {
					store.serialize(link.getMetadataRootElement());
					e.printStackTrace();
					fail("No exception expected.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
}
