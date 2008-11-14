package org.xbrlapi.aspects;

import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import org.xbrlapi.utilities.XBRLException;

/**
 * Abstract implementation of common aspect methods.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
abstract public class BaseAspect implements Aspect {

    TreeMap<String,AspectValue> values = new TreeMap<String,AspectValue>();
    
    private AspectModel model = null;

    /**
     * @see org.xbrlapi.aspects.Aspect#getAspectModel()
     */
    public AspectModel getAspectModel() {
        return model;
    }

    private String dimension = null;
    
    /**
     * @see org.xbrlapi.aspects.Aspect#getDimension()
     */
    public String getDimension() {
        return dimension;
    }

    /**
     * @see org.xbrlapi.aspects.Aspect#getLength()
     */
    public int getLength() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * @see org.xbrlapi.aspects.Aspect#getValues()
     */
    public List<AspectValue> getValues() {
        List<AspectValue> v = new Vector<AspectValue>();
        v.addAll(values.values());
        return v;
    }
    
    /**
     * @see org.xbrlapi.aspects.Aspect#addValue()
     */
    public void addValue(AspectValue value) throws XBRLException {
        values.put(this.getTransformer().transform(value), value);
    }    

    /**
     * @see org.xbrlapi.aspects.Aspect#isEmpty()
     */
    public boolean isEmpty() {
        return values.isEmpty();
    }

    /**
     * @see org.xbrlapi.aspects.Aspect#isSingular()
     */
    public boolean isSingular() {
        return (this.getLength() == 1);
    }
    
    /**
     * @see org.xbrlapi.aspects.Aspect#setAspectModel(org.xbrlapi.aspects.AspectModel)
     */
    public void setAspectModel(AspectModel aspectModel) throws XBRLException {
        if (aspectModel == null) throw new XBRLException("The aspect model must not be null.");
        model = aspectModel;
    }

    /**
     * @see org.xbrlapi.aspects.Aspect#setDimension(java.lang.String)
     */
    public void setDimension(String dimension) {
        this.dimension = dimension;
    }
    
    private AspectValueTransformer transformer = null;

    /**
     * @see Aspect#getTransformer()
     */
    public AspectValueTransformer getTransformer() {
        return transformer;
    }

    /**
     * @see Aspect#setTransformer(AspectValueTransformer)
     */
    public void setTransformer(AspectValueTransformer transformer) {
        this.transformer = transformer;
    }
    
    
}
