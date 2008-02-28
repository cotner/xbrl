package org.xbrlapi.impl;

import org.xbrlapi.Context;
import org.xbrlapi.ContextComponent;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ContextComponentImpl extends FragmentImpl implements ContextComponent {

    /**
     * Determine the context that contains this component
     * @throws XBRLException
     * @see org.xbrlapi.ContextComponent#getContext()
     */
    public Context getContext() throws XBRLException {
    	return (Context) this.getAncestorOrSelf("org.xbrlapi.impl.ContextImpl");
    }
    
}
