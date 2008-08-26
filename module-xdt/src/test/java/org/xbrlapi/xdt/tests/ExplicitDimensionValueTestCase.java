package org.xbrlapi.xdt.tests;

import org.xbrlapi.FragmentList;
import org.xbrlapi.Item;
import org.xbrlapi.xdt.DimensionValue;
import org.xbrlapi.xdt.DimensionValueAccessor;
import org.xbrlapi.xdt.DimensionValueAccessorImpl;
import org.xbrlapi.xdt.ExplicitDimension;

/**
 * Tests the identification of XDT fragments.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class ExplicitDimensionValueTestCase extends BaseTestCase {

    private final String STARTING_POINT = "test.data.xdt.explicit.dimension.values";
    
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		
	}	
	
	public ExplicitDimensionValueTestCase(String arg0) {
		super(arg0);
	}

	public void testExplicitDimensionValue() {

		try {

	        DimensionValueAccessor dva = new DimensionValueAccessorImpl();

            loader.discover(this.getURL(STARTING_POINT));
            FragmentList<ExplicitDimension> dimensions = store.<ExplicitDimension>getFragments("org.xbrlapi.xdt.ExplicitDimensionImpl");
            assertTrue(dimensions.getLength() > 0);
            FragmentList<Item> items = store.<Item>getFragments("SimpleNumericItem");
            assertTrue(items.getLength() > 0);
	        
            boolean foundSomeValues = false;
	        for (Item item: items) {
	            for (ExplicitDimension dimension: dimensions) {
	                DimensionValue value = dva.getValue(item,dimension.getTargetNamespaceURI(),dimension.getName());
	                if (value != null) {
	                    foundSomeValues = true;
	                    assertTrue(value.isExplicitDimension());
                        assertTrue(value.getExplicitDimensionValue() != null);
	                }
	            }
	        }
	        assertTrue(foundSomeValues);
	        
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
