

public class DataProviderTest<integer, string> implements DataProvider<Integer, String> {

	public DataProviderTest() {
		
	}
	
	public String get(Integer key) {
		return "" + key;
	}
}
