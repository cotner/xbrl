package org.xbrlapi.aspects.alt;

import java.net.URI;

import org.xbrlapi.utilities.XBRLException;

public interface AspectHandler {

    /**
     * @return the aspect itself
     * @throws XBRLException if the aspect cannot be retrieved.
     */
    public URI getAspectId() throws XBRLException;
}
