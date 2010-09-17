package org.xbrlapi.aspects.alt;

import java.net.URI;

import org.apache.log4j.Logger;
import org.xbrlapi.Context;
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
public class EntityAspect extends AspectImpl implements Aspect {

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
    public EntityAspect(Domain domain) throws XBRLException {
        super(domain);
    }
    
    /**
     * @see Aspect#getValue(Fact)
     */
    public EntityAspectValue getValue(Fact fact) throws XBRLException {
        if (fact.isTuple()) return getMissingValue();
        if (fact.isNil()) return getMissingValue();
        return getValue(((Item) fact).getContext());
    }
    
    /**
     * @see Aspect#getValue(Context)
     */
    public EntityAspectValue getValue(Context context) throws XBRLException {
        return getValue(context.getEntity());
    }
    
    /**
     * @param entity The entity fragment to base the entity aspect value on.
     * @return the entity aspect value reflecting the scheme and value expressed by the entity aspect.
     * @throws XBRLException if the entity is null.
     */
    public EntityAspectValue getValue(Entity entity) throws XBRLException {
        if (entity == null) throw new XBRLException("The entity must not be null.");
        return new EntityAspectValue(entity.getIdentifierScheme(),entity.getIdentifierValue());
    }
    
    /**
     * @see Aspect#getMissingValue()
     */
    public EntityAspectValue getMissingValue() {
        return new EntityAspectValue();
    }

}
