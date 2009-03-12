package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import java.util.List;
import org.xbrlapi.ReferencePart;
import org.xbrlapi.ReferencePartDeclaration;
import org.xbrlapi.ReferenceResource;

/**
 * Tests the implementation of the org.xbrlapi.ReferencePart interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class ReferencePartTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.reference.links";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public ReferencePartTestCase(String arg0) {
		super(arg0);
	}
	
	public void testGetValue() {	

		try {
			List<ReferenceResource> fragments = store.<ReferenceResource>gets("ReferenceResource");
			assertTrue(fragments.size() > 0);
			ReferenceResource fragment = fragments.get(0);
			
			List<ReferencePart> parts = fragment.getReferenceParts();
			assertTrue(parts.size() > 0);
			
			ReferencePart part = parts.get(0);
			assertEquals("Fixed Assets", part.getValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	public void testGetReferencePartDeclaration() {	
		try {
			List<ReferenceResource> fragments = store.<ReferenceResource>gets("ReferenceResource");
			ReferenceResource fragment = fragments.get(0);			
			ReferencePart part = fragment.getReferenceParts().get(0);
			store.serialize(part.getDataRootElement());
			ReferencePartDeclaration declaration = part.getDeclaration();
			assertEquals(part.getLocalname(),declaration.getName());
			assertEquals(part.getNamespace(),declaration.getTargetNamespace());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}		
}
