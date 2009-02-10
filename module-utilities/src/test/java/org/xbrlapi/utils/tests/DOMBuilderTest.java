package org.xbrlapi.utils.tests;

import java.net.URI;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.xbrlapi.utilities.XMLDOMBuilder;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class DOMBuilderTest extends TestCase {
    protected static Logger logger = Logger.getLogger(DOMBuilderTest.class);    
    
    public DOMBuilderTest(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
    }   
    
    public void testDOMConstructionFromAURI() {
        try {
            XMLDOMBuilder builder = new XMLDOMBuilder();
            builder.newDocument(new URI("http://www.sec.gov/Archives/edgar/data/1316631/000119312508210779/lbtya-20080331.xml"));
        } catch (Exception e) {
            e.printStackTrace();
            fail("An unexpected exception was thrown.");
        }
    }
    
}
