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
     * @param index The source fragment index.
     */
    public void setSourceIndex(String index) throws XBRLException;
    
    /**
     * @return the fragment index for the target of the relationship.
     */
    public String getTargetIndex();

    /**
     * @param index The target fragment index.
     */
    public void setTargetIndex(String index) throws XBRLException;

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
     * @param role The containing extended link XLink role value
     */
    public void setLinkRole(URI role) throws XBRLException;
    
    
    /**
     * @return the XLink arcrole of the relationship.
     */
    public URI getArcrole();
    
    /**
     * @param role The XLink arcrole value
     */
    public void setArcrole(URI role) throws XBRLException;

    /**
     * @return the source element namespace.
     */
    public URI getSourceNamespace();        
    
    /**
     * @param namespace The namespace of the source element.
     */
    public void setSourceNamespace(URI namespace) throws XBRLException;
    
    /**
     * @return the target element namespace.
     */
    public URI getTargetNamespace();        
    
    /**
     * @param namespace The namespace of the target element.
     */
    public void setTargetNamespace(URI namespace) throws XBRLException;

    /**
     * @return the arc element namespace.
     */
    public URI getArcNamespace();        
    
    /**
     * @param namespace The namespace of the arc element.
     */
    public void setArcNamespace(URI namespace) throws XBRLException;
    
    /**
     * @return the link element namespace.
     */
    public URI getLinkNamespace();            

    /**
     * @param namespace The namespace of the containing extended link element.
     */
    public void setLinkNamespace(URI namespace) throws XBRLException;
    
    /**
     * @return the arc fragment index.
     */
    public String getArcIndex();

    /**
     * @param index the index of the arc defining this relationship.
     */
    public void setArcIndex(String index) throws XBRLException;
    
    /**
     * @return the source element name.
     */
    public String getSourceName();

    /**
     * @param name The local name of the source element.
     */
    public void setSourceName(String name) throws XBRLException;

    /**
     * @return the target element name.
     */
    public String getTargetName();

    /**
     * @param name The local name of the target element.
     */
    public void setTargetName(String name) throws XBRLException;

    /**
     * @return the arc element name.
     */
    public String getArcName();

    /**
     * @param name The local name of the arc element.
     */
    public void setArcName(String name) throws XBRLException;

    /**
     * @return the link element name.
     */
    public String getLinkName();
    
    /**
     * @param name The local name of the containing extended link element.
     */
    public void setLinkName(String name) throws XBRLException;
    
    /**
     * @return the source XLink role or null if none exists.
     */
    public URI getSourceRole();

    /**
     * @param role the source XLink role.
     */
    public void setSourceRole(URI role) throws XBRLException;

    /**
     * @return the target XLink role or null if none exists.
     */
    public URI getTargetRole();

    /**
     * @param role the target XLink role.
     */
    public void setTargetRole(URI role) throws XBRLException;


    /**
     * @return the source XML language code or null if none exists.
     */
    public String getSourceLanguageCode();

    /**
     * @param code the source XML language code.
     */
    public void setSourceLanguageCode(String code) throws XBRLException;

    /**
     * @return the target XML language code or null if none exists.
     */
    public String getTargetLanguageCode();

    /**
     * @param code the target XML language code.
     */
    public void setTargetLanguageCode(String code) throws XBRLException;

    /**
     * @return the type of the source fragment.
     */
    public String getSourceType();

    /**
     * @param code the source fragment type.
     */
    public void setSourceFragmentType(String type) throws XBRLException;

    
    /**
     * @return the type of the target fragment.
     */
    public String getTargetType();

    /**
     * @param code the target fragment type.
     */
    public void setTargetFragmentType(String type) throws XBRLException;
    
    /**
     * @return true if the relationship is to an XBRL 2.1 or generic label
     */
    public boolean isToLabel();
    
}
