package org.xbrlapi.impl;

import java.util.Comparator;

import org.apache.log4j.Logger;
import org.xbrlapi.Language;
import org.xbrlapi.utilities.XBRLException;

/**
 * Facilitates sorting of fragments so that 
 * they can be organised into 
 * complete XML document in the right order.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class LanguageComparator implements Comparator<Language> {

    protected final static Logger logger = Logger.getLogger(LanguageComparator.class);
    
	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Language f1, Language f2) throws ClassCastException {
		try {

			// Handle direct equality
			if (f1.equals(f2)) {
				return 0;
			}
			
            int comparison = f1.getName().compareTo(f2.getName());
            if (comparison != 0) return comparison;
            comparison = f1.getEncoding().compareTo(f2.getEncoding());
            if (comparison != 0) return comparison;
            return f1.getCode().compareTo(f2.getCode());

		} catch (XBRLException e) {
			throw new ClassCastException("XBRL metadata for fragments is not available to facilitate a comparison. " + e.getMessage());
		}
	}

}
