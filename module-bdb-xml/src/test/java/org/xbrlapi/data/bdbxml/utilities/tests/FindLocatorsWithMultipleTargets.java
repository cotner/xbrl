package org.xbrlapi.data.bdbxml.utilities.tests;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.xbrlapi.ExtendedLink;
import org.xbrlapi.Fragment;
import org.xbrlapi.Locator;
import org.xbrlapi.data.bdbxml.tests.BaseTestCase;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.utilities.Constants;

/**
 * Use this unit test to find XLink locators with multiple targets.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class FindLocatorsWithMultipleTargets extends BaseTestCase {
    
    public FindLocatorsWithMultipleTargets(String arg0) {
        super(arg0);
    }

    private List<URI> resources = new Vector<URI>();
    
    Loader secondLoader = null;
    
    protected void setUp() throws Exception {
        super.setUp();

    }

    protected void tearDown() throws Exception {
        // Close but DO NOT delete the data stores.
        for (int i=0; i<stores.size(); i++) {
            stores.get(i).close();
        }
    }   
    
    public void testRelationshipPersistence() {
        try {

            URI linkbaseURI = new URI("http://www.sec.gov/Archives/edgar/data/1160330/000129281407002220/bbd-20070618_pre.xml");
            Set<String> linkIndices = store.getFragmentIndicesFromDocument(linkbaseURI,"ExtendedLink");
            for (String linkIndex: linkIndices) {
                ExtendedLink link = (ExtendedLink) store.getXMLResource(linkIndex);
                List<Locator> locators = link.getLocators();
                for (Locator locator: locators) {
                    URI targetURI = locator.getTargetDocumentURI();
                    String targetPointer = locator.getTargetPointerValue();
                    String query = "#roots#[@uri='" + targetURI + "' and " + Constants.XBRLAPIPrefix + ":xptr/@value='" + targetPointer + "']";
                    List<Fragment> targets = store.queryForXMLResources(query);
                    logger.info("# targets for locator " + locator.getIndex() + " = " + targets.size());
                    if (targets.size() > 1) {
                        locator.serialize();
                        for (Fragment target: targets) {
                            target.serialize();
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("An unexpected exception was thrown.");
        }
    }
    
}
