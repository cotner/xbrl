package org.xbrlapi.fragment.tests;

import java.net.URI;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Fact;
import java.util.List;
import org.xbrlapi.Item;
import org.xbrlapi.Tuple;

/**
 * Tests the implementation of the org.xbrlapi.Tuple interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class TupleTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.tuple.instance";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public TupleTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test getting all child facts.
	 */
	public void testGetAllChildFacts() {

		try {
	        List<Tuple> fragments = store.getFragments("Tuple");
	        assertTrue(fragments.size() > 0);
	        for (Tuple tuple: fragments) {
	            assertEquals(tuple.getAllChildren().size(), tuple.getChildFacts().size());	            
	        }
		
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting some child facts.
	 */
	public void testGetChildFactsByNameAndOrContextRef() {

		try {

		    List<Tuple> tuples = store.getFragments("Tuple");
            assertTrue(tuples.size() > 0);
            for (Tuple tuple: tuples) {
                List<Fact> children = tuple.getChildFacts();
                assertTrue(children.size() > 0);
                for (Fact child: children) {
                    URI namespace = child.getNamespace();
                    String localname = child.getLocalname();
                    assertTrue(tuple.getChildFacts(namespace,localname).size() > 0);
                    if (! child.isTuple()) {
                        String cr = ((Item) child).getContext().getId();
                        assertTrue(tuple.getChildFacts(namespace,localname,cr).size() > 0);
                    }
                }
            }

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	
	

}
