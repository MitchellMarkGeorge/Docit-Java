package cache;

// import java.util.HashMap;
import java.util.Map;

import java.util.HashMap;

public class Cache<T> {

    private CacheNode<T> head;
    private CacheNode<T> tail;
    private int maxCapacity;
    private Map<String, CacheNode<T>> cacheMap = new HashMap<String, CacheNode<T>>();

    public Cache(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public T get(String key) {

        if (!cacheMap.containsKey(key)) {
            return null;

        }

        CacheNode<T> node = cacheMap.get(key);

        removeNode(node); // removes node from current position
        setAsHead(node); // sets it as ahead


        return node.value;


    }

    public void set(String key, T value) {
        if (this.cacheMap.containsKey(key)) {
            return; // return error? // or should i let the value be updated?
        }

        CacheNode<T> newNode = new CacheNode<T>(key, value);

        if (cacheMap.size() == maxCapacity) { // >=???
            cacheMap.remove(tail.key); 
            removeNode(tail);
        }

        cacheMap.put(key, newNode);
        setAsHead(newNode);

    }

    public boolean has(String key) {
        return cacheMap.containsKey(key);
    }

    private void removeNode(CacheNode<T> node) {
        CacheNode<T> previous = node.previous;
        CacheNode<T> next = node.next;

        if (previous != null) {
            previous.next = next;
        } else {
            this.head = next;
        }

        if (next != null) {
            next.previous = previous;
        } else {
            this.tail = previous;
        }
    }
    private void setAsHead(CacheNode<T> node) {
        node.next = this.head;
        node.previous = null;

        if (head != null) {
            head.previous = node;
        }

        this.head = node;

        if (this.tail == null) {
            this.tail = node;
        }
    }

    // set()
    // get()
    // has()
}
