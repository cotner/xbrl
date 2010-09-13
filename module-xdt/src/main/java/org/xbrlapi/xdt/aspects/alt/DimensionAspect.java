package org.xbrlapi.xdt.aspects.alt;

import java.net.URI;

import org.apache.log4j.Logger;
import org.xbrlapi.aspects.alt.Aspect;
import org.xbrlapi.aspects.alt.AspectImpl;
import org.xbrlapi.aspects.alt.Domain;
import org.xbrlapi.utilities.XBRLException;

/**
 * <h2>XDT dimension aspect details</h2>
 * 
 * <p>
 * This class provides for functionality that is common to 
 * both explicit and typed dimensions.
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public abstract class DimensionAspect extends AspectImpl implements Aspect {

    /**
     * 
     */
    private static final long serialVersionUID = -1064727564185247805L;

    private static final Logger logger = Logger.getLogger(DimensionAspect.class);

    /**
     * The namespace of this dimension.
     */
    private URI dimensionNamespace;
    
    /**
     * The local name of this dimension.
     */
    private String dimensionLocalname;
    
    /**
     * @see Aspect#getId()
     */
    public URI getId() {
        return URI.create(dimensionNamespace + "#" + dimensionLocalname);
    }

    /**
     * @see Aspect#isExtensible()
     */
    @Override
    public boolean isExtensible() {
        return true;
    }
    
    /**
     * @param domain The domain for this aspect.
     * @param namespace The namespace for this dimension aspect.
     * @param localname The local name for this dimension aspect.
     * @throws XBRLException if the dimension namespace or local name are null.
     */
    public DimensionAspect(Domain domain, URI dimensionNamespace, String dimensionLocalname) throws XBRLException {
        super(domain);
        if (dimensionNamespace == null) throw new XBRLException("The dimension namespace must not be null.");
        if (dimensionLocalname == null) throw new XBRLException("The dimension local name must not be null.");
        this.dimensionNamespace = dimensionNamespace;
        this.dimensionLocalname = dimensionLocalname;
    }

    
    public URI getDimensionNamespace() {
        return dimensionNamespace;
    }
    
    public String getDimensionLocalname() {
        return dimensionLocalname;
    }

}
