package cache;

// import java.util.HashMap;
import java.util.Map;

import java.util.HashMap;

public class Cache<T> {

    private CacheNode<T> head;
    private CacheNode<T> tail;
    private int maxCapacity;
    private int size;
    private Map<String, CacheNode<T>> cacheMap = new HashMap<String, CacheNode<T>>();

    public Cache(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public T get(String key) {

        if (!cacheMap.containsKey(key))
            return null;

        CacheNode<T> node = cacheMap.get(key);

        // string equality
        if (node.key.equals(head.key)) {
            return node.value;
        }

        

        CacheNode<T> previous = node.previous;
        CacheNode<T> next = node.next;

        // string equality
        if (tail.key.equals(node.key) ) {
            previous.next = null;
            tail = previous;
        } else {
            previous.next = next;
            next.previous = previous;
        }

        // CacheNode<T> oldHead = head;
        head.previous = node;
        node.next = head;
        node.previous = null;
        head = node;


        return node.value;


    }

    public void set(String key, T value) {
        if (this.cacheMap.containsKey(key)) {
            return; // return error?
        }

        CacheNode<T> newNode = new CacheNode<T>(key, value);

        if (size == 0) {
            head = newNode;
            tail = newNode;
            size++;
            cacheMap.put(key, newNode);
            return;
        }

        if (size == this.maxCapacity) {
            this.cacheMap.remove(this.tail.key);
            tail = tail.previous;
            tail.next = null;
            size = size - 1;
        }

        head.previous = newNode;
        newNode.next = head;
        head = newNode;
        

        cacheMap.put(key, newNode);
        size++;

    }

    public boolean has(String key) {
        return cacheMap.containsKey(key);
    }

    // set()
    // get()
    // has()
}
