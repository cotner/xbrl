package org.xbrlapi.fragment.tests;

/**
 * Tests the implementation of the org.xbrlapi.Fragment interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import org.w3c.dom.Element;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.Schema;
import org.xbrlapi.SimpleLink;
import org.xbrlapi.data.dom.tests.BaseTestCase;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

public class Fragment_LoaderDependentTestCase extends BaseTestCase {

	private final String STARTING_POINT = "test.data.small.schema";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURL(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public Fragment_LoaderDependentTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test throwing of an exception when an attempt is
	 * made to change the data store that the fragment is stored in.
	 * TODO Decide if the Fragment setStore method should be protected rather than public.
	 */
	public void testExceptionExpectedChangingTheFragmentStore() {
		try {
		    FragmentList<Fragment> fragments = store.<Fragment>getFragments("Schema");
		    for (Fragment fragment: fragments) {
		        Fragment f = store.getFragment(fragment.getFragmentIndex());
		        try {
		            f.setStore(store);
	                fail("The store for a fragment cannot be changed once it is set.");
		        } catch (Exception e) {
		            ; // Expected
		        }
		    }
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test retrieval of the type of fragment from a stored fragment.
	 */
	public void testGetFragmentTypeForAStoredFragment() {

        try {
            FragmentList<Fragment> fragments = store.<Fragment>getFragments("Schema");
            assertTrue(fragments.getLength() > 0);
            for (Fragment fragment: fragments) {
                assertEquals("org.xbrlapi.impl.SchemaImpl",fragment.getType());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
	    
	}	

	/**
	 * Test retrieval of the URL of a stored fragment.
	 * TODO Figure out how to test operations on an unstored fragment using the mockfragmentimpl.
	 */
	public void testGetURLOfAStoredFragment() {
        try {
            Fragment fragment = store.getRootFragmentForDocument(this.getURL(STARTING_POINT));
            assertEquals(this.getURL(STARTING_POINT), fragment.getURL());
        } catch (Exception e) {
            fail(e.getMessage());
        }	    
	}	

	/**
	 * Test retrieval of the namespace URI of root node in the fragment
	 * when the fragment has a namespace for the root element.
	 */
	public void testGetNamespaceURIOfAStoredFragmentWithANamespace() {

        try {
            FragmentList<Fragment> fragments = store.<Fragment>getFragments("Schema");
            assertTrue(fragments.getLength() > 0);
            for (Fragment fragment: fragments) {
                assertEquals(Constants.XMLSchemaNamespace,fragment.getNamespaceURI());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }	    
	}
	
	/**
	 * Test retrieval of the local name of root node in the fragment.
	 */
	public void testGetLocalNameOfAStoredFragment() {

        try {
            FragmentList<Fragment> fragments = store.<Fragment>getFragments("Schema");
            assertTrue(fragments.getLength() > 0);
            for (Fragment fragment: fragments) {
                assertEquals("schema",fragment.getLocalname());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }

	}	
	
	
	
	/**
	 * Test retrieval of the sequence to the parent element.
	 */
	public void testGetSequenceToParentElement() {

        try {
            FragmentList<Fragment> fragments = store.<Fragment>getFragments("Schema");
            assertTrue(fragments.getLength() > 0);
            for (Fragment fragment: fragments) {
                FragmentList<Fragment> children = fragment.getAllChildren();
                for (Fragment child: children) {
                    String required = "";
                    if (child.getMetaAttribute("SequenceToParentElement") != null) {
                        required = child.getMetaAttribute("SequenceToParentElement");
                    }
                    assertEquals(required,child.getSequenceToParentElementAsString());
                }
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }	    
		
	}
	
	/**
	 * Test retrieval of the sequence of child simple link fragments
	 */
	public void testGetChildSimpleLinks() {

		try {
			FragmentList<Schema> schemas = store.<Schema>getFragments("Schema");
			for (Schema schema: schemas) {
				if (schema.getURL().equals(this.getURL(STARTING_POINT))) {
					FragmentList<SimpleLink> links = schema.getSimpleLinks();
					assertEquals(2,links.getLength());		
				}
			}
		} catch (XBRLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
	}
	

	/**
	 * Test retrieval of the parent fragment.
	 */
	public void testGetParentFragment() {

        try {
            FragmentList<Fragment> fragments = store.<Fragment>getFragments("Schema");
            assertTrue(fragments.getLength() > 0);
            for (Fragment fragment: fragments) {
                FragmentList<Fragment> children = fragment.getAllChildren();
                for (Fragment child: children) {
                    assertEquals(fragment.getFragmentIndex(),child.getParent().getFragmentIndex());
                }
            }    
		} catch (Exception e) {
			fail(e.getMessage());
		}

	}	
	
	/**
	 * Test retrieval of XPath to parent element.
	 */
	public void testGetXPathToParentElement() {
	    
        try {
            FragmentList<Schema> fragments = store.<Schema>query("/*[@url='" + this.getURL(STARTING_POINT) + "' and @parentIndex='none']");
            assertTrue(fragments.getLength() > 0);
            for (Fragment fragment: fragments) {
                FragmentList<Fragment> children = fragment.getAllChildren();
                Fragment child = children.get(0);
                assertEquals("./*[1]/*[1]",child.getXPath());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }   	    

	}	
	
	/**
	 * Test retrieval of parent element.
	 */
	public void testGetParentElement() {
        try {
            FragmentList<Schema> fragments = store.<Schema>query("/*[@url='" + this.getURL(STARTING_POINT) + "' and @parentIndex='none']");
            assertTrue(fragments.getLength() > 0);
            for (Fragment fragment: fragments) {
                FragmentList<Fragment> children = fragment.getAllChildren();
                Fragment child = children.get(0);
                Fragment parent = child.getParent();
                Element parentElement = child.getParentElement(parent.getDataRootElement());
                assertEquals("appinfo",parentElement.getLocalName());       
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }           

	}
	
	
	
	

}
