package org.xbrlapi.aspects.alt;

import java.net.URI;

import org.apache.log4j.Logger;

public class TupleAspectValue extends AspectValueImpl implements AspectValue {

    /**
     * 
     */
    private static final long serialVersionUID = 4497623473009130035L;

    protected final static Logger logger = Logger.getLogger(TupleAspectValue.class);

    /**
     * The boolean flag indicating if the fact is a tuple or not.
     */
    private Boolean isTuple = null;
    
    /**
     * Missing value constructor
     */
    public TupleAspectValue() {
        super();
    }
    
    /**
     * @param isTuple true if the fact is a tuple or a child of a tuple and false otherwise.
     */
    public TupleAspectValue(boolean isTuple) {
        super();
        this.isTuple = new Boolean(isTuple);
    }

    /**
     * @see AspectValue#getId()
     */
    public String getId() {
        if (this.isMissing()) return "";
        if (isTuple) return "tuple";
        return "simple";
    }

    /**
     * @see AspectHandler#getAspectId()
     */
    public URI getAspectId() {
        return TupleAspect.ID;
    }
    
    /**
     * @see AspectValue#isMissing()
     */
    public boolean isMissing() {
        return isTuple == null;
    }
    
}
