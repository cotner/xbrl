package org.xbrlapi.aspects;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.xbrlapi.Concept;
import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.LabelResource;
import org.xbrlapi.impl.InstanceImpl;
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
            Fact fact = value.<Fact>getFragment();
            String id = fact.getIndex();
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
                String filename = parent.getURI().toString();
                int index = filename.lastIndexOf('/')+1;
                if (index < filename.length()) {
                    filename = filename.substring(filename.lastIndexOf('/')+1);
                }
                label = filename;
                setMapLabel(id,label);
                return label;
            }
            
            Concept concept = (((Fact) parent).getConcept());
            List<LabelResource> labels = concept.getLabels(getLanguageCodes(),getLabelRoles(),getLinkRoles());
            if (labels.isEmpty()) label = concept.getName();
            else label = labels.get(0).getStringValue();
            setMapLabel(id,label);
            return label;

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
        return fact.getIndex();
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
