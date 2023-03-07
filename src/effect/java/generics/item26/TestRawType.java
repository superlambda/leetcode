package effect.java.generics.item26;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TestRawType {

	public static void main(String[] args) {
		List<String> strings =  new ArrayList<>();
//		unsafeAdd(strings, Integer.valueOf(42));
		String s = strings.get(0);
		
		Collection<?> c = new ArrayList<> ();
		c.add("a");
 		
	}
	
	private static void unsafeAdd(List<Object> list, Object o) {
		list.add(o);
	}
}
