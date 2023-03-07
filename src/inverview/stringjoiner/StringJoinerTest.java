package inverview.stringjoiner;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class StringJoinerTest {

	public static void main(String[] args) {
		// Join String by a delimiter
		StringJoiner sj = new StringJoiner(",");
		sj.add("aaa");
		sj.add("bbb");
		sj.add("ccc");
		String result = sj.toString(); // aaa,bbb,ccc
		System.out.println("pfl delimiter: " + result);
		// 2 Join String by a delimiter and starting with a supplied prefix and ending
		// with a supplied suffix.
		sj = new StringJoiner("/", "prefix-", "-suffix");
		sj.add("2016");
		sj.add("02");
		sj.add("26");
		result = sj.toString(); // prefix-2016/02/26-suffix
		System.out.println("pfl prefix: " + result);

		// StringJoiner is used internally by static String.join().
		// Join String by a delimiter.
		result = String.join("-", "2015", "10", "31");
		System.out.println("pfl String.join: " + result);

		// Join a List by a delimiter.
		List<String> list = Arrays.asList("java", "python", "nodejs", "ruby"); // java, python, nodejs, ruby
		result = String.join(", ", list);
		System.out.println("pfl Array.join: " + result);

		// Collectors.joining
		// Join List<String> example.
		list = Arrays.asList("java", "python", "nodejs", "ruby");

		// java | python | nodejs | ruby
		result = list.stream().map(x -> x).collect(Collectors.joining(" | "));
		System.out.println("pfl Collectors.join: " + result);

		// Join List<Object> example.
		List<Game> list2 = Arrays.asList(
				new Game("Dragon Blaze", 5),
				new Game("Angry Bird", 5),
				new Game("Candy Crush", 5));

		// {Dragon Blaze, Angry Bird, Candy Crush}
		result = list2.stream().map(x -> x.getName()).collect(Collectors.joining(", ", "{", "}"));
		System.out.println("pfl Collectors.join 2: " + result);

	}
}
class Game {
	String name;
	int ranking;

	public Game(String name, int ranking) {
		this.name = name;
		this.ranking = ranking;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRanking() {
		return ranking;
	}

	public void setRanking(int ranking) {
		this.ranking = ranking;
	}
}
