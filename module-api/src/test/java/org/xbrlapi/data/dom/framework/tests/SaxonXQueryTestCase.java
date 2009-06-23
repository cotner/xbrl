package org.xbrlapi.data.dom.framework.tests;

import net.sf.saxon.s9api.DOMDestination;
import net.sf.saxon.s9api.Destination;
import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.XQueryCompiler;
import net.sf.saxon.s9api.XQueryEvaluator;
import net.sf.saxon.s9api.XQueryExecutable;
import net.sf.saxon.s9api.XdmNode;

import org.w3c.dom.Document;
import org.xbrlapi.data.Store;
import org.xbrlapi.data.dom.StoreImpl;
import org.xbrlapi.utilities.BaseTestCase;
import org.xbrlapi.utilities.XMLDOMBuilder;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
*/

public class SaxonXQueryTestCase extends BaseTestCase {
	
	private final String data = 
	"<my:root xmlns:my=\"http://www.w3.org/2001/XMLSchema\">\n"
	+ "<my:child xlink:attr=\"value\" xmlns:xlink=\"http://www.w3.org/1999/xlink\"/>\n"
	+ "</my:root>";
	

	
	private XQueryCompiler compiler = null;
	
	Processor processor = null;
	
    XMLDOMBuilder builder = null;
	
    XdmNode source = null;

    Document outputDocument = null;
    
    Destination destination = null;

    private Store store = null;

    
    /**
	 * Set up the fixtures.
	 */
	protected void setUp() throws Exception {
		super.setUp();
		
		try {

		    store = new StoreImpl();
		    
            processor = new Processor(false);

            compiler = processor.newXQueryCompiler();

            builder = new XMLDOMBuilder();
            Document sourceDocument = builder.newDocument(data);
            DocumentBuilder sdb = processor.newDocumentBuilder();
            source = sdb.wrap(sourceDocument);        
            
            builder = new XMLDOMBuilder();
            outputDocument = builder.newDocument();
            destination = new DOMDestination(outputDocument);
    
		} catch (Exception e) {
		    e.printStackTrace();
		    fail(e.getMessage());
		}
        
	}

	/**
	 * Tear down the fixtures.
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public SaxonXQueryTestCase(String arg0) {
		super(arg0);		
	}

	public void testXPathQueryRootElement() {
	    try {
    	    String query = "/xsd:root";
            compiler.declareNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
            XQueryExecutable executable = compiler.compile(query);
            XQueryEvaluator evaluator = executable.load();
            evaluator.setContextItem(source);
            evaluator.run(destination);
            assertEquals(1,outputDocument.getChildNodes().getLength());
            store.serialize(outputDocument.getDocumentElement());
	    } catch (Exception e) {
	        e.printStackTrace();
	        fail(e.getMessage());
	    }
	}


	

	
}
