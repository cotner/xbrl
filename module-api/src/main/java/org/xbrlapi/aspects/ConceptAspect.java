package org.xbrlapi.aspects;

import org.xbrlapi.Concept;
import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.LabelResource;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class ConceptAspect extends BaseAspect implements Aspect {

    /**
     * @param aspectModel The aspect model with this aspect.
     * @throws XBRLException.
     */
    public ConceptAspect(AspectModel aspectModel) throws XBRLException {
        setAspectModel(aspectModel);
        setTransformer(new Transformer());
    }

    /**
     * @see Aspect#getType()
     */
    public String getType() {
        return Aspect.CONCEPT;
    }

    private class Transformer extends BaseAspectValueTransformer implements AspectValueTransformer {

        public Transformer() {
            super();
        }

        /**
         * @see AspectValueTransformer#validate(AspectValue)
         */
        public void validate(AspectValue value) throws XBRLException {
            super.validate(value);
            if (! value.getFragment().isa("org.xbrlapi.impl.ConceptImpl")) {
                throw new XBRLException("The aspect value must have a concept fragment.");
            }
        }
        
        /**
         * @see AspectValueTransformer#getIdentifier(AspectValue)
         */
        public String getIdentifier(AspectValue value) throws XBRLException {
            validate(value);
            if (hasMapId(value)) {
                return getMapId(value);
            }
            Concept f = ((Concept) value.getFragment());
            String id = f.getTargetNamespaceURI() + ": " + f.getName();
            setMapId(value,id);
            return id;
        }
        
        /**
         * @see AspectValueTransformer#getLabel(AspectValue)
         */
        public String getLabel(AspectValue value) throws XBRLException {
            String id = getIdentifier(value);
            if (hasMapLabel(id)) {
                return getMapLabel(id);
            }
            Concept f = ((Concept) value.getFragment());
            FragmentList<LabelResource> labels = f.getLabelsWithLanguageAndRole(getLanguageCode(),getLabelRole());
            if (labels.isEmpty()) return id;
            String label = labels.get(0).getStringValue();
            setMapLabel(id,label);
            return label;
        }
        
        /**
         * The label role is used in constructing the label for the
         * concept aspect values.
         */
        private String role = Constants.StandardLabelRole;
        /**
         * @return the label resource role.
         */
        public String getLabelRole() {
            return role;
        }
        /**
         * @param role The label resource role to use in
         * selecting labels for the concept.
         */
        public void setLabelRole(String role) {
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

        
    }

    /**
     * @see org.xbrlapi.aspects.Aspect#getValue(org.xbrlapi.Fact)
     */
    @SuppressWarnings("unchecked")
    public AspectValue getValue(Fact fact) throws XBRLException {
        return new ConceptAspectValue(this,getFragment(fact));
    }
    
    /**
     * @see Aspect#getFragmentFromStore(Fact)
     */
    public Fragment getFragmentFromStore(Fact fact) throws XBRLException {
        return fact.getConcept();
    }
    
    /**
     * @see Aspect#getFragmentKey(Fact)
     */
    public String getFragmentKey(Fact fact) throws XBRLException {
        return fact.getNamespaceURI() + fact.getLocalname();
    }

}
