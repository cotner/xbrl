package org.xbrlapi.xdt.tests;

import java.util.List;

import org.xbrlapi.xdt.Dimension;
import org.xbrlapi.xdt.Hypercube;

/**
 * Tests the identification of XDT fragments.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class HypercubeTestCase extends BaseTestCase {

    private final String STARTING_POINT = "test.data.xdt.hypercube.declaration";
    
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		
	}	
	
	public HypercubeTestCase(String arg0) {
		super(arg0);
	}

	public void testHypercubeIdentification() {

		try {
	        loader.discover(this.getURI(STARTING_POINT));
			List<Hypercube> fragments = store.<Hypercube>getXMLResources("org.xbrlapi.xdt.HypercubeImpl");
			assertTrue(fragments.size() > 0);
			for (Hypercube fragment: fragments) {
	            assertEquals("org.xbrlapi.xdt.HypercubeImpl",fragment.getType());
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
    public void testGettingDimensionsForAnEmptyHypercube() {

        try {
            loader.discover(this.getURI(STARTING_POINT));
            List<Hypercube> fragments = store.<Hypercube>getXMLResources("org.xbrlapi.xdt.HypercubeImpl");
            assertTrue(fragments.size() > 0);
            for (Hypercube fragment: fragments) {
                List<Dimension> dimensions = fragment.getDimensions();
                assertTrue(dimensions.size() == 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }	


}
