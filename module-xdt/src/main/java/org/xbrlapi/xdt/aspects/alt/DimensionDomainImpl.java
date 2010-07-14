package org.xbrlapi.xdt.aspects.alt;

import java.net.URI;

import org.apache.log4j.Logger;
import org.xbrlapi.aspects.alt.Domain;
import org.xbrlapi.aspects.alt.DomainImpl;
import org.xbrlapi.data.Store;
import org.xbrlapi.utilities.XBRLException;

public abstract class DimensionDomainImpl extends DomainImpl implements Domain {

    /**
     * 
     */
    private static final long serialVersionUID = 3554939207956749455L;

    protected final static Logger logger = Logger.getLogger(DimensionDomainImpl.class);

    /**
     * The namespace of the relevant explicit dimension.
     */
    private URI dimensionNamespace;
    
    /**
     * The local name of the relevant explicit dimension.
     */
    private String dimensionLocalname;

    public DimensionDomainImpl(Store store, URI dimensionNamespace, String dimensionLocalname) throws XBRLException {
        super(store);
        if (dimensionNamespace == null) throw new XBRLException("The dimension namespace must not be null.");
        if (dimensionLocalname == null) throw new XBRLException("The dimension localname must not be null.");
        this.dimensionNamespace = dimensionNamespace;
        this.dimensionLocalname = dimensionLocalname;
    }

    /**
     * @see Domain#getAspectId()
     */
    public URI getAspectId() {
        return URI.create(this.dimensionNamespace + "#" + this.dimensionLocalname);
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
     * @param dimensionNamespace the dimensionNamespace to set
     * @throws XBRLException if the parameter is null
     */
    private void setDimensionNamespace(URI dimensionNamespace) throws XBRLException {
        if (dimensionNamespace == null) throw new XBRLException("The namespace of the dimension must not be null.");
        this.dimensionNamespace = dimensionNamespace;
    }

    /**
     * @param dimensionLocalname the dimensionLocalname to set
     * @throws XBRLException if the parameter is null
     */
    private void setDimensionLocalname(String dimensionLocalname) throws XBRLException 
    {
        if (dimensionLocalname == null) throw new XBRLException("The local name of the dimension must not be null.");
        this.dimensionLocalname = dimensionLocalname;
    }
        
}
