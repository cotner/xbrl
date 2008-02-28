package org.xbrlapi.utilities;

import org.apache.log4j.Logger;
import org.apache.xerces.util.XMLGrammarPoolImpl;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;

public class XBRLAPIGrammarPoolImpl extends XMLGrammarPoolImpl {

	protected static Logger logger = Logger.getLogger(XBRLAPIGrammarPoolImpl.class);	
	
	public XBRLAPIGrammarPoolImpl() {
		super();
	}

	public XBRLAPIGrammarPoolImpl(int arg0) {
		super(arg0);
	}

	/* (non-Javadoc)
	 * @see org.apache.xerces.util.XMLGrammarPoolImpl#cacheGrammars(java.lang.String, org.apache.xerces.xni.grammars.Grammar[])
	 */
	public void cacheGrammars(String arg0, Grammar[] arg1) {
		super.cacheGrammars(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see org.apache.xerces.util.XMLGrammarPoolImpl#containsGrammar(org.apache.xerces.xni.grammars.XMLGrammarDescription)
	 */
	public boolean containsGrammar(XMLGrammarDescription arg0) {
		boolean b = super.containsGrammar(arg0);
		return b;
	}

	/* (non-Javadoc)
	 * @see org.apache.xerces.util.XMLGrammarPoolImpl#getGrammar(org.apache.xerces.xni.grammars.XMLGrammarDescription)
	 */
	public Grammar getGrammar(XMLGrammarDescription arg0) {
		Grammar g =  super.getGrammar(arg0);
		return g;
	}

	/* (non-Javadoc)
	 * @see org.apache.xerces.util.XMLGrammarPoolImpl#putGrammar(org.apache.xerces.xni.grammars.Grammar)
	 */
	public void putGrammar(Grammar arg0) {
		super.putGrammar(arg0);
	}

	/* (non-Javadoc)
	 * @see org.apache.xerces.util.XMLGrammarPoolImpl#retrieveGrammar(org.apache.xerces.xni.grammars.XMLGrammarDescription)
	 */
	public Grammar retrieveGrammar(XMLGrammarDescription arg0) {
		Grammar g = super.retrieveGrammar(arg0);
		return g;
	}
}
