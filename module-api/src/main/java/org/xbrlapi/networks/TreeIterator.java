package org.xbrlapi.networks;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Vector;

import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
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
    private List<List<Relationship>> state = new Vector<List<Relationship>>();
    
    public TreeIterator(Network network) throws XBRLException {
        if (network ==null) throw new XBRLException("The network must not be null.");
        this.network = network;
        FragmentList<Fragment> roots = network.getRootFragments();
        if (roots.getLength() > 1) throw new XBRLException("The network has 2 or more roots.  It must be a tree.");
        if (roots.getLength() == 0) throw new XBRLException("The network no nodes.");
        for (Fragment root: roots) {
            this.root = root;
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
                List<Relationship> relationships = network.getActiveRelationshipsFrom(root.getFragmentIndex());
                if (! relationships.isEmpty()) state.add(relationships);
                Fragment next = root;
                root = null;
                return next;
            }

            if (! hasNext()) {
                throw new NoSuchElementException("The tree iterator has no more fragments to iterate.");
            }

            // Update the state
            List<Relationship> first = state.get(0);
            Relationship next = first.get(0);
            first.remove(0);
            if (first.isEmpty()) {
                state.remove(0);
            }
            List<Relationship> relationships = network.getActiveRelationshipsFrom(next.getTargetIndex());
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
