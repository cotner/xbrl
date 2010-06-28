package org.xbrlapi.aspects.alt;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.xbrlapi.Schema;
import org.xbrlapi.data.Store;
import org.xbrlapi.impl.ConceptImpl;
import org.xbrlapi.impl.SchemaImpl;
import org.xbrlapi.utilities.XBRLException;

public class ConceptDomain extends Base implements Domain, StoreHandler {

    /**
     * 
     */
    private static final long serialVersionUID = -1180707610130423730L;

    protected final static Logger logger = Logger.getLogger(ConceptDomain.class);
    
    public ConceptDomain(Store store) throws XBRLException {
        super(store);
    }
    
    /**
     * @see Domain#getAspectId()
     */
    public URI getAspectId() { return ConceptAspect.ID; }
    
    /**
     * @see Domain#getAllAspectValues()
     */
    public List<AspectValue> getAllAspectValues() throws XBRLException {
        
        List<AspectValue> values = new Vector<AspectValue>();
        Set<String> schemaIndices = getStore().queryForIndices("for $root in #roots#[@type='"+SchemaImpl.class.getName()+"'] return $root");
        for (String schemaIndex: schemaIndices) {
            Schema schema = getStore().<Schema>getXMLResource(schemaIndex);
            URI namespace = schema.getTargetNamespace();
            String query = "for $root in #roots#[@type='" + ConceptImpl.class.getName() + "' and @parentIndex='" + schemaIndex + "'] return $root/xbrlapi:data/xsd:element/@name";
            for (String name: getStore().queryForStrings(query)) {
                AspectValue value = new ConceptAspectValue(namespace,name);
                values.add(value);
            }
        }
        return values;
    }

    /**
     * @see Domain#getChildren(AspectValue)
     */
    public List<AspectValue> getChildren(AspectValue parent)
            throws XBRLException {
        return new Vector<AspectValue>();
    }

    /**
     * @see Domain#getDepth(AspectValue)
     */
    public int getDepth(AspectValue aspectValue) throws XBRLException {
        return 0;
    }

    /**
     * @see Domain#getParent(AspectValue)
     */
    public AspectValue getParent(AspectValue child)
            throws XBRLException {
        return null;
    }

    /**
     * @see Domain#getSize()
     */
    public long getSize() throws XBRLException {
        return getStore().getNumberOfXMLResources(ConceptImpl.class);
    }

    /**
     * @see Domain#hasChildren(AspectValue)
     */
    public boolean hasChildren(AspectValue value)
            throws XBRLException {
        return false;
    }

    /**
     * @see Domain#hasParent(AspectValue)
     */
    public boolean hasParent(AspectValue child) throws XBRLException {
        return false;
    }

    /**
     * @see Domain#isInDomain(AspectValue)
     */
    public boolean isInDomain(AspectValue candidate)
            throws XBRLException {
        try {
            if (candidate instanceof ConceptAspectValue) {
                ConceptAspectValue value = (ConceptAspectValue) candidate;
                getStore().getConcept(value.getNamespace(), value.getLocalname());
                return true;
            }
            return false;
        } catch (XBRLException e) {
            return false;
        }
    }

    /**
     * @see Domain#isFinite()
     */
    public boolean isFinite() {
        return true;
    }

    /**
     * Returns false.
     * @see Domain#allowsMissingValues()
     */
    public boolean allowsMissingValues() {
        return false;
    }

    /**
     * @param first
     *            The first aspect value
     * @param second
     *            The second aspect value
     * @return -1 if the first aspect value is less than the second, 0 if they
     *         are equal and 1 if the first aspect value is greater than the
     *         second. Any aspect values that are not in this domain
     *         are placed last in the aspect value ordering.
     *         Otherwise, the comparison is based upon the natural ordering of
     *         the concept namespaces and then the concept local names.
     *         Missing values are ranked last among aspect values of the same type.
     */
    public int compare(AspectValue first, AspectValue second) {
        if (! (first instanceof ConceptAspectValue)) {
            logger.error("Aspect values of the wrong type are being compared.");
            return 1;
        }
        if (! (second instanceof ConceptAspectValue)) {
            logger.error("Aspect values of the wrong type are being compared.");
            return -1;
        }

        if (first.isMissing()) {
            if (second.isMissing()) return 0;
            return 1;
        }
        if (second.isMissing()) return -1;
        
        ConceptAspectValue f = (ConceptAspectValue) first;
        ConceptAspectValue s = (ConceptAspectValue) second;
     
        int result = f.getNamespace().compareTo(s.getNamespace());
        if (result != 0) return result;
        
        return (f.getLocalname().compareTo(s.getLocalname()));
    }

}
