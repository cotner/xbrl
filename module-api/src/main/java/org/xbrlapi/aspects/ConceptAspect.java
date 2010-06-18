package org.xbrlapi.aspects;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import org.apache.log4j.Logger;
import org.xbrlapi.Concept;
import org.xbrlapi.Fact;
import org.xbrlapi.LabelResource;
import org.xbrlapi.utilities.XBRLException;

/**
 * <h2>Concept aspect details</h2>
 * 
 * <p>
 * All facts have a value for the concept aspect.  The concept aspect reflects 
 * the element that is used to express fact values.  Ideally, this would also be
 * able to reflect substitution group and/or XLink relationships between concepts.
 * Perhaps such complexities are best left to more sophisticated variants of the 
 * concept aspect.  What you want is to be able to filter facts based upon the 
 * concepts at the top of a heirarchy and to get all facts that have a concept aspect
 * value that is within the aspect value subtree specified by that higher-level concept.
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class ConceptAspect extends BaseAspect implements Aspect {

    /**
     * The serial version UID.
     * @see 
     * http://java.sun.com/javase/6/docs/platform/serialization/spec/version.html#6678
     * for information about what changes will require the serial version UID to be
     * modified.
     */
    private static final long serialVersionUID = 6087576193997045566L;
    public static String TYPE = "concept";
    
    /**
     * @see Aspect#getType()
     */
    public String getType() {
        return TYPE;
    }
    
    private static final Logger logger = Logger.getLogger(ConceptAspect.class);
    
    /**
     * @param aspectModel The aspect model with this aspect.
     * @throws XBRLException.
     */
    public ConceptAspect(AspectModel aspectModel) throws XBRLException {
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
            String id = getMapId(value);
            if (id == null) {
                Concept concept = value.<Concept>getFragment();
                id = concept.getTargetNamespace() + "#" + concept.getName();
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
            List<LabelResource> labels = concept.getLabels(getLanguageCodes(),getLabelRoles(), getLinkRoles());
            if (! labels.isEmpty()) {
                label = labels.get(0).getStringValue();
            } else {
                label = concept.getName();
            }
            setMapLabel(id,label);
            return label;
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
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
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
