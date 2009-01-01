package org.xbrlapi.xdt.tests;

import org.xbrlapi.FragmentList;
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
			FragmentList<Hypercube> fragments = store.<Hypercube>getFragments("org.xbrlapi.xdt.HypercubeImpl");
			assertTrue(fragments.getLength() > 0);
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
            FragmentList<Hypercube> fragments = store.<Hypercube>getFragments("org.xbrlapi.xdt.HypercubeImpl");
            assertTrue(fragments.getLength() > 0);
            for (Hypercube fragment: fragments) {
                FragmentList<Dimension> dimensions = fragment.getDimensions();
                assertTrue(dimensions.getLength() == 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }	


}
