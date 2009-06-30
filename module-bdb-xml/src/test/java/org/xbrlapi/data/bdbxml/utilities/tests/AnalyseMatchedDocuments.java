package org.xbrlapi.data.bdbxml.utilities.tests;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.xbrlapi.Match;
import org.xbrlapi.data.bdbxml.tests.BaseTestCase;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.utilities.Constants;

/**
 * Use this unit test to analyse the matched documents.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class AnalyseMatchedDocuments extends BaseTestCase {
    
    public AnalyseMatchedDocuments(String arg0) {
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
    
    public void testAnalyseDuplicateDocument() {
        try {

            Set<URI> uris = store.getDocumentURIs();
            logger.info("# Documents = " + uris.size());
            
            List<Match> matches = store.<Match>getXMLs("Match");
            logger.info("# matches = " + matches.size());
            for (Match match: matches) {
                if (match.getMetadataRootElement().getElementsByTagNameNS(Constants.XBRLAPINamespace,"match").getLength() > 1)
                    match.serialize();
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail("An unexpected exception was thrown.");
        }
    }
    
}
