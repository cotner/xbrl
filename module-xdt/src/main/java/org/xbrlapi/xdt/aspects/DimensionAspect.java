package org.xbrlapi.xdt.aspects;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import org.apache.log4j.Logger;
import org.xbrlapi.LabelResource;
import org.xbrlapi.aspects.Aspect;
import org.xbrlapi.aspects.AspectModel;
import org.xbrlapi.aspects.AspectValueTransformer;
import org.xbrlapi.aspects.BaseAspect;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xdt.Dimension;
import org.xbrlapi.xdt.values.DimensionValueAccessor;
import org.xbrlapi.xdt.values.DimensionValueAccessorImpl;

/**
 * Provides common functionality for XDT dimension aspects.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public abstract class DimensionAspect extends BaseAspect implements Aspect {

    /**
     * The serial version UID.
     * @see 
     * http://java.sun.com/javase/6/docs/platform/serialization/spec/version.html#6678
     * for information about what changes will require the serial version UID to be
     * modified.
     */
    private static final long serialVersionUID = -4753093179444639254L;

    private static final Logger logger = Logger.getLogger(DimensionAspect.class);

    private Dimension dimension = null;

    transient private DimensionValueAccessor accessor;

    /**
     * @param aspectModel The aspect model with this aspect.
     * @param dimension The dimension defining this aspect.
     * @throws XBRLException.
     */
    public DimensionAspect(AspectModel aspectModel, Dimension dimension) throws XBRLException {
        super(aspectModel);
        initialize(dimension);
    }
    
    protected void initialize(Dimension dimension) throws XBRLException {
        setDimension(dimension);
        accessor = new DimensionValueAccessorImpl();
    }    
    
    /**
     * @param dimension The dimension defining this aspect.
     * @throws XBRLException if the dimension is null.
     */
    private void setDimension(Dimension dimension) throws XBRLException {
        if (dimension == null) throw new XBRLException("The dimension is null.");
        this.dimension = dimension;
    }
    
    /**
     * @return the dimension defining this aspect.
     */
    @SuppressWarnings("unchecked")
    public <D extends Dimension> D getDimension() {
        return (D) dimension;
    }

    /**
     * @return the aspect value accessor.
     */
    public DimensionValueAccessor getAccessor() {
        return accessor;
    }
    
    /**
     * @see Aspect#getType()
     * @throws XBRLException if the dimension does not have a namespace and local name.
     */
    public String getType() throws XBRLException {
        return getDimension().getTargetNamespace() + "#" + getDimension().getName();
    }
 
    /**
     * Handles object inflation.
     * @param in The input object stream used to access the object's serialization.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject( );
        try {
            initialize((Dimension) in.readObject());
        } catch (XBRLException e) {
            throw new IOException("The dimension could not be initialized.",e);
        }
    }
    
    /**
     * Handles object serialization
     * @param out The input object stream used to store the serialization of the object.
     * @throws IOException
     */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(getDimension());
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((dimension == null) ? 0 : dimension.hashCode());
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        DimensionAspect other = (DimensionAspect) obj;
        if (dimension == null) {
            if (other.dimension != null)
                return false;
        } else if (!dimension.equals(other.dimension))
            return false;
        return true;
    }

    /**
     * Where possible the 
     * dimension label is generated from the XBRL labelling
     * of the Dimension concept.
     * @see org.xbrlapi.aspects.Aspect#getLabel()
     * @see org.xbrlapi.aspects.BaseAspect#getLabel()
     */
    @Override
    public String getLabel() throws XBRLException {

        Dimension dimension = this.getDimension();
        AspectValueTransformer transformer = this.getTransformer();
        List<LabelResource> labels = dimension.getLabels(transformer.getLanguageCodes(),transformer.getLabelRoles(),transformer.getLinkRoles());
        if (! labels.isEmpty()) {
            return labels.get(0).getStringValue();
        }

        return super.getLabel();
    }
   
}


