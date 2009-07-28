package org.xbrlapi.networks;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import java.util.Vector;

import org.xbrlapi.Fragment;
import org.xbrlapi.Relationship;
import org.xbrlapi.utilities.XBRLException;

/**
 * This iterator iterates over the fragments in a relationship
 * network.  It requires the network to be a strict tree.
 * It does a preorder traversal of the fragments in the tree,
 * returning parent fragments before their children.  It also returns
 * siblings with lower relationship orders before their siblings with
 * higher relationship orders.
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class TreeIterator implements Iterator<Fragment> {

    // The network to iterate
    private Network network = null;
    
    // The state of the iterator
    private Fragment root = null;
    private List<SortedSet<Relationship>> state = new Vector<SortedSet<Relationship>>();

    private List<Fragment> initialise(Network network) throws XBRLException {
        if (network == null) throw new XBRLException("The network must not be null.");
        this.network = network;
        List<Fragment> roots = network.getRootFragments();
        if (roots.size() == 0) throw new XBRLException("The network no nodes.");
        return roots;
    }
    
    /**
     * Use this constructor when the network defines a single tree.
     * @param network The network to use.
     * @throws XBRLException if the network does not define a single tree.
     */
    public TreeIterator(Network network) throws XBRLException {
        List<Fragment> roots = initialise(network);
        if (roots.size() == 0) throw new XBRLException("The network no nodes.");
        if (roots.size() > 1) throw new XBRLException("The network has 2 or more roots.  It must be a tree.");
        this.root = roots.get(0);
    }    

    /**
     * Use this constructor when the network defines a number of trees, each distinguished by 
     * its own root fragment. 
     * @param network The network to use.
     * @param start The tree root fragment to use.
     * @throws XBRLException
     */
    public TreeIterator(Network network, Fragment start) throws XBRLException {
        List<Fragment> roots = initialise(network);
        for (Fragment root: roots) {
            if (root.equals(start)) this.root = root;
        }
    }

    /**
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
        if (this.root != null) return true;
        if (state.isEmpty()) return false;
        return true;
    }

    /**
     * @see java.util.Iterator#next()
     */
    public Fragment next() throws NoSuchElementException {
        
        try {

            if (root != null) {
                SortedSet<Relationship> relationships = network.getActiveRelationshipsFrom(root.getIndex());
                if (! relationships.isEmpty()) state.add(relationships);
                Fragment next = root;
                root = null;
                return next;
            }

            if (! hasNext()) {
                throw new NoSuchElementException("The tree iterator has no more fragments to iterate.");
            }

            // Update the state
            SortedSet<Relationship> first = state.get(0);
            Relationship next = first.first();
            first.remove(next);
            if (first.isEmpty()) {
                state.remove(0);
            }
            SortedSet<Relationship> relationships = network.getActiveRelationshipsFrom(next.getTargetIndex());
            if (! relationships.isEmpty()) state.add(relationships);
            return next.getTarget();

        } catch (XBRLException e) {
            throw new NoSuchElementException("The tree iterator encountered an XBRLException.");
        }
    }

    /**
     * This method is not supported by this implementation.
     * @see java.util.Iterator#remove()
     */
    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("The remove operation is not supported by this iterator implementation.");
    }

}
