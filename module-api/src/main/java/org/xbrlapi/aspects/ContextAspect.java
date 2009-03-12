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
     * @see Aspect#getFromStore(Fact)
     */
    public Fragment getFromStore(Fact fact) throws XBRLException {
        try {
            Item item = (Item) fact;
            return item.getContext();
        } catch (ClassCastException e) {
            throw new XBRLException("The fact must be an item.");
        }
    }
    
    /**
     * @see Aspect#getKey(Fact)
     */
    public String getKey(Fact fact) throws XBRLException {
        Context context = (Context) getFromStore(fact);
        return context.getURI() + context.getId();
    }    
}
