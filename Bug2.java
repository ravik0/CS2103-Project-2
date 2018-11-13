import java.util.HashMap;

/**
 * An implementation of <tt>Cache</tt> that uses a least-recently-used (LRU)
 * eviction policy.
 */
public class LRUCache<T, U> implements Cache<T, U>
{
	//Node class (for doublylinkedlist)
    static class Node<T,U> {
    	Node right;
        Node left;
        U value;
        T key;
    }
    
    //DoubleLinkedList class, organizes and changes nodes
    static class DoublyLinkedList<T> {
        private Node head = new Node();

        DoublyLinkedList() {
            head.left = head.right = head;
        }

        T removeHead() {
            Node ret = head.right;
            remove(ret);
            return (T) ret.key;
        }

        //Links a node into the list
        private void link(Node n) {
            Node tail = head.left;
            tail.right = n;
            n.left = tail;
            n.right = head;
            head.left = n;
        }

        //removes given node from list
        private void remove(Node n) {
        	Node next = n.right;
            Node prev = n.left;
            next.left = prev;
            prev.right = next;
            n.left = n.right = null;
        }

    }
    
    int capacity = 0;

    DoublyLinkedList list = new DoublyLinkedList();
    HashMap<T, Node> mapCache = new HashMap<>();

    // insert into linked list, mapCache
    private void insert(Node n) {
        mapCache.put((T) n.key, n);
        list.link(n);
    }
    
    // delete from the linked list, mapCache
    private Node delete(T key) {
        if (!mapCache.containsKey(key)){
        	return null;
        }
        Node n = mapCache.get(key);
        mapCache.remove(n.key);
        list.remove(n);
        return n;
    }
    
    int numMisses = 0;
    
	/**
	 * @param provider the data provider to consult for a cache miss
	 * @param capacity the exact number of (key,value) pairs to store in the cache
	 */
	public LRUCache (DataProvider<T, U> provider, int capacity) {
		this.capacity = capacity;
	}

	/**
	 * Returns the value associated with the specified key.
	 * @param key the key
	 * @return the value associated with the key
	 */
	public U get (T key) {
		Node n = delete(key);
        if (n == null){
        	numMisses++;
        	set(key, null);
            return null;
        }
        insert(n);
        return (U) n.value;
	}
	
	public void set(T key, U value) {
        delete(key);
        Node n = new Node();
        n.value = value;
        n.key = key;
        insert(n);
        if (mapCache.size() > capacity) {
            T removedKey = (T) list.removeHead();
            mapCache.remove(removedKey);
        }
	}
	
	/**
	 * Returns the number of cache misses since the object's instantiation.
	 * @return the number of cache misses since the object's instantiation.
	 */
	public int getNumMisses () {
		return numMisses;
	}
}
