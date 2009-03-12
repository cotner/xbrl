package org.xbrlapi.fragment.tests;

import org.xbrlapi.Arc;
import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Fragment;
import java.util.List;
import org.xbrlapi.Locator;

/**
 * Tests the implementation of the org.xbrlapi.Locator interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class LocatorTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.multi.concept.schema";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public LocatorTestCase(String arg0) {
		super(arg0);
	}
	
	/**
	 * Test getting xlink href value.
	 */
	public void testGetHref() {	

		try {
			List<Locator> fragments = store.<Locator>gets("Locator");
			Locator fragment = fragments.get(0);
			assertEquals("397-ABC.xsd#A", fragment.getHref());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	

	/**
	 * Test getting absolute href value.
	 */
	public void testGetAbsoluteHref() {	

		try {
			List<Locator> fragments = store.<Locator>gets("Locator");
			Locator fragment = fragments.get(0);
			assertEquals(configuration.getProperty("test.data.baseURI") + "Common/instance/397-ABC.xsd#A", fragment.getAbsoluteHref().toString());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting target fragment.
	 */
	public void testGetTarget() {

		try {
			List<Locator> fragments = store.<Locator>gets("Locator");
			Locator fragment = fragments.get(0);
			Fragment target = fragment.getTarget();
			assertEquals(fragment.getTargetDocumentURI().toString(), target.getURI().toString());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting arcs from the locator.
	 */
	public void testGetArcsFrom() {

		try {
			List<Locator> fragments = store.<Locator>gets("Locator");
			Locator fragment = fragments.get(0);
			List<Arc> arcs = fragment.getArcsFrom();
			assertEquals(1, arcs.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting arcs to the locator.
	 */
	public void testGetArcsTo() {

		try {
			List<Locator> fragments = store.<Locator>gets("Locator");
			Locator fragment = fragments.get(0);
			List<Arc> arcs = fragment.getArcsTo();
			assertEquals(0, arcs.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	
}
