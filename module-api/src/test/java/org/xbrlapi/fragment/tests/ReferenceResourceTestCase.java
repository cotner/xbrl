package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.FragmentList;
import org.xbrlapi.ReferencePart;
import org.xbrlapi.ReferenceResource;

/**
 * Tests the implementation of the org.xbrlapi.ReferenceResource interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ReferenceResourceTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.reference.links";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public ReferenceResourceTestCase(String arg0) {
		super(arg0);
	}
	
	/**
	 * Test getting the reference parts in the reference.
	 */
	public void testGetReferenceParts() {	

		try {
			FragmentList<ReferenceResource> fragments = store.<ReferenceResource>getFragments("ReferenceResource");
			ReferenceResource fragment = fragments.getFragment(0);
			assertEquals(1, fragment.getReferenceParts().getLength());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting the reference parts in the reference.
	 */
	public void testGetReferencePart() {	

		try {
			FragmentList<ReferenceResource> fragments = store.<ReferenceResource>getFragments("ReferenceResource");
			ReferenceResource fragment = fragments.getFragment(0);
			ReferencePart part = fragment.getReferenceParts().getFragment(0);
			assertEquals("org.xbrlapi.impl.ReferencePartImpl", fragment.getReferencePart(part.getNamespace(),part.getLocalname()).getType());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	
}
