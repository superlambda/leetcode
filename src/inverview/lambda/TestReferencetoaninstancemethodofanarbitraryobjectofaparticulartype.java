package inverview.lambda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestReferencetoaninstancemethodofanarbitraryobjectofaparticulartype {

	public static void main(String[] args) {
		List<String> personList = new ArrayList<>();

		// Adding elements to above object of List
		// using add() method
		personList.add("vicky");
		personList.add("poonam");
		personList.add("sachin");

		// Method reference to String type
		Collections.sort(personList, String::compareToIgnoreCase);

		// Printing the elements(names) on console
		personList.forEach(System.out::println);
	}

}
