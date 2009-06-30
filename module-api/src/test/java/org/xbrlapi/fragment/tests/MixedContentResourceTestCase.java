package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import java.util.List;
import org.xbrlapi.MixedContentResource;

/**
 * Tests the implementation of the org.xbrlapi.MixedContentResource interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class MixedContentResourceTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.label.links";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public MixedContentResourceTestCase(String arg0) {
		super(arg0);
	}
	
	/**
	 * Test getting the language code.
	 */
	public void testGetLanguageCode() {	

		try {			
			List<MixedContentResource> fragments = store.<MixedContentResource>getXMLs("LabelResource");
			MixedContentResource fragment = fragments.get(0);
			assertEquals("en", fragment.getLanguage());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting the content as a node list.
	 */
	public void testGetContent() {	

		try {
			List<MixedContentResource> fragments = store.<MixedContentResource>getXMLs("LabelResource");
			MixedContentResource fragment = fragments.get(0);
			assertEquals(1, fragment.getContent().getLength());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	

}
