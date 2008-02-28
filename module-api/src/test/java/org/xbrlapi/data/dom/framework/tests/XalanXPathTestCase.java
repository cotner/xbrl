package org.xbrlapi.data.dom.framework.tests;
/**
 * Test the Xalan XPath processor implementation
 * of XPath querying of DOM objects where
 * the XPath includes namespaces that are
 * only used on attributes in the DOM object used
 * to create the namespace resolver.
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
*/

import org.apache.xpath.domapi.XPathEvaluatorImpl;
import org.w3c.dom.Document;
import org.w3c.dom.xpath.XPathEvaluator;
import org.w3c.dom.xpath.XPathNSResolver;
import org.w3c.dom.xpath.XPathResult;
import org.xbrlapi.data.dom.XPathNSResolverImpl;
import org.xbrlapi.utilities.BaseTestCase;
import org.xbrlapi.utilities.XMLDOMBuilder;

public class XalanXPathTestCase extends BaseTestCase {
	
	private final String c1 = 
	"<my:root xmlns:my=\"http://www.w3.org/2001/XMLSchema\">\n"
	+ "<my:child xlink:attr=\"value\" xmlns:xlink=\"http://www.w3.org/1999/xlink\"/>\n"
	+ "</my:root>";
	
	private Document d1;
	
	private XPathEvaluator evaluator = null;
	
	private XPathNSResolver resolver = null;
	
	/**
	 * Set up the fixtures.
	 */
	protected void setUp() throws Exception {
		super.setUp();
		d1 = XMLDOMBuilder.newDocument(c1);
		evaluator = new XPathEvaluatorImpl(d1);
		resolver = new XPathNSResolverImpl();
	}

	/**
	 * Tear down the fixtures.
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public XalanXPathTestCase(String arg0) {
		super(arg0);		
	}

	public void testXPathQueryRootElement() {
		String query = "/xsd:root";
		XPathResult result = (XPathResult) evaluator.evaluate(query, d1, resolver, XPathResult.UNORDERED_NODE_SNAPSHOT_TYPE, null);
		assertEquals(1,result.getSnapshotLength());
	}

	public void testXPathQueryChildElement() {
		String query = "/xsd:root/xsd:child";
		XPathResult result = (XPathResult) evaluator.evaluate(query, d1, resolver, XPathResult.UNORDERED_NODE_SNAPSHOT_TYPE, null);
		assertEquals(1,result.getSnapshotLength());
	}
	
	public void testXPathQueryNamespacedAttribute() {
		String query = "/xsd:root/xsd:child/@xlink:attr";
		XPathResult result = (XPathResult) evaluator.evaluate(query, d1, resolver, XPathResult.UNORDERED_NODE_SNAPSHOT_TYPE, null);
		assertEquals(1,result.getSnapshotLength());
	}
	
}
