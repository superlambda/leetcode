package effect.java.lambdasandstreams.item46;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import effect.java.lambdasandstreams.item42.Operation;

public class StreamParadigm {

	public Map<String, Long> initializeFrequencyTable(File file) throws FileNotFoundException {

		Map<String, Long> freq;

		/*
		 * The forEach operation should be used only to report the result of a stream
		 * computation, not to perform the computation.
		 */
		try (Stream<String> words = new Scanner(file).tokens()) {
			/*
			 * The object produced by a collector is typically a collection.
			 */
			freq = words.collect(Collectors.groupingBy(String::toLowerCase, Collectors.counting()));
		}
		return freq;

	}

	public List<String> topTenList(Map<String, Long> freq) {

		List<String> topTen = freq.keySet().stream().sorted(Comparator.comparing(freq::get).reversed()).limit(10)
				.collect(Collectors.toList());
		return topTen;

	}

	public Map<Artist, Album> bestSellingAlbum(List<Album> albums) {
		Map<Artist, Album> topHits = albums.stream().collect(Collectors.toMap(Album::getArtist, a -> a,
				BinaryOperator.maxBy(Comparator.comparing(Album::getSales))));
		return topHits;
	}

	public static void main(String[] args) {
		System.out.println(Operation.fromString("+"));
		System.out.println(Operation.fromString("%"));

	}

}
