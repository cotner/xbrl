package org.xbrlapi.aspects.tests;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Fact;
import org.xbrlapi.FragmentList;
import org.xbrlapi.aspects.Aspect;
import org.xbrlapi.aspects.AspectModel;
import org.xbrlapi.aspects.AspectValue;
import org.xbrlapi.aspects.NonDimensionalAspectModel;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class NonDimensionalAspectModelTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.small.instance";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURL(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		
	}	
	
	public NonDimensionalAspectModelTestCase(String arg0) {
		super(arg0);
	}

	
	

	
	public void testCreatingNonDimensionalAspectModel() {
		try {
			FragmentList<Fact> facts = store.<Fact>getFragments("SimpleNumericItem");
            AspectModel model = new NonDimensionalAspectModel();
            for (Fact fact: facts) {
                model.addFact(fact);
            }
            for (Aspect aspect: model.getAspects()) {
                logger.info(aspect.getType());
                for (AspectValue value: aspect.getValues()) {
                    logger.info(value.getTransformedValue());
                }
            }
            assertEquals(6,model.getAspects().size());
            
            model.setCriterion(model.getAspect(Aspect.CONCEPT).getValues().get(0));
            model.setCriterion(model.getAspect(Aspect.ENTITY_IDENTIFIER).getValues().get(0));
            model.setCriterion(model.getAspect(Aspect.PERIOD).getValues().get(0));
            model.setCriterion(model.getAspect(Aspect.UNIT).getValues().get(0));
            assertEquals(1,model.getMatchingFacts().size());
            
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	
}
