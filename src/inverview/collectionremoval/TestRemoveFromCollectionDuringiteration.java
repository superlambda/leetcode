package inverview.collectionremoval;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TestRemoveFromCollectionDuringiteration {

	public static void main(String[] args) {
		List<String> list1 = new LinkedList<>();
		list1.add("a");
		list1.add("b");
		list1.add("c");
		Iterator<String> iterator1= list1.iterator();
		while(iterator1.hasNext()) {
			iterator1.next();
			iterator1.remove();
		}
		System.out.println(list1);
		
		List<String> list2 = new LinkedList<>();
		list2.add("a");
		list2.add("b");
		list2.add("c");
		Iterator<String> iterator2= list2.iterator();
		while(iterator2.hasNext()) {
			iterator2.next();
			list2.remove(2);
		}
		System.out.println(list2);
	}

}
