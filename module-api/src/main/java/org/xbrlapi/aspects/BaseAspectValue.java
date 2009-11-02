package org.xbrlapi.aspects;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.xbrlapi.Fragment;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public abstract class BaseAspectValue implements AspectValue {

    private final static Logger logger = Logger.getLogger(BaseAspectValue.class);  
    
    private Aspect aspect;
    
    private Fragment fragment;

    /**
     * @param aspect The aspect with this value
     * @param fragment The fragment expressing this value
     * @throws XBRLException if either parameter is null.
     */
    public BaseAspectValue(Aspect aspect, Fragment fragment) throws XBRLException {
        super();
        setAspect(aspect);
        setFragment(fragment);
    }

    private void setAspect(Aspect aspect) throws XBRLException {
       if (aspect == null) throw new XBRLException("The aspect must not be null."); 
       this.aspect = aspect;
    }

    private void setFragment(Fragment fragment) {
        this.fragment = fragment;
     }

    /**
     * @see AspectValue#getAspect()
     */
    public Aspect getAspect() {
        return aspect;
    }

    /**
     * @see AspectValue#getFragment()
     */
    @SuppressWarnings("unchecked")
    public <F extends Fragment> F getFragment() throws XBRLException {
        try {
            return (F) fragment;
        } catch (ClassCastException e) {
            throw new XBRLException("The fragment is not of the required type.",e);
        }
    }

    /**
     * @see AspectValue#getIdentifier()
     */
    public String getIdentifier() throws XBRLException {
        return getAspect().getTransformer().getIdentifier(this);
    }
    
    /**
     * @see AspectValue#getLabel()
     */
    public String getLabel() throws XBRLException {
        return getAspect().getTransformer().getLabel(this);
    }

    /**
     * @see AspectValue#getParent()
     */
    public AspectValue getParent() throws XBRLException {
        return null;
    }
    
    /**
     * @see AspectValue#hasParent()
     */
    public boolean hasParent() throws XBRLException {
        return false;
    }
    
    /**
     * @see AspectValue#getDepth()
     */
    public int getDepth() throws XBRLException {
        if (! hasParent()) return 0;
        return (this.getParent().getDepth() + 1);
    }
    
    /**
     * @see AspectValue#getChildren()
     */
    public List<AspectValue> getChildren() throws XBRLException {
        return new Vector<AspectValue>();
    }    
 

    /**
     * Handles object serialization
     * @param out The input object stream used to store the serialization of the object.
     * @throws IOException
     */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(aspect);
        out.writeObject(fragment);
   }
    
    /**
     * Handles object inflation.
     * @param in The input object stream used to access the object's serialization.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject( );
        aspect = (Aspect) in.readObject();
        fragment = (Fragment) in.readObject();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fragment == null) ? 0 : fragment.hashCode());
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BaseAspectValue other = (BaseAspectValue) obj;
        if (fragment == null) {
            if (other.fragment != null)
                return false;
        } else if (!fragment.equals(other.fragment))
            return false;
        return true;
    }
    
    /**
     * @see AspectValue#isMissing()
     */
    public boolean isMissing() throws XBRLException {
        return (this.<Fragment>getFragment() == null);
    }
    
}
