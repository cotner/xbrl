package org.xbrlapi.grabber;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Implementation of an XBRL document URI grabber for the SEC
 * RSS feed.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class SecGrabberImpl extends AbstractGrabberImpl implements Grabber {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	public SecGrabberImpl(URI source) {
		setSource(source);
	}

	URI source;	
	private void setSource(URI source) {this.source = source; }
	private URI getSource() { return source; }
	
	private static final String NAMESPACE = "http://www.sec.gov/Archives/edgar";
	private static final String NAME = "xbrlFile";
	
	public List<URI> getResources() {
		List<URI> resources = new ArrayList<URI>();
		Document feed = getDocument(getSource());
		NodeList nodes = feed.getElementsByTagNameNS(NAMESPACE,NAME);
		LOOP: for (int i=0; i<nodes.getLength(); i++) {
			Element element = (Element) nodes.item(i);
            String type = element.getAttribute("type");
            String uri = element.getAttribute("url");
            if (! (type.equals("EX-100.INS") || type.equals("EX-101.INS"))) {
                logger.info("Skipping " + uri);
                continue LOOP;// Only interested in certain XBRL instances as entry points.
            }
			if (
					(uri != null) &&
					(
						(uri.endsWith(".xml")) 
						|| (uri.endsWith(".xbrl"))
						|| (uri.endsWith(".xsd"))
					)
				) {
				try {
					resources.add(new URI(uri));
				} catch (URISyntaxException e) {
					logger.warn("SEC source URI: " + uri + " is malformed and has been ignored.");
				}
			}
		}
        logger.info("# URIs in SEC feed = " + resources.size());
		return resources;
	}
	
}
