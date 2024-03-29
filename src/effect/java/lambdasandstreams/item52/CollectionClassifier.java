package effect.java.lambdasandstreams.item52;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollectionClassifier {

	public static String classify(Set<?> s) {
		return "Set";
	}

	public static String classify(List<?> lst) {
		return "List";
	}

	public static String classify(Collection<?> c) {
//		return "Unknow Collection";
		return c instanceof Set ? "Set" : c instanceof List ? "List" : "Unknow Collection";
	}

	public static void main(String[] args) {
		Collection<?>[] collections = { 
				new HashSet<String>(), 
				new ArrayList<BigInteger>(),
				new HashMap<String, String>().values() };
		for (Collection<?> c : collections) {
			System.out.println(classify(c));
		}

	}

}
