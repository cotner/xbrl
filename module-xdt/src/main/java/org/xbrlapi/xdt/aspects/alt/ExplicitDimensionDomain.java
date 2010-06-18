package org.xbrlapi.xdt.aspects.alt;

import java.net.URI;
import java.util.List;
import java.util.Vector;

import org.xbrlapi.aspects.alt.AspectValue;
import org.xbrlapi.aspects.alt.Base;
import org.xbrlapi.aspects.alt.Domain;
import org.xbrlapi.aspects.alt.StoreHandler;
import org.xbrlapi.data.Store;
import org.xbrlapi.utilities.XBRLException;

public class ExplicitDimensionDomain extends Base implements Domain<ExplicitDimensionAspectValue>, StoreHandler {

    /**
     * 
     */
    private static final long serialVersionUID = 3282971616865549155L;

    /**
     * The namespace of the relevant explicit dimension.
     */
    private URI dimensionNamespace;
    
    /**
     * The local name of the relevant explicit dimension.
     */
    private String dimensionLocalname;

    public ExplicitDimensionDomain(Store store, URI dimensionNamespace, String dimensionLocalname) throws XBRLException {
        super(store);
        if (dimensionNamespace == null) throw new XBRLException("The dimension namespace must not be null.");
        if (dimensionLocalname == null) throw new XBRLException("The dimension localname must not be null.");
        this.dimensionNamespace = dimensionNamespace;
        this.dimensionLocalname = dimensionLocalname;
    }
    
    /**
     * @see Domain#getAllAspectValues()
     */
    public List<ExplicitDimensionAspectValue> getAllAspectValues() throws XBRLException {
        throw new XBRLException("The domain is not finite.");
    }

    /**
     * @see Domain#getChildren(AspectValue)
     */
    public List<ExplicitDimensionAspectValue> getChildren(ExplicitDimensionAspectValue parent)
            throws XBRLException {
        return new Vector<ExplicitDimensionAspectValue>();
    }

    /**
     * @see Domain#getDepth(AspectValue)
     */
    public int getDepth(ExplicitDimensionAspectValue aspectValue) throws XBRLException {
        return 0;
    }

    /**
     * @see Domain#getParent(AspectValue)
     */
    public ExplicitDimensionAspectValue getParent(ExplicitDimensionAspectValue child)
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
    public boolean hasChildren(ExplicitDimensionAspectValue value)
            throws XBRLException {
        return false;
    }

    /**
     * @see Domain#hasParent(AspectValue)
     */
    public boolean hasParent(ExplicitDimensionAspectValue child) throws XBRLException {
        return false;
    }

    /**
     * @see Domain#isInDomain(AspectValue)
     */
    public boolean isInDomain(ExplicitDimensionAspectValue candidate)
            throws XBRLException {
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
    
}
