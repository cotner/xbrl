package org.xbrlapi.impl;

import org.xbrlapi.Concept;
import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.Instance;
import org.xbrlapi.Tuple;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class FactImpl extends FragmentImpl implements Fact {

    /**
     * Return true if the fact is a tuple and false otherwise
     */
    public boolean isTuple() throws XBRLException {
        return (! this.getDataRootElement().hasAttribute("contextRef"));
    }
    
    
	/**
	 * @see org.xbrlapi.Fact#getInstance()
	 */
	public Instance getInstance() throws XBRLException {
		return (Instance) this.getAncestorOrSelf("org.xbrlapi.impl.InstanceImpl");
	}


	
	/**
	 * @see org.xbrlapi.Fact#getTuple()
	 */
	public Tuple getTuple() throws XBRLException {
		Fragment parent = this.getParent();
		if (parent.getType().equals("org.xbrlapi.impl.TupleImpl")) return (Tuple) parent;
		return null;
	}



	/**
	 * @see org.xbrlapi.Fact#getConcept()
	 */
	public Concept getConcept() throws XBRLException {
		
		FragmentList<Concept> candidates = getStore().<Concept>query("/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@type='org.xbrlapi.impl.ConceptImpl' and "+ Constants.XBRLAPIPrefix+ ":" + "data/*/@name='" + this.getLocalname() + "']");
		for (Concept concept: candidates) {
			if (this.getNamespaceURI().equals(concept.getTargetNamespaceURI())) 
				return concept;
		}
		throw new XBRLException("No concept could be found for the fact.");
	}
	
	/**
	 * @see org.xbrlapi.Fact#isNumeric()
	 */
	public boolean isNumeric() throws XBRLException {
		
		String unitRef = this.getDataRootElement().getAttribute("unitRef");
		return (unitRef != "");
	}

}
