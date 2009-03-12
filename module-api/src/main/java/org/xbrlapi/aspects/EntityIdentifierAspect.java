package org.xbrlapi.aspects;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.xbrlapi.Context;
import org.xbrlapi.Entity;
import org.xbrlapi.EntityResource;
import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
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
            String id = f.getIdentifierScheme() + ": " + f.getIdentifierValue().trim().replaceFirst("^0+","");
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
            
            Entity entity = ((Entity) value.getFragment());
            List<EntityResource> entityResources = entity.getEntityResources();
            Set<EntityResource> equivalentEntityResources = new HashSet<EntityResource>();
            equivalentEntityResources.addAll(entityResources);
            for (EntityResource entityResource: entityResources) {
                equivalentEntityResources.addAll(entityResource.getEquivalents());
            }
            
            Set<LabelResource> labels = new HashSet<LabelResource>();
            for (EntityResource entityResource: equivalentEntityResources) {
                labels.addAll(entityResource.getLabelsWithLanguageAndResourceRole(getLanguageCode(),getLabelRole()));
            }
            if (! labels.isEmpty()) label = labels.iterator().next().getStringValue();

            logger.info("Entity id aspect value label is " + label);
            setMapLabel(id,label);
            return label;
        }
        
        /**
         * The label role is used in constructing the label for the
         * concept aspect values.
         */
        private URI role = Constants.StandardLabelRole();
        /**
         * @return the label resource role.
         */
        public URI getLabelRole() {
            return role;
        }
        /**
         * @param role The label resource role to use in
         * selecting labels for the concept.
         */
        public void setLabelRole(URI role) {
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
        try {
            return new EntityIdentifierAspectValue(this,get(fact));
        } catch (XBRLException e) {
            return null;
        }
    }
    
    /**
     * @see Aspect#getFromStore(Fact)
     */
    public Fragment getFromStore(Fact fact) throws XBRLException {
        return ((Context) super.getFromStore(fact)).getEntity();
    }
    
}
