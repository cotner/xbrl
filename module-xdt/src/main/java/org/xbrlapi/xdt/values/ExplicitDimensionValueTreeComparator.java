package org.xbrlapi.xdt.values;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import org.xbrlapi.Concept;
import org.xbrlapi.Fragment;
import org.xbrlapi.networks.Network;
import org.xbrlapi.networks.TreeIterator;
import org.xbrlapi.utilities.XBRLException;

/**
 * Replaces the naive sorting of explicit dimension values
 * for the same explicit dimension with one based upon
 * a tree of domain members.
 * The tree is specified in terms of a network and the 
 * root of the tree in that network to be analysed.
 * If two domain members for the one dimension are not
 * both in the specified tree, then any that are not
 * in the network come after those in the tree.
 * Among domain members not in the tree, the ordering
 * is based upon the domain member namespaces and local names.
 * In the tree-based ordering, parents come before children.
 * Siblings are ordered based upon relationship ordering.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ExplicitDimensionValueTreeComparator extends DimensionValueComparator implements Comparator<DimensionValue> {
    
    private HashMap<String,Integer> map = null;
    
    /**
     * @param network The network to use for the ordering.
     * @param root The node in the network to use as the root for network analysis
     * @throws XBRLException if any parameters are null or any roles are not a valid URI 
     * or if the network has cycles.
     */
    public ExplicitDimensionValueTreeComparator(Network network, Fragment root)  throws XBRLException {
        super();
        if (network == null) throw new XBRLException("The network must not be null");

        // Traverse the tree generating a map from node fragment indices to an ordering.
        map = new HashMap<String,Integer>();
        Iterator<Fragment> iterator = new TreeIterator(network,root);
        int order = 1;
        while (iterator.hasNext()) {
            Fragment node = iterator.next();
            map.put(node.getFragmentIndex(),new Integer(order));
            order++;
        }
        
    }
    
    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(DimensionValue v1, DimensionValue v2) throws ClassCastException {
        try {

            int result = compareDimensions(v1,v2);
            if (result != 0) return result;

            if (v1.isTypedDimensionValue() && v2.isTypedDimensionValue()) {
                result = compareTypedDimensionValues(v1, v2);
            }
            if (result != 0) return result;

            if (v1.isExplicitDimensionValue() && v2.isExplicitDimensionValue()) {
                result = compareExplicitDimensionValues(v1, v2);
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

        Concept m1 = (Concept) v1.getValue();
        Concept m2 = (Concept) v2.getValue();

        String i1 = m1.getFragmentIndex();
        String i2 = m2.getFragmentIndex();
        if (map.containsKey(i1) && map.containsKey(i2)) {
            Integer o1 = map.get(i1);
            Integer o2 = map.get(i2);
            return o1.compareTo(o2);
        }
        
        if (map.containsKey(i1)) return -1;
        if (map.containsKey(i2)) return 1;
        
        String v1ns = m1.getTargetNamespaceURI();
        String v2ns = m2.getTargetNamespaceURI();
        int result = v1ns.compareTo(v2ns);
        if (result != 0) return result;
        
        String v1ln = m1.getName();
        String v2ln = m2.getName();
        return v1ln.compareTo(v2ln);

    }
    
}
