package inverview.hashmap;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TestHashMap {
	public static void main(String[] args) {
		Map<String,String> hashMap = new HashMap<>();
		hashMap.put(null, null);
		
		Map<String,String> concurrentHashMap = new ConcurrentHashMap<>();
		concurrentHashMap.put(null, null);
	}
}
