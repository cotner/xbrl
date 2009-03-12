package org.xbrlapi.fragment.tests;

import java.util.List;

import org.xbrlapi.Context;
import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Unit;

/**
 * Tests the implementation of the org.xbrlapi.FactDimensionContainer interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class FactDimensionContainerTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.tuple.instance";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public FactDimensionContainerTestCase(String arg0) {
		super(arg0);
	}
	
	/**
	 * Test getting the id of a context.
	 */
	public void testGetContextId() {

		try {
		    List<Context> contexts = store.gets("Context");
		    for (Context context: contexts) {
	            assertEquals(context.getId(), context.getDataRootElement().getAttribute("id"));		        
		    }
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	

	/**
	 * Test getting the id of a unit.
	 */
	public void testGetUnitId() {

		try {
            List<Unit> units= store.gets("Unit");
            for (Unit unit: units) {
                assertEquals(unit.getId(), unit.getDataRootElement().getAttribute("id"));             
            }
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	
}
