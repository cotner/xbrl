package org.xbrlapi.aspects.alt;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public interface CacheHandler {

    /**
     * @return The aspect cache used by this object
     * @throws XBRLException
     */
    public boolean getCache() throws XBRLException;

}
