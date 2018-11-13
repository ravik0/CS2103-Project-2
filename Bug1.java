import java.util.HashMap;

/**
 * An implementation of <tt>Cache</tt> that uses a least-recently-used (LRU)
 * eviction policy.
 */
public class LRUCache<T, U> implements Cache<T, U> {
  //LList is a linked hashmap;
  private class Node{
    public T key;
    public U item;
    public Node next;
    public Node prev;
    public Node(T n, U d){
      key = n;
      item = d; 
      next = null;
      prev = null;
    }
  }
  private class LList{
    private Node head;
    private Node tail;
    private int size; 
    private int capacity;
    private HashMap data;
    
    public LList(int c){
      head = null;
      tail = null;
      capacity = c;
      size = 0;
      data = new HashMap(c);
    }
    
    public void queue(Node n){
      if(head == null){
        head = n;
        tail = n;
        size++;
        data.put(n.key, n);
      }
      else{
        n.next = head;
        head.prev = n;
        head = n;
        size++;
        head.prev = tail;
        data.put(n.key, n);
        if(size > capacity){
          data.remove(tail.key);
          tail = tail.prev;
          tail.next = head;
          head.prev = tail;
        }
      }
    }
    
    public void requeue(Node n){
      if(size == 2){
        Node x = head;
        head = tail;
        tail = x;
      }
      if(size > 2){
        n.prev.next = n.next;
        n.next.prev = n.prev;
        n.next = head;
        head.prev = n;
        head = n;
        n.prev = tail;
      }
    }
    
    public Node get(T key){
      return (Node) data.get(key);
    }
  }
  /**
   * @param provider the data provider to consult for a cache miss
   * @param capacity the exact number of (key,value) pairs to store in the cache
   */
  private DataProvider<T, U> provider;
  private int capacity;
  private int misses;
  private LList storage;
  
  public LRUCache (DataProvider<T, U> p, int c) {
    provider = p;
    capacity = c;
    storage = new LList(c);
  }
  /**
   * Returns the value associated with the specified key.
   * @param key the key
   * @return the value associated with the key
   */
  public U get(T key){
    if(storage.get(key) != null){
      Node x = storage.get(key);
      storage.requeue(x);
      return x.item;
    }
    else{
      misses++;
      storage.queue(new Node(key, provider.get(key)));
      return (U) provider.get(key);
    }
  }
  /**
   * Returns the number of cache misses since the object's instantiation.
   * @return the number of cache misses since the object's instantiation.
   */
  public int getNumMisses () {
    return misses;
  }
}  
