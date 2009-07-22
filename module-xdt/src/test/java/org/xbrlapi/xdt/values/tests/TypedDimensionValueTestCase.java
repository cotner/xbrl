package org.xbrlapi.xdt.values.tests;

import java.util.List;

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

            loader.discover(this.getURI(STARTING_POINT));
            List<TypedDimension> dimensions = store.<TypedDimension>getXMLResources("org.xbrlapi.xdt.TypedDimensionImpl");
            assertTrue(dimensions.size() > 0);
            List<Item> items = store.<Item>getXMLResources("NonNumericItem");
            assertTrue(items.size() > 0);
            
            boolean foundSomeValues = false;
            for (Item item: items) {
                for (TypedDimension dimension: dimensions) {
                    DimensionValue value = dva.getValue(item,dimension);
                    if (value != null) {
                        foundSomeValues = true;
                        assertTrue(value.isTypedDimensionValue());
                        assertTrue(value.getValue() != null);
                        //store.serialize((Element) value.getValue());
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
