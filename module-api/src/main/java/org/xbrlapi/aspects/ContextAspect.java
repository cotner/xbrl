package org.xbrlapi.aspects;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.xbrlapi.Context;
import org.xbrlapi.Fact;
import org.xbrlapi.Item;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public abstract class ContextAspect extends BaseAspect implements Aspect {

    private final static Logger logger = Logger.getLogger(ContextAspect.class);
    
    public ContextAspect(AspectModel model) throws XBRLException {
        super(model);
        initialize();
    }
    
    protected void initialize() {
        ;
    }
    
    /**
     * @return the context of the fact.
     * @see Aspect#getFragmentFromStore(Fact)
     */
    protected Context getContextFromStore(Fact fact) throws XBRLException {
        if (fact.isTuple()) return null;
        Item item = (Item) fact;
        return item.getContext();
    }
    
    /**
     * @see Aspect#getKey(Fact)
     */
    public String getKey(Fact fact) throws XBRLException {
        Context context = getContextFromStore(fact);
        if (context == null) return "";
        String key = context.getURI().toString() + "#" + context.getId();
        return key;
    }
    
    /**
     * Handles object inflation.
     * @param in The input object stream used to access the object's serialization.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject( );
        initialize();
    }
    
    /**
     * Handles object serialization
     * @param out The input object stream used to store the serialization of the object.
     * @throws IOException
     */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }
    
    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return super.hashCode();
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
       return true;
    }
    
}
