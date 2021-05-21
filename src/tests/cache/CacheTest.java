/**
 * This class is responsible for tesing the Cache class and all its methods.
 * It extends the TestableAbstract class to ihertit is default behaviour 
 */

package tests.cache;
// Nesseccary imports
import org.junit.Assert;
import org.junit.Test;

import cache.Cache;
import tests.Testable;

public class CacheTest extends Testable {

    /**
     * This method tests creating a cache with a negative maximum capacity
     */
    @Test
    public void testCacheNegativeCapacity() {
        // error flag
        boolean hasError = false;

        try {
            // try and create a Cache object with a negative maximum capacity
            new Cache<String>(-1);
        } catch (Error e) {
            // error occured
            hasError = true;
        }
        // an error must be thrown if an attempt is made to create a cahe with a negative mavimum capacity
        Assert.assertTrue(hasError);
    }

    /**
     * This method tests creating a cache with a  maximum capacity of zero
     */
    @Test
    public void testCacheZeroCapacity() {

        // error flag
        boolean hasError = false;

        try {
            // try and create a Cache object with a negative maximum capacity
            new Cache<String>(0);
        } catch (Error e) {
            // error occured
            hasError = true;
        }
        // an error must be thrown if an attempt is made to create a cahe with a maximum capacity of zero
        Assert.assertTrue(hasError);
    }

    /**
     * This method tests creating a cache with a valid maximim capacity
     */
    @Test
    public void testCacheCapacity() {
        // error flag
        boolean hasError = false;

        try {
            // create a cache with a valid maximum capacity
            new Cache<String>(1);
        } catch (Error e) {
            // error occured
            hasError = true;
        }
        // no error should be thrown
        Assert.assertFalse(hasError);
    }

    /**
     * This method tets getting an item from the cache if it is empty
     */
    @Test
    public void testCacheGetEmpty() {
        // creates a cache with a valid maximum capacity
        Cache<String> testCache = new Cache<String>(1);
        // try to retreive an item not in the cache (cache miss)
        String result = testCache.get("A");
        // the result should be null
        Assert.assertNull(result);
    }

    /**
     * This method tests saving and retriving an item in the cache  
     */
    @Test
    public void testCacheSetAndGet() {

        // creates a cache with a valid maximum capacity
        Cache<String> testCache = new Cache<String>(1);
        // add an item to the cache
        testCache.set("A", "a");
        // retrieve an item in the cache
        String result = testCache.get("A");

        // the result should not be null
        Assert.assertNotNull(result);
        // the cahe should have the item stored (cache hit)
        Assert.assertTrue(testCache.has("A"));
    }

    /**
     * This method tests getting an item in the cache with a key of null
     */
    @Test
    public void testCacheGetNullKey() {
        // creates a cache with a valid maximum capacity 
        Cache<String> testCache = new Cache<String>(1);
        // error flag
        boolean hasError = false;

        try {
            // try to cretive an item in the cache with a key of null
            testCache.get(null);
        } catch (Error e) {
            // error occured
            hasError = true;
        }

        
        // an error must be thrown if an attempt is made to retrive an item in the cahe with a key of null
        Assert.assertTrue(hasError);

    }

    /**
     * This method tests saving an item to the cahe
     */
    @Test
    public void testCacheSetAndHas() {
        // creates a cache with a valid maximum capacity
        Cache<String> testCache = new Cache<String>(1);
        // saves an item to the cache
        testCache.set("A", "a");
        // the cache should contain the item (cache hit)
        Assert.assertTrue(testCache.has("A"));
        // the cache item shouls have the correct value
        Assert.assertEquals("a", testCache.get("A"));
    }

    
    /**
     * This method terst storing an item with a value of null
     */
    @Test
    public void testCacheSetNullValue() {
        // creates a cache with a valid maximum capacity
        Cache<String> testCache = new Cache<String>(1);
        // error flag
        boolean hasError = false;

        try {
            // tues to store an item with a null value
            testCache.set("A", null);
        } catch (Error e) {
            // error occured
            hasError = true;
        }
        // error must be thrown if an attempt is made to store a value of null
        Assert.assertTrue(hasError);

    }

