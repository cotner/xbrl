package org.xbrlapi.xdt.aspects.alt;

import java.net.URI;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xbrlapi.aspects.alt.AspectHandler;
import org.xbrlapi.aspects.alt.AspectValue;
import org.xbrlapi.aspects.alt.AspectValueImpl;
import org.xbrlapi.aspects.alt.IDGenerator;
import org.xbrlapi.utilities.XBRLException;

public class TypedDimensionAspectValue extends AspectValueImpl implements AspectValue {

    /**
     * 
     */
    private static final long serialVersionUID = -9066792364652801758L;

    protected final static Logger logger = Logger
    .getLogger(TypedDimensionAspectValue.class);
    
    /**
     * The aspect ID.
     */
    private URI aspectId;
    
    /** 
     * The list of child element and text nodes of the typed dimension value container.
     */
    List<Node> children = null;
    
    /**
     * Missing aspect value constructor.
     */
    public TypedDimensionAspectValue(URI aspectId) throws XBRLException {
        super();
        if (aspectId == null) throw new XBRLException("The aspect ID must not be null.");
        this.aspectId = aspectId;
    }

    /**
     * @param aspectId The aspect ID
     * @param container The element containing the aspect value.
     * @throws XBRLException
     */
    public TypedDimensionAspectValue(URI aspectId, Element container) throws XBRLException {
        super();
        if (aspectId == null) throw new XBRLException("The aspect ID must not be null.");
        this.aspectId = aspectId;
    }

    /**
     * @see AspectHandler#getAspectId()
     */
    public URI getAspectId() {
        return aspectId;
    }
    
    /**
     * @see AspectValue#isMissing()
     */
    public boolean isMissing() {
        return (this.children == null);
    }

    /**
     * The missing aspect value ID is the empty string.
     * @see AspectValue#getId()
     */
    public String getId() {
        if (isMissing()) return "";
        return IDGenerator.getLabelFromMixedNodes(children);
    }
    
}
