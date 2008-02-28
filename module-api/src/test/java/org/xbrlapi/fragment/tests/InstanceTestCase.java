package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Instance;

/**
 * Tests the implementation of the org.xbrlapi.Instance interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class InstanceTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.tuple.instance";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURL(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public InstanceTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test getting contexts.
	 */
	public void testGetContexts() {

		try {
			Instance fragment = (Instance) store.getFragment("1");
			assertEquals(1, fragment.getContexts().getLength());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting a specific context.
	 */
	public void testGetSpecificContext() {

		try {
			Instance fragment = (Instance) store.getFragment("1");
			assertEquals("context", fragment.getContext("ci").getLocalname());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting units.
	 */
	public void testGetUnits() {

		try {
			Instance fragment = (Instance) store.getFragment("1");
			assertEquals(2, fragment.getUnits().getLength());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting a specific unit.
	 */
	public void testGetSpecificUnit() {

		try {
			Instance fragment = (Instance) store.getFragment("1");
			assertEquals("unit", fragment.getUnit("u1").getLocalname());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting schemaRefs.
	 */
	public void testGetSchemaRefs() {

		try {
			Instance fragment = (Instance) store.getFragment("1");
			assertEquals(1, fragment.getSchemaRefs().getLength());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting linkbaseRefs.
	 */
	public void testGetLinkbaseRefs() {

		try {
			Instance fragment = (Instance) store.getFragment("1");
			assertEquals(0, fragment.getLinkbaseRefs().getLength());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting facts.
	 */
	public void testGetFacts() {

		try {
			Instance fragment = (Instance) store.getFragment("1");
			assertEquals(2, fragment.getFacts().getLength());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting footnote links.
	 */
	public void testGetFootnoteLinks() {

		try {
			Instance fragment = (Instance) store.getFragment("1");
			assertEquals(0, fragment.getFootnoteLinks().getLength());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	
		
}
