package org.xbrlapi.aspects.alt;

import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.Tuple;
import org.xbrlapi.XML;
import org.xbrlapi.aspects.AspectValue;
import org.xbrlapi.data.Store;
import org.xbrlapi.impl.InstanceImpl;
import org.xbrlapi.impl.TupleImpl;
import org.xbrlapi.utilities.XBRLException;

public class LocationDomain extends Base implements Domain<LocationAspectValue>, StoreHandler {

    /**
     * 
     */
    private static final long serialVersionUID = -2049528909761058435L;

    public LocationDomain(Store store) throws XBRLException {
        super(store);
    }
    
    /**
     * @see Domain#getAllAspectValues()
     */
    public List<LocationAspectValue> getAllAspectValues() throws XBRLException {
        Set<String> factIndices = getStore().queryForIndices("for $root in #roots#[@fact] return $root");
        List<LocationAspectValue> values = new Vector<LocationAspectValue>();
        for (String factIndex: factIndices) {
            LocationAspectValue value = new LocationAspectValue(factIndex);
            values.add(value);
        }
        return values;
    }

    /**
     * @see Domain#getChildren(AspectValue)
     */
    public List<LocationAspectValue> getChildren(LocationAspectValue parent)
            throws XBRLException {
        List<LocationAspectValue> result = new Vector<LocationAspectValue>();
        Fact fact = getStore().<Fact> getXMLResource(parent.getFactIndex());
        if (fact.isa(TupleImpl.class)) {
            List<Fact> children = ((Tuple) fact).getChildFacts();
            for (Fact child: children) {
                result.add( new LocationAspectValue(child.getIndex()));
            }
        }
        return result;        
    }

    /**
     * @see Domain#getDepth(AspectValue)
     */
    public int getDepth(LocationAspectValue aspectValue) throws XBRLException {
        if (! hasParent(aspectValue)) return 0;
        return (getDepth(getParent(aspectValue)) + 1);
    }

    /**
     * @see Domain#getParent(AspectValue)
     */
    public LocationAspectValue getParent(LocationAspectValue child)
            throws XBRLException {
        Fact fact = getStore().<Fact> getXMLResource(child.getFactIndex());
        Fragment parent = fact.getParent();
        if (parent.isa(InstanceImpl.class)) {
            return null;
        }
        return new LocationAspectValue(parent.getIndex());
    }

    /**
     * @see Domain#getSize()
     */
    public long getSize() throws XBRLException {
        Set<String> factIndices = getStore().queryForIndices("for $root in #roots#[@fact] return $root");
        return factIndices.size();
    }

    /**
     * @see Domain#hasChildren(AspectValue)
     */
    public boolean hasChildren(LocationAspectValue value)
            throws XBRLException {
        Fact fact = getStore().<Fact> getXMLResource(value.getFactIndex());
        if (! fact.isa(TupleImpl.class)) return false;
        List<Fact> children = ((Tuple) fact).getChildFacts();
        return (children.size() > 0);
    }

    /**
     * @see Domain#hasParent(AspectValue)
     */
    public boolean hasParent(LocationAspectValue child) throws XBRLException {
        Fact fact = getStore().<Fact> getXMLResource(child.getFactIndex());
        if (fact.getParent().isa(InstanceImpl.class)) return false;
        return true;
    }

    /**
     * @see Domain#isInDomain(AspectValue)
     */
    public boolean isInDomain(LocationAspectValue candidate)
            throws XBRLException {
        
        XML resource = null;
        try {
            resource = getStore().<XML>getXMLResource(candidate.getFactIndex());
            String factAttribute = resource.getMetaAttribute("fact");
            if (factAttribute == null) return false;
            return true;
        } catch (XBRLException e) {
            return false;
        }

    }

    /**
     * @see Domain#isFinite()
     */
    public boolean isFinite() {
        return true;
    }

    /**
     * Returns false.
     * @see Domain#allowsMissingValues()
     */
    public boolean allowsMissingValues() {
        return false;
    }

}
