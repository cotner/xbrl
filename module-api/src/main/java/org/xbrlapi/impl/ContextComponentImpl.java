package org.xbrlapi.impl;

import org.xbrlapi.Context;
import org.xbrlapi.ContextComponent;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ContextComponentImpl extends FragmentImpl implements ContextComponent {

    /**
     * The serial version UID.
     * @see 
     * http://java.sun.com/javase/6/docs/platform/serialization/spec/version.html#6678
     * for information about what changes will require the serial version UID to be
     * modified.
     */
    private static final long serialVersionUID = -4641121755978081684L;

    /**
     * Determine the context that contains this component
     * @throws XBRLException
     * @see org.xbrlapi.ContextComponent#getContext()
     */
    public Context getContext() throws XBRLException {
    	return (Context) this.getAncestorOrSelf("org.xbrlapi.impl.ContextImpl");
    }
    
}
