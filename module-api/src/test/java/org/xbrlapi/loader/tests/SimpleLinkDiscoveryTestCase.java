package org.xbrlapi.loader.tests;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import org.xbrlapi.SimpleLink;
import org.xbrlapi.data.dom.tests.BaseTestCase;
import org.xbrlapi.utilities.XBRLException;

/**
 * This class contains unit tests that 
 * address the ability of to discover all simple
 * links (custom and normal) in XML Schemas.
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class SimpleLinkDiscoveryTestCase extends BaseTestCase {
	
	private final String STARTING_POINT = "real.data.sbr.gov.au.root";
	private URI uri1 = null;
	private List<URI> uris = new LinkedList<URI>();
	
	protected void setUp() throws Exception {
		super.setUp();
        uri1 = getURI(this.STARTING_POINT);
        //store.setAnalyser(new AnalyserImpl(store));
		loader.discover(uri1);
	}
	
	public SimpleLinkDiscoveryTestCase(String arg0) {
		super(arg0);
	}

	public void testDiscoverURIList() {
		try {
		    List<SimpleLink> links = store.<SimpleLink>getXMLResources("SimpleLink");
		    for (SimpleLink link: links) {
		        URI targetURI = link.getTargetDocumentURI();
		        logger.info(link.getIndex() + " references " + targetURI);
		        assertTrue(store.hasDocument(targetURI));
		    }
		} catch (XBRLException e) {
			fail(e.getMessage());
		}		
	}

}
