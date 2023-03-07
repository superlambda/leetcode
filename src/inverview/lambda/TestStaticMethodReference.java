package inverview.lambda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestStaticMethodReference {

	public static int compareByName(Person a, Person b) {
		return a.getName().compareTo(b.getName());
	}

	public static int compareByAge(Person a, Person b) {
		return a.getAge().compareTo(b.getAge());
	}

	public static void main(String[] args) {
		List<Person> personList = new ArrayList<>();


		personList.add(new Person("vicky", 24));
		personList.add(new Person("poonam", 25));
		personList.add(new Person("sachin", 19));

		// Using static method reference to sort array by name
		Collections.sort(personList, TestStaticMethodReference::compareByName);

		System.out.println("Sort by name :");

		// Using streams over above object of Person type
		personList.stream().map(x -> x.getName()).forEach(System.out::println);

		// Now using static method reference to sort array by age
		Collections.sort(personList, TestStaticMethodReference::compareByAge);

		// Display message only
		System.out.println("Sort by age :");

		// Using streams over above object of Person type
		personList.stream().map(x -> x.getName()).forEach(System.out::println);
		
	}

}