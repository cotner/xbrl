package org.xbrlapi.aspects.alt;

import java.net.URI;

import org.apache.log4j.Logger;
import org.xbrlapi.Entity;
import org.xbrlapi.Fact;
import org.xbrlapi.Item;
import org.xbrlapi.utilities.XBRLException;

/**
 * <h2>Entity aspect details</h2>
 * 
 * <p>
 * Entity aspects capture the entity identifier information in XBRL contexts.
 * They do not capture entity segment information.
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class EntityAspect extends AspectImpl<EntityAspectValue> implements Aspect {

    /**
     * 
     */
    private static final long serialVersionUID = -5421107025896386283L;

    private static final Logger logger = Logger.getLogger(EntityAspect.class);

    /**
     * The URI uniquely identifying this concept aspect.
     */
    public static final URI ID = URI.create("http://xbrlapi.org/aspect/entity/1.0");
    
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
    public EntityAspect(Domain<EntityAspectValue> domain) throws XBRLException {
        super(domain);
    }
    
    /**
     * @see Aspect#getValue(Fact)
     */
    @SuppressWarnings("unchecked")
    public EntityAspectValue getValue(Fact fact) throws XBRLException {
        if (fact.isTuple()) return new EntityAspectValue();
        if (fact.isNil()) return new EntityAspectValue();
        Item item = (Item) fact;
        Entity entity = item.getContext().getEntity();
        return new EntityAspectValue(entity.getIdentifierScheme(),entity.getIdentifierValue());
    }

    /**
     * @see Aspect#getDomain()
     */
    @SuppressWarnings("unchecked")
    public Domain<EntityAspectValue> getDomain() {
        return domain;
    }
    
    /**
     * @see Aspect#getMissingValue()
     */
    @SuppressWarnings("unchecked")
    public EntityAspectValue getMissingValue() {
        return new EntityAspectValue();
    }

}
