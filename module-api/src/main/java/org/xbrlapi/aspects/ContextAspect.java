package org.xbrlapi.aspects;

import org.xbrlapi.Context;
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
        try {
            Item item = (Item) fact;
            return item.getContext();
        } catch (ClassCastException e) {
            throw new XBRLException("The fact must be an item.");
        }
    }
    
    /**
     * @see Aspect#getFragmentKey(Fact)
     */
    public String getFragmentKey(Fact fact) throws XBRLException {
        Context context = (Context) getFragmentFromStore(fact);
        return context.getURI() + context.getId();
    }    
}
