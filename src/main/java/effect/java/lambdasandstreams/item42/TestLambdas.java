package effect.java.lambdasandstreams.item42;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class TestLambdas {

	public static void main(String[] args) {
		List<String> words = new LinkedList<>();
		words.add("a");
		words.add("ab");
		words.add("abc");
		System.out.println(words);
		Collections.sort(words, (s1,s2)->Integer.compare(s1.length(), s2.length()));
		System.out.println(words);
		Collections.reverse(words);
		System.out.println(words);
		Collections.sort(words, Comparator.comparingInt(String::length));
		System.out.println(words);
		Collections.reverse(words);
		System.out.println(words);
		words.sort(Comparator.comparingInt(String::length));;
		System.out.println(words);

	}

}
