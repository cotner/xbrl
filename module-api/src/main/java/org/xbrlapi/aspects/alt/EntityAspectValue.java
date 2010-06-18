package org.xbrlapi.aspects.alt;

import java.net.URI;

import org.apache.log4j.Logger;
import org.xbrlapi.utilities.XBRLException;

public class EntityAspectValue extends AspectValueImpl implements AspectValue {

    /**
     * 
     */
    private static final long serialVersionUID = -5629903281523671450L;

    protected final static Logger logger = Logger
    .getLogger(EntityAspectValue.class);

    /**
     * The entity identification scheme.
     */
    private URI scheme;
    
    /**
     * The entity identification value.
     */
    private String value;
    
    /**
     * Missing aspect value constructor - relevant for tuples and nil facts.
     */
    public EntityAspectValue() {
    }
    
    /**
     * @param scheme The namespace of the concept.
     * @param value The local name of the concept.
     * @throws XBRLException if either of the parameters is null.
     */
    public EntityAspectValue(URI scheme, String value) throws XBRLException {
        super();
        if (scheme == null) throw new XBRLException("The scheme for an entity aspect value must not be null.");
        if (value == null) throw new XBRLException("The value for an entity aspect value must not be null.");
        this.scheme = scheme;
        this.value = value;
    }

    /**
     * @see AspectValue#getId()
     */
    public String getId() {
        if (scheme == null) return "";
        return scheme + "#" + value;
    }

    /**
     * @see AspectHandler#getAspectId()
     */
    public URI getAspectId() {
        return EntityAspect.ID;
    }
    
    /**
     * There is no such thing as a missing concept aspect value.
     * All facts are facts specifying a value for a concept.
     * @see AspectValue#isMissing()
     */
    public boolean isMissing() {
        return scheme == null;
    }

    /**
     * @return the entity identification scheme or null for missing values.
     */
    public URI getScheme() {
        return scheme;
    }
    
    /**
     * @return the entity identification value or null for missing values.
     */
    public String getValue() {
        return this.value;
    }
    
}
