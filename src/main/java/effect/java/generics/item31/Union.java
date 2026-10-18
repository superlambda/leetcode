package effect.java.generics.item31;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

public class Union {

	public static <T> Set<T> union(Set<T> s1, Set<T> s2) {
		Set<T> result = new HashSet<>(s1);
		result.addAll(s2);
		return result;
	}

	public static <T extends Comparable<? super T>> T max(List<? extends T> c) {
		if (c.isEmpty()) {
			throw new IllegalArgumentException("Tmpty collection");
		}
		T result = null;
		for (T e : c) {
			if (result == null || e.compareTo(result) > 0) {
				result = Objects.requireNonNull(e);
			}
		}
		return result;
	}
	
	public static void swap(List<?> list, int i, int j) {
		swapHelper(list, i, j);
	}
	
	private static<E> void swapHelper(List<E> list, int i, int j) {
		list.set(i, list.set(j, list.get(i)));
	}

	public static void main(String[] args) {
		List<ScheduledFuture<?>> scheduledFutures = null;
		max(scheduledFutures);
	}

}
