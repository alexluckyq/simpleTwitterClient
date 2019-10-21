package bell.assignment.simpletwitterclient.managers.cache

import android.content.Context
import android.location.Location
import bell.assignment.simpletwitterclient.managers.location.LocationManagerImpl
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking

object CacheManager {

    val LATLNG_KEY = "latlngKey"
    val RADIUS_KEY = "radiusKey"
    val DEFAULT_RADIUS = 5

    lateinit var  sharedPreferenceCache: SharedPreferencesCache
    lateinit var lruCache: Cache<Any, Any>

    fun init(context: Context) {
        sharedPreferenceCache = SharedPreferencesCache(context,"SimpleTwitterClientApplicationSharedPreference")
        lruCache  = Cache.createLruCache(3)
    }

    /**
     * get stored location from lrucache else defaulted to montreal location
     * @return Location object from googles
     */
    fun getCachedLocation(): Location {
        return runBlocking {
            var loccation = lruCache.get(LATLNG_KEY).await()
            if (loccation != null) {
                loccation as Location
            } else {
                val location = Location("")
                location.latitude = LocationManagerImpl.DEFAULT_LAT
                location.longitude = LocationManagerImpl.DEFAULT_LNG
                loccation = location
            }
            loccation as Location
        }
    }

    /**
     * Cache current retrieved location
     */
    fun setCurrentLocation(location: Location) {
        runBlocking {
            lruCache.set(LATLNG_KEY, value = location).await()
        }
    }

    /**
     * get cache from LurCache by Key
     */
    fun getLurCacheByKey(key: Any): Deferred<Any?> {
        return lruCache.get(key)
    }

    /**
     * remove the lurCache by key
     */
    fun removeLurCacheByKey(key: Any): Deferred<Unit> {
        return lruCache.remove(key)
    }

    /**
     * Update lurCache with Key and Value
     */
    fun setLurCache(key: Any, value: Any): Deferred<Unit> {
        return lruCache.set(key, value)
    }

    fun getCachedRadius(): Int {
        return runBlocking {
            sharedPreferenceCache.withInt().get(RADIUS_KEY).await()
                ?: DEFAULT_RADIUS
        }
    }

    fun setRadiusCache(value: Int) {
        runBlocking {
            sharedPreferenceCache.withInt().set(RADIUS_KEY, value = value).await()
        }
    }


}