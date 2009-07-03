package org.xbrlapi.fragment.tests;

/**
 * Tests the implementation of the org.xbrlapi.Fragment interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import java.util.List;

import org.w3c.dom.Element;
import org.xbrlapi.Fragment;
import org.xbrlapi.Schema;
import org.xbrlapi.SimpleLink;
import org.xbrlapi.data.dom.tests.BaseTestCase;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

public class Fragment_LoaderDependentTestCase extends BaseTestCase {

	private final String STARTING_POINT = "test.data.small.schema";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
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
		    List<Fragment> fragments = store.<Fragment>getXMLResources("Schema");
		    for (Fragment fragment: fragments) {
		        Fragment f = store.getXMLResource(fragment.getIndex());
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
            List<Fragment> fragments = store.<Fragment>getXMLResources("Schema");
            assertTrue(fragments.size() > 0);
            for (Fragment fragment: fragments) {
                assertEquals("org.xbrlapi.impl.SchemaImpl",fragment.getType());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
	    
	}	

	/**
	 * Test retrieval of the URI of a stored fragment.
	 * TODO Figure out how to test operations on an unstored fragment using the mockfragmentimpl.
	 */
	public void testGetURIOfAStoredFragment() {
        try {
            Fragment fragment = store.getRootFragmentForDocument(this.getURI(STARTING_POINT));
            assertEquals(this.getURI(STARTING_POINT), fragment.getURI());
        } catch (Exception e) {
            fail(e.getMessage());
        }	    
	}	

	/**
	 * Test retrieval of the namespace URI of root node in the fragment
	 * when the fragment has a namespace for the root element.
	 */
	public void testGetNamespaceOfAStoredFragmentWithANamespace() {
        try {
            List<Schema> schemas = store.<Schema>getXMLResources("Schema");
            assertTrue(schemas.size() > 0);
            for (Fragment fragment: schemas) {
                assertEquals(Constants.XMLSchemaNamespace,fragment.getNamespace().toString());
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
            List<Fragment> fragments = store.<Fragment>getXMLResources("Schema");
            assertTrue(fragments.size() > 0);
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
            List<Fragment> fragments = store.<Fragment>getXMLResources("Schema");
            assertTrue(fragments.size() > 0);
            for (Fragment fragment: fragments) {
                List<Fragment> children = fragment.getAllChildren();
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
			List<Schema> schemas = store.<Schema>getXMLResources("Schema");
			for (Schema schema: schemas) {
				if (schema.getURI().equals(this.getURI(STARTING_POINT))) {
					List<SimpleLink> links = schema.getSimpleLinks();
					assertEquals(2,links.size());		
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
            List<Fragment> fragments = store.<Fragment>getXMLResources("Schema");
            assertTrue(fragments.size() > 0);
            for (Fragment fragment: fragments) {
                List<Fragment> children = fragment.getAllChildren();
                for (Fragment child: children) {
                    assertEquals(fragment.getIndex(),child.getParent().getIndex());
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
            List<Schema> fragments = store.<Schema>queryForXMLResources("#roots#[@uri='" + this.getURI(STARTING_POINT) + "' and @parentIndex='']");
            assertTrue(fragments.size() > 0);
            for (Fragment fragment: fragments) {
                List<Fragment> children = fragment.getAllChildren();
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
            List<Schema> fragments = store.<Schema>queryForXMLResources("#roots#[@uri='" + this.getURI(STARTING_POINT) + "' and @parentIndex='']");
            assertTrue(fragments.size() > 0);
            for (Fragment fragment: fragments) {
                List<Fragment> children = fragment.getAllChildren();
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
