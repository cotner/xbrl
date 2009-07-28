package org.xbrlapi;

import java.net.URI;

import org.xbrlapi.utilities.XBRLException;

/**
 * This XML resource supports capture of active 
 * relationship information directly in the data store.  
 * This can facilitate faster relationship analysis. 
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Relationship extends NonFragmentXML {

    /**
     * @return the fragment index for the source of the relationship.
     */
    public String getSourceIndex();

    /**
     * @return the fragment index for the target of the relationship.
     */
    public String getTargetIndex();

    /**
     * @return the source fragment of the relationship.
     * @throws XBRLException
     */
    public <F extends Fragment> F getSource() throws XBRLException;
    
    /**
     * @return the target fragment of the relationship.
     */
    public <F extends Fragment> F getTarget() throws XBRLException;
    
    /**
     * @return the XLink link role of the relationship.
     */
    public URI getLinkRole();
    
    /**
     * @return the XLink arcrole of the relationship.
     */
    public URI getArcrole();
    
    /**
     * @return the XLink arc order for the relationship.
     */
    public Double getArcOrder();
    
    /**
     * @return the arc priority for the relationship.
     */
    public Integer getArcPriority();
    
    /**
     * @return the arc use for the relationship.
     */
    public String getUse();
    
    /**
     * @return true if the relationship has prohibited use
     * and false otherwise.  Note that this <em>does not</em> test if this
     * relationship is prohibited by another relationship with a higher
     * priority.
     * @throws XBRLException
     */
    public boolean isProhibiting() throws XBRLException;    

    /**
     * Use this method if the attribute on the arc has its
     * own namespace.
     * @param namespace The namespace of the attribute.
     * @param name The local name of the attribute.
     * @return The value of the attribute on the arc 
     * or null if no such attribute exists.
     * @throws XBRLException
     */
    public String getArcAttributeValue(URI namespace, String name) throws XBRLException;
    
    /**
     * Use this method if the attribute on the arc does not have 
     * its own namespace.
     * @param name The name of the attribute.
     * @return The value of the attribute on the arc 
     * or null if no such attribute exists.
     * @throws XBRLException
     */
    public String getArcAttributeValue(String name) throws XBRLException;   
        
    /**
     * @return the source element namespace.
     */
    public URI getSourceNamespace();        
    
    /**
     * @return the target element namespace.
     */
    public URI getTargetNamespace();        
    
    /**
     * @return the arc element namespace.
     */
    public URI getArcNamespace();        
    
    /**
     * @return the link element namespace.
     */
    public URI getLinkNamespace();            

    /**
     * @return the arc fragment defining the relationship.
     * @throws XBRLException
     */
    public Arc getArc() throws XBRLException;
    
    /**
     * @return the arc fragment index.
     */
    public String getArcIndex();
    
    /**
     * @return the URI of the document containing the arc
     * defining this persisted relationship.
     */
    public String getArcURI();
    
    /**
     * @return the source element name.
     */
    public String getSourceName();

    /**
     * @return the target element name.
     */
    public String getTargetName();

    /**
     * @return the arc element name.
     */
    public String getArcName();

    /**
     * @return the index of the containing extended link.
     * @throws XBRLException
     */
    public String getLinkIndex() throws XBRLException;

    /**
     * @return the extended link that contains the arc defining the
     * relationship.
     * @throws XBRLException
     */
    public ExtendedLink getExtendedLink() throws XBRLException;

    /**
     * @return the link element name.
     */
    public String getLinkName();
    
    /**
     * @return the source XLink role or null if none exists.
     */
    public URI getSourceRole();

    /**
     * @return the target XLink role or null if none exists.
     */
    public URI getTargetRole();
    
    /**
     * @return the relationship signature which is what is 
     * used to match up relationships that override or
     * prohibit one another.
     */
    public String getSignature();    

    /**
     * @return the source XML language code or null if none exists.
     */
    public String getSourceLanguageCode();

    /**
     * @return the target XML language code or null if none exists.
     */
    public String getTargetLanguageCode();

    /**
     * @return the type of the source fragment.
     */
    public String getSourceType();
    
    /**
     * @return the type of the target fragment.
     */
    public String getTargetType();

    /**
     * @return true if the relationship is to an XBRL 2.1 or generic label
     */
    public boolean isToLabel();
    
    /**
     * @return true if the relationship is to an XBRL 2.1 or generic reference
     */
    public boolean isToReference();    
    
    /**
     * @return true if the relationship is from a source fragment that is
     * not also a target fragment in the same network of relationships that
     * this relationship participates in.  Returns false otherwise.
     * @throws XBRLException
     */
    public boolean isFromRoot() throws XBRLException;
    
    /**
     * @return the string expression of the relationship as a sequence of fragment indices
     * source:arc:target.
     */
    public String toString();    
    
    /**
     * @return true if the relationship is marked as being
     * not prohibited or overridden.
     * @throws XBRLException
     */
/*    public boolean isActive() throws XBRLException;*/

    /**
     * @param active true if the relationship is set to active
     * (not prohibited or overridden) and false otherwise.
     * @throws XBRLException
     */
/*    public void setActiveStatus(boolean active) throws XBRLException;*/
    
}
