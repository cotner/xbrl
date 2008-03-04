package org.xbrlapi.grabber;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class SecGrabberImpl extends AbstractGrabberImpl implements Grabber {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	public SecGrabberImpl(URL source) {
		setSource(source);
	}

	URL source;	
	private void setSource(URL source) {this.source = source; }
	private URL getSource() { return source; }
	
	private static final String NAMESPACE = "http://www.sec.gov/Archives/edgar";
	private static final String NAME = "xbrlFile";
	
	public List<URL> getResources() {
		List<URL> resources = new ArrayList<URL>();
		Document feed = getDocument(getSource());
		NodeList nodes = feed.getElementsByTagNameNS(NAMESPACE,NAME);
		for (int i=0; i< nodes.getLength(); i++) {
			Element element = (Element) nodes.item(i);
			String url = element.getAttribute("url");
			if (
					(url != null) &&
					(
						(url.endsWith(".xml")) 
						|| (url.endsWith(".xbrl"))
						|| (url.endsWith(".xsd"))
					)
				) {
				try {
					resources.add(new URL(url));
				} catch (MalformedURLException e) {
					logger.info("The SEC source URL was malformed");
				}
			}
		}
		return resources;
	}
	
}
