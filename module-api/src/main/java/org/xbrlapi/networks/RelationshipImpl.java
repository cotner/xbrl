package org.xbrlapi.networks;


import java.net.URI;

import org.apache.log4j.Logger;
import org.xbrlapi.Arc;
import org.xbrlapi.ExtendedLink;
import org.xbrlapi.Fragment;
import org.xbrlapi.PersistedRelationship;
import org.xbrlapi.data.Store;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.utilities.XBRLException;

/**
 * Implementation of the relationship interface, providing
 * access to each of the fragments that contributes to the
 * relationship definition.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class RelationshipImpl implements Relationship {

	protected static Logger logger = Logger.getLogger(Loader.class);
	
	private Store store = null;

	private Network network = null;
	
	private Arc arc = null;
	private Fragment source = null;
	private Fragment target = null;
	private ExtendedLink link = null;

	/**
	 * @param store The data store containing the fragments.
	 * @param a The arc index
	 * @param s The source index
	 * @param t The target index
	 * @throws XBRLException
	 */
	public RelationshipImpl(Store store, String a, String s, String t) throws XBRLException {
		setStore(store);
		try {
			setArc((Arc) get(a));
		} catch (ClassCastException e) {
			throw new XBRLException("The arc index does not correspond to an arc.",e);
		}
		setSource(get(s));
		setTarget(get(t));
		try {
			setLink((ExtendedLink) get(s));
		} catch (ClassCastException e) {
			throw new XBRLException("The link index does not correspond to an extended link.",e);
		}
		
		logger.debug("Created relationship: " + this.toString());
	}
	
	/**
	 * @param a The arc
	 * @param s The source
	 * @param t The target
	 * @throws XBRLException
	 */
	public RelationshipImpl(Arc a, Fragment s, Fragment t) throws XBRLException {
		setStore(a.getStore());
		setArc(a);
		setSource(s);
		setTarget(t);
		setLink((ExtendedLink) get(a.getParentIndex()));
	}
	
    /**
     * @param pr The persisted relationship.
     * @throws XBRLException
     */
    public RelationshipImpl(PersistedRelationship pr) throws XBRLException {
        this(pr.getArc(),pr.getSource(),pr.getTarget());
    }	

	/**
	 * @param link The extended link containing the arc.
	 * @throws XBRLException  if the link is null.
	 */
	private void setLink(ExtendedLink link) throws XBRLException {
		if (link == null) throw new XBRLException("The extended link must not be null.");
		this.link = link;
	}
	
	/**
	 * @param arc The arc declaring the relationship.
	 * @throws XBRLException  if the arc is null.
	 */
	private void setArc(Arc arc) throws XBRLException {
		if (arc == null) throw new XBRLException("The arc must not be null.");
		this.arc = arc;
	}

	/**
	 * The source fragment is the fragment that is identified
	 * by the xlink:from attribute on the relationship arc or
	 * the fragment identified by the locator that is identified 
	 * by the xlink:from attribute on the relationship arc.
	 * @param source The source fragment.
	 * @throws XBRLException if the source fragment is null.
	 */
	private void setSource(Fragment source) throws XBRLException {
		if (source == null) throw new XBRLException("The source fragment must not be null.");
		this.source = source;
	}

	/**
	 * The target fragment is the fragment that is identified
	 * by the xlink:to attribute on the relationship arc or
	 * the fragment identified by the locator that is identified 
	 * by the xlink:to attribute on the relationship arc. 
	 * @param target The target fragment.
	 * @throws XBRLException if the target fragment is null.
	 */
	private void setTarget(Fragment target) throws XBRLException {
		if (target == null) throw new XBRLException("The target fragment must not be null.");
		this.target = target;
	}
	
	/**
	 * @see org.xbrlapi.networks.RelationshipImpl#toString()
	 */
	public String toString() {
		return 
			"The relationship runs from fragment " + 
			source.getIndex() + " to fragment " + target.getIndex() + " using arc " + arc.getIndex();
	}
	
	/**
	 * Convenience method that enables a single fragment to be shared through
	 * a network among the different relationships contained by the network.
	 * If the relationship is not yet in a network, then the relationship takes
	 * care of getting the fragment from the data store.
	 * @param index The index of the fragment to get.
	 * @return The fragment corresponding to the supplied index.
	 * @throws XBRLException
	 */
	private Fragment get(String index) throws XBRLException {
		if (network == null) {
			return getStore().getFragment(index);
		}
		return network.get(index);
	}
	
	/**
	 * @see org.xbrlapi.networks.Relationship#setStore(Store)
	 */
	public void setStore(Store store) throws XBRLException {
		if (store == null) throw new XBRLException("The store must not be null.");
		this.store = store;
	}
	
	/**
	 * @see org.xbrlapi.networks.Relationship#getStore()
	 */
	public Store getStore() {
		return store;
	}
	
	/**
	 * @see org.xbrlapi.networks.Relationship#getNetwork()
	 */
	public Network getNetwork() {
		return network;
	}

	/**
	 * @see org.xbrlapi.networks.Relationship#setNetwork(Network)
	 */
	public void setNetwork(Network network) {
		this.network = network;
	}

	/**
	 * @see org.xbrlapi.networks.Relationship#getSourceIndex()
	 */
	public String getSourceIndex() throws XBRLException {
		Fragment s = getSource();
		return s.getIndex();
	}
	
	/**
	 * @see org.xbrlapi.networks.Relationship#getTargetIndex()
	 */
	public String getTargetIndex() throws XBRLException {
		Fragment t = getTarget();
		return t.getIndex();
	}

	/**
	 * @see org.xbrlapi.networks.Relationship#getLinkIndex()
	 */
	public String getLinkIndex() throws XBRLException {
		return getLink().getIndex();
	}

	/**
	 * @see org.xbrlapi.networks.Relationship#getArcIndex()
	 */
	public String getArcIndex() throws XBRLException {
		return getArc().getIndex();
	}
	
	/**
	 * @see org.xbrlapi.networks.Relationship#getArc()
	 */
	public Arc getArc() throws XBRLException {
		return arc;
	}

	/**
	 * @see org.xbrlapi.networks.Relationship#getArcAttributeValue(String, String)
	 */
	public String getArcAttributeValue(String namespace, String name) throws XBRLException {
		return getArc().getAttribute(namespace, name);
	}
	
    /**
     * @see org.xbrlapi.networks.Relationship#getArcAttributeValue(String)
     */
    public String getArcAttributeValue(String name) throws XBRLException {
        return getArc().getAttribute(name);
    }	

	/**
	 * @see org.xbrlapi.networks.Relationship#getArcrole()
	 */
	public URI getArcrole() throws XBRLException {
		return getArc().getArcrole();
	}

	/**
	 * @see org.xbrlapi.networks.Relationship#getLink()
	 */
	public ExtendedLink getLink() throws XBRLException {
		return link;
	}

	/**
	 * @see org.xbrlapi.networks.Relationship#getLinkRole()
	 */
	public URI getLinkRole() throws XBRLException {
		return getLink().getLinkRole();
	}

	/**
	 * @see org.xbrlapi.networks.Relationship#getOrder()
	 */
	public Double getOrder() throws XBRLException {
	    return getArc().getOrder();
	}

	/**
	 * @see org.xbrlapi.networks.Relationship#getSource()
	 */
	@SuppressWarnings("unchecked")
	public <F extends Fragment> F getSource() throws XBRLException {
		return (F) source;
	}

	/**
	 * @see org.xbrlapi.networks.Relationship#getTarget()
	 */
	@SuppressWarnings("unchecked")
	public <F extends Fragment> F  getTarget() throws XBRLException {
		return (F) target;
	}

	/**
	 * @see org.xbrlapi.networks.Relationship#equals(Relationship)
	 */
	public boolean equals(Relationship other) throws XBRLException {

		Fragment os = other.getSource();
		Fragment ts = this.getSource();
		if (! ts.getIndex().equals(os.getIndex())) return false;

		Fragment tt = this.getTarget();
		Fragment ot = other.getTarget();
		if (! tt.getIndex().equals(ot.getIndex())) return false;

		if (! this.getArc().semanticEquals(other.getArc())) return false;

		return true;
	}
	
	/**
	 * @see org.xbrlapi.networks.Relationship#getPriority()
	 */
	public Integer getPriority() throws XBRLException {
		return this.getArc().getPriority();
	}
	
	/**
	 * @see Relationship#getUse()
	 */
	public String getUse() throws XBRLException {
		return this.getArc().getUse();
	}
	
	/**
	 * @see Relationship#isProhibited()
	 */
	public boolean isProhibited() throws XBRLException {
	    return getUse().equals("prohibited");
	}

	/**
	 * @see org.xbrlapi.networks.Relationship#getSignature()
	 */
	public String getSignature() throws XBRLException {
		return this.getArc().getSemanticKey();
	}

    /**
     * @see org.xbrlapi.networks.Relationship#isFromRoot()
     */
    public boolean isFromRoot() throws XBRLException {
        return getNetwork().isRoot(this.getSourceIndex());
    }
	

	
}
