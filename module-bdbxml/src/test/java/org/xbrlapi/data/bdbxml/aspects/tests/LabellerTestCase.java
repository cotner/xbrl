package org.xbrlapi.data.bdbxml.aspects.tests;

import java.util.List;

import org.xbrlapi.Fact;
import org.xbrlapi.aspects.alt.AspectModel;
import org.xbrlapi.aspects.alt.AspectValue;
import org.xbrlapi.aspects.alt.FactSet;
import org.xbrlapi.aspects.alt.FactSetImpl;
import org.xbrlapi.aspects.alt.Labeller;
import org.xbrlapi.data.bdbxml.BaseTestCase;
import org.xbrlapi.impl.AspectValueLabelImpl;
import org.xbrlapi.xdt.aspects.alt.DimensionalAspectModelWithStoreCachingLabellers;


/**
 * Tests XDT store caching aspect labelling
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class LabellerTestCase extends BaseTestCase {
    
    private final String FIRST_SMALL_INSTANCE = "test.data.small.instance";
    private final String SECOND_SMALL_INSTANCE = "test.data.small.instance.2";
    private final String TUPLE_INSTANCE = "test.data.local.xbrl.instance.tuples.with.units";
    private final String EXPLICIT_DIMENSIONS = "test.data.local.xdt.several.explicit.dimension.values";
    private final String EXPLICIT_DIMENSIONS_WITH_DEFAULTS = "test.data.local.xdt.several.explicit.dimension.values.with.defaults";
    private final String TYPED_DIMENSIONS = "test.data.local.xdt.typed.dimension.values";

    private final String MEASURES = "real.data.xbrl.api.measures";
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public LabellerTestCase(String arg0) {
		super(arg0);
	}

	public void testAspectLabelsWithInStoreCaching() {
		try {

			// Set up the aspect model
            AspectModel model = new DimensionalAspectModelWithStoreCachingLabellers(store);
            model.initialise();
            
            // Load and retrieve the facts
            loader.discover(this.getURI(MEASURES));       
            loader.discover(this.getURI(FIRST_SMALL_INSTANCE));       
            loader.discover(this.getURI(SECOND_SMALL_INSTANCE));
            loader.discover(this.getURI(TUPLE_INSTANCE));       
            loader.discover(this.getURI(EXPLICIT_DIMENSIONS));       
            loader.discover(this.getURI(EXPLICIT_DIMENSIONS_WITH_DEFAULTS));       

            List<Fact> facts = store.getAllFacts();
            assertTrue(facts.size() > 0);
            
            // Create the fact set
            FactSet factSet = new FactSetImpl(model);
            factSet.addFacts(facts);

            long fCount = store.getSize();
            
            logger.info(System.currentTimeMillis());
            for (Fact fact: factSet.getFacts()) {
                for (AspectValue value: factSet.getAspectValues(fact)) {
                    @SuppressWarnings("unused")
                    Labeller labeller = model.getLabeller(value.getAspectId());
                    //logger.info(store.getSize() + ": " + labeller.getAspectLabel(null,null,null) + " " + labeller.getAspectValueLabel(value,"en",null,null));
                }
            }
            
            assertEquals(store.getSize(),fCount + store.getNumberOfXMLResources(AspectValueLabelImpl.class));
            
            logger.info(System.currentTimeMillis());
            for (Fact fact: factSet.getFacts()) {
                for (AspectValue value: factSet.getAspectValues(fact)) {
                    @SuppressWarnings("unused")
                    Labeller labeller = model.getLabeller(value.getAspectId());
                    //logger.info(labeller.getAspectLabel(null,null,null) + " " + labeller.getAspectValueLabel(value,"en",null,null));
                }
            }
            logger.info(System.currentTimeMillis());
            
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	
	
	
	
}
