package org.xbrlapi.aspects;

import org.xbrlapi.Fragment;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class BaseAspectValue implements AspectValue {

    private Aspect aspect = null;
    
    private Fragment fragment = null;
    
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

    private void setFragment(Fragment fragment) throws XBRLException {
        if (fragment == null) throw new XBRLException("The fragment must not be null."); 
        this.fragment = fragment;
     }

    /**
     * @see AspectValue#getValue()
     */
    public Aspect getAspect() {
        return aspect;
    }

    /**
     * @see AspectValue#getFragment()
     */
    public Fragment getFragment() {
        return fragment;
    }

    /**
     * @see AspectValue#getValue()
     */
    public String getValue() throws XBRLException {
        return getAspect().getTransformer().transform(this);
    }
    
}
