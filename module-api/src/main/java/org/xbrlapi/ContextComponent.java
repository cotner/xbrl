package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface ContextComponent extends Fragment {
    
    /**
     * Determine the context that contains this component
     *
     * @throws XBRLException
     */
    public Context getContext() throws XBRLException;
    
}
