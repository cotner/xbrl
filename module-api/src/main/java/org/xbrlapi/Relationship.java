package org.xbrlapi;

import java.net.URI;

import org.xbrlapi.utilities.XBRLException;

/**
 * This XML resource supports capture of relationship
 * information directly in the data store.  This 
 * can facilitate faster relationship analysis. 
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Relationship extends XML {

    /**
     * @return the fragment index for the source of the relationship.
     */
    public String getSourceIndex();

    /**
     * @param index The source fragment index.
     */
    public void setSourceIndex(String index);
    
    /**
     * @return the fragment index for the target of the relationship.
     */
    public String getTargetIndex();

    /**
     * @param index The target fragment index.
     */
    public void setTargetIndex(String index);

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
    public void setLinkRole(URI role);
    
    
    /**
     * @return the XLink arc role of the relationship.
     */
    public URI getArcRole();
    
    /**
     * @param role The arc XLink role value
     */
    public void setArcRole(URI role);
    
    /**
     * @return the arc element namespace.
     */
    public URI getArcNamespace();        
    
    /**
     * @param namespace The namespace of the arc element.
     */
    public void setArcNamespace(URI namespace);
    
    /**
     * @return the link element namespace.
     */
    public URI getLinkNamespace();            

    /**
     * @param namespace The namespace of the containing extended link element.
     */
    public void setLinkNamespace(URI namespace);
    
    /**
     * @return the arc element name.
     */
    public String getArcName();

    /**
     * @param name The local name of the arc element.
     */
    public void setArcName(String name);

    /**
     * @return the link element name.
     */
    public String getLinkName();
    
    /**
     * @param name The local name of the containing extended link element.
     */
    public void setLinkName(String name);
    
    /**
     * @return the source XLink role or null if none exists.
     */
    public URI getSourceRole();

    /**
     * @param role the source XLink role.
     */
    public void setSourceRole(URI role);

    /**
     * @return the target XLink role or null if none exists.
     */
    public URI getTargetRole();

    /**
     * @param role the target XLink role.
     */
    public void setTargetRole(URI role);

    /**
     * @return the source XML language code or null if none exists.
     */
    public String getSourceLanguageCode();

    /**
     * @param code the source XML language code.
     */
    public void setSourceLanguageCode(String code);

    /**
     * @return the target XML language code or null if none exists.
     */
    public String getTargetLanguageCode();

    /**
     * @param code the target XML language code.
     */
    public void setTargetLanguageCode(String code);

    /**
     * @return the type of the source fragment.
     */
    public String getSourceType();

    /**
     * @param code the source fragment type.
     */
    public void setSourceFragmentType(String type);

    
    /**
     * @return the type of the target fragment.
     */
    public String getTargetType();

    /**
     * @param code the target fragment type.
     */
    public void setTargetFragmentType(String type);
}
