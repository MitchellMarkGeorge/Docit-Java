package tests.services.cache;

import org.junit.Assert;
import org.junit.Test;

import cache.Cache;
import tests.Testable;

public class CacheTest extends Testable {

    @Test
    public void testCacheNegativeCapacity() {

        boolean hasError = false;

        try {
            new Cache<String>(-1);
        } catch (Error e) {
            hasError = true;
        }

        Assert.assertTrue(hasError);
    }

    @Test
    public void testCacheZeroCapacity() {

        boolean hasError = false;

        try {
            new Cache<String>(0);
        } catch (Error e) {
            hasError = true;
        }

        Assert.assertTrue(hasError);
    }

    @Test
    public void testCacheCapacity() {

        boolean hasError = false;

        try {
            new Cache<String>(1);
        } catch (Error e) {
            hasError = true;
        }

        Assert.assertFalse(hasError);
    }

    @Test
    public void testCacheGetEmpty() {

        Cache<String> testCache = new Cache<String>(1);
        String result = testCache.get("A");

        Assert.assertNull(result);
    }

    @Test
    public void testCacheSetAndGet() {

        Cache<String> testCache = new Cache<String>(1);
        testCache.set("A", "a");
        String result = testCache.get("A");

        Assert.assertNotNull(result);
        Assert.assertTrue(testCache.has("A"));
    }

    @Test
    public void testCacheGetNullKey() {

        Cache<String> testCache = new Cache<String>(1);
        boolean hasError = false;

        try {
            testCache.get(null);
        } catch (Error e) {
            hasError = true;
        }

        // String result = testCache.get("A");

        // Assert.assertNotNull(result);
        Assert.assertTrue(hasError);

    }

    @Test
    public void testCacheSetAndHas() {

        Cache<String> testCache = new Cache<String>(1);
        testCache.set("A", "a");
        // String result = testCache.get("A");

        // Assert.assertNotNull(result);
        Assert.assertTrue(testCache.has("A"));
        Assert.assertEquals("a", testCache.get("A"));
    }

    @Test
    public void testCacheSetNullAll() {

        Cache<String> testCache = new Cache<String>(1);
        boolean hasError = false;

        try {
            testCache.set(null, null);
        } catch (Error e) {
            hasError = true;
        }

        // String result = testCache.get("A");

        // Assert.assertNotNull(result);
        Assert.assertTrue(hasError);

    }

    @Test
    public void testCacheSetNullValue() {

        Cache<String> testCache = new Cache<String>(1);
        boolean hasError = false;

        try {
            testCache.set("A", null);
        } catch (Error e) {
            hasError = true;
        }

        Assert.assertTrue(hasError);

    }

    @Test
    public void testCacheSetNullKey() {

        Cache<String> testCache = new Cache<String>(1);
        boolean hasError = false;

        try {
            testCache.set(null, "a");
        } catch (Error e) {
            hasError = true;
        }

        // String result = testCache.get("A");

        // Assert.assertNotNull(result);
        Assert.assertTrue(hasError);

    }

    @Test
    public void testCacheSetAlreadyInCache() {

        Cache<String> testCache = new Cache<String>(1);
        boolean hasError = false;

        try {
            testCache.set("A", "a");
            testCache.set("A", "a");
        } catch (Error e) {
            hasError = true;
        }

        // String result = testCache.get("A");

        // Assert.assertNotNull(result);
        Assert.assertTrue(hasError);
        Assert.assertTrue(testCache.has("A"));

    }

    @Test
    public void testCacheSetAtCapacity() {

        Cache<String> testCache = new Cache<String>(2);

        testCache.set("A", "a");
        testCache.set("B", "b");
        // testCache.set("A", "c");

        // String result = testCache.get("A");

        // Assert.assertNotNull(result);
        Assert.assertTrue(testCache.has("A"));
        Assert.assertTrue(testCache.has("B"));

        Assert.assertEquals("b", testCache.get("B"));
        Assert.assertEquals("a", testCache.get("A"));
    }

    @Test
    public void testCacheSetMoreThanCapacity() {

        Cache<String> testCache = new Cache<String>(2);

        testCache.set("A", "a");
        testCache.set("B", "b");
        testCache.set("C", "c");

        // String result = testCache.get("A");

        // Assert.assertNotNull(result);
        Assert.assertTrue(testCache.has("B"));
        Assert.assertTrue(testCache.has("C"));
        Assert.assertFalse(testCache.has("A"));
        Assert.assertEquals("b", testCache.get("B"));
        Assert.assertEquals("c", testCache.get("C"));
    }

    @Test
    public void testCacheHasNull() {

        Cache<String> testCache = new Cache<String>(2);
        boolean hasError = false;
        try {
            testCache.has(null);
        } catch (Error e) {
            hasError = true;
        }

        Assert.assertTrue(hasError);

    }

    @Test
    public void testCacheHasFalse() {

        Cache<String> testCache = new Cache<String>(2);

        Assert.assertFalse(testCache.has("A"));

    }

}
