package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Fact;
import org.xbrlapi.FragmentList;
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
		loader.discover(this.getURL(STARTING_POINT));		
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
	        FragmentList<Tuple> fragments = store.getFragments("Tuple");
	        assertTrue(fragments.getLength() > 0);
	        for (Tuple tuple: fragments) {
	            assertEquals(tuple.getAllChildren().getLength(), tuple.getChildFacts().getLength());	            
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

		    FragmentList<Tuple> tuples = store.getFragments("Tuple");
            assertTrue(tuples.getLength() > 0);
            for (Tuple tuple: tuples) {
                FragmentList<Fact> children = tuple.getChildFacts();
                assertTrue(children.getLength() > 0);
                for (Fact child: children) {
                    String ns = child.getNamespaceURI();
                    String ln = child.getLocalname();
                    assertTrue(tuple.getChildFacts(ns,ln).getLength() > 0);
                    if (! child.isTuple()) {
                        String cr = ((Item) child).getContext().getId();
                        assertTrue(tuple.getChildFacts(ns,ln,cr).getLength() > 0);
                    }
                }
            }

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	
	

}
