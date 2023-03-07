package inverview.lambda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestInstanceMethodReferenceofaParticularObject {

	public static void main(String[] args) {
		List<Person> personList = new ArrayList<>();

		personList.add(new Person("vicky", 24));
		personList.add(new Person("poonam", 25));
		personList.add(new Person("sachin", 19));

		// A comparator class with multiple
		// comparator methods
		ComparisonProvider comparator = new ComparisonProvider();

		// Now using instance method reference
		// to sort array by name
		Collections.sort(personList, comparator::compareByName);

		// Display message only
		System.out.println("Sort by name :");

		// Using streams
		personList.stream().map(x -> x.getName()).forEach(System.out::println);

		// Using instance method reference
		// to sort array by age
		Collections.sort(personList, comparator::compareByAge);

		// Display message only
		System.out.println("Sort by age :");

		personList.stream().map(x -> x.getName()).forEach(System.out::println);

	}

}

class Person {

	private String name;
	private Integer age;

	public Person(String name, int age) {

		this.name = name;
		this.age = age;
	}

	public Integer getAge() {
		return age;
	}

	public String getName() {
		return name;
	}
}

class ComparisonProvider {

	public int compareByName(Person a, Person b) {
		return a.getName().compareTo(b.getName());
	}

	public int compareByAge(Person a, Person b) {
		return a.getAge().compareTo(b.getAge());
	}
}