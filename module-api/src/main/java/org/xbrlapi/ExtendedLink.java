package org.xbrlapi;

import java.net.URI;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface ExtendedLink extends Link {

    /**
     * Get the list of locators contained by the extended link.
     * Returns null if the extended link contains no locators.
     * @throws XBRLException
     */
    public FragmentList<Locator> getLocators() throws XBRLException;
    
    /**
     * Get the list of Arc Ends (locators or resources) with a specified label in 
     * the extended link.
     * @param label The string value of the xlink:label attribute on the arc ends.
     * @return the list of matching fragments or the empty list if none are found.
     * @throws XBRLException
     */
    public <E extends ArcEnd> FragmentList<E> getArcEndsByLabel(String label) throws XBRLException;
    
    /**
     * Get the list of locators with a specified label in the extended link.
     * @param label The string value of the xlink:label attribute on the locator.
     * @return the list of matching locator fragments or the empty list if none are found.
     * @throws XBRLException
     */
    public FragmentList<Locator> getLocatorsByLabel(String label) throws XBRLException;
    
    /**
     * Get the list of locators in the extended link with a specified absolute HREF.
     * @param href The resolved value of the xlink:href attribute on the locator.
     * @return the list of matching locator fragments or the empty list if none are found.
     * @throws XBRLException
     */
    public FragmentList<Locator> getLocatorsByHref(String href) throws XBRLException;

    /**
     * Get the list of arcs contained by the extended link.
     * @return the list of matching arc fragments or the empty list if none are found.
     * @throws XBRLException
     */
    public FragmentList<Arc> getArcs() throws XBRLException;
    
    /**
     * @param arcrole The arcrole of the required arcs.
     * Get the list of arcs with the given arcrole that are 
     * contained by the extended link.
     * @return the list of matching arc fragments or the empty list if none are found.
     * @throws XBRLException
     */
    public FragmentList<Arc> getArcsByArcrole(URI arcrole) throws XBRLException;
    
    
    /**
     * @param from The required value of the xlink:from attribute of the arcs.
     * @return the list of arcs with a given xlink:from label in extended link.
     * or the empty list if none are found.
     * @throws XBRLException
     */
    public FragmentList<Arc> getArcsByFromLabel(String from) throws XBRLException;

    /**
     * @param from The required value of the xlink:from attribute of the arcs.
     * @param arcrole The arcrole value for the arcs being sought.
     * @return the list of arcs in the extended link starting at the given xlink label 
     * and arcrole value or the empty list if none are found.
     * @throws XBRLException
     */
    public FragmentList<Arc> getArcsByFromLabelAndArcrole(String from, URI arcrole) throws XBRLException;
    
    /**
     * @param to The required value of the xlink:to attribute of the arcs.
     * @return the list of arcs with a given xlink:from label in extended link
     * or the empty list if none are found.
     * @throws XBRLException
     */
    public FragmentList<Arc> getArcsByToLabel(String to) throws XBRLException;
    
    /**
     * @param to The required value of the xlink:to attribute of the arcs.
     * @param arcrole The arcrole value for the arcs being sought.
     * @return the list of arcs in the extended link ending at the given xlink label 
     * and arcrole value or the empty list if none are found.
     * @throws XBRLException
     */
    public FragmentList<Arc> getArcsByToLabelAndArcrole(String to, URI arcrole) throws XBRLException;

    /**
     * Get the list of resources contained by the extended link.
     * @return the list of resource fragments in the extended link.
     * @throws XBRLException
     */
    public FragmentList<Resource> getResources() throws XBRLException;
    
    /**
     * Get the list of resources with the specified label.
     * @param label The value of the label used to select resources in the extended link.
     * @return the list of resource fragments with the given xlink:label attribute value.
     * @throws XBRLException
     */
    public FragmentList<Resource> getResourcesByLabel(String label) throws XBRLException;    
    




    /**
     * Get the list of documentation fragments contained by the extended link.
     * Returns the list of documentation fragments in the extended link.
     * @throws XBRLException
     */
    public FragmentList<XlinkDocumentation> getDocumentations() throws XBRLException;
    




}
