package org.xbrlapi.aspects.tests;

import java.util.List;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Fact;
import org.xbrlapi.aspects.Aspect;
import org.xbrlapi.aspects.AspectModel;
import org.xbrlapi.aspects.AspectValue;
import org.xbrlapi.aspects.ConceptAspect;
import org.xbrlapi.aspects.EntityIdentifierAspect;
import org.xbrlapi.aspects.NonDimensionalAspectModel;
import org.xbrlapi.aspects.PeriodAspect;
import org.xbrlapi.aspects.ScenarioAspect;
import org.xbrlapi.aspects.UnitAspect;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class AspectModelSerializationTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT1 = "test.data.small.instance";
    private final String STARTING_POINT2 = "test.data.small.instance.2";	
	
	protected void setUp() throws Exception {
		super.setUp();
        loader.discover(this.getURI(STARTING_POINT1));       
		loader.discover(this.getURI(STARTING_POINT2));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		
	}	
	
	public AspectModelSerializationTestCase(String arg0) {
		super(arg0);
	}

	public void testCreatingNonDimensionalAspectModel() {
		try {
			List<Fact> facts = store.<Fact>getXMLResources("SimpleNumericItem");
			assertEquals(2,facts.size());
            AspectModel model = new NonDimensionalAspectModel();
            model.arrangeAspect(ConceptAspect.TYPE,"row");
            model.arrangeAspect(EntityIdentifierAspect.TYPE,"row");
            model.arrangeAspect(PeriodAspect.TYPE,"col");
            model.arrangeAspect(UnitAspect.TYPE,"col");
            model.arrangeAspect(ScenarioAspect.TYPE,"col");
            for (Fact fact: facts) {
                model.addFact(fact);
            }
            
            this.assessCustomEquality(model,getDeepCopy(model));
            
            for (Aspect aspect: model.getAspects()) {
                this.assessCustomEquality(aspect,getDeepCopy(aspect));
                for (AspectValue value: aspect.getValues()) {
                    this.assessCustomEquality(value,getDeepCopy(value));
                }
            }
            
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	
}
