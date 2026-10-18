package effect.java.generics.item30;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Union {

	public static <E> Set<E> union(Set<E> s1, Set<E> s2) {
		Set<E> result = new HashSet<>(s1);
		result.addAll(s2);
		return result;
	}

	public static <E extends Comparable<E>> E max(Collection<E> c) {
		if (c.isEmpty()) {
			throw new IllegalArgumentException("Empty collection");
		}
		E result = null;
		for (E e : c) {
			if (result == null || e.compareTo(result) > 0) {
				result = Objects.requireNonNull(e);
			}
		}
		return result;
	}

	public static void main(String[] args) {
		Set<String> guys = Set.of("Tom", "Dick", "Harry");
		Set<String> stooges = Set.of("Larry", "Moe", "Curly");
		Set<String> aflCio = union(guys, stooges);
		System.out.println(aflCio);
	}

}
