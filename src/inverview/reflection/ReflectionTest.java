package inverview.reflection;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ReflectionTest {

	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
		Person person = new Person("Doug", "Sparling", 31);
		 
        Map<String, Object> values = new HashMap<>();
        for (Field field : person.getClass().getDeclaredFields()) {
            values.put(field.getName(), field.get(person));
        }
 
        // prints {firstName=Doug, lastName=Sparling, age=31}
        System.out.println(values);
	}
}	

class Person{
	 String firstName;
	 String lastName;
	 int age;
	
	public Person(String firstName, String lastName, int age) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
}
