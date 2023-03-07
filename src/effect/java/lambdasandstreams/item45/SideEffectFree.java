package effect.java.lambdasandstreams.item45;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class SideEffectFree {

	public static void main(String[] args) {
		String file = args[0];
		Map<String, Long> freq;
		try (Stream<String> words = new Scanner(file).tokens()) {
			freq = words.collect(groupingBy(String::toLowerCase, counting()));
		}
		List<String> topTen = freq.keySet().stream().sorted(comparing(freq::get).reversed()).limit(10)
				.collect(toList());
		topTen.forEach(number -> {
			System.out.println(number);
		});

	}

}
