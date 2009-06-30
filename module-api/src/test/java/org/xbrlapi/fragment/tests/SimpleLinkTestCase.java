package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import java.util.List;
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
		loader.discover(this.getURI(STARTING_POINT));		
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
            List<SimpleLink> links = store.<SimpleLink>getXMLs("SimpleLink");
            assertTrue(links.size() > 0);
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
            List<SimpleLink> links = store.<SimpleLink>getXMLs("SimpleLink");
            assertTrue(links.size() > 0);
            for (SimpleLink link: links) {
                if (link.getLocalname().equals("roleTypeRef")) {
                    assertEquals(configuration.getProperty("test.data.baseURI") + "Common/linkbase/RoleE.xsd#newExtendedRoleType",link.getAbsoluteHref().toString());
                }
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }           
	}

	/**
	 * Test get absolute target fragment
	 */
	public void testGetTarget() {	
        try {
            List<SimpleLink> links = store.<SimpleLink>getXMLs("SimpleLink");
            assertTrue(links.size() > 0);
            for (SimpleLink link: links) {
                if (link.getLocalname().equals("roleTypeRef")) {
                    assertEquals("roleType",link.getTarget().getLocalname());
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
            List<SimpleLink> links = store.<SimpleLink>getXMLs("SimpleLink");
            assertTrue(links.size() > 0);
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
