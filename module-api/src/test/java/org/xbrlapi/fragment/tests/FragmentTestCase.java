package org.xbrlapi.fragment.tests;

import java.net.URI;
import java.util.List;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Fragment;
import org.xbrlapi.Measure;
import org.xbrlapi.Schema;
import org.xbrlapi.TypeDeclaration;
import org.xbrlapi.Unit;
import org.xbrlapi.impl.UnitImpl;
import org.xbrlapi.utilities.Constants;

/**
 * Tests the fragment interface implementation.
 * @see org.xbrlapi.impl.FragmentImpl
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class FragmentTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.local.xbrl.unit.namespace.resolution";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public FragmentTestCase(String arg0) {
		super(arg0);
	}

	public void testNamespaceResolution() {

        try {
            List<Unit> units = store.<Unit>getXMLResources(UnitImpl.class);
            assertTrue(units.size() > 0);
            for (Unit unit: units) {
                logger.info("Starting to get namespace for a new unit.");
                URI namespace = unit.getNamespaceFromQName(unit.getNumeratorMeasures().item(0).getTextContent().trim(),unit.getNumeratorMeasures().item(0));
                logger.info(unit.getId() + " " + namespace);
                if (! unit.getId().equals("u8"))
                    assertEquals("http://xbrlapi.org/metric/"+ unit.getId(),namespace.toString());
                else 
                    assertEquals(Constants.XBRL21Namespace,namespace);
                    
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
	}
	
	public void testIsRoot() {
        try {
            List<Fragment> roots= store.getRootFragments();
            assertTrue(roots.size() > 0);
            for (Fragment root: roots) {
                assertTrue(root.isRoot());
                assertFalse(root.isChild());
                List<Fragment> children = root.getAllChildren();
                for (Fragment child: children) {
                    assertTrue(child.isChild());
                    assertFalse(child.isRoot());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
	}
	
    public void testNamespaceResolutionForQNamesWithoutPrefixes() {

        try {
            Schema schema = store.getSchema(Constants.XBRL21Namespace);
            TypeDeclaration type = schema.getGlobalDeclaration("monetaryItemType");
            TypeDeclaration parentType = type.getParentType();
            store.serialize(parentType.getMetadataRootElement());
            URI ns = parentType.getNamespaceFromQName("decimal",parentType.getDataRootElement());
            assertEquals(Constants.XMLSchemaNamespace,ns);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }	

    public void testNamespaceResolutionGivenMultiplePrefixesForTheOneNamespace() {

        try {
            
            List<Unit> units = store.<Unit>getXMLResources("Unit");
            assertTrue(units.size() > 0);
            for (Unit unit: units) {
                if (unit.getId().equals("u8")) {
                    List<Measure> measures = unit.getResolvedNumeratorMeasures();
                    for (Measure measure: measures) {
                        if (measure.getLocalname().equals("shares")) {
                            assertEquals(Constants.XBRL21Namespace,measure.getNamespace());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
    
    
	
	
}
