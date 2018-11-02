import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of <tt>Cache</tt> that uses a least-recently-used (LRU)
 * eviction policy.
 */
public class LRUCache<T, U> implements Cache<T, U> {
	private Map<T, CachedObject> cache;
	private DataProvider<T,U> provider;
	private int capacity;
	private int timesMissed;
	private CachedObject front;
	private CachedObject back;
	/**
	 * @param provider the data provider to consult for a cache miss
	 * @param capacity the exact number of (key,value) pairs to store in the cache
	 */
	public LRUCache (DataProvider<T, U> provider, int capacity) {
		cache = new HashMap<T,CachedObject>(capacity);
		this.provider = provider;
		this.capacity = capacity;
		timesMissed = 0;
		front = null;
		back = null;
	}
	
	/**
	 * Returns the value associated with the specified key.
	 * @param key the key
	 * @return the value associated with the key
	 */
	public U get (T key) {
		if(cache.containsKey(key)) {
			final CachedObject ret = cache.get(key);
			addToFront(evict(ret));
			return ret.obj;
		}
		else if(throwAway()) {
			final CachedObject bck = back;
			cache.remove(bck.key);
			evict(bck);
		}
		final CachedObject ret = new CachedObject(key, provider.get(key));
		addToFront(ret);
		cache.put(key, ret);
		timesMissed++;
		return ret.obj;
	}
	
	private void addToFront(CachedObject x) {
		x.after = front;
		x.before = null;
		if(front != null) front.before = x;
		front = x;
		if(back == null) back = front;

	}

	private CachedObject evict(CachedObject x) {
		final CachedObject newBefore = x.before;
		final CachedObject newAfter = x.after;
		if(x.after != null) {
			x.after.before = newBefore;
		}
		if(x.before != null) {
			x.before.after = newAfter;
		}
		if(x.before == null) {
			front = x.after;
		}
		if(x.after == null) {
			back = x.before;
		}
		return x;
	}
	/**
	 * Returns the number of cache misses since the object's instantiation.
	 * @return the number of cache misses since the object's instantiation.
	 */
	public int getNumMisses () {
		return timesMissed;
	}
	
	private class CachedObject {
		public U obj;
		public T key;
		public CachedObject before;
		public CachedObject after;
		public CachedObject(T key, U object) {
			obj = object;
			before = null;
			after = null;
			this.key = key;
		}
	}
	
	public void iterate() {
		CachedObject n = front;
		while(n != null) {
			System.out.println(n.key.toString());
			n = n.after;
		}
		System.out.println();
	}
	
	private boolean throwAway() {
		return cache.size() >= capacity;
	}
}
