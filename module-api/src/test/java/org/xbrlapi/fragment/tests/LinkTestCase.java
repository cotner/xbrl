package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.ExtendedLink;
import java.util.List;
import org.xbrlapi.Link;
import org.xbrlapi.utilities.Constants;

/**
 * Tests the implementation of the org.xbrlapi.Link interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class LinkTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.custom.link.role";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public LinkTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test getting link role value when it is missing.
	 */
	public void testGetLinkRoles() {	

		try {
		    List<ExtendedLink> links = store.<ExtendedLink>getXMLs("ExtendedLink");
		    for (Link link: links) {
    		    String role = link.getDataRootElement().getAttributeNS(Constants.XLinkNamespace,"role");
    		    if (! role.equals(""))
    		        assertEquals(role,link.getLinkRole().toString());
    		    else
                    assertNull(link.getLinkRole());
		    }
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting link role value when it is provided.
	 */
	public void testGetLinkRoleWhenItProvided() {	
        try {
            List<Link> fragments = store.<Link>getXMLs("ExtendedLink");
            assertTrue(fragments.size() > 0);
            for (Link fragment: fragments) {
                store.serialize(fragment);
                if (fragment.getLocalname().equals("presentationLink")) {
                    assertEquals("http://mycompany.com/xbrl/roleE/newExtendedRoleType",fragment.getLinkRole().toString());
                }
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
	}
	
		
	
}
