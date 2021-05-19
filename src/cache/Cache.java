/**
 * This class is implementation of a simple and general caching system (hence the use of generics). This is used to cache all the selected projects
 * in the application to reduce the ammount of visits the application makes to the filesystem in orger to load projects. 
 * This cache implementation relies on the LRU (Last Recently Used) eviction policy, where the least recently used item 
 * (in this case, a project), is evicted from the cache when a new one is put in and the cache is at capacity.
 * @author Mitchell Mark-George
 */

//Nesseccary imports
package cache;

import java.util.Map;

import java.util.HashMap;

public class Cache<T> {

    /**
     * The actual cached items are stored (and sorted) in a linked list, and their
     * order is manipulated based on the LRU policy (from most recently used to least recently used)
     */

    /** The node of the list */
    private CacheNode<T> head;
    /** The tail node of the list */
    private CacheNode<T> tail;
    /** the maximum amount of the items that can be stored before eviction */
    private int maxCapacity;
    /** A hashmap is used so the set() and get() commands are super fact (O(1)) */
    private Map<String, CacheNode<T>> cacheMap = new HashMap<String, CacheNode<T>>();

    /**
     * A constructor that creates the cache and sets the maximum capacity
     * @param maxCapacity the manximim capacity of the cache
     * precondition: the max capacity should be an int and not zero or null
     * postcondition: a new Cache object should be reated withthe max capacity set
     */
    public Cache(int maxCapacity) {
        // if the given capacity if less than or equal to 0, throw an error
        // Cannot have a pacity of zero or a negative number
        if (maxCapacity <= 0) {
            throw new Error("The maxCapacity must be more than 0");
        }
        this.maxCapacity = maxCapacity;
    }

    /**
     * This method is resoinsible for getting an item in the cache. T
     * If the requested is not in the cache, it return null.
     * If the item is in the cache, it remobes it from its current position
     * and moves it to the head position (being the most resently used item in the cache).
     * @param key The key of the requested item in the cache
     * @return The requested item in the cache
     * 
     * precondition: the key should be a string and not null
     * postcondition: the item with the given key should be retirned and be set as the head of rthe cahe linked list
     */
    public T get(String key) {
        // the key cannot be null. Throw an error if it is
        if (key == null) {
            throw new Error("Key cannot be null.");
        }

        // if the requested key is not in the cachemap, retun null
        // (cache miss)
        // All items in the cache are in this map for fast and easy access.
        if (!cacheMap.containsKey(key)) {
            return null;

        }

        // get the cacheneode from the hashmap
        CacheNode<T> node = cacheMap.get(key);

        removeNode(node); // removes node from current position in the cache linke
        setAsHead(node); // sets it as the head of the cache linked list 

        return node.value; // retun the value of the node (the requested item in the cache)

    }

    /**
     * This method adds a new item to the cache. It saves the item in hashmap with a key for fast and easy access,
     * and the new item is also set as the head of the cache linked list. 
     * If the cache it at capacity when the new item is added to the cache, the least recently used item is evicted.
     * @param key the key of the new item in the cace. Used to acces this item later
     * @param value the item to tbe stored in the cache. 
     * 
     * 
     * precondition: neither the key or valie should be null. The key must be a string and the value should be of type T
     * the kay cannot be the same ke of an existing item in the cache
     * postcondition: the new node (with the key and value) should be added to the cache, and should be put as the head.
     * If the the maxcapacity has been reached, the least recently used item should be evicted.
     */
    public void set(String key, T value) {

        // if either the key or value are null, throw an error
        if (key == null || value == null) {
            throw new Error("Parameters cannot be null."); // can value be null
        }

        // if there is already an item in the cache this that key, throw an error
        // No items in the cache should be overwritten
        if (this.cacheMap.containsKey(key)) {
            throw new Error("An item with that key already exists in the cache.");
        }

        // creates a new cachenode with the item as its value and the keu
        CacheNode<T> newNode = new CacheNode<T>(key, value);

        // if the cache is at capacity, evict the least recently used item
        // removes the item from the map and the actual cache linked list.
        if (cacheMap.size() == maxCapacity) {
            cacheMap.remove(tail.key);
            removeNode(tail);
        }

        // Puts the new cachenode in the hashmao for easy access
        // and sets the node as the new head (most recently used item)
        cacheMap.put(key, newNode);
        setAsHead(newNode);

    }

    /**
     * This method returns a boolean, showing if an item with a specific key is in the cache
     * @param key the key of the requested item
     * @return boolean indicating of an item with that key is in the cache
     * 
     * precondition: the key is a string and not null
     * postcondition: the returned boolean should indicate if the the item is in the cache
     */
    public boolean has(String key) {
        // If the key is null throw an error
        if (key == null) {
            throw new Error("Key cannot be null.");
        }

        // return if the there is an item with the given key in the cache map
        return cacheMap.containsKey(key);
    }

    /**
     * This method is responsible for removing a node from its current position in the cache linked list
     * @param node the node to to be romoved
     * 
     * precondicitons node is a CacheNode and is in the cache (linkedlist). 
     * postcondition: the node should not be in the cache linked list
     */
    private void removeNode(CacheNode<T> node) {
        // gets the previous and next nodes of the current node
        CacheNode<T> previous = node.previous;
        CacheNode<T> next = node.next;

        // if there is a previous node, set the next pointer of that node as the current nodes next
        // Essentially remove all pointers for the previous node to the current node.
        if (previous != null) {
            previous.next = next;
        } else {
            this.head = next;
        } // if it is null, set the head of the linked list as the current node's next.
        // this is because the only time the current node would not have a previous node
        // would be if it was the head of the linked list

        // if there is a next node, set the previous pointer of that node to the previous node of the current
        // removes all pointer for the next node to the current node.
        if (next != null) {
            next.previous = previous;
        } else {
            this.tail = previous;
        } // if this is null, set the current nodes's previous node as the tail of the linked list
        // the nevt pointer for any node would only be null if it is the tail, so in this case
        // the current node is/was the tail
    }

    /**
     * This method sets a given node as the head of the cache linked list
     * @param node the node to be set as the head of the linked list
     * 
     * precondition: the node is a CacheNode and is current in the cache. 
     * It should also be the most recently used item.
     * precondition: the node should be set as the head of the linked list
     */
    
    private void setAsHead(CacheNode<T> node) {
        // set the current nodes next pointer at the head
        node.next = this.head;
        // set the previous pointer as null (there is no node before the head)
        node.previous = null;

        // if the head isn't null, the old head's previous pointer should point to the current node (the new head)
        if (this.head != null) {
            head.previous = node;
        }

        // set the head pointer to the current node (new head)
        this.head = node;

        // if there is no tail node (if the cache is empty), set the tail pointer to the current node
        if (this.tail == null) {
            this.tail = node;
        }
    }

   
}
