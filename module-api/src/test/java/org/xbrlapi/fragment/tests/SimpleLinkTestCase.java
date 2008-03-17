package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.FragmentList;
import org.xbrlapi.SimpleLink;

/**
 * Tests the implementation of the org.xbrlapi.SimpleLink interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class SimpleLinkTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.custom.link.role";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURL(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public SimpleLinkTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test get Href
	 */
	public void testGetHref() {	
        try {
            FragmentList<SimpleLink> links = store.<SimpleLink>getFragments("SimpleLink");
            assertTrue(links.getLength() > 0);
            for (SimpleLink link: links) {
                if (link.getLocalname().equals("roleTypeRef")) {
                    assertEquals("RoleE.xsd#newExtendedRoleType",link.getHref());
                }
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }     	    
	}	
	
	/**
	 * Test get absolute Href
	 */
	public void testGetAbsoluteHref() {	
        try {
            FragmentList<SimpleLink> links = store.<SimpleLink>getFragments("SimpleLink");
            assertTrue(links.getLength() > 0);
            for (SimpleLink link: links) {
                if (link.getLocalname().equals("roleTypeRef")) {
                    assertEquals(configuration.getProperty("test.data.baseURL") + "Common/linkbase/RoleE.xsd#newExtendedRoleType",link.getAbsoluteHref().toString());
                }
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }           
	}

	/**
	 * Test get absolute target fragment
	 */
	public void testGetTargetFragment() {	
        try {
            FragmentList<SimpleLink> links = store.<SimpleLink>getFragments("SimpleLink");
            assertTrue(links.getLength() > 0);
            for (SimpleLink link: links) {
                if (link.getLocalname().equals("roleTypeRef")) {
                    assertEquals("roleType",link.getTargetFragment().getLocalname());
                }
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }  
	}
	
	/**
	 * Test get arcrole
	 */
	public void testGetArcrole() {	
        try {
            FragmentList<SimpleLink> links = store.<SimpleLink>getFragments("SimpleLink");
            assertTrue(links.getLength() > 0);
            for (SimpleLink link: links) {
                if (link.getLocalname().equals("roleTypeRef")) {
                    assertEquals("http://www.w3.org/1999/xlink/properties/linkbase",link.getArcrole());
                }
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }    

	}	
	
}
