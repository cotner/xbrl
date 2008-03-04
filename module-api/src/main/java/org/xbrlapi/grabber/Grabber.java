package org.xbrlapi.grabber;

import java.net.URL;
import java.util.List;

/**
 * Defines an XBRL grabber
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public interface Grabber {
	
	/**
	 * @return a list of URLs of XBRL resources provided
	 * by the source.
	 */
	public List<URL> getResources();
}
