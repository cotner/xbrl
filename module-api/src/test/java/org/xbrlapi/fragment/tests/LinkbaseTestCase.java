package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Fragment;
import java.util.List;
import org.xbrlapi.Linkbase;
import org.xbrlapi.SimpleLink;

/**
 * Tests the implementation of the org.xbrlapi.Linkbase interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class LinkbaseTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.arcrole.reference.instance";
	private final String STARTING_POINT_B = "test.data.linkbase.documentation.element";
    private final String STARTING_POINT_C = "test.data.custom.resource.role";	
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));
        loader.discover(this.getURI(STARTING_POINT_B));     
		loader.discover(this.getURI(STARTING_POINT_C));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public LinkbaseTestCase(String arg0) {
		super(arg0);
	}
	
	/**
	 * Test getting arcrole references from the linkbase.
	 */
	public void testGetArcroleRef() {	
		try {
            List<SimpleLink> links = store.<SimpleLink>getXMLResources("SimpleLink");
            for (SimpleLink link: links) {
                if (link.getLocalname().equals("arcroleRef")) {
                    Linkbase parent = (Linkbase) link.getParent();
                    int count = 0;
                    List<Fragment> children = parent.getAllChildren();
                    for (Fragment child: children) {
                        if (child.getLocalname().equals("arcroleRef")) count++;                            
                    }
                    assertEquals(count,parent.getArcroleRefs().size());
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test getting role references from the linkbase.
	 */
	public void testGetRoleRef() {	

		try {
            List<SimpleLink> links = store.<SimpleLink>getXMLResources("SimpleLink");
            for (SimpleLink link: links) {
                if (link.getLocalname().equals("roleRef")) {
                    Linkbase parent = (Linkbase) link.getParent();
                    int count = 0;
                    List<Fragment> children = parent.getAllChildren();
                    for (Fragment child: children) {
                        if (child.getLocalname().equals("roleRef")) count++;                            
                    }
                    assertEquals(count,parent.getRoleRefs().size());
                }
            }
		} catch (Exception e) {
		    e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting extended links from the linkbase.
	 */
	public void testGetExtendedLinks() {	

		try {
            List<Linkbase> linkbases = store.<Linkbase>getXMLResources("Linkbase");
            for (Linkbase linkbase: linkbases) {
                List<Fragment> children = linkbase.getAllChildren();
                int count = 0;
                for (Fragment child: children) {
                    if (child.getType().equals("org.xbrlapi.impl.ExtendedLinkImpl")) 
                        count++;
                }
                assertEquals(count,linkbase.getExtendedLinks().size());
            }

		} catch (Exception e) {
		    e.printStackTrace();
			fail(e.getMessage());
		}
	}	
	
	/**
	 * Test getting documentation fragments from the linkbase.
	 */
	public void testGetDocumentations() {	

		try {
            List<Linkbase> linkbases = store.<Linkbase>getXMLResources("Linkbase");
            for (Linkbase linkbase: linkbases) {
                List<Fragment> children = linkbase.getAllChildren();
                int count = 0;
                for (Fragment child: children) {
                    if (child.getType().equals("org.xbrlapi.impl.XlinkDocumentationImpl")) 
                        count++;
                }
                logger.info(count);
                assertEquals(count,linkbase.getDocumentations().size());
            }
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}	
	
}
