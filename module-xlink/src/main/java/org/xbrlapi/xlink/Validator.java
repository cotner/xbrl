package org.xbrlapi.xlink;

import java.io.File;
import java.net.URI;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Utility handler to perform validation of
 * XLink content in XML documents
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class Validator extends XLinkHandlerDefaultImpl {

	/**
     * 
     */
    private static final long serialVersionUID = 3959551538061351755L;

    /**
	 * The input source to be XLink validated
	 */
	private InputSource is;
	
	/**
	 * The XLink processor
	 */
	private XLinkProcessor xlinkProcessor;
	
	/**
	 * XML DOM document used to capture the error messages
	 */
	private Document document;
	
	/**
	 * Constructor that is given the URI of the document to be validated
	 * @param uri The URI of the document to be validated
	 */
	public Validator(URI uri) {
		super();
		createDocument();
		is = new InputSource(uri.toString());
	}

	/**
	 * Constructor that is given the URI of the document to be validated
	 * @param file The file to be validated
	 */
	public Validator(File file) {
		super();
		createDocument();
		is = new InputSource(file.toURI().toString());		
	}

	/**
	 * Constructor that is given the URI of the 
	 * document to be validated.
	 * @param is The input source for the XML to be validated
	 */
	public Validator(InputSource is) {
		super();
		createDocument();
		this.is = is;
	}

	/**
	 * Create the DOM document that will hold the validation
	 * output.
	 */
	private void createDocument() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.newDocument();
			document.appendChild(document.createElement("messages"));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Returns an XML DOM document that
	 * contains one message element (as a child of the
	 * root message element) for each of the errors and
	 * warnings generated during the validation process.
	 * The message elements contain a 
	 * <ul>
	 * <li>type attribute that takes a value of "error" or "warning"</li>
	 * <li>message attribute that is the text of the message</li>
	 * <li>lName attribute containing the element local name</li> 
	 * <li>qName  attribute containing the element local name</li> 
	 * <li>namespaceURI attribute containing the element's namespace URI</li> 
	 * <li>a duplicate of each of the attribute on the element that generated the message</li> 
	 * </ul> 
	 * @return The DOM document containing the error messages and warning messages
	 */
	public Document getResults() {
		return document;
	}
	
	/**
	 * @see org.xbrlapi.xlink.XLinkHandler#startSimpleLink(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void startSimpleLink(String namespaceURI, String lName,
			String qName, Attributes attrs, String href, String role,
			String arcrole, String title, String show, String actuate)
			throws XLinkException {
		;
	}

	/**
	 * @see org.xbrlapi.xlink.XLinkHandler#endSimpleLink(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endSimpleLink(String namespaceURI, String sName, String qName)
			throws XLinkException {
		;
	}

	/**
	 * @see org.xbrlapi.xlink.XLinkHandler#startTitle(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startTitle(String namespaceURI, String lName, String qName,
			Attributes attrs) throws XLinkException {
		;
	}

	/**
	 * @see org.xbrlapi.xlink.XLinkHandler#endTitle(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endTitle(String namespaceURI, String sName, String qName)
			throws XLinkException {
		;
	}

	/**
	 * @see org.xbrlapi.xlink.XLinkHandler#titleCharacters(char[], int, int)
	 */
	public void titleCharacters(char[] buf, int offset, int len)
			throws XLinkException {
		;
	}

	/**
	 * @see org.xbrlapi.xlink.XLinkHandler#startExtendedLink(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes, java.lang.String, java.lang.String)
	 */
	public void startExtendedLink(String namespaceURI, String lName,
			String qName, Attributes attrs, String role, String title)
			throws XLinkException {
		;
	}

	/**
	 * @see org.xbrlapi.xlink.XLinkHandler#endExtendedLink(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endExtendedLink(String namespaceURI, String sName, String qName)
			throws XLinkException {
		;
	}

	/**
	 * @see org.xbrlapi.xlink.XLinkHandler#startResource(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void startResource(String namespaceURI, String lName, String qName,
			Attributes attrs, String role, String title, String label)
			throws XLinkException {
		;
	}

	/**
	 * @see org.xbrlapi.xlink.XLinkHandler#endResource(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endResource(String namespaceURI, String sName, String qName)
			throws XLinkException {
		;
	}

	/**
	 * @see org.xbrlapi.xlink.XLinkHandler#startLocator(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void startLocator(String namespaceURI, String lName, String qName,
			Attributes attrs, String href, String role, String title,
			String label) throws XLinkException {
		;
	}

	/**
	 * @see org.xbrlapi.xlink.XLinkHandler#endLocator(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endLocator(String namespaceURI, String sName, String qName)
			throws XLinkException {
		;
	}

	/**
	 * @see org.xbrlapi.xlink.XLinkHandler#startArc(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void startArc(String namespaceURI, String lName, String qName,
			Attributes attrs, String from, String to, String arcrole,
			String title, String show, String actuate) throws XLinkException {
		;
	}

	/**
	 * @see org.xbrlapi.xlink.XLinkHandler#endArc(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endArc(String namespaceURI, String sName, String qName)
			throws XLinkException {
		;
	}
	
	/**
	 * @see org.xbrlapi.xlink.XLinkHandler#xmlBaseStart(java.lang.String)
	 */
	public void xmlBaseStart(String value) throws XLinkException {
		;
	}

	/**
	 * @see org.xbrlapi.xlink.XLinkHandler#xmlBaseEnd()
	 */
	public void xmlBaseEnd() throws XLinkException {
		;
	}
	
	/**
	 * Record the error message
	 */
	public void error(String namespaceURI, String lName, String qName,
			Attributes attrs,String message) throws XLinkException {
		recordMessage("error",namespaceURI,lName,qName,attrs,message);
	}
	
	/**
	 * Record the warning
	 */
	public void warning(String namespaceURI, String lName, String qName,
			Attributes attrs,String message) throws XLinkException {
		recordMessage("warning",namespaceURI,lName,qName,attrs,message);
	}
	
	private void recordMessage(String type,String namespaceURI, String lName, String qName,
			Attributes attrs,String message) {
		Element e = document.createElement("message");
		e.setAttribute("type",type);
		e.setAttribute("message",message);
		e.setAttribute("lName",lName);
		e.setAttribute("qName",qName);
		e.setAttribute("namespaceURI",namespaceURI);
		for (int i=0; i<attrs.getLength(); i++) {
			e.setAttributeNS(attrs.getURI(i),attrs.getQName(i),attrs.getValue(i));
		}
		document.getDocumentElement().appendChild(e);
	}
	
	/**
	 * Print the errors and warnings to System out
	 */
	public void printMessages() {
		NodeList nodes = document.getDocumentElement().getChildNodes();
		for (int i=0; i<nodes.getLength(); i++) {
			Element e = (Element) nodes.item(i);
			System.out.println(
					e.getAttribute("namespaceURI") + ": "
					+ e.getAttribute("lName") + ": "
					+ e.getAttribute("type") + ": "
					+ e.getAttribute("message")
					);
		}
	}
	/**
	 * Enable commandline usage of the XLink validator
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			
			// HashMap to facilitate the handling of arguments
			HashMap<String,String> arguments = new HashMap<String,String>();
			
			// Process command line arguments
			int i = 0;
			while (true) {
				if (i == args.length)
					break;
				else if (args[i].charAt(0) == '-') {
	
					if (args[i].equals("-f")) {
						i++;
						arguments.put("file", args[i]);
	
					} else if (args[i].equals("-u")) {
						i++;
						arguments.put("uri", args[i]);
	
					} else
						badUsage("Unknown option " + args[i]);
				}
				i++;
			}
			
			// Create the validator
			Validator validator = null;
			if (arguments.containsKey("file")) {
				File file = new File(arguments.get("file"));
				if (! file.exists()) {
					badUsage("The specified file does not exist");
				} else {
					validator = new Validator(file);
				}
			} else if (arguments.containsKey("uri")) {
				URI uri = new URI(arguments.get("uri"));
				validator = new Validator(uri);
			}
	
			// Create the XLinkProcessor that will find what to validate
			validator.setXLinkProcessor(new XLinkProcessorImpl(validator));
		
			// Do the validation
			validator.validate();			

			// Print out the messages
			validator.printMessages();
			
			System.exit(0);
	
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	/**
	 * Validate the XLink in the input XML 
	 */
	private void validate() {
		try {
			ValidatorSAXContentHandlerImpl handler = new ValidatorSAXContentHandlerImpl(xlinkProcessor);
			XMLReader reader = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
			reader.setContentHandler(handler);
			reader.setFeature("http://xml.org/sax/features/namespaces",true);
			reader.parse(is);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Set the XLinkProcessor for the validator to use
	 * @param xp The XLink Processor to use for validation of XLinks
	 */
	private void setXLinkProcessor(XLinkProcessor xp) {
		this.xlinkProcessor = xp;
	}
	
	/**
	 * Report incorrect usage of the command line XLink validator
	 * 
	 * @param message The error message describing why the 
	 * commandline usage of the XLink validator failed.
	 */
	static protected void badUsage(String message) {
		if (!"".equals(message)) {
			System.err.println(message);
		}
		System.err.println("Command line usage: java org.xbrlapi.impl.LoaderImpl [parameters]");
		System.err.println("Parameters: ");
		System.err.println("  -f    path and filename, in local filesystem, of file to be analysed");
		System.err.println("  -u    URI of file to be analysed");
		if ("".equals(message)) {
			System.exit(0);
		} else {
			System.exit(1);
		}
	}	
}
