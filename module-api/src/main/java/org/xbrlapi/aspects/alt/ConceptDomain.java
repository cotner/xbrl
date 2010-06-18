package org.xbrlapi.aspects.alt;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.xbrlapi.Schema;
import org.xbrlapi.data.Store;
import org.xbrlapi.impl.ConceptImpl;
import org.xbrlapi.impl.SchemaImpl;
import org.xbrlapi.utilities.XBRLException;

public class ConceptDomain extends Base implements Domain<ConceptAspectValue>, StoreHandler {

    /**
     * 
     */
    private static final long serialVersionUID = -1180707610130423730L;

    public ConceptDomain(Store store) throws XBRLException {
        super(store);
    }
    
    /**
     * @see Domain#getAllAspectValues()
     */
    public List<ConceptAspectValue> getAllAspectValues() throws XBRLException {
        
        List<ConceptAspectValue> values = new Vector<ConceptAspectValue>();
        Set<String> schemaIndices = getStore().queryForIndices("for $root in #roots#[@type='"+SchemaImpl.class.getName()+"'] return $root");
        for (String schemaIndex: schemaIndices) {
            Schema schema = getStore().<Schema>getXMLResource(schemaIndex);
            URI namespace = schema.getTargetNamespace();
            String query = "for $root in #roots#[@type='" + ConceptImpl.class.getName() + "' and @parentIndex='" + schemaIndex + "'] return $root/xbrlapi:data/xsd:element/@name";
            for (String name: getStore().queryForStrings(query)) {
                ConceptAspectValue value = new ConceptAspectValue(namespace,name);
                values.add(value);
                
            }
        }
        return values;
    }

    /**
     * @see Domain#getChildren(AspectValue)
     */
    public List<ConceptAspectValue> getChildren(ConceptAspectValue parent)
            throws XBRLException {
        return new Vector<ConceptAspectValue>();
    }

    /**
     * @see Domain#getDepth(AspectValue)
     */
    public int getDepth(ConceptAspectValue aspectValue) throws XBRLException {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * @see Domain#getParent(AspectValue)
     */
    public ConceptAspectValue getParent(ConceptAspectValue child)
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
    public boolean hasChildren(ConceptAspectValue value)
            throws XBRLException {
        return false;
    }

    /**
     * @see Domain#hasParent(AspectValue)
     */
    public boolean hasParent(ConceptAspectValue child) throws XBRLException {
        return false;
    }

    /**
     * @see Domain#isInDomain(AspectValue)
     */
    public boolean isInDomain(ConceptAspectValue candidate)
            throws XBRLException {
        try {
            getStore().getConcept(candidate.getNamespace(), candidate.getLocalname());
            return true;
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

}
