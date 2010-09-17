package org.xbrlapi.aspects.alt;

import java.net.URI;

import org.apache.log4j.Logger;
import org.xbrlapi.Fact;
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
public class ConceptAspect extends AspectImpl implements Aspect {

    /**
     * The serial version UID.
     * @see 
     * http://java.sun.com/javase/6/docs/platform/serialization/spec/version.html#6678
     * for information about what changes will require the serial version UID to be
     * modified.
     */
    private static final long serialVersionUID = -1595719766119532306L;
    
    private static final Logger logger = Logger.getLogger(ConceptAspect.class);

    /**
     * The URI uniquely identifying this concept aspect.
     */
    public static final URI ID = URI.create("http://xbrlapi.org/aspect/concept/1.0");
    
    /**
     * @see Aspect#getId()
     */
    public URI getId() {
        return ID;
    }
    
    /**
     * @param domain The domain for this aspect.
     * @throws XBRLException
     */
    public ConceptAspect(Domain domain) throws XBRLException {
        super(domain);
    }
    
    /**
     * @see Aspect#getValue(Fact)
     */
    public ConceptAspectValue getValue(Fact fact) throws XBRLException {
        return new ConceptAspectValue(fact.getNamespace(),fact.getLocalname());
    }
    


    

    /**
     * @see Aspect#getMissingValue()
     */
    public ConceptAspectValue getMissingValue() {
        return new ConceptAspectValue();
    }

}
