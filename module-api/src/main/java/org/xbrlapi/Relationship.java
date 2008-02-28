package org.xbrlapi;

import org.xbrlapi.data.Store;
import org.xbrlapi.utilities.XBRLException;

/**
 * The relationship interface is intended to expose 
 * information about XBRL relationships expressed using
 * arcs in extended links.
 * The full set of information about a relationship depends
 * upon:
 * <ul>
 * <li>The source and target fragments</li>
 * <li>The xlink:role of the extended link containing the 
 * arc that defines the relationship</li>
 * <li>The xlink:arcrole of the arc that defines the relationship</li>
 * <ul>
 * Note that the XBRL specification limits the application of
 * certain relationship semantics depending upon the arc element
 * and the containing extended link element also.  Such limitations
 * are ignored in the XBRLAPI.
 * Note also that a relationship only ever involves one source
 * fragment and one target fragment.  This is despite the possibility
 * of an arc defining several relationships.
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Relationship {

	/**
	 * @param store The data store containing the relationship.
	 * @throws XBRLException if the store is null.
	 */
	public void setStore(Store store) throws XBRLException;	
	
	/**
	 * @return The data store containing the relationship.
	 */
	public Store getStore();
	
	/**
	 * @return the network
	 */
	public Network getNetwork();

	/**
	 * @param network the network to set
	 */
	public void setNetwork(Network network);
	
	
	/**
	 * The source fragment is the fragment that is identified
	 * by the xlink:from attribute on the relationship arc or
	 * the fragment identified by the locator that is identified 
	 * by the xlink:from attribute on the relationship arc.
	 * @return the fragment that the relationship runs from.
	 * @throws XBRLException if the source fragment is null.
	 */
	public <F extends Fragment> F getSource() throws XBRLException;

	/**
	 * @return the index of the source fragment.
	 * @throws XBRLException
	 */
	public String getSourceIndex() throws XBRLException;
	
	/**
	 * @return the index of the target fragment.
	 * @throws XBRLException
	 */
	public String getTargetIndex() throws XBRLException;

	/**
	 * @return the index of the containing extended link.
	 * @throws XBRLException
	 */
	public String getLinkIndex() throws XBRLException;

	/**
	 * @return the index of the arc.
	 * @throws XBRLException
	 */
	public String getArcIndex() throws XBRLException;

	
	/**
	 * The target fragment is the fragment that is identified
	 * by the xlink:to attribute on the relationship arc or
	 * the fragment identified by the locator that is identified 
	 * by the xlink:to attribute on the relationship arc.
	 * @return the fragment that the relationship runs to.
	 * @throws XBRLException if the target fragment is null.
	 */
	public <F extends Fragment> F  getTarget() throws XBRLException;
	
	/**
	 * @return the extended link that contains the arc defining the
	 * relationship.
	 * @throws XBRLException if the extended link is null.
	 */
	public ExtendedLink getLink() throws XBRLException;

	/**
	 * @return the arc defining the relationship.
	 * @throws XBRLException if the arc is not in the store
	 * or is a fragment in the store but is not an arc.
	 */
	public Arc getArc() throws XBRLException;	
	
	/**
	 * @return the XLink role attribute value
	 * on the containing extended link.
	 * @throws XBRLException
	 */
	public String getLinkRole() throws XBRLException;
	
	/**
	 * @return the XLink arc role value on the arc
	 * defining the relationship.
	 * @throws XBRLException
	 */
	public String getArcRole() throws XBRLException;
	
	/**
	 * Use this method if the attribute on the arc has its
	 * own namespace.
	 * @param namespace The namespace of the attribute.
	 * @param name The local name of the attribute.
	 * @return The value of the attribute on the arc 
	 * or null if no such attribute exists.
	 * @throws XBRLException
	 */
	public String getArcAttributeValue(String namespace, String name) throws XBRLException;
	
	/**
	 * 
	 * @return the value of the order attribute on the arc defining
	 * the relationship.
	 * @throws XBRLException
	 */
	public String getOrder() throws XBRLException;
	
	/**
	 * @param other The other relationship.
	 * @return true if and only if the two relationships are 
	 * equal in the sense that the connect the same fragments in 
	 * the same direction and their arcs are semantically equal.
	 * @throws XBRLException
	 * @see org.xbrlapi.Arc#semanticEquals(Arc)
	 */
	public boolean equals(Relationship other) throws XBRLException;
	
	/**
	 * @return the integer priority of the relationship.
	 * @throws XBRLException
	 */
	public Integer getPriority() throws XBRLException;
	
	/**
	 * @return the use, "optional" or "prohibited", 
	 * of the relationship.
	 * @throws XBRLException
	 */
	public String getUse() throws XBRLException;

	/**
	 * @return a String that will be identical for semantically
	 * equal relationships.
	 * @throws XBRLException
	 */
	public String getSemanticKey() throws XBRLException;

	/**
	 * @return the string expression of the relationship in terms of the fragment indices.
	 */
	public String toString();	
	
}
