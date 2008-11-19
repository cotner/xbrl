package org.xbrlapi.aspects;

import org.xbrlapi.Context;
import org.xbrlapi.Entity;
import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.LabelResource;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class EntityIdentifierAspect extends ContextAspect implements Aspect {

    /**
     * @param aspectModel The aspect model with this aspect.
     * @throws XBRLException.
     */
    public EntityIdentifierAspect(AspectModel aspectModel) throws XBRLException {
        setAspectModel(aspectModel);
        setTransformer(new Transformer());
    }

    /**
     * @see Aspect#getType()
     */
    public String getType() {
        return Aspect.ENTITY_IDENTIFIER;
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
            if (! value.getFragment().isa("org.xbrlapi.impl.EntityImpl")) {
                throw new XBRLException("The aspect value must have an entity fragment.");
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
            Entity f = ((Entity) value.getFragment());
            String id = f.getIdentifierScheme() + ": " + f.getIdentifierValue();
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
            String label = id;
            Entity f = ((Entity) value.getFragment());
            FragmentList<LabelResource> labels = f.getEntityLabels();
            FINDLABEL: for (LabelResource l: labels) {
                if (
                        l.getLanguage().equals(getLanguageCode()) &&
                        l.getResourceRole().equals(getLabelRole())
                   ) {
                    label = l.getStringValue();
                    break FINDLABEL;
                }
            }
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
    public EntityIdentifierAspectValue getValue(Fact fact) throws XBRLException {
        try {
            return new EntityIdentifierAspectValue(this,getFragment(fact));
        } catch (XBRLException e) {
            return null;
        }
    }
    
    /**
     * @see Aspect#getFragmentFromStore(Fact)
     */
    public Fragment getFragmentFromStore(Fact fact) throws XBRLException {
        return ((Context) super.getFragmentFromStore(fact)).getEntity();
    }
    
}
