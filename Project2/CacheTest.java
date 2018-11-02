import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Code to test an <tt>LRUCache</tt> implementation.
 */
public class CacheTest {
	@Test
	public void leastRecentlyUsedIsCorrect () {
		DataProvider<Integer,String> provider = new DataProviderTest(); // Need to instantiate an actual DataProvider
		LRUCache<Integer,String> cache = new LRUCache<Integer,String>(provider, 3);
		cache.get(1);
		cache.get(2);
		cache.get(3);
		cache.iterate();
		cache.get(4);
		cache.iterate();
		cache.get(2);
		cache.iterate();
		cache.get(4);
		cache.iterate();
		cache.get(1);
		cache.iterate();
	}
}
