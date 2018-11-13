import java.util.HashMap;

/**
 * An implementation of <tt>Cache</tt> that uses a least-recently-used (LRU)
 * eviction policy.
 */
public class LRUCache<T, U> implements Cache<T, U> {
	
	DataProvider<T, U> provider ;
	int capacity  , miss ,removed ;
	HashMap<T, Node<T,U>> map ;
	Node<T,U> head ; 
	Node<T,U> tail ; 
	
	 static class Node<T,U> 
	{
		Node<T,U> prev ;
		Node<T,U> next ;
		U data;
		T key;
		
		Node(Node<T,U> next , T key, U data) 
		{
			this.prev=null;
			this.next=next;
			this.data=data;
			this.key=key;
		}
	}
	
	/**
	 * @param provider the data provider to consult for a cache miss
	 * @param capacity the exact number of (key,value) pairs to store in the cache
	 */
	public LRUCache (DataProvider<T, U> provider, int capacity) 
	{
		this.provider=provider;
		this.capacity = capacity;
		miss=0;
		removed=0;
		map = new HashMap<T, Node<T,U> >(capacity , (float)1.0);
		Node<T, U> node = new Node<T, U>(null, null, null);
		head=node;
		tail=node;
	}
	
	
	/**
	 * Returns the value associated with the specified key.
	 * @param key the key
	 * @return the value associated with the key
	 */
	public U get (T key) 
	{
		if (map.containsKey(key))
			return refresh (map.get(key)) ;
		else 
		{
			++miss;
			return NewNode (key); 
		}
	}

	/**
	 * Returns the number of cache misses since the object's instantiation.
	 * @return the number of cache misses since the object's instantiation.
	 */
	public int getNumMisses () {
		return miss;
	}
	
	
	U refresh (Node<T,U> node )
	{
		// take the node out if it is not the head
		if (node.prev != null)
			node.prev.next= node.next;
		else if (node != tail)
			node.next.prev=node.prev;
		
		// put the node in the front
		head.prev=node;
		node.next=head;
		head=node;
		
		return node.data;
			
	}
	
	U NewNode (T key)
	
	{
		
		if (map.size()>=capacity)
		{
			// remove last node 
			tail=tail.prev;
			map.remove(tail.next.key);
			++removed;
		}
		
		// insert new node 
		Node<T,U> node = new Node<T,U>(head,key, provider.get(key));
		head.prev=node;
		node.next=head;
		head=node;
		map.put(key, node);
		
		return node.data;
	}

	
	
}



