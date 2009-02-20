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

public interface ActiveRelationship extends XML {

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
     * @return the arc fragment index.
     */
    public String getArcIndex();
    
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
    
}
