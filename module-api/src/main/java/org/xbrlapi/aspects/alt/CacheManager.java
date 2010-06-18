package org.xbrlapi.aspects.alt;

/**
 * 
 * <p>
 * The aspect cache manager provides universal access to the aspect cache.
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class CacheManager {

    /**
     * The aspect cache that is being managed
     */
    private static Cache cache;

    {
        cache = new CacheImpl();
    }

    public static Cache getCache() {
        return cache;
    }

    public void setCache(Cache newCache) {
        if (newCache == null)
            cache = new CacheImpl();
        else
            cache = newCache;
    }

}
