import java.util.*;

public class LRUCache<T, U> implements Cache<T, U> {
	protected static class Node<T, U> {
		T key;
		U value;
		Node<T, U> next, prev;

		Node (T key_, U value_) {
			key = key_;
			value = value_;
		}
	}
	final protected Map<T, Node<T, U> > keyToNodeMap;
	final int capacity;
	final Node<T, U> head, tail;
	int numMisses;
	DataProvider<T, U> provider;

	public int getNumMisses () {
		return numMisses;
	}

	public LRUCache (DataProvider<T, U> provider_, int capacity_) {
		if (capacity_ < 1) {
			throw new IllegalArgumentException("capacity must be at least 1");
		}
		keyToNodeMap = new HashMap<T, Node<T, U> >(capacity_);
		//keyToNodeMap = new TreeMap<T, Node>();
		provider = provider_;
		head = new Node<T, U>(null, null);
		tail = new Node<T, U>(null, null);
		head.next = tail;
		tail.prev = head;
		capacity = capacity_;
		numMisses = 0;
	}

	public U get (T key) {
		Node<T, U> node;
		if (! keyToNodeMap.containsKey(key)) {
			numMisses++;
			if (keyToNodeMap.size() == capacity) {
				// Need to evict the tail
				keyToNodeMap.remove(tail.prev.key);  // remove from hashmap
				tail.prev.prev.next = tail;
				tail.prev = tail.prev.prev;
			}
			node = new Node<T, U>(key, provider.get(key));
			keyToNodeMap.put(key, node);
		} else {
			node = keyToNodeMap.get(key);
			// Remove node
			node.prev.next = node.next;
			node.next.prev = node.prev;
		}
		// Move to front of list
		addToFront(node);
		return provider.get(key);  // BUG
	}

	protected void addToFront (Node<T, U> node) {
		node.next = head.next;
		node.prev = head;
		head.next.prev = node;
		head.next = node;
	}
}
