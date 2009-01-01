package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
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
            FragmentList<SimpleLink> links = store.<SimpleLink>getFragments("SimpleLink");
            for (SimpleLink link: links) {
                if (link.getLocalname().equals("arcroleRef")) {
                    Linkbase parent = (Linkbase) link.getParent();
                    int count = 0;
                    FragmentList<Fragment> children = parent.getAllChildren();
                    for (Fragment child: children) {
                        if (child.getLocalname().equals("arcroleRef")) count++;                            
                    }
                    assertEquals(count,parent.getArcroleRefs().getLength());
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
            FragmentList<SimpleLink> links = store.<SimpleLink>getFragments("SimpleLink");
            for (SimpleLink link: links) {
                if (link.getLocalname().equals("roleRef")) {
                    Linkbase parent = (Linkbase) link.getParent();
                    int count = 0;
                    FragmentList<Fragment> children = parent.getAllChildren();
                    for (Fragment child: children) {
                        if (child.getLocalname().equals("roleRef")) count++;                            
                    }
                    assertEquals(count,parent.getRoleRefs().getLength());
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
            FragmentList<Linkbase> linkbases = store.<Linkbase>getFragments("Linkbase");
            for (Linkbase linkbase: linkbases) {
                FragmentList<Fragment> children = linkbase.getAllChildren();
                int count = 0;
                for (Fragment child: children) {
                    if (child.getType().equals("org.xbrlapi.impl.ExtendedLinkImpl")) 
                        count++;
                }
                assertEquals(count,linkbase.getExtendedLinks().getLength());
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
            FragmentList<Linkbase> linkbases = store.<Linkbase>getFragments("Linkbase");
            for (Linkbase linkbase: linkbases) {
                FragmentList<Fragment> children = linkbase.getAllChildren();
                int count = 0;
                for (Fragment child: children) {
                    if (child.getType().equals("org.xbrlapi.impl.XlinkDocumentationImpl")) 
                        count++;
                }
                logger.info(count);
                assertEquals(count,linkbase.getDocumentations().getLength());
            }
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}	
	
}
