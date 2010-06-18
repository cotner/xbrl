package org.xbrlapi.aspects.alt;

import java.io.Serializable;
import java.util.List;

import org.xbrlapi.utilities.XBRLException;

/**
 * <h2>Domain</h2>
 * 
 * <p>
 * A domain represents a collection of values for a single aspect, which may
 * have a strict heirarchical ordering.
 * </p>
 * 
 * <p>
 * The extent of the domain is determined by its definition. All domain
 * definitions include the aspect identifier because a domain must only contain
 * aspect values that are values for the same aspect.
 * </p>
 * 
 * <p>
 * Domain definitions allow testing of aspect values to determine if they are
 * part of the domain or not. They also allow analysis of heirarchical
 * relationships between the aspect values in the domain, if such heirarchial
 * relationships exist.
 * </p>
 * 
 * <p>
 * Domain definitions can be established in any number of ways, including but
 * not restricted to:
 * </p>
 * 
 * <ul>
 * <li>An enumeration of the aspect values that are included in the domain;</li>
 * <li>A set of rules for determining those aspect values in the domain, such as
 * an XLink relationship network.</li>
 * </ul>
 * 
 * @param <T>
 *            The aspect value that this is a domain for.
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public interface Domain<T extends AspectValue> extends Serializable {
    
    /**
     * @param value
     *            The aspect value.
     * @return true if the aspect value has child aspect values and false
     *         otherwise.
     * @throws XBRLException
     */
    public boolean hasChildren(T value) throws XBRLException;

    /**
     * @param parent
     *            The parent aspect value.
     * @return the ordered list of child aspect values for the given parent
     *         aspect value. The list is empty if there are no child aspect
     *         values in the domain for the given parent aspect value.
     * @throws XBRLException
     */
    public List<T> getChildren(T parent)
            throws XBRLException;

    /**
     * @param child
     *            The child aspect value.
     * @return true if this aspect value has a parent aspect value in the aspect
     *         model and false otherwise.
     * @throws XBRLException
     */
    public boolean hasParent(T child) throws XBRLException;

    /**
     * @param child
     *            The child aspect value.
     * @return the parent aspect value or null if none exists.
     * @throws XBRLException
     */
    public T getParent(T child) throws XBRLException;

    /**
     * @param aspectValue
     *            The aspect value whose ancestor aspect values are to be
     *            counted.
     * @return the number of ancestor aspect values that this aspect value has
     *         in this domain. The return value will be zero if the domain does
     *         not define a heirarchical ordering of aspect values.
     * @throws XBRLException
     */
    public int getDepth(T aspectValue) throws XBRLException;
    
    /**
     * @return a list of all of the aspect values in the domain. The list is
     *         ordered based on a depth first traversal of the aspect values in
     *         the domain. Thus, parent values come immediately before their
     *         child values and child values are sorted by their strict ordering
     *         if there is one.
     * @throws XBRLException
     */
    public List<T> getAllAspectValues() throws XBRLException;
    
    /**
     * @return the number of aspect values in the domain or null if
     * the domain size is infinite.
     */
    public long getSize() throws XBRLException;
    
    /**
     * @param candidate The candidate aspect value.
     * @return true if the aspect value is in the domain and false otherwise.
     * @throws XBRLException
     */
    public boolean isInDomain(T candidate) throws XBRLException;
    
    /**
     * The getSize() and the getAllAspectValues methods must only be implemented
     * if this method returns true.  Otherwise they need to throw an exception.
     * @return true if the domain has a finite number of members in it and
     * false otherwise.
     */
    public boolean isFinite();
    
    /**
     * If this returns true, then the relevant aspect value implementation must
     * include a no-parameter constructor that produces missing aspect values.
     * 
     * @return true if the domain includes the missing value and false
     *         otherwise.
     */
    public boolean allowsMissingValues();
    
}