    /**
     * This method is responsible for tesing storing an item with a key of null
     */
    @Test
    public void testCacheSetNullKey() {
        // creates a cache with a valid maximum capacity
        Cache<String> testCache = new Cache<String>(1);
        // error flag
        boolean hasError = false;

        try {
            // try and set an item in the cache with a key of null
            testCache.set(null, "a");
        } catch (Error e) {
            // error occured
            hasError = true;
        }

        // error must be thrown if an attempt is made to set an item with a key of null in the cache
        Assert.assertTrue(hasError);

    }

    /**
     * This method test trying to add an item that is already in the cache
     */
    @Test
    public void testCacheSetAlreadyInCache() {
        // create a vlaid cahe
        Cache<String> testCache = new Cache<String>(1);
        // error flag
        boolean hasError = false;

        try {
            // try and add an item to the cache twice (added to the cache the first time)
            testCache.set("A", "a");
            testCache.set("A", "a");
        } catch (Error e) {
            hasError = true;
            //error occured
        }

        // error must be thrown if an attempt is made to add an already existing item to teh cache
        Assert.assertTrue(hasError);
        // the item should be in the cahe
        Assert.assertTrue(testCache.has("A"));

    }

    /**
     * This method is responsible for testing adding a number of items to meet maximum capacity
     */
    @Test
    public void testCacheSetAtCapacity() {
        // creates a valid cahce
        Cache<String> testCache = new Cache<String>(2);
        // adds 2 items to the cache (capacity)
        testCache.set("A", "a");
        testCache.set("B", "b");
        
        // both items should be in the cache (cahe hit)
        Assert.assertTrue(testCache.has("A"));
        Assert.assertTrue(testCache.has("B"));
        // both item should be stored correctly with the corrent values
        Assert.assertEquals("b", testCache.get("B"));
        Assert.assertEquals("a", testCache.get("A"));
    }

    /**
     * This method is responsible for testing storing more items than the maximum capcity,
     * forcing the cqache to evict the least recently used item
     */
    @Test
    public void testCacheSetMoreThanCapacity() {
        // creates a valid cache
        Cache<String> testCache = new Cache<String>(2);
        // stores items in the cache (more than capacity)
        testCache.set("A", "a");
        testCache.set("B", "b");
        testCache.set("C", "c");

        // the "B" and "C" item should be in the cache (Most recently used)
        Assert.assertTrue(testCache.has("B"));
        Assert.assertTrue(testCache.has("C"));
        // the "A" items should not be in the cache (it should be eveicted -> least recently used)
        Assert.assertFalse(testCache.has("A"));
        // the items in the cache should be stored correctly with their correct values
        Assert.assertEquals("b", testCache.get("B"));
        Assert.assertEquals("c", testCache.get("C"));
    }

    /**
     * This method is responsible for testing if the cache has an item with a key of null
     */
    @Test
    public void testCacheHasNull() {
        // creates a valid cache
        Cache<String> testCache = new Cache<String>(2);
        // error flag
        boolean hasError = false;
        try {
            // try to test of the cache contains item with key of null
            testCache.has(null);
        } catch (Error e) {
            hasError = true;
            // error occured
        }
        // error must be thrown if an attempt is made to check is there is an item with a key of null
        // no keys can be null
        Assert.assertTrue(hasError);
    }

    /**
     * This method is responsible for testing if the cache has an item that wass not stored
     */
    @Test
    public void testCacheHasFalse() {
        // creates a valid cache
        Cache<String> testCache = new Cache<String>(2);
        // if an item with that key is not in the cache the result should be false
        Assert.assertFalse(testCache.has("A"));

    }

}
