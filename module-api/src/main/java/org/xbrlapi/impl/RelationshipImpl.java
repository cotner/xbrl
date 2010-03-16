package org.xbrlapi.impl;

import java.net.URI;
import java.net.URISyntaxException;

import org.xbrlapi.Arc;
import org.xbrlapi.ExtendedLink;
import org.xbrlapi.Fragment;
import org.xbrlapi.Relationship;
import org.xbrlapi.Resource;
import org.xbrlapi.builder.BuilderImpl;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class RelationshipImpl extends NonFragmentXMLImpl implements Relationship {

    /**
     * No argument constructor.
     * @throws XBRLException
     */
    public RelationshipImpl() throws XBRLException {
        super();
        setBuilder(new BuilderImpl());
    }    
    

    
    /**
     * @param arc The arc defining the relationship
     * @param source The source of the relationship
     * @param target The target of the relationship
     * @throws XBRLException
     */
    public RelationshipImpl(Arc arc, Fragment source, Fragment target) throws XBRLException {
        this();

        setStore(arc.getStore());
        
        if (arc == null) throw new XBRLException("The arc must not be null");
        if (source == null) throw new XBRLException("The source must not be null");
        if (source == null) throw new XBRLException("The target must not be null");
        ExtendedLink link = arc.getExtendedLink();

        setIndex(arc.getIndex() + source.getIndex() + target.getIndex());

        setSourceURI(source.getURI());
        setSourceIndex(source.getIndex());
        setSourceType(source.getType());
        setSourceName(source.getLocalname());
        setSourceNamespace(source.getNamespace());
        if (source.isa(ResourceImpl.class)) {
            setSourceLanguageCode(((Resource) source).getLanguage());
            setSourceRole(((Resource) source).getResourceRole());
        }

        setTargetURI(target.getURI());
        setTargetIndex(target.getIndex());
        setTargetType(target.getType());
        setTargetName(target.getLocalname());
        setTargetNamespace(target.getNamespace());
        if (target.isa(ResourceImpl.class)) {
            setTargetLanguageCode(((Resource) target).getLanguage());
            setTargetRole(((Resource) target).getResourceRole());
        }

        setArcIndex(arc.getIndex());
        setArcURI(arc.getURI());
        setArcName(arc.getLocalname());
        setArcNamespace(arc.getNamespace());
        setArcrole(arc.getArcrole());
        setArcOrder(arc.getOrder());
        setArcPriority(arc.getPriority());
        setArcUse(arc.getUse());

        setLinkIndex(link.getIndex());
        setLinkName(link.getLocalname());
        setLinkNamespace(link.getNamespace());
        setLinkRole(link.getLinkRole());

        setLabelStatus();
        setReferenceStatus();
        setSignature(arc);

        this.finalizeBuilder();
        
    }    
    
    /**
     * @param key The relationship to use in generating
     * the relationship signature that will be persisted.  THe 
     * signature is matched for relationship prohibition and overriding.
     * @throws XBRLException
     */
    private void setSignature(Arc arc) throws XBRLException {
        this.setMetaAttribute("signature",arc.getSemanticKey());
    }

    /**
     * @see org.xbrlapi.Relationship#getArc()
     */
    public Arc getArc() throws XBRLException {
        return (Arc) this.getStore().getXMLResource(getArcIndex());
    }
    
    
    /**
     * @see org.xbrlapi.Relationship#getArcIndex()
     */
    public String getArcIndex() {
        return getMetaAttribute("arcIndex");
    }
    
    /**
     * @see org.xbrlapi.Relationship#getArcURI()
     */
    public URI getArcURI() throws XBRLException {
        try {
            if (this.hasMetaAttribute("arcURI")) 
                return new URI(getMetaAttribute("arcURI"));
            throw new XBRLException("The arc URI is not defined for this relationship.The relationship has not be initialised properly.");
        } catch (URISyntaxException e) {
            throw new XBRLException("The arc URI is malformed.", e);
        }
    }
    
    /**
     * @see org.xbrlapi.Relationship#getSourceURI()
     */
    public URI getSourceURI() throws XBRLException {
        try {
            if (this.hasMetaAttribute("sourceURI")) 
                return new URI(getMetaAttribute("sourceURI"));
            throw new XBRLException("The source URI is not defined for this relationship.The relationship has not be initialised properly.");
        } catch (URISyntaxException e) {
            throw new XBRLException("The source URI is malformed.", e);
        }
    }
    
    /**
     * @see org.xbrlapi.Relationship#getTargetURI()
     */
    public URI getTargetURI() throws XBRLException {
        try {
            if (this.hasMetaAttribute("targetURI")) 
                return new URI(getMetaAttribute("targetURI"));
            throw new XBRLException("The target URI is not defined for this relationship.The relationship has not be initialised properly.");
        } catch (URISyntaxException e) {
            throw new XBRLException("The target URI is malformed.", e);
        }
    }

    /**
     * @param index the index of the arc defining this relationship.
     */
    private void setArcIndex(String index) throws XBRLException {
        this.setMetaAttribute("arcIndex",index);
    }
    
    /**
     * @param uri the URI of the arc defining this relationship.
     */
    private void setArcURI(URI uri) throws XBRLException {
        if (uri == null) throw new XBRLException("The arc URI must not be null.");
        this.setMetaAttribute("arcURI",uri.toString());
    }
    
    /**
     * @param uri the URI of the relationship source.
     */
    private void setSourceURI(URI uri) throws XBRLException {
        if (uri == null) throw new XBRLException("The source URI must not be null.");
        this.setMetaAttribute("sourceURI",uri.toString());
    }
    
    /**
     * @param uri the URI of the relationship target.
     */
    private void setTargetURI(URI uri) throws XBRLException {
        if (uri == null) throw new XBRLException("The target URI must not be null.");
        this.setMetaAttribute("targetURI",uri.toString());
    }
    
    /**
     * @see org.xbrlapi.Relationship#getArcName()
     */
    public String getArcName() {
        return getMetaAttribute("arcName");
    }

    /**
     * @see org.xbrlapi.Relationship#getArcNamespace()
     */
    public URI getArcNamespace() {
        try {
            return new URI(getMetaAttribute("arcNamespace"));
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * @see org.xbrlapi.Relationship#getArcrole()
     */
    public URI getArcrole() {
        try {
            return new URI(getMetaAttribute("arcRole"));
        } catch (URISyntaxException e) {
            return null;
        }
    }
    
    /**
     * @see org.xbrlapi.Relationship#getArcOrder()
     */
    public Double getArcOrder() {
        return new Double(getMetaAttribute("arcOrder"));
    }
    
    /**
     * @see org.xbrlapi.Relationship#getArcPriority()
     */
    public Integer getArcPriority() {
        return new Integer(getMetaAttribute("arcPriority"));
    }
    
    /**
     * @see org.xbrlapi.Relationship#getUse()
     */
    public String getUse() {
        String use = getMetaAttribute("arcUse");
        if (use== null) return "optional";
        return "prohibited";
    }    

    /**
     * @see org.xbrlapi.Relationship#getLinkName()
     */
    public String getLinkName() {
        return getMetaAttribute("linkName");
    }

    /**
     * @see org.xbrlapi.Relationship#getLinkNamespace()
     */
    public URI getLinkNamespace() {
        try {
            return new URI(getMetaAttribute("linkNamespace"));
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * @see org.xbrlapi.Relationship#getLinkRole()
     */
    public URI getLinkRole() {
        try {
            return new URI(getMetaAttribute("linkRole"));
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * @see org.xbrlapi.Relationship#getSource()
     */
    @SuppressWarnings("unchecked")
    public <F extends Fragment> F getSource() throws XBRLException {
        return (F) this.getStore().getXMLResource(getSourceIndex());
    }

    /**
     * @see org.xbrlapi.Relationship#getSourceIndex()
     */
    public String getSourceIndex() {
        return getMetaAttribute("sourceIndex");
    }

    /**
     * @see org.xbrlapi.Relationship#getSourceLanguageCode()
     */
    public String getSourceLanguageCode() {
        return getMetaAttribute("sourceLanguage");
    }

    /**
     * @see org.xbrlapi.Relationship#getSourceRole()
     */
    public URI getSourceRole() {
        try {
            String role = getMetaAttribute("sourceRole");
            if (role == null) return null;
            return new URI(role);
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * @see org.xbrlapi.Relationship#getSourceType()
     */
    public String getSourceType() {
        return getMetaAttribute("sourceType");
    }

    /**
     * @see org.xbrlapi.Relationship#getSourceName()
     */
    public String getSourceName() {
        return getMetaAttribute("sourceName");
    }    
    
    /**
     * @see org.xbrlapi.Relationship#getSourceNamespace()
     */
    public URI getSourceNamespace() {
        try {
            return new URI(getMetaAttribute("sourceNamespace"));
        } catch (URISyntaxException e) {
            return null;// Cannot be thrown.
        }
    }

    /**
     * @see org.xbrlapi.Relationship#getTarget()
     */
    @SuppressWarnings("unchecked")
    public <F extends Fragment> F getTarget() throws XBRLException {
        return (F) this.getStore().getXMLResource(getTargetIndex());
    }

    /**
     * @see org.xbrlapi.Relationship#getTargetIndex()
     */
    public String getTargetIndex() {
        return getMetaAttribute("targetIndex");
    }

    /**
     * @see org.xbrlapi.Relationship#getTargetLanguageCode()
     */
    public String getTargetLanguageCode() {
        return getMetaAttribute("targetLanguage");
    }

    /**
     * @see org.xbrlapi.Relationship#getTargetRole()
     */
    public URI getTargetRole() {
        try {
            String role = getMetaAttribute("targetRole");
            if (role == null) return null;
            return new URI(role);
        } catch (URISyntaxException e) {
            return null;
        }
    }

    
    
    /**
     * @see org.xbrlapi.Relationship#getSignature()
     */
    public String getSignature() {
        return getMetaAttribute("signature");
    }    

    /**
     * @see org.xbrlapi.Relationship#getTargetType()
     */
    public String getTargetType() {
        return getMetaAttribute("targetType");
    }

    /**
     * @see org.xbrlapi.Relationship#getTargetName()
     */
    public String getTargetName() {
        return getMetaAttribute("targetName");
    }    
    
    /**
     * @see org.xbrlapi.Relationship#getTargetNamespace()
     */
    public URI getTargetNamespace() {
        try {
            return new URI(getMetaAttribute("targetNamespace"));
        } catch (URISyntaxException e) {
            return null;// Cannot be thrown.
        }
    }
    
    /**
     * @param name The local name of the arc element.
     */
    private void setArcName(String name) throws XBRLException {
        this.setMetaAttribute("arcName",name);
    }

    /**
     * @param namespace The namespace of the arc element.
     */
    private void setArcNamespace(URI namespace) throws XBRLException {
        this.setMetaAttribute("arcNamespace",namespace.toString());
    }

    /**
     * @param role The XLink arcrole value
     * @throws XBRLException 
     */
    private void setArcrole(URI role) throws XBRLException {
        this.setMetaAttribute("arcRole",role.toString());
    }
    
    /**
     * @param order The arc order
     * @throws XBRLException if the order is null. 
     */
    private void setArcOrder(Double order) throws XBRLException {
        if (order == null) throw new XBRLException("The order must not be null."); 
        this.setMetaAttribute("arcOrder",order.toString());
    }
    
    /**
     * @param priority The arc priority
     * @throws XBRLException if the priority is null. 
     */
    private void setArcPriority(Integer priority) throws XBRLException {
        if (priority == null) throw new XBRLException("The priority must not be null."); 
        this.setMetaAttribute("arcPriority",priority.toString());
    }
    
    /**
     * This method sets a metadata attribute called arcUse
     * to a value of <code>prohibited</code> if the arc is
     * prohibiting and the attribute is left out of the metadata
     * otherwise.
     * @param use The arc use
     * @throws XBRLException if the use is null. 
     */
    private void setArcUse(String use) throws XBRLException {
        if (use == null) throw new XBRLException("The use must not be null."); 
        if (use.equals("prohibited")) 
            this.setMetaAttribute("arcUse","prohibited");
        else this.removeMetaAttribute("arcUse");
    }    

    /**
     * @param name The index of the containing extended link element.
     * @throws XBRLException 
     */
    private void setLinkIndex(String index) throws XBRLException {
        this.setMetaAttribute("linkIndex",index);
    }
    
    /**
     * @param name The local name of the containing extended link element.
     * @throws XBRLException 
     */
    private void setLinkName(String name) throws XBRLException {
        this.setMetaAttribute("linkName",name);
    }

    /**
     * @param namespace The namespace of the containing extended link element.
     * @throws XBRLException 
     */
    private void setLinkNamespace(URI namespace) throws XBRLException {
        this.setMetaAttribute("linkNamespace",namespace.toString());
    }

    /**
     * @param role The containing extended link XLink role value
     * @throws XBRLException 
     */
    private void setLinkRole(URI role) throws XBRLException {
        this.setMetaAttribute("linkRole",role.toString());
    }

    /**
     * @param code the source fragment type.
     * @throws XBRLException 
     */
    private void setSourceType(String type) throws XBRLException {
        this.setMetaAttribute("sourceType",type);
    }

    /**
     * @param index The source fragment index.
     * @throws XBRLException 
     */
    private void setSourceIndex(String index) throws XBRLException {
        this.setMetaAttribute("sourceIndex",index);
    }

    /**
     * @param code the source XML language code.
     * @throws XBRLException 
     */
    private void setSourceLanguageCode(String code) throws XBRLException {
        this.setMetaAttribute("sourceLanguage",code);
    }

    /**
     * @param role the source XLink role.
     * @throws XBRLException 
     */
    private void setSourceRole(URI role) throws XBRLException {
        if (role != null) {
            this.setMetaAttribute("sourceRole",role.toString());
        }
    }
    
    /**
     * @param name The local name of the source element.
     * @throws XBRLException 
     */
    private void setSourceName(String name) throws XBRLException {
        this.setMetaAttribute("sourceName",name);
    }    
    
    /**
     * @param namespace The namespace of the source element.
     * @throws XBRLException 
     */
    private void setSourceNamespace(URI namespace) throws XBRLException {
        this.setMetaAttribute("sourceNamespace",namespace.toString());
    }    

    /**
     * @param code the target fragment type.
     * @throws XBRLException 
     */
    private void setTargetType(String type) throws XBRLException {
        this.setMetaAttribute("targetType",type);
    }

    /**
     * @param index The target fragment index.
     * @throws XBRLException 
     */
    private void setTargetIndex(String index) throws XBRLException {
        this.setMetaAttribute("targetIndex",index);
    }

    /**
     * @param code the target XML language code.
     * @throws XBRLException 
     */
    private void setTargetLanguageCode(String code) throws XBRLException {
        this.setMetaAttribute("targetLanguage",code);
    }

    /**
     * @param role the target XLink role.
     * @throws XBRLException 
     */
    private void setTargetRole(URI role) throws XBRLException {
        if (role != null) {
            this.setMetaAttribute("targetRole",role.toString());
        }
    }

    /**
     * @param name The local name of the target element.
     * @throws XBRLException 
     */
    private void setTargetName(String name) throws XBRLException {
        this.setMetaAttribute("targetName",name);
    }    
    
    /**
     * @param namespace The namespace of the target element.
     * @throws XBRLException 
     */
    public void setTargetNamespace(URI namespace) throws XBRLException {
        this.setMetaAttribute("targetNamespace",namespace.toString());
    }        
    
    /**
     * @see org.xbrlapi.Relationship#isToLabel()
     */
    public boolean isToLabel() {
        if (this.getMetaAttribute("label") != null) return true;
        return false;
    }
    
    /**
     * @see org.xbrlapi.Relationship#isToReference()
     */
    public boolean isToReference() {
        if (this.getMetaAttribute("reference") != null) return true;
        return false;
    }    
    
    

    /**
     * Stores in the XML resource a 'label' attribute of
     * the root element if the relationship is to an XBRL 2.1 
     * or a Generic label.
     * @throws XBRLException 
     */
    private void setLabelStatus() throws XBRLException {
        if (getTargetName().equals("label")) {
            URI namespace = getTargetNamespace();
            if (namespace.equals(Constants.XBRL21LinkNamespace) || namespace.equals(Constants.GenericLabelNamespace)) {
                URI arcrole = getArcrole();
                if (arcrole.equals(Constants.LabelArcrole) || arcrole.equals(Constants.GenericLabelArcrole)) {
                    this.setMetaAttribute("label","true");
                }
            }
        }
    }

    /**
     * Stores in the XML resource a 'reference' attribute of
     * the root element if the relationship is to an XBRL 2.1 
     * or a Generic reference.
     * @throws XBRLException 
     */
    private void setReferenceStatus() throws XBRLException {
        if (getTargetName().equals("reference")) {
            URI namespace = getTargetNamespace();
            if (namespace.equals(Constants.XBRL21LinkNamespace) || namespace.equals(Constants.GenericLabelNamespace)) {
                URI arcrole = getArcrole();
                if (arcrole.equals(Constants.ReferenceArcrole) || arcrole.equals(Constants.GenericReferenceArcrole)) {
                    this.setMetaAttribute("reference","true");
                }
            }
        }
    }    
    
    /**
     * @see Relationship#isFromRoot()
     */
    public boolean isFromRoot() throws XBRLException {
        String sourceIndex = this.getSourceIndex();
        String query = "#roots#[@active and @targetIndex='" + sourceIndex + "' and @arcRole='" + this.getArcrole() + "'  and @linkRole='"+this.getLinkRole()+"']";
        return (getStore().queryCount(query) == 0);
    }

    /**
     * @see org.xbrlapi.Relationship#getArcAttributeValue(URI, java.lang.String)
     */
    public String getArcAttributeValue(URI namespace, String name)
            throws XBRLException {
        return getArc().getAttribute(namespace, name);
    }

    /**
     * @see org.xbrlapi.Relationship#getArcAttributeValue(java.lang.String)
     */
    public String getArcAttributeValue(String name) throws XBRLException {
        return getArc().getAttribute(name);
    }

    /**
     * @see org.xbrlapi.Relationship#getExtendedLink()
     */
    public ExtendedLink getExtendedLink() throws XBRLException {
        return getStore().<ExtendedLink>getXMLResource(getLinkIndex());
    }

    /**
     * @see org.xbrlapi.Relationship#getLinkIndex()
     */
    public String getLinkIndex() throws XBRLException {
        return this.getMetaAttribute("linkIndex");
    }

    /**
     * @see org.xbrlapi.Relationship#isProhibiting()
     */
    public boolean isProhibiting() throws XBRLException {
        return (this.getUse().equals("prohibited"));
    }
    
    

    
    
}
