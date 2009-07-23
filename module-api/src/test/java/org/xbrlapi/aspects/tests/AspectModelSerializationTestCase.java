package org.xbrlapi.aspects.tests;

import java.util.List;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Fact;
import org.xbrlapi.aspects.Aspect;
import org.xbrlapi.aspects.AspectModel;
import org.xbrlapi.aspects.AspectValue;
import org.xbrlapi.aspects.NonDimensionalAspectModel;

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
            model.arrangeAspect(Aspect.CONCEPT,"row");
            model.arrangeAspect(Aspect.ENTITY_IDENTIFIER,"row");
            model.arrangeAspect(Aspect.PERIOD,"col");
            model.arrangeAspect(Aspect.UNIT,"col");
            model.arrangeAspect(Aspect.SCENARIO,"col");
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
