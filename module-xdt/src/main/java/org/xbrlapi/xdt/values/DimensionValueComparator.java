package org.xbrlapi.xdt.values;

import java.util.Comparator;

import org.w3c.dom.Element;
import org.xbrlapi.Concept;
import org.xbrlapi.data.Store;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xdt.Dimension;

/**
 * Supports a naive sorting of dimension values.
 * Different dimensions are ordered by their namespace and then
 * their local name.
 * Values for the one explicit dimension are ordered based on string 
 * comparisons of the namespaces and then, if those are equal, based on string
 * comparisons of their local names.
 * Typed dimension values are ordered based on a string comparison of their
 * serialised representation.
 * If two dimension values are equal, then the DimensionValue objects
 * are ordered based upon the fragment index of the items that they are
 * values for.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class DimensionValueComparator implements Comparator<DimensionValue> {
    
    public DimensionValueComparator() {
        ;
    }
    
    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(DimensionValue v1, DimensionValue v2) throws ClassCastException {
        try {

            int result = compareDimensions(v1,v2);
            if (result != 0) return result;
            
            if (v1.isExplicitDimensionValue() && v2.isExplicitDimensionValue()) {
                result = compareExplicitDimensionValues(v1, v2);
            }
            if (result != 0) return result;

            if (v1.isTypedDimensionValue() && v2.isTypedDimensionValue()) {
                result = compareTypedDimensionValues(v1, v2);
            }
            if (result != 0) return result;
            
            return compareItems(v1,v2);
            
        } catch (XBRLException e) {
            throw new ClassCastException("Dimension value comparison is not possible." + e.getMessage());
        }
    }

    /**
     * @param v1 The first explicit dimension value.
     * @param v2 The second explicit dimension value.
     * @return a negative integer, zero, or a positive integer 
     * as the first argument is less than, equal to, or greater than the second.
     * @throws XBRLException
     */
    private int compareExplicitDimensionValues(DimensionValue v1, DimensionValue v2) throws XBRLException {

        Concept v1m = (Concept) v1.getValue();
        Concept v2m = (Concept) v2.getValue();
        
        String v1ns = v1m.getTargetNamespaceURI();
        String v2ns = v2m.getTargetNamespaceURI();
        int result = v1ns.compareTo(v2ns);
        if (result != 0) return result;
        
        String v1ln = v1m.getName();
        String v2ln = v2m.getName();
        return v1ln.compareTo(v2ln);

    }
    
    /**
     * @param v1 The first typed dimension value.
     * @param v2 The second typed dimension value.
     * @return a negative integer, zero, or a positive integer 
     * as the first argument is less than, equal to, or greater than the second.
     * @throws XBRLException
     */
    protected int compareTypedDimensionValues(DimensionValue v1, DimensionValue v2) throws XBRLException {
        Store store = v1.getItem().getStore();
        String v1s = store.serializeToString((Element) v1.getValue());
        String v2s = store.serializeToString((Element) v2.getValue());
        return v1s.compareTo(v2s);
    }

    /**
     * Do a comparison of the dimensions themselves.  This covers the dimensions but not the values of
     * those dimensions.
     * @param v1 The first typed dimension value.
     * @param v2 The second typed dimension value.
     * @return a negative integer, zero, or a positive integer 
     * as the first argument is less than, equal to, or greater than the second.
     * @throws XBRLException
     */
    protected int compareDimensions(DimensionValue v1, DimensionValue v2) throws XBRLException {

        if (v1.isTypedDimensionValue() && v2.isExplicitDimensionValue()) return 1;

        if (v1.isExplicitDimensionValue() && v2.isTypedDimensionValue()) return -1;

        Dimension v1d = v1.getDimension();
        Dimension v2d = v2.getDimension();

        String d1ns = v1d.getTargetNamespaceURI();
        String d2ns = v2d.getTargetNamespaceURI();
        int result = d1ns.compareTo(d2ns);
        if (result != 0) return result;

        String d1ln = v1d.getName();
        String d2ln = v2d.getName();
        return d1ln.compareTo(d2ln);
        
    }
    
    /**
     * Do a comparison of the items that the dimension values are for, ranking them based on
     * the string comparison of their index values.
     * @param v1 The first typed dimension value.
     * @param v2 The second typed dimension value.
     * @return a negative integer, zero, or a positive integer 
     * as the first argument is less than, equal to, or greater than the second.
     * @throws XBRLException
     */
    protected int compareItems(DimensionValue v1, DimensionValue v2) throws XBRLException {
        return v1.getItem().compareTo(v2.getItem());
    }
    
}
