package org.xbrlapi.aspects.alt;

import java.net.URI;
import java.util.List;

import org.xbrlapi.Fact;
import org.xbrlapi.utilities.XBRLException;

public class CacheImpl implements Cache {

    public <V> String getLabel(V object) throws XBRLException {
        // TODO Auto-generated method stub
        return null;
    }

    public <V> String getLabel(V object, List<String> languages)
            throws XBRLException {
        // TODO Auto-generated method stub
        return null;
    }

    public <V> String getLabel(V object, List<String> languages, List<URI> roles)
            throws XBRLException {
        // TODO Auto-generated method stub
        return null;
    }

    public <V extends AspectValue> V getValue(Aspect aspect, Fact fact)
            throws XBRLException {
        // TODO Auto-generated method stub
        return null;
    }

    public <V> boolean hasLabel(V object) throws XBRLException {
        // TODO Auto-generated method stub
        return false;
    }

    public <V> boolean hasLabel(V object, List<String> languages)
            throws XBRLException {
        // TODO Auto-generated method stub
        return false;
    }

    public <V> boolean hasLabel(V object, List<String> languages,
            List<URI> roles) throws XBRLException {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean hasValue(Aspect aspect, Fact fact) throws XBRLException {
        // TODO Auto-generated method stub
        return false;
    }

}
