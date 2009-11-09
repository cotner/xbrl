package org.xbrlapi.xdt.tests.aspects;

import java.util.List;

import org.xbrlapi.NonNumericItem;
import org.xbrlapi.aspects.Aspect;
import org.xbrlapi.aspects.AspectValue;
import org.xbrlapi.impl.NonNumericItemImpl;
import org.xbrlapi.xdt.aspects.DimensionalAspectModel;
import org.xbrlapi.xdt.aspects.ExplicitDimensionAspect;
import org.xbrlapi.xdt.tests.BaseTestCase;

/**
 * Tests the identification of XDT fragments.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class DimensionalAspectModelTestCase extends BaseTestCase {

    private final String EXPLICIT_DIMENSIONS = "test.data.local.xdt.several.explicit.dimension.values";
    private final String EXPLICIT_DIMENSIONS_WITH_DEFAULTS = "test.data.local.xdt.several.explicit.dimension.values.with.defaults";
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
	        
	        DimensionalAspectModel model = new DimensionalAspectModel(store);
            model.addFacts(store.<NonNumericItem>getXMLResources(NonNumericItemImpl.class));
            assertEquals(4, model.getAllFacts().size());
            assertEquals(10,model.getAspects().size());
            List<ExplicitDimensionAspect> aspects = model.getExplicitDimensionAspects();
            assertEquals(2, aspects.size());
            for (Aspect aspect: aspects) {
                assertEquals(3, aspect.getValues().size());
                for (AspectValue value: aspect.getValues()) {
                    assertEquals(1, aspect.getFacts(value).size());
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}
	
    public void testDimensionalAspectModelWithDefaults() {

        try {
            loader.discover(this.getURI(this.EXPLICIT_DIMENSIONS_WITH_DEFAULTS));
            DimensionalAspectModel model = new DimensionalAspectModel(store);
            model.addFacts(store.<NonNumericItem>getXMLResources(NonNumericItemImpl.class));
            List<ExplicitDimensionAspect> aspects = model.getExplicitDimensionAspects();
            assertEquals(2, aspects.size());

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

    }
	


}
