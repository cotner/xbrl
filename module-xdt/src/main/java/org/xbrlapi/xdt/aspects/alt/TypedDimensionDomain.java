package org.xbrlapi.xdt.aspects.alt;

import java.net.URI;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.xbrlapi.aspects.alt.AspectValue;
import org.xbrlapi.aspects.alt.Base;
import org.xbrlapi.aspects.alt.Domain;
import org.xbrlapi.aspects.alt.StoreHandler;
import org.xbrlapi.data.Store;
import org.xbrlapi.utilities.XBRLException;

public class TypedDimensionDomain extends Base implements Domain, StoreHandler {

    /**
     * 
     */
    private static final long serialVersionUID = -6321737264369339787L;

    protected final static Logger logger = Logger.getLogger(TypedDimensionDomain.class);

    /**
     * The namespace of the relevant explicit dimension.
     */
    private URI dimensionNamespace;
    
    /**
     * The local name of the relevant explicit dimension.
     */
    private String dimensionLocalname;

    public TypedDimensionDomain(Store store, URI dimensionNamespace, String dimensionLocalname) throws XBRLException {
        super(store);
        if (dimensionNamespace == null) throw new XBRLException("The dimension namespace must not be null.");
        if (dimensionLocalname == null) throw new XBRLException("The dimension localname must not be null.");
        this.dimensionNamespace = dimensionNamespace;
        this.dimensionLocalname = dimensionLocalname;
    }
    
    public URI getAspectId() { return URI.create(this.dimensionNamespace + "#" + this.dimensionLocalname); }
    
    /**
     * @see Domain#getAllAspectValues()
     */
    public List<AspectValue> getAllAspectValues() throws XBRLException {
        throw new XBRLException("The domain is not finite.");
    }

    /**
     * @see Domain#getChildren(AspectValue)
     */
    public List<AspectValue> getChildren(AspectValue parent)
            throws XBRLException {
        return new Vector<AspectValue>();
    }

    /**
     * @see Domain#getDepth(AspectValue)
     */
    public int getDepth(AspectValue aspectValue) throws XBRLException {
        return 0;
    }

    /**
     * @see Domain#getParent(AspectValue)
     */
    public AspectValue getParent(AspectValue child)
            throws XBRLException {
        return null;
    }

    /**
     * @see Domain#getSize()
     */
    public long getSize() throws XBRLException {
        throw new XBRLException("The domain is not finite.");
    }

    /**
     * @see Domain#hasChildren(AspectValue)
     */
    public boolean hasChildren(AspectValue value)
            throws XBRLException {
        return false;
    }

    /**
     * @see Domain#hasParent(AspectValue)
     */
    public boolean hasParent(AspectValue child) throws XBRLException {
        return false;
    }

    /**
     * @see Domain#isInDomain(AspectValue)
     */
    public boolean isInDomain(AspectValue candidate)
            throws XBRLException {
        if (! (candidate instanceof TypedDimensionAspectValue)) return false;
        return true;

    }

    /**
     * @see Domain#isFinite()
     */
    public boolean isFinite() {
        return false;
    }

    /**
     * Returns true.
     * @see Domain#allowsMissingValues()
     */
    public boolean allowsMissingValues() {
        return true;
    }

    /**
     * @return the namespace of the dimension that this is a domain for.
     */
    public URI getDimensionNamespace() {
        return dimensionNamespace;
    }

    /**
     * @return the local name of the dimension that this is a domain for.
     */
    public String getDimensionLocalname() {
        return dimensionLocalname;
    }

    /**
     * @param first
     *            The first aspect value
     * @param second
     *            The second aspect value
     * @return -1 if the first aspect value is less than the second, 0 if they
     *         are equal and 1 if the first aspect value is greater than the
     *         second. Any aspect values that are not in this domain
     *         are placed last in the aspect value ordering.
     *         Otherwise, the comparison is based upon the natural ordering of
     *         the aspect value IDs.
     *         Missing values are ranked last among aspect values of the same type.
     */
    public int compare(AspectValue first, AspectValue second) {
        if (! (first instanceof TypedDimensionAspectValue)) {
            logger.error("Aspect values of the wrong type are being compared.");
            return 1;
        }
        if (! (second instanceof TypedDimensionAspectValue)) {
            logger.error("Aspect values of the wrong type are being compared.");
            return -1;
        }

        if (first.isMissing()) {
            if (second.isMissing()) return 0;
            return 1;
        }
        if (second.isMissing()) return -1;
        return first.getId().compareTo(second.getId());
    }         
}
