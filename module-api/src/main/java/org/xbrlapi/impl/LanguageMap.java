package org.xbrlapi.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xbrlapi.Language;
import org.xbrlapi.utilities.XBRLException;

/**
 * Enables easy management of collections of language definitions.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class LanguageMap {
    
    private Map<String,Map<String,Language>> maps = new HashMap<String,Map<String,Language>>();
    
    /**
     * Adds a language to the map.
     * @param language The language to add.
     * @throws XBRLException
     */
    public void addLanguage(Language language) throws XBRLException {
        String code = language.getCode();
        if (maps.containsKey(code)) {
            maps.get(code).put(language.getEncoding(),language);
        } else {
            Map<String,Language> map = new HashMap<String,Language>();
            map.put(language.getEncoding(),language);
            maps.put(code,map);
        }
    }
    
    /**
     * Adds a list of languages to the map.
     * @param languages The languages to add.
     * @throws XBRLException
     */
    public void addAll(List<Language> languages) throws XBRLException {
        for (Language language: languages) {
            this.addLanguage(language);
        }
    }
    
    /**
     * @return the set of all codes of languages in the map.
     */
    public Set<String> getCodes() { 
        return this.maps.keySet();
    }

    /**
     * @param code The code to test for.
     * @param encoding The encoding to test for.
     * @return true if a language is in the map that
     * gives the name for the code language in the encoding language.
     */
    public boolean contains(String code,String encoding) {
        if (!maps.containsKey(code)) return false;
        if (! maps.get(code).containsKey(encoding)) return false;
        return true;
    }

    /**
     * @param code the code of the language.
     * @param encoding the encoding of the langauge.
     * @return the language or null if none is available.
     */
    public Language get(String code,String encoding) {
        if (! maps.containsKey(code)) return null;
        return maps.get(code).get(encoding);
    }
}