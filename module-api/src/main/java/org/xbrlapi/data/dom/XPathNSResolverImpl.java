package org.xbrlapi.data.dom;

/**
 * Implements the namespace resolver for the Xalan XPath functionality
 * available as part of the DOM data store.
 * 
 * The class provides an implementation of XPathNSResolver according 
 * to the DOM L3 XPath Specification, Working Group Note 26 February 2004.
 *
 * <p>
 * See <a href='http://www.w3.org/TR/2004/NOTE-DOM-Level-3-XPath-20040226'>
 * Document Object Model (DOM) Level 3 XPath Specification</a>.
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */

import java.util.HashMap;
import java.util.Map;

import org.apache.xml.utils.PrefixResolver;
import org.w3c.dom.xpath.XPathNSResolver;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

public class XPathNSResolverImpl implements XPathNSResolver, PrefixResolver {

	Map<String,String> map = new HashMap<String,String>();
	
	/**
	 * Constructor for XPathNSResolverImpl.
	 * Builds a mapping from prefixes to namespaces for 
	 * the namespaces covered by the XBRLAPI constants.
	 */
	public XPathNSResolverImpl() {
		map.put(Constants.XBRL21LinkPrefix,Constants.XBRL21LinkNamespace);
		map.put(Constants.XBRL21Prefix,Constants.XBRL21Namespace);
		map.put(Constants.XBRLAPIPrefix,Constants.XBRLAPINamespace);
		map.put(Constants.XBRLAPILanguagesPrefix,Constants.XBRLAPILanguagesNamespace);
		map.put(Constants.XLinkPrefix,Constants.XLinkNamespace);
		map.put(Constants.XMLPrefix,Constants.XMLNamespace);
		map.put(Constants.XMLSchemaPrefix,Constants.XMLSchemaNamespace);
	}
	
	public void setNamespaceBinding(String prefix,String namespace) throws XBRLException {
	    if (namespace == null) throw new XBRLException("Null namespace being bound.");
        if (prefix == null) throw new XBRLException("Null prefix being bound.");
	    map.put(prefix,namespace);
	}

	/**
	 * Given a namespace, get the corresponding prefix.
	 * @param prefix The prefix to resolve.
	 * @return namespace that prefix resolves to, or 
	 * null if the prefix is not mapped.
	 */
	public String lookupNamespaceURI(String prefix) {
		try {
			return map.get(prefix);
		} catch (Exception e) {
			return null;
		}
	}
	
	  /**
	 * Given a namespace, get the corrisponding prefix.  This assumes that
	 * the PrefixResolver holds its own namespace context, or is a namespace
	 * context itself.
	 *
	 * @param prefix The prefix to look up, which may be an empty string ("") 
	 * for the default Namespace.
	 *
	 * @return The associated Namespace URI, or null if the prefix
	 *         is undeclared in this context.
	 */
	public String getNamespaceForPrefix(String prefix) {
		return lookupNamespaceURI(prefix);
	}

	/**
	 * Given a namespace, get the corresponding prefix, based on the context node.
	 *
	 * @param prefix The prefix to look up, which may be an empty string ("") 
	 * for the default Namespace.
	 * @param context The node context from which to look up the URI.
	 *
	 * @return The associated Namespace URI as a string, or null if the prefix
	 *         is undeclared in this context.
	 */
	public String getNamespaceForPrefix(String prefix, org.w3c.dom.Node context) {
		return lookupNamespaceURI(prefix);
	}

	/**
	 * Return the base identifier.
	 * @return null.
	 */
	public String getBaseIdentifier() {
		return null;
	}

	/**
	 * Indicates if the implementation handles mapping of null
	 * prefixes to namespaces.
	 * @return false
	 */
	public boolean handlesNullPrefixes() {
		return false;
	}
}
