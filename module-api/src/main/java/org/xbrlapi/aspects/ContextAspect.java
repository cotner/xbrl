package org.xbrlapi.aspects;

import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.Item;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public abstract class ContextAspect extends BaseAspect implements Aspect {

    /**
     * @see Aspect#getFragmentFromStore(Fact)
     */
    public Fragment getFragmentFromStore(Fact fact) throws XBRLException {
        if (fact.isTuple()) {
            throw new XBRLException("The fact must not be a tuple.");
        }
        Item item = (Item) fact;
        return item.getContext();
    }
    
    /**
     * @see Aspect#getFragmentKey(Fact)
     */
    public String getFragmentKey(Fact fact) throws XBRLException {
        if (fact.isTuple()) {
            throw new XBRLException("The fact must not be a tuple.");
        }
        Item item = (Item) fact;
        return item.getURL() + item.getContextId();
    }    
}
