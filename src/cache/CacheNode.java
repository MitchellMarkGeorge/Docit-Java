package cache;



public class CacheNode<T> {
    public CacheNode<T> previous;
    public CacheNode<T> next;
    public String key;
    public T value;

    public CacheNode(String key, T value) {
        this.key = key;
        this.value = value;
    }
}
