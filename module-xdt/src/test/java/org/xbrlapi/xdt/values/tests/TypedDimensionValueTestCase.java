package org.xbrlapi.xdt.values.tests;

import org.w3c.dom.Element;
import org.xbrlapi.FragmentList;
import org.xbrlapi.Item;
import org.xbrlapi.xdt.TypedDimension;
import org.xbrlapi.xdt.tests.BaseTestCase;
import org.xbrlapi.xdt.values.DimensionValue;
import org.xbrlapi.xdt.values.DimensionValueAccessor;
import org.xbrlapi.xdt.values.DimensionValueAccessorImpl;

/**
 * Tests the identification of XDT fragments.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class TypedDimensionValueTestCase extends BaseTestCase {

    private final String STARTING_POINT = "test.data.xdt.typed.dimension.values";
    
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		
	}	
	
	public TypedDimensionValueTestCase(String arg0) {
		super(arg0);
	}

	public void testTypedDimensionValue() {

		try {

            DimensionValueAccessor dva = new DimensionValueAccessorImpl();

            loader.discover(this.getURL(STARTING_POINT));
            FragmentList<TypedDimension> dimensions = store.<TypedDimension>getFragments("org.xbrlapi.xdt.TypedDimensionImpl");
            assertTrue(dimensions.getLength() > 0);
            FragmentList<Item> items = store.<Item>getFragments("NonNumericItem");
            assertTrue(items.getLength() > 0);
            
            boolean foundSomeValues = false;
            for (Item item: items) {
                for (TypedDimension dimension: dimensions) {
                    DimensionValue value = dva.getValue(item,dimension);
                    if (value != null) {
                        foundSomeValues = true;
                        assertTrue(value.isTypedDimensionValue());
                        assertTrue(value.getValue() != null);
                        store.serialize((Element) value.getValue());
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
