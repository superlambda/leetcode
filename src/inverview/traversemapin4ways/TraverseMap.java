package inverview.traversemapin4ways;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class TraverseMap {

	public static void main(String[] srgs) {
		HashMap<String, String> loans = new HashMap<String, String>();
		loans.put("home loan", "citibank");
		loans.put("personal loan", "Wells Fargo");
		System.out.println("Iterating or looping map using java5 foreach loop");
		for (String key : loans.keySet()) {
			System.out.println("key: " + key + " value: " + loans.get(key));
		}
		System.out.println("Iterating Map in Java using KeySet Iterator");

		Set<String> keySet = loans.keySet();
		Iterator<String> keySetIterator = keySet.iterator();
		while (keySetIterator.hasNext()) {
			String key = keySetIterator.next();
			System.out.println("key: " + key + " value: " + loans.get(key));
		}
		
		System.out.println("looping HashMap in Java using EntrySet and java5 for loop");
		Set<Map.Entry<String, String>> entrySet = loans.entrySet();
		for (Entry<String, String> entry : entrySet) {
			System.out.println("key: " + entry.getKey() + " value: " + entry.getValue());
		}
		
		System.out.println("Iterating HashMap in Java using EntrySet and Java iterator");
		
		Set<Map.Entry<String, String>> entrySet1 = loans.entrySet();
		Iterator<Entry<String, String>> entrySetIterator = entrySet1.iterator();
		while (entrySetIterator.hasNext()) {
			Entry<String, String> entry = entrySetIterator.next();
			System.out.println("key: " + entry.getKey() + " value: " + entry.getValue());
		}

	}

}
