package org.xbrlapi.xdt.tests;

import java.util.List;

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
	        loader.discover(this.getURI(EXPLICIT_DIMENSION_STARTING_POINT));
			List<ExplicitDimension> fragments = store.<ExplicitDimension>gets("org.xbrlapi.xdt.ExplicitDimensionImpl");
			assertTrue(fragments.size() > 0);
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
            loader.discover(this.getURI(TYPED_DIMENSION_STARTING_POINT));
            List<TypedDimension> fragments = store.<TypedDimension>gets("org.xbrlapi.xdt.TypedDimensionImpl");
            assertTrue(fragments.size() > 0);
            for (TypedDimension fragment: fragments) {
                assertEquals("org.xbrlapi.xdt.TypedDimensionImpl",fragment.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }	
	
	


}
