package org.xbrlapi.xdt.values.tests;

import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.Item;
import org.xbrlapi.xdt.ExplicitDimension;
import org.xbrlapi.xdt.tests.BaseTestCase;
import org.xbrlapi.xdt.values.DimensionValue;
import org.xbrlapi.xdt.values.DimensionValueAccessor;
import org.xbrlapi.xdt.values.DimensionValueAccessorImpl;

/**
 * Tests the identification of XDT fragments.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class ExplicitDimensionValueTestCase extends BaseTestCase {

    private final String STARTING_POINT = "test.data.xdt.explicit.dimension.values";
    private final String DEFAULTS_STARTING_POINT = "test.data.xdt.default.dimension.values";
    
    
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
	                DimensionValue value = dva.getValue(item,dimension);
	                if (value != null) {
	                    foundSomeValues = true;
	                    assertTrue(value.isExplicitDimensionValue());
                        assertTrue(value.getValue() != null);
                        assertTrue(value.equals(value));
	                }
	            }
	        }
	        assertTrue(foundSomeValues);
	        
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
    public void testDefaultDimensionValues() {

        try {

            DimensionValueAccessor dva = new DimensionValueAccessorImpl();

            loader.discover(this.getURL(this.DEFAULTS_STARTING_POINT));
            FragmentList<ExplicitDimension> dimensions = store.<ExplicitDimension>getFragments("org.xbrlapi.xdt.ExplicitDimensionImpl");
            assertTrue(dimensions.getLength() > 0);
            FragmentList<Item> items = store.<Item>getFragments("NonNumericItem");
            assertTrue(items.getLength() > 0);
            
            boolean foundSomeValues = false;
            for (Item item: items) {
                for (ExplicitDimension dimension: dimensions) {
                    DimensionValue value = dva.getValue(item,dimension);
                    if (value != null) {
                        foundSomeValues = true;
                        assertTrue(value.isExplicitDimensionValue());
                        assertTrue(value.getValue() != null);
                        store.serialize((Fragment) value.getValue());
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
