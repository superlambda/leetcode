package inverview.readonlyarraylist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestUnmodifiableList {

	public static void main(String[] argv) {
		try {
			// creating object of ArrayList<Character>
			List<Character> list = new ArrayList<Character>();

			// populate the list
			list.add('X');
			list.add('Y');
			list.add('Z');

			// printing the list
			System.out.println("Initial list: " + list);

			// using unmodifiableList() method
			List<Character> immutablelist = Collections.unmodifiableList(list);

			// printing the list
			System.out.println("ReadOnly ArrayList: " + immutablelist);

			// Adding element to new Collection
			System.out.println("\nTrying to modify" + " the ReadOnly ArrayList.");
			immutablelist.add('A');
		}catch (UnsupportedOperationException e) {
			System.out.println("Exception thrown : " + e);
		}
	}

}
