package org.xbrlapi.aspects.alt;

import java.net.URI;
import java.util.List;

import org.xbrlapi.Fact;
import org.xbrlapi.utilities.XBRLException;

/**
 * <h2>Aspect caching system explanation</h2>
 * 
 * <p>
 * The analysis of aspect values can be computationally intensive, warranting
 * performance improvements that can be achieved by caching values that have
 * previously been obtained. The aspect cache interface defines the features
 * required of caching systems for aspects.
 * </p>
 * 
 * <p>
 * It is important to be able to quickly map from a fact/aspect combination to
 * an aspect value. This is supported by the caching system. It is also
 * important to be able to quickly obtain labels for a given aspect value based
 * upon various label selection criteria. This can also be supported by caching
 * systems.
 * </p>
 * 
 * <p>
 * Caching systems generally last for at least the life of the aspect models
 * that use them. It is possible for them to last for considerably longer than
 * the individual aspect models that rely on them and cache information relating
 * to multiple aspect models.
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 * 
 */
public interface Cache {

    /**
     * @param <V>
     *            The type of object
     * @param object
     *            The object that a cached label is being sought for.
     * @return true if the cache contains a suitable label for the object
     *         and false otherwise.
     * @throws XBRLException
     */
    public <V> boolean hasLabel(V object) throws XBRLException;

    /**
     * If the label is not in the cache then implementations of this method can
     * elect to inject the label into the cache for future usage.
     * 
     * @param <V>
     *            The type of object being labelled
     * @param object
     *            The object that a cached label is being sought for.
     * @return The label based upon default locale and role selections.
     * @throws XBRLException
     */
    public <V> String getLabel(V object) throws XBRLException;

    /**
     * @param <V>
     *            The type of object
     * @param object
     *            The object that a cached label is being sought for.
     * @param languages
     *            A list of label language codes, from most preferred (first in
     *            the list) to least preferred (last in the list).
     * @return true if the cache contains a suitable label for the object
     *         and false otherwise.
     * @throws XBRLException
     */
    public <V> boolean hasLabel(V object, List<String> languages)
            throws XBRLException;

    /**
     * If the label is not in the cache then implementations of this method can
     * elect to inject the label into the cache for future usage.
     * 
     * @param <V>
     *            The type of object
     * @param object
     *            The object that a cached label is being sought for.
     * @param languages
     *            A list of label language codes, from most preferred (first in
     *            the list) to least preferred (last in the list).
     * @return The label based upon default locale and role selections or null if none is available.
     * @throws XBRLException
     */
    public <V> String getLabel(V object, List<String> languages)
            throws XBRLException;

    /**
     * @param <V>
     *            The type of object
     * @param object
     *            The object that a cached label is being sought for.
     * @param languages
     *            A list of label language codes, from most preferred (first in
     *            the list) to least preferred (last in the list).
     * @param roles
     *            A list of label roles, from most preferred (first in the list)
     *            to least preferred (last in the list).
     * @return true if the cache contains a suitable label for the object
     *         and false otherwise.
     * @throws XBRLException
     */
    public <V> boolean hasLabel(V object, List<String> languages,
            List<URI> roles) throws XBRLException;

    /**
     * If the label is not in the cache then implementations of this method can
     * elect to inject the label into the cache for future usage.
     * 
     * @param <V>
     *            The type of object
     * @param object
     *            The object that a cached label is being sought for.
     * @param languages
     *            A list of label language codes, from most preferred (first in
     *            the list) to least preferred (last in the list).
     * @param roles
     *            A list of label roles, from most preferred (first in the list)
     *            to least preferred (last in the list).
     * @return The label based upon default locale and role selections or null if none
     * is available.
     * @throws XBRLException
     */
    public <V> String getLabel(V object, List<String> languages, List<URI> roles)
            throws XBRLException;

    /**
     * @param aspect
     *            The aspect.
     * @param fact
     *            The fact.
     * @return true if the cache system has an aspect value for the given aspect
     *         and fact and false otherwise.
     * @throws XBRLException
     */
    public boolean hasValue(Aspect aspect, Fact fact) throws XBRLException;

    /**
     * Implementations of this method can elect to store the aspect value in the
     * cache if it is not already there. This is the only way to actually inject
     * aspect values into the cache via this interface.
     * 
     * @param <V> The class of aspect value.
     * @param aspect
     *            The aspect.
     * @param fact
     *            The fact.
     * @return the aspect value for the given aspect and fact.
     * @throws XBRLException
     */
    public <V extends AspectValue> V getValue(Aspect aspect, Fact fact)
            throws XBRLException;
    
}

