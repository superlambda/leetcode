package effect.java.generics.item28;

import java.util.List;

public class ArrayIsCovariant {

	public static void main(String[] args) {
//		Object[] objectArray = new Long[1];
//		objectArray[0] = "I don't fit in";
//		
//		List<Object> ol = new ArrayList<Long>();
//		ol.add("I don't fit in");
//		//Covariant vs invariant
		
		List<String>[] stringLists = new List<String> [1];
		List<Integer> intList = List.of(42);
		Object[] objects = stringLists;
		objects[0] = intList;
		String s = stringLists[0].get(0);
	}

}
