package effect.java.lambdasandstreams.item43;

import java.util.HashMap;
import java.util.Map;

public class TestMethodReference {

	public static void main(String[] args) {
		Map<String,Integer> map = new HashMap<> ();
		
		/* Without duplication */
		map.merge("Key1", 2, (count, incr) -> count + incr);
		map.merge("Key2", 2, (count, incr) -> count + incr);
		map.merge("Key3", 2, (count, incr) -> count + incr);
		System.out.println(map);
		
		/* With duplication */
		map.merge("Key1", 3, (count, incr) -> count + incr);
		map.merge("Key2", 3, (count, incr) -> count + incr);
		map.merge("Key3", 3, (count, incr) -> count + incr);
		System.out.println(map);
		
		/* With duplication */
		map.merge("Key1", 1, Integer::sum);
		map.merge("Key2", 1, Integer::sum);
		map.merge("Key3", 1, Integer::sum);
		System.out.println(map);
		
	}

}
