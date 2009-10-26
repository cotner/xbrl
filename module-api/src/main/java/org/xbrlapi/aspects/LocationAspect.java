package org.xbrlapi.aspects;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.apache.log4j.Logger;
import org.xbrlapi.Concept;
import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.LabelResource;
import org.xbrlapi.impl.InstanceImpl;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class LocationAspect extends BaseAspect implements Aspect {

    public final static String TYPE = "location";
    
    /**
     * @see Aspect#getType()
     */
    public String getType() {
        return TYPE;
    }
    

    
    private final static Logger logger = Logger.getLogger(LocationAspect.class);
    
    /**
     * @param aspectModel The aspect model with this aspect.
     * @throws XBRLException.
     */
    public LocationAspect(AspectModel aspectModel) throws XBRLException {
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
         * @see AspectValueTransformer#getIdentifier(AspectValue)
         */
        public String getIdentifier(AspectValue value) throws XBRLException {
            if (hasMapId(value)) {
                return getMapId(value);
            }
            Fact fact = value.<Fact>getFragment();
            String id = fact.getParentIndex() + fact.getIndex();
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
            
            String label = "";
                
            Fragment parent = value.getFragment().getParent();
            
            if (parent.isa(InstanceImpl.class)) {
                setMapLabel(id,label);
                return label;
            }
            
            Concept concept = (((Fact) parent).getConcept());
            List<LabelResource> labels = concept.getLabelsWithLanguageAndResourceRole(getLanguageCode(),getLabelRole());
            if (labels.isEmpty()) label = id;
            else label = labels.get(0).getStringValue();
            setMapLabel(id,label);
            return label;
            
        }
        
        
        /**
         * The label role is used in constructing the label for the
         * concept aspect values.
         */
        private URI role = Constants.StandardLabelRole;
        
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
    public LocationAspectValue getValue(Fact fact) throws XBRLException {
        return new LocationAspectValue(this,this.<Fact>getFragment(fact));
    }
    
    /**
     * @see Aspect#getFragmentFromStore(Fact)
     */
    @SuppressWarnings("unchecked")
    public Fact getFragmentFromStore(Fact fact) throws XBRLException {
        return fact;
    }
    
    /**
     * @see Aspect#getKey(Fact)
     */
    public String getKey(Fact fact) throws XBRLException {
        return fact.getParentIndex() + " " + fact.getIndex();
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
