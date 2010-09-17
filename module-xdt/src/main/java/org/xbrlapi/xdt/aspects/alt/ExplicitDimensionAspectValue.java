package org.xbrlapi.xdt.aspects.alt;

import java.net.URI;

import org.apache.log4j.Logger;
import org.xbrlapi.aspects.alt.AspectHandler;
import org.xbrlapi.aspects.alt.AspectValue;
import org.xbrlapi.aspects.alt.AspectValueImpl;
import org.xbrlapi.utilities.XBRLException;

public class ExplicitDimensionAspectValue extends AspectValueImpl implements AspectValue {

    /**
     * 
     */
    private static final long serialVersionUID = -6980141374852428096L;

    protected final static Logger logger = Logger
    .getLogger(ExplicitDimensionAspectValue.class);
    
    /**
     * The aspect ID.
     */
    private URI aspectId;
    
    /**
     * The URI namespace of the member.
     */
    private URI namespace;
    
    /**
     * The local name of the member.
     */
    private String localName;
    
    /**
     * Missing aspect value constructor.
     */
    public ExplicitDimensionAspectValue(URI aspectId) throws XBRLException {
        super();
        if (aspectId == null) throw new XBRLException("The aspect ID must not be null.");
        this.aspectId = aspectId;
    }
    
    /**
     * @param aspectId The aspect ID.
     * @param namespace The dimension member namespace.
     * @param name The dimension member local name.
     * @throws XBRLException if the aspectId is null.
     */
    public ExplicitDimensionAspectValue(URI aspectId, URI namespace, String name) throws XBRLException {
        this(aspectId);
        if (namespace == null) throw new XBRLException("The member namespace must not be null.");
        if (name == null) throw new XBRLException("The local name must not be null.");
        this.namespace = namespace;
        this.localName = name;
    }
    
    /**
     * @see AspectHandler#getAspectId()
     */
    public URI getAspectId() {
        return aspectId;
    }

    /**
     * @return the namespace of the member.
     */
    public URI getNamespace() {
        return namespace;
    }

    /**
     * @return the local name of the member.
     */
    public String getLocalname() {
        return localName;
    }
    
    /**
     * @see AspectValue#isMissing()
     */
    public boolean isMissing() {
        return (namespace == null);
    }

    /**
     * The missing aspect value ID is the empty string.
     * @see AspectValue#getId()
     */
    public String getId() {
        if (isMissing()) return "";
        return namespace + "#" + localName;
    }
    
}
