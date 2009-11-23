package org.xbrlapi.aspects;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.xbrlapi.Context;
import org.xbrlapi.Entity;
import org.xbrlapi.EntityResource;
import org.xbrlapi.Fact;
import org.xbrlapi.LabelResource;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class EntityIdentifierAspect extends ContextAspect implements Aspect {

    public static String TYPE = "entity";
    
    /**
     * @see Aspect#getType()
     */
    public String getType() {
        return TYPE;
    }
    

    
    private final static Logger logger = Logger.getLogger(EntityIdentifierAspect.class);
    
    /**
     * @param aspectModel The aspect model with this aspect.
     * @throws XBRLException.
     */
    public EntityIdentifierAspect(AspectModel aspectModel) throws XBRLException {
        super(aspectModel);
        initialize();
    }
    
    protected void initialize() {
        this.setTransformer(new Transformer(this));
    }


    
    

    public class Transformer extends BaseAspectValueTransformer implements AspectValueTransformer {

        public Transformer(Aspect aspect) {
            super(aspect);
        }
        
        /**
         * @see AspectValueTransformer#getIdentifier(AspectValue)
         */
        public String getIdentifier(AspectValue value) throws XBRLException {

            if (hasMapId(value)) {
                return getMapId(value);
            }

            String id = "";
            Entity entity = value.<Entity>getFragment();
            if (entity != null) {
                id = entity.getIdentifierScheme() + "#" + entity.getIdentifierValue().trim().replaceFirst("^0+","");
            }
            
            setMapId(value,id);
            return id;
        }
        
        /**
         * @see AspectValueTransformer#getLabel(AspectValue)
         */
        public String getLabel(AspectValue value) throws XBRLException {
            Entity entity = ((Entity) value.getFragment());
            if (entity == null) return null;
            
            String id = getIdentifier(value);
            if (hasMapLabel(id)) {
                return getMapLabel(id);
            }

            // The default label to use if no other is available.
            String label = id;
            List<LabelResource> labels = new Vector<LabelResource>();
            
            // Get all the direct entity resources.
            List<EntityResource> entityResources = entity.getEntityResources();
            for (EntityResource entityResource: entityResources) {
                labels = entityResource.getLabels(getLanguageCodes(),getLabelRoles(), getLinkRoles());
                if (! labels.isEmpty()) {
                    label = labels.get(0).getStringValue();
                    break;
                }
            }
            
            // Try for a label on an equivalent entity resource.
            if (labels.isEmpty()) {
                Set<EntityResource> equivalentEntityResources = new HashSet<EntityResource>();
                for (EntityResource entityResource: entityResources) {
                    equivalentEntityResources.addAll(entityResource.getEquivalents());
                }
                equivalentEntityResources.removeAll(entityResources);
                for (EntityResource entityResource: equivalentEntityResources) {
                    labels = entityResource.getLabels(getLanguageCodes(),getLabelRoles(), getLinkRoles());
                    if (! labels.isEmpty()) {
                        label = labels.get(0).getStringValue();
                        break;
                    }
                }
            }

            setMapLabel(id,label);
            return label;
        }
    
    }
    
    /**
     * @see org.xbrlapi.aspects.Aspect#getValue(org.xbrlapi.Fact)
     */
    @SuppressWarnings("unchecked")
    public EntityIdentifierAspectValue getValue(Fact fact) throws XBRLException {
        Entity entity = this.<Entity>getFragment(fact);
        return new EntityIdentifierAspectValue(this,entity);
    }
    
    /**
     * @see Aspect#getFragmentFromStore(Fact)
     */
    @SuppressWarnings("unchecked")
    public Entity getFragmentFromStore(Fact fact) throws XBRLException {
        Context context = getContextFromStore(fact);
        if (context == null) return null;
        Entity entity = context.getEntity();
        return entity;
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
