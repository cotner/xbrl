package org.xbrlapi.fragment.tests;

import java.net.URI;
import java.util.List;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Measure;
import org.xbrlapi.Unit;
import org.xbrlapi.utilities.Constants;

/**
 * Tests the fragment interface implementation.
 * @see org.xbrlapi.impl.FragmentImpl
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class FragmentTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.local.xbrl.unit.namespace.resolution";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public FragmentTestCase(String arg0) {
		super(arg0);
	}

	public void testNamespaceResolution() {

        try {
            List<Unit> units = store.<Unit>getXMLResources("Unit");
            assertTrue(units.size() > 0);
            for (Unit unit: units) {
                URI namespace = unit.getNamespaceFromQName(unit.getId()+":km",unit.getNumeratorMeasures().item(0));
                logger.info(unit.getId() + " " + namespace);
                assertEquals("http://xbrlapi.org/metric/"+ unit.getId(),namespace.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
	}	

    public void testNamespaceResolutionGivenMultiplePrefixesForTheOneNamespace() {

        try {
            
            List<Unit> units = store.<Unit>getXMLResources("Unit");
            assertTrue(units.size() > 0);
            for (Unit unit: units) {
                if (unit.getId().equals("u8")) {
                    List<Measure> measures = unit.getResolvedNumeratorMeasures();
                    for (Measure measure: measures) {
                        if (measure.getLocalname().equals("shares")) {
                            assertEquals(Constants.XBRL21Namespace.toString(),measure.getNamespace());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }   
	
	
}
