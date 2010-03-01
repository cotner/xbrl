package org.xbrlapi.aspects.tests;

import java.net.URI;
import java.util.List;
import java.util.Set;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Fact;
import org.xbrlapi.aspects.Aspect;
import org.xbrlapi.aspects.AspectModel;
import org.xbrlapi.aspects.AspectValue;
import org.xbrlapi.aspects.ConceptAspect;
import org.xbrlapi.aspects.EntityIdentifierAspect;
import org.xbrlapi.aspects.LocationAspect;
import org.xbrlapi.aspects.NonDimensionalAspectModel;
import org.xbrlapi.aspects.PeriodAspect;
import org.xbrlapi.aspects.ScenarioAspect;
import org.xbrlapi.aspects.UnitAspect;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class NonDimensionalAspectModelTestCase extends DOMLoadingTestCase {
	private final String FIRST_SMALL_INSTANCE = "test.data.small.instance";
    private final String SECOND_SMALL_INSTANCE = "test.data.small.instance.2";
    
    private final String TUPLE_INSTANCE = "test.data.local.xbrl.instance.tuples.with.units";
	
	protected void setUp() throws Exception {
		super.setUp();

	}

	protected void tearDown() throws Exception {
		super.tearDown();
		
	}	
	
	public NonDimensionalAspectModelTestCase(String arg0) {
		super(arg0);
	}

	public void testNonDimensionalAspectModel() {
		try {
	        loader.discover(this.getURI(FIRST_SMALL_INSTANCE));       
	        loader.discover(this.getURI(SECOND_SMALL_INSTANCE));
			List<Fact> facts = store.<Fact>getXMLResources("SimpleNumericItem");
			assertEquals(2,facts.size());
            AspectModel model = new NonDimensionalAspectModel();

            assertEquals(7,model.getAspects().size());
            
            model.arrangeAspect(ConceptAspect.TYPE,"row");
            model.arrangeAspect(EntityIdentifierAspect.TYPE,"row");
            model.arrangeAspect(PeriodAspect.TYPE,"col");
            model.arrangeAspect(UnitAspect.TYPE,"col");
            model.arrangeAspect(ScenarioAspect.TYPE,"col");
            model.arrangeAspect(LocationAspect.TYPE,"col");

            assertEquals(2,model.getAxisAspects("row").size());
            assertEquals(4,model.getAxisAspects("col").size());
            
            model.addFacts(facts);
            assertEquals(7,model.getAspects().size());
            assertEquals(facts.size(),model.getFactCount());
                        
            for (Aspect aspect: model.getAspects()) {
                for (AspectValue value: aspect.getValues()) {
                    logger.info(aspect.getType() + " has value " + value.getIdentifier());
                }
            }
            
            model.setCriterion(model.getAspect(ConceptAspect.TYPE).getValues().get(0));
            model.setCriterion(model.getAspect(EntityIdentifierAspect.TYPE).getValues().get(0));
            model.setCriterion(model.getAspect(PeriodAspect.TYPE).getValues().get(0));
            model.setCriterion(model.getAspect(UnitAspect.TYPE).getValues().get(0));
            List<List<AspectValue>> rowMatrix = model.getAspectValueCombinationsForAxis("row");
            List<List<AspectValue>> colMatrix = model.getAspectValueCombinationsForAxis("col");
            
            for (AspectValue v: colMatrix.get(0)) {
                logger.info("First combination: " + v.getAspect().getType() + "=" + v.getIdentifier());
            }
            assertEquals(4,rowMatrix.size());
            assertEquals(2,rowMatrix.get(0).size());
            assertEquals(4,colMatrix.size());
            assertEquals(4,colMatrix.get(0).size());

            for (List<AspectValue> rowCombination: rowMatrix) {
                for (AspectValue rValue: rowCombination) {
                    logger.debug("R: " + rValue.getAspect().getType() + " = " + rValue.getLabel());
                }
                for (List<AspectValue> colCombination: colMatrix) {
                    for (AspectValue cValue: colCombination) {
                        logger.debug("C:" + cValue.getAspect().getType() + " = " + cValue.getLabel());
                    }
                    model.clearAllCriteria();
                    model.setCriteria(rowCombination);
                    model.setCriteria(colCombination);
                    Set<Fact> matchingFacts = model.getMatchingFacts();
                    for (Fact matchingFact: matchingFacts) {
                        logger.debug(matchingFact.getIndex());
                    }
                }
            }
            
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
    public void testLocationAspectHandling() {
        try {
            URI uri = this.getURI(TUPLE_INSTANCE);
            loader.discover(uri);       
            List<Fact> facts = store.getAllFacts(uri);
            assertEquals(8,facts.size());
            AspectModel model = new NonDimensionalAspectModel();
            
            for (Fact fact: facts) {
                model.addFact(fact);
            }
            
            Aspect aspect = model.getAspect(LocationAspect.TYPE);
            assertEquals(8,aspect.getValues().size());
            for (AspectValue value: aspect.getValues()) {
                logger.info(value.getAspect().getType() + "=" + value.getLabel());
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }	
}
