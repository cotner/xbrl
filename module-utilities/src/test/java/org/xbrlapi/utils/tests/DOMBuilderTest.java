package org.xbrlapi.utils.tests;

import java.net.URI;

import org.xbrlapi.utilities.BaseTestCase;
import org.xbrlapi.utilities.XMLDOMBuilder;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class DOMBuilderTest extends BaseTestCase {
    
    private final String DOCUMENT = "test.data.small.schema";
    private URI uri = null;
    
    public DOMBuilderTest(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
        uri = getURI(DOCUMENT);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }   
    
    public void testDOMConstructionFromAURI() {
        try {
            XMLDOMBuilder builder = new XMLDOMBuilder();
            builder.newDocument(uri);
        } catch (Exception e) {
            e.printStackTrace();
            fail("An unexpected exception was thrown.");
        }
    }
    
}
