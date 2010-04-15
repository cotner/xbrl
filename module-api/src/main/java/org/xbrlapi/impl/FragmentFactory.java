package org.xbrlapi.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.xbrlapi.XML;
import org.xbrlapi.data.Store;
import org.xbrlapi.utilities.XBRLException;

/**
 * Fragment instantiation factory used to create new fragments
 * of the correct fragment type from data in the data store.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class FragmentFactory<F> {

	private static final Logger logger = Logger.getLogger(FragmentFactory.class);
	
	/**
	 * Get the class for the specified class name.
	 * @param className The full name of the class to get.
	 * @return The class that has been named.
	 * @throws XBRLException if the class cannot be obtained.
	 */
	@SuppressWarnings("unchecked")
	public static Class getClass(String className) throws XBRLException {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if (loader != null) {
                try {
                    return loader.loadClass(className);
                } catch (Exception e) {
                    return Class.forName(className);
                }
            }
            return Class.forName(className);
        }
        catch (Exception e) {
            throw new XBRLException("Failed to load fragment class " + className,e);
        }
	}

	  /**
	    * Instantiate a fragment using its type.
	    * @param store The data store that contains the fragment.
	    * @param root The root element of the fragment.
	    * @return an instance of the fragment or null if it is not loadable.
	    * @throws XBRLException if the class cannot be loaded.
	    */
		@SuppressWarnings("unchecked")
	    public static <F extends XML> F newFragment(Store store, Element root) throws XBRLException {
			try {
			    
				if (root == null) throw new XBRLException("The data XML is null.");
				if (! root.hasAttribute("type")) {
					throw new XBRLException("The data does not identify the fragment type.");
				}

				String className = root.getAttribute("type");
		    	Class fragmentClass = getClass(className);

	        	Constructor constructor = fragmentClass.getConstructor();
		    	XML fragment = (XML) constructor.newInstance();
	            fragment.setStore(store);
	            fragment.setResource(root);
	            return (F) fragment;
	            
	        } catch (InvocationTargetException e) {
	            throw new XBRLException("Failed to instantiate the correct type of fragment because the constructor could not be invoked.",e);
	        } catch (IllegalAccessException e) {
	            throw new XBRLException("Attempted to access an inaccessible fragment constructor.",e);
	        } catch (NoSuchMethodException e) {
	            throw new XBRLException("Attempted to access a constructor that does not exist.  There was no such method.",e);
	        } catch (InstantiationException e) {
	            throw new XBRLException("Attempted to access a constructor that does not exist.Instantiation failed.",e);
	        }
	    }
}
