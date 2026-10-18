package effect.java.generics.item32;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GenericsAndVarargs {

	static void dangerous(List<String>... stringLists) {
		List<Integer> intList = List.of(42);
		Object[] objects = stringLists;
		objects[0] = intList; // Heap pollution
		String s = stringLists[0].get(0); // ClassCasetException
	}

	static <T> T[] toArray(T... args) {
		return args;
	}

	static <T> T[] pickTwo(T a, T b, T c) {

		switch (ThreadLocalRandom.current().nextInt(3)) {
		case 0:
			return toArray(a, b);
		case 1:
			return toArray(a, c);
		case 2:
			return toArray(b, c);
		}
		throw new AssertionError();

	}

	static <T> List<T> pickTwoWithList(T a, T b, T c) {

		switch (ThreadLocalRandom.current().nextInt(3)) {
		case 0:
			return List.of(a, b);
		case 1:
			return List.of(a, c);
		case 2:
			return List.of(b, c);
		}
		throw new AssertionError();

	}

	@SafeVarargs
	static <T> List<T> flatten(List<? extends T>... lists) {
		List<T> result = new ArrayList<>();
		for (List<? extends T> list : lists) {
			result.addAll(list);
		}
		return result;
	}

	// List as a typesafe alternative to a generic varargs parameter
	static <T> List<T> typeSafeFlatten(List<List<? extends T>> lists) {
		List<T> result = new ArrayList<>();
		for (List<? extends T> list : lists) {
			result.addAll(list);
		}
		return result;
	}

	public static void main(String[] args) {
		List<String> atributes = pickTwoWithList("Good", "Fast", "Cheap");
//		String[] atributes = pickTwo("Good", "Fast", "Cheap");

	}

}
