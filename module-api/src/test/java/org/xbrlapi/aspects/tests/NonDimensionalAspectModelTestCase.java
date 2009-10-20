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

	
	

	
	public void testCreatingNonDimensionalAspectModel() {
		try {
	        loader.discover(this.getURI(FIRST_SMALL_INSTANCE));       
	        loader.discover(this.getURI(SECOND_SMALL_INSTANCE));
			List<Fact> facts = store.<Fact>getXMLResources("SimpleNumericItem");
			assertEquals(2,facts.size());
            AspectModel model = new NonDimensionalAspectModel();
            
            for (Aspect a: model.getAspects()) {
                logger.info(a.getType());
            }
            
            model.arrangeAspect(ConceptAspect.TYPE,"row");
            model.arrangeAspect(EntityIdentifierAspect.TYPE,"row");
            model.arrangeAspect(PeriodAspect.TYPE,"col");
            model.arrangeAspect(UnitAspect.TYPE,"col");
            model.arrangeAspect(ScenarioAspect.TYPE,"col");
            model.arrangeAspect(LocationAspect.TYPE,"col");
            for (Fact fact: facts) {
                model.addFact(fact);
            }
            for (Aspect aspect: model.getAspects()) {
                logger.info(aspect.getType());
                for (AspectValue value: aspect.getValues()) {
                    logger.info(value.getId());
                }
            }
            assertEquals(7,model.getAspects().size());
            
            model.setCriterion(model.getAspect(ConceptAspect.TYPE).getValues().get(0));
            model.setCriterion(model.getAspect(EntityIdentifierAspect.TYPE).getValues().get(0));
            model.setCriterion(model.getAspect(PeriodAspect.TYPE).getValues().get(0));
            model.setCriterion(model.getAspect(UnitAspect.TYPE).getValues().get(0));
            List<List<AspectValue>> rowMatrix = model.getAspectValueCombinationsForDimension("row");
            List<List<AspectValue>> colMatrix = model.getAspectValueCombinationsForDimension("col");
            
            for (AspectValue v: colMatrix.get(0)) {
                if (v==null) logger.info("First combination: " + null + "=" + null);
                else logger.info("First combination: " + v.getAspect().getType() + "=" + v.getId());
            }
            assertEquals(4,rowMatrix.size());
            assertEquals(2,rowMatrix.get(0).size());
            assertEquals(2,colMatrix.size());
            assertEquals(3,colMatrix.get(0).size());
            for (List<AspectValue> rowCombination: rowMatrix) {
                for (List<AspectValue> colCombination: colMatrix) {
                    model.clearAllCriteria();
                    model.setCriteria(rowCombination);
                    model.setCriteria(colCombination);
                    Set<Fact> matchingFacts = model.getMatchingFacts();
                    for (Fact matchingFact: matchingFacts) {
                        logger.info(matchingFact.getIndex());
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
            for (AspectValue value: aspect.getValues()) {
                logger.info(value.getId());
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }	
}
