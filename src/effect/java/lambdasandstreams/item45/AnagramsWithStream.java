package effect.java.lambdasandstreams.item45;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*易位构词*/
/* Overuse of streams - don't do this !*/
public class AnagramsWithStream {
	public static void main(String[] args) throws IOException {
//		Path dictionary = Paths.get(args[0]);
//		int minGroupSize = Integer.parseInt(args[1]);
//		try (Stream<String> words = Files.lines(dictionary)) {
//			words.collect(Collectors.groupingBy(word -> word.chars().sorted()
//					.collect(StringBuilder::new, (sb, c) -> sb.append((char) c), StringBuilder::append).toString()))
//					.values().stream().filter(group -> group.size() >= minGroupSize)
//					.map(group -> group.size() + ": " + group).forEach(System.out::println);
//
//		}
		/* Java is lack of support for primitive char streams*/
		"Hello world".chars().forEach(System.out::print);//7210110810811132119111114108100
		
		"Hello world".chars().forEach(x -> System.out.print((char) x));
		
		System.out.println("");
		System.out.println("Hello world".chars());
		
	}

	/* Tasteful use of streams enhances clarity and conciseness */
	public static void main2(String[] args) throws IOException {
		Path dictionary = Paths.get(args[0]);
		int minGroupSize = Integer.parseInt(args[1]);
		try (Stream<String> words = Files.lines(dictionary)) {
			words.collect(Collectors.groupingBy(word -> alphabetize(word))).values().stream()
					.filter(group -> group.size() >= minGroupSize).forEach(group -> System.out.println(group.size() + ":" + group));

		}
	}

	private static String alphabetize(String s) {
		char[] a = s.toCharArray();
		Arrays.sort(a);
		return new String(a);
	}

}
