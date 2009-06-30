package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import java.util.List;
import org.xbrlapi.Unit;

/**
 * Tests the implementation of the org.xbrlapi.Unit interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class UnitTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.units.simple";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public UnitTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test checking for denopminator.
	 */
	public void testHasDenominator() {

		try {
		    List<Unit> units = store.<Unit>getXMLs("Unit");
		    assertTrue(units.size() > 0);
		    for (Unit unit: units) {
	            assertFalse(unit.hasDenominator());
		    }
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting numerator measures.
	 */
	public void testGetNumeratorMeasures() {

		try {
            List<Unit> units = store.<Unit>getXMLs("Unit");
            assertTrue(units.size() > 0);
            for (Unit unit: units) {
                assertFalse(unit.hasDenominator());
                assertEquals(2,unit.getNumeratorMeasures().getLength());
            }
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting denominator measures.
	 */
	public void testGetDenominatorMeasures() {

        try {
            List<Unit> units = store.<Unit>getXMLs("Unit");
            assertTrue(units.size() > 0);
            for (Unit unit: units) {
                assertNull(unit.getDenominatorMeasures());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
	}
	
	/**
	 * Test checking equality.
	 */
	public void testCheckEquality() {

        try {
            List<Unit> units = store.<Unit>getXMLs("Unit");
            assertTrue(units.size() > 0);
            for (Unit unit: units) {
                assertTrue(unit.equals(unit));
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
	}	
}
