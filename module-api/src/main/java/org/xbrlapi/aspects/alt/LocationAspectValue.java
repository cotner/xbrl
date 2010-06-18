package org.xbrlapi.aspects.alt;

import java.net.URI;

import org.apache.log4j.Logger;
import org.xbrlapi.utilities.XBRLException;

public class LocationAspectValue extends AspectValueImpl implements AspectValue {

    /**
     * 
     */
    private static final long serialVersionUID = -9147415332504512016L;

    protected final static Logger logger = Logger
    .getLogger(LocationAspectValue.class);

    /**
     * The index of the fact.
     */
    private String factIndex;
    
    /**
     * @param index The index of the fact.
     * @throws XBRLException if the parameter is null.
     */
    public LocationAspectValue(String index) throws XBRLException {
        super();
        if (index == null) throw new XBRLException("The fact index must not be null.");
        factIndex = index;
    }

    /**
     * @see AspectValue#getId()
     */
    public String getId() {
        return factIndex;
    }

    /**
     * @see AspectHandler#getAspectId()
     */
    public URI getAspectId() {
        return LocationAspect.ID;
    }
    
    /**
     * There is no such thing as a missing location aspect value.
     * @see AspectValue#isMissing()
     */
    public boolean isMissing() {
        return false;
    }
    
    /**
     * @return the index of the fact fragment.
     */
    public String getFactIndex() {
        return this.factIndex;
    }
    
}
