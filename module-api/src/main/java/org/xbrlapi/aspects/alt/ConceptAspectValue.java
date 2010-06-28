package org.xbrlapi.aspects.alt;

import java.net.URI;

import org.apache.log4j.Logger;
import org.xbrlapi.utilities.XBRLException;

public class ConceptAspectValue extends AspectValueImpl implements AspectValue {

    /**
     * 
     */
    private static final long serialVersionUID = 5049800914749987634L;

    protected final static Logger logger = Logger
    .getLogger(ConceptAspectValue.class);

    /**
     * The namespace of the concept associated with this aspect value.
     */
    private URI namespace;
    
    /**
     * The namespace of the concept associated with this aspect value.
     */
    private String localname;

    /**
     * Missing value constructor
     */
    public ConceptAspectValue() {
        super();
    }
    
    /**
     * @param namespace The namespace of the concept.
     * @param localname The local name of the concept.
     * @throws XBRLException if either of the parameters is null.
     */
    public ConceptAspectValue(URI namespace, String localname) throws XBRLException {
        super();
        if (namespace == null) throw new XBRLException("The namespace for a concept aspect value must not be null.");
        if (localname == null) throw new XBRLException("The local name for a concept aspect value must not be null.");
        this.namespace = namespace;
        this.localname = localname;
    }

    /**
     * @see AspectValue#getId()
     */
    public String getId() {
        if (this.isMissing()) return "missing";
        return namespace + "#" + localname;
    }

    /**
     * @see AspectHandler#getAspectId()
     */
    public URI getAspectId() {
        return ConceptAspect.ID;
    }
    
    /**
     * There is no such thing as a missing concept aspect value.
     * All facts are facts specifying a value for a concept.
     * @see AspectValue#isMissing()
     */
    public boolean isMissing() {
        return false;
    }

    /**
     * @return the namespace of the concept.
     */
    public URI getNamespace() {
        return namespace;
    }
    
    /**
     * @return the local name of the concept.
     */
    public String getLocalname() {
        return this.localname;
    }
    
}
