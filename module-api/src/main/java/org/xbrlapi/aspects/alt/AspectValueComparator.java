package org.xbrlapi.aspects.alt;

import java.util.List;

import org.xbrlapi.utilities.XBRLException;

/**
 * <h2>Aspect value comparator</h2>
 * 
 * <p>
 * Aspect values can have strictly heirarchical orderings.
 * This interface defines the functionality of comparators
 * that define such strict orderings.
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public interface AspectValueComparator<T extends AspectValue> {

    /**
     * @param parent
     *            The aspect value.
     * @return true if the parent aspect value has child aspect values and false
     *         otherwise.
     * @throws XBRLException
     */
    public List<T> hasChildren(T parent) throws XBRLException;

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
    
}
