package org.xbrlapi.aspects;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.xbrlapi.Concept;
import org.xbrlapi.Fact;
import org.xbrlapi.LabelResource;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * This aspect draws on information from a chosen concept 
 * presentation network.  It also draws on location information.
 * 
 * Each aspect value reflects fact location (within tuple structures)
 * and the relevant node in the presentation network.
 * 
 * For a fact to have a value for this aspect, its concept 
 * MUST be a node in the presentation network.
 * 
 * Two facts for the one concept that are in different tuples 
 * (or where one is nested within a tuple
 * and one is not) result in two different aspect values.  Otherwise
 * they imply the same value for this presentation aspect.
 * 
 * Note that adding a fact to this aspect can lead to the aspect 
 * having multiple aspect values added to it, one for each of the 
 * concepts in the presentation network along the branches from the network
 * roots to the nodes at which the fact's concept is located.
 * 
 * If you are using this aspect, consider removing the concept and 
 * location aspects from the relevant aspect model.
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class PresentationAspect extends BaseAspect implements Aspect {

    public static String TYPE = "presentation";
    
    /**
     * @see Aspect#getType()
     */
    public String getType() {
        return TYPE;
    }
    
    private final static Logger logger = Logger.getLogger(PresentationAspect.class);
    
    /**
     * @param aspectModel The aspect model with this aspect.
     * @throws XBRLException.
     */
    public PresentationAspect(AspectModel aspectModel) throws XBRLException {
        super(aspectModel);
        initialize();
    }
    
    protected void initialize() {
        this.setTransformer(new Transformer());
    }

    public class Transformer extends BaseAspectValueTransformer implements AspectValueTransformer {

        public Transformer() {
            super();
        }

        /**
         * The presentation aspect value identifier has to reflect
         * the relevant node of the presentation network.  This requires
         * the XLink arc index value and the relationship's target concept
         * index value to be part of the identifier.
         * 
         * The presentation aspect value identifier also has to reflect location
         * information and so it includes the parent tuple's index value for those 
         * aspect values that correspond to facts that are nested within tuples.
         * 
         * @see AspectValueTransformer#getIdentifier(AspectValue)
         */
        public String getIdentifier(AspectValue value) throws XBRLException {
            String id = getMapId(value);
            if (id == null) {
                Concept concept = value.<Concept>getFragment();
                id = concept.getTargetNamespace() + ":" + concept.getName();
                setMapId(value,id);
            }
            return id;
        }
        
        /**
         * @see AspectValueTransformer#getLabel(AspectValue)
         */
        public String getLabel(AspectValue value) throws XBRLException {

            String id = getIdentifier(value);
            if (hasMapLabel(id)) return getMapLabel(id);
            
            String label = id;
            Concept concept = value.<Concept>getFragment();
            if (concept == null) return id;
            List<String> languages = new Vector<String>();
            languages.add(getLanguageCode());
            languages.add(null);
            List<URI> roles = new Vector<URI>();
            roles.add(getLinkRole());
            roles.add(null);
            List<LabelResource> labels = concept.getLabels(languages,roles);
            if (! labels.isEmpty()) {
                label = labels.get(0).getStringValue();
            } else {
                label = concept.getName();
            }
            setMapLabel(id,label);
            return label;
        }
        
        /**
         * The label role is used in constructing the label for the
         * concept aspect values.
         */
        private URI role = Constants.StandardLabelRole;
        
        /**
         * @return the presentation network link role.
         */
        public URI getLinkRole() {
            return role;
        }
        /**
         * @param role The presentation network link role to use.
         */
        public void setLinkRole(URI role) {
            this.role = role;
        }

        /**
         * The language code is used in constructing the label for the
         * concept aspect values.
         */
        private String language = "en";
        /**
         * @return the language code.
         */
        public String getLanguageCode() {
            return language;
        }
        
        /**
         * @param language The ISO language code
         */
        public void setLanguageCode(String language) throws XBRLException {
            if (language == null) throw new XBRLException("The language must not be null.");
            this.language = language;
        }

        /**
         * The label role is used in constructing the label for the
         * concept aspect values.
         */
        private URI labelRole = Constants.StandardLabelRole;
        
        /**
         * @return the label resource role.
         */
        public URI getLabelRole() {
            return labelRole;
        }
        /**
         * @param role The label resource role to use in
         * selecting labels for the concept.
         */
        public void setLabelRole(URI role) {
            this.labelRole = role;
        }
        
    }

    /**
     * @see org.xbrlapi.aspects.Aspect#getValue(org.xbrlapi.Fact)
     */
    @SuppressWarnings("unchecked")
    public ConceptAspectValue getValue(Fact fact) throws XBRLException {
        return new ConceptAspectValue(this,this.<Concept>getFragment(fact));
    }
    
    /**
     * @see Aspect#getFragmentFromStore(Fact)
     */
    @SuppressWarnings("unchecked")
    public Concept getFragmentFromStore(Fact fact) throws XBRLException {
        return fact.getConcept();
    }
    
    /**
     * @see Aspect#getKey(Fact)
     */
    public String getKey(Fact fact) throws XBRLException {
        return fact.getNamespace() + "#" + fact.getLocalname();
    }

    /**
     * Handles object inflation.
     * @param in The input object stream used to access the object's serialization.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject( );
        initialize();
    }
    
    /**
     * Handles object serialization
     * @param out The input object stream used to store the serialization of the object.
     * @throws IOException
     */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }
 
    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
       return true;
    }
    
}
