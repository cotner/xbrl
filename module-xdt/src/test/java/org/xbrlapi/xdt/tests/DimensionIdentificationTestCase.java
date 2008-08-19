package org.xbrlapi.xdt.tests;

import org.xbrlapi.FragmentList;
import org.xbrlapi.xdt.ExplicitDimension;
import org.xbrlapi.xdt.TypedDimension;

/**
 * Tests the identification of XDT fragments.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class DimensionIdentificationTestCase extends BaseTestCase {

    private final String TYPED_DIMENSION_STARTING_POINT = "test.data.xdt.typed.dimension.declaration";
    private final String EXPLICIT_DIMENSION_STARTING_POINT = "test.data.xdt.explicit.dimension.declaration";
    
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		
	}	
	
	public DimensionIdentificationTestCase(String arg0) {
		super(arg0);
	}

	public void testExplicitDimensionIdentification() {

		try {
	        loader.discover(this.getURL(EXPLICIT_DIMENSION_STARTING_POINT));
			FragmentList<ExplicitDimension> fragments = store.<ExplicitDimension>getFragments("org.xbrlapi.xdt.ExplicitDimensionImpl");
			assertTrue(fragments.getLength() > 0);
			for (ExplicitDimension fragment: fragments) {
	            assertEquals("org.xbrlapi.xdt.ExplicitDimensionImpl",fragment.getType());
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
    public void testTypedDimensionIdentification() {

        try {
            loader.discover(this.getURL(TYPED_DIMENSION_STARTING_POINT));
            FragmentList<TypedDimension> fragments = store.<TypedDimension>getFragments("org.xbrlapi.xdt.TypedDimensionImpl");
            assertTrue(fragments.getLength() > 0);
            for (TypedDimension fragment: fragments) {
                assertEquals("org.xbrlapi.xdt.TypedDimensionImpl",fragment.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }	
	
	


}
