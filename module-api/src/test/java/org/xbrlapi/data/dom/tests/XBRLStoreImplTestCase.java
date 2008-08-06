package org.xbrlapi.data.dom.tests;

import java.net.URL;
import java.util.HashMap;

import org.xbrlapi.Arc;
import org.xbrlapi.ArcroleType;
import org.xbrlapi.FragmentList;
import org.xbrlapi.RoleType;
import org.xbrlapi.utilities.Constants;

/**
 * Test the XBRL Store implementation of the XBRL specific store functions.
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
*/

public class XBRLStoreImplTestCase extends BaseTestCase {
	private final String STARTING_POINTA = "test.data.custom.link.role";
	private final String STARTING_POINTB = "test.data.custom.link.arcrole";
	private final String STARTING_POINTC = "test.data.custom.resource.role";
	
	
	
	
	protected void setUp() throws Exception {
		super.setUp();
	    loader.stashURL(new URL(Constants.ROLES_URL));
		loader.discover(this.getURL(STARTING_POINTA));
		loader.discover(this.getURL(STARTING_POINTB));	
		loader.discover(this.getURL(STARTING_POINTC));	
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public XBRLStoreImplTestCase(String arg0) {
		super(arg0);
	}

	public void testGetLinkRoles() throws Exception {
		try {
			HashMap<String,String> roles = store.getLinkRoles();
			assertEquals(2, roles.size());
		} catch (Exception e) {
		    e.printStackTrace();
			fail(e.getMessage());
		}
	}

	public void testGetArcRoles() throws Exception {
		try {
			HashMap<String,String> roles = store.getArcRoles();
			assertTrue(roles.size() > 7);
		} catch (Exception e) {
		    e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	public void testGetResourceRoles() throws Exception {
		try {
			HashMap<String,String> roles = store.getResourceRoles();
			assertEquals(3, roles.size());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	public void testGetRoleTypes() throws Exception {
		try {
			FragmentList<RoleType> roleTypes = store.getRoleTypes();
			assertEquals(35, roleTypes.getLength());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	public void testGetArcroleTypes() throws Exception {
		try {
			FragmentList<ArcroleType> arcroleTypes = store.getArcroleTypes();
			assertEquals(15, arcroleTypes.getLength());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	public void testGetLinkrolesForAGivenArcrole() throws Exception {
		try {
			HashMap<String,String> arcroles = store.getArcRoles();
			for (String arcrole: arcroles.keySet()) {
				HashMap<String,String> linkroles = store.getLinkRoles(arcrole);
				logger.info(arcrole + " " + linkroles.size());
			}
		} catch (Exception e) {
		    e.printStackTrace();
			fail(e.getMessage());
		}
	}	
	
	public void testGetSpecificArcroleTypes() throws Exception {
		try {
			FragmentList<ArcroleType> arcroleTypes = store.getArcroleTypes(Constants.PresentationArcRole);
			assertEquals(1, arcroleTypes.getLength());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	public void testGetSpecificRoleTypes() throws Exception {
		try {
			FragmentList<RoleType> roleTypes = store.getRoleTypes(Constants.VerboseLabelRole);
			assertEquals(1, roleTypes.getLength());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	public void testCustomArcroleIsUsedCorrectly() throws Exception {
		try {
			FragmentList<ArcroleType> roleTypes = store.getArcroleTypes(Constants.PresentationArcRole);
			ArcroleType type = roleTypes.get(0);
			assertTrue(type.isUsedOn(Constants.XBRL21LinkNamespace,"presentationArc"));
			assertFalse(type.isUsedOn(Constants.XBRL21LinkNamespace,"calculationArc"));
			FragmentList<Arc> arcs = store.getFragments("Arc");
			for (Arc arc: arcs) {
			    if (arc.getLocalname().equals("presentationArc"))
			        assertTrue(type.isUsedCorrectly(arc));
			    else
                    assertFalse(type.isUsedCorrectly(arc));
			}
		} catch (Exception e) {
		    e.printStackTrace();
			fail(e.getMessage());
		}
	}	
	
	
}
