package org.xbrlapi.xdt.aspects.tests;

import java.util.List;

import org.xbrlapi.Item;
import org.xbrlapi.aspects.AspectModel;
import org.xbrlapi.xdt.aspects.DimensionalAspectModel;
import org.xbrlapi.xdt.tests.BaseTestCase;

/**
 * Tests the identification of XDT fragments.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class DimensionalAspectModelTestCase extends BaseTestCase {

    private final String EXPLICIT_DIMENSIONS = "test.data.local.xdt.several.explicit.dimension.values";
    private final String TYPED_DIMENSIONS = "test.data.local.xdt.typed.dimension.values";
    
    
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		
	}	
	
	public DimensionalAspectModelTestCase(String arg0) {
		super(arg0);
	}

	public void testDimensionalAspectModelConstruction() {

		try {
	        loader.discover(this.getURI(this.EXPLICIT_DIMENSIONS));
            loader.discover(this.getURI(this.TYPED_DIMENSIONS));
	        
	        AspectModel model = new DimensionalAspectModel();
	        
			List<Item> fragments = store.<Item>gets("NonNumericItem");
			assertTrue(fragments.size() > 0);
			for (Item fragment: fragments) {
			    model.addFact(fragment);
			}
            assertEquals(9,model.getAspects().size());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}
	
	


}
