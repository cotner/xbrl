package org.xbrlapi.fragment.tests;

import org.xbrlapi.Arc;
import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
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
		loader.discover(this.getURL(STARTING_POINT));		
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
			FragmentList<Locator> fragments = store.<Locator>getFragments("Locator");
			Locator fragment = fragments.getFragment(0);
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
			FragmentList<Locator> fragments = store.<Locator>getFragments("Locator");
			Locator fragment = fragments.getFragment(0);
			assertEquals(configuration.getProperty("test.data.baseURL") + "Common/instance/397-ABC.xsd#A", fragment.getAbsoluteHref().toString());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting target fragment.
	 */
	public void testGetTargetFragment() {

		try {
			FragmentList<Locator> fragments = store.<Locator>getFragments("Locator");
			Locator fragment = fragments.getFragment(0);
			Fragment target = fragment.getTargetFragment();
			assertEquals(fragment.getTargetDocumentURL().toString(), target.getURL().toString());
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
			FragmentList<Locator> fragments = store.<Locator>getFragments("Locator");
			Locator fragment = fragments.getFragment(0);
			FragmentList<Arc> arcs = fragment.getArcsFrom();
			assertEquals(1, arcs.getLength());
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
			FragmentList<Locator> fragments = store.<Locator>getFragments("Locator");
			Locator fragment = fragments.getFragment(0);
			FragmentList<Arc> arcs = fragment.getArcsTo();
			assertEquals(0, arcs.getLength());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	
}
