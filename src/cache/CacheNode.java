/**
 * This class represents a single node that is used in the cache linked list and hashmap. 
 * It stores the value and key of the cached item, as well as pointers to the next and previous nodes
 */

package cache;

public class CacheNode<T> {
    /** The previous node in the cache linked list */
    public CacheNode<T> previous;
    /** The next node in the cache linked list */
    public CacheNode<T> next;
    /**The key of the item in the cache */
    public String key;
    /**The value of rhe item in the cache */
    public T value;

    /**
     * This is a constructor that creates a new CacheNode given the key and value of the item
     * @param key the key of the item
     * @param value the value of the item
     * 
     * precondition: the key should be a string and the value should be of type T. 
     * Because of how it is used in the Cache class, they will not be null.
     * postcondition: returns a new CacheNode
     */
    public CacheNode(String key, T value) {
        this.key = key;
        this.value = value;
    }
}
