package org.xbrlapi.aspects.alt;

import java.net.URI;
import java.util.List;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public interface LabelHandler {

    /**
     * @return true if the cache contains a suitable label and false otherwise.
     * @throws XBRLException
     */
    public boolean hasLabel() throws XBRLException;

    /**
     * If the label is not in the cache then implementations of this method can
     * elect to inject the label into the cache for future usage.
     * 
     * @return The label based upon default locale and role selections or null
     *         if none exists.
     * @throws XBRLException
     */
    public String getLabel() throws XBRLException;

    /**
     * @param languages
     *            A list of label language codes, from most preferred (first in
     *            the list) to least preferred (last in the list).
     * @return true if there is a suitable label and false otherwise.
     * @throws XBRLException
     */
    public boolean hasLabel(List<String> languages) throws XBRLException;

    /**
     * If the label is not in the cache then implementations of this method can
     * elect to inject the label into the cache for future usage.
     * 
     * @param languages
     *            A list of label language codes, from most preferred (first in
     *            the list) to least preferred (last in the list).
     * @return The label based upon default locale and role selections or null
     *         if none exists.
     * @throws XBRLException
     */
    public String getLabel(List<String> languages) throws XBRLException;

    /**
     * @param languages
     *            A list of label language codes, from most preferred (first in
     *            the list) to least preferred (last in the list).
     * @param roles
     *            A list of label roles, from most preferred (first in the list)
     *            to least preferred (last in the list).
     * @return true if there is a suitable label and false otherwise.
     * @throws XBRLException
     */
    public boolean hasLabel(List<String> languages, List<URI> roles)
            throws XBRLException;

    /**
     * @param languages
     *            A list of label language codes, from most preferred (first in
     *            the list) to least preferred (last in the list).
     * @param roles
     *            A list of label roles, from most preferred (first in the list)
     *            to least preferred (last in the list).
     * @return The label based upon default locale and role selections or null
     *         if none exists.
     * @throws XBRLException
     */
    public String getLabel(List<String> languages, List<URI> roles)
            throws XBRLException;

}
