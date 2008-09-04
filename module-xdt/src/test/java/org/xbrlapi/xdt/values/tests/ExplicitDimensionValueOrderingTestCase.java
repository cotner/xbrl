package org.xbrlapi.xdt.values.tests;

import java.util.TreeSet;

import org.xbrlapi.Concept;
import org.xbrlapi.FragmentList;
import org.xbrlapi.Item;
import org.xbrlapi.data.XBRLStore;
import org.xbrlapi.xdt.ExplicitDimension;
import org.xbrlapi.xdt.tests.BaseTestCase;
import org.xbrlapi.xdt.values.DimensionValue;
import org.xbrlapi.xdt.values.DimensionValueAccessor;
import org.xbrlapi.xdt.values.DimensionValueAccessorImpl;
import org.xbrlapi.xdt.values.DimensionValueComparator;

/**
 * Tests the identification of XDT fragments.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class ExplicitDimensionValueOrderingTestCase extends BaseTestCase {

    private final String STARTING_POINT = "test.data.local.xdt.several.explicit.dimension.values";
    
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		
	}	
	
	public ExplicitDimensionValueOrderingTestCase(String arg0) {
		super(arg0);
	}

	public void testExplicitDimensionValueOrdering() {
    
    	try {
    
            DimensionValueAccessor dva = new DimensionValueAccessorImpl();
    
            String url = this.getURL(STARTING_POINT);

            logger.info(url);
            
            loader.discover(url);
            
            FragmentList<ExplicitDimension> dimensions = store.<ExplicitDimension>getFragments("org.xbrlapi.xdt.ExplicitDimensionImpl");
            assertEquals(2,dimensions.getLength());
            FragmentList<Item> items = ((XBRLStore) store).getItems();
            assertEquals(2,items.getLength());

            TreeSet <DimensionValue> values = new TreeSet<DimensionValue>(new DimensionValueComparator());

            for (Item item: items) {
                for (ExplicitDimension dimension: dimensions) {
                    DimensionValue value = dva.getValue(item,dimension);
                    if (value != null) {
                        assertTrue(value.isExplicitDimensionValue());
                        assertTrue(value.getValue() != null);
                        values.add(value);
                    }
                }
            }
            
            assertEquals(4, values.size());
            
            for (DimensionValue value: values) {
                store.serialize(value.getItem().getDataRootElement());
                store.serialize(value.getDimension().getDataRootElement());
                store.serialize(((Concept) value.getValue()).getDataRootElement());
            }
            
    	} catch (Exception e) {
    		e.printStackTrace();
    		fail(e.getMessage());
    	}
    }

    
	
	

}
