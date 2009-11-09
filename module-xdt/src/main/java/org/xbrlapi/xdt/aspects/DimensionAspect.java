package org.xbrlapi.xdt.aspects;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.xbrlapi.LabelResource;
import org.xbrlapi.aspects.Aspect;
import org.xbrlapi.aspects.AspectModel;
import org.xbrlapi.aspects.BaseAspect;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xdt.Dimension;
import org.xbrlapi.xdt.values.DimensionValueAccessor;
import org.xbrlapi.xdt.values.DimensionValueAccessorImpl;

/**
 * Provides common functionality for XDT dimension aspects.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public abstract class DimensionAspect extends BaseAspect implements Aspect {

    private final static Logger logger = Logger.getLogger(DimensionAspect.class);

    private Dimension dimension = null;

    private URI role = Constants.StandardLabelRole;
    
    private String language = "en";    
    
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
     * @return the label resource role.
     */
    public URI getLabelRole() {
        return role;
    }
    
    /**
     * @param role The label resource role to use in
     * selecting labels for the concept.
     */
    public void setLabelRole(URI role) {
        this.role = role;
    }    

    /**
     * @return the language code.
     */
    public String getLanguageCode() {
        return language;
    }
    
    /**
     * @param language The ISO language code
     */
    public void setLanguageCode(String language) throws XBRLException {
        if (language == null) throw new XBRLException("The language must not be null.");
        this.language = language;
    }
        
    
    /**
     * Handles object inflation.
     * @param in The input object stream used to access the object's serialization.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject( );
        try {
            initialize((Dimension) in.readObject());
            language = (String) in.readObject();
            role = (URI) in.readObject();
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
        out.writeObject(getLanguageCode());
        out.writeObject(getLabelRole());
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
        result = prime * result
                + ((language == null) ? 0 : language.hashCode());
        result = prime * result + ((role == null) ? 0 : role.hashCode());
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
        if (language == null) {
            if (other.language != null)
                return false;
        } else if (!language.equals(other.language))
            return false;
        if (role == null) {
            if (other.role != null)
                return false;
        } else if (!role.equals(other.role))
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

        List<String> languages = new Vector<String>();
        languages.add(getLanguageCode());
        languages.add(null);
        List<URI> roles = new Vector<URI>();
        roles.add(getLabelRole());
        roles.add(null);
        Dimension dimension = this.getDimension();
        List<LabelResource> labels = dimension.getLabels(languages,roles);
        if (! labels.isEmpty()) {
            return labels.get(0).getStringValue();
        }

        return super.getLabel();
    }
   
}


