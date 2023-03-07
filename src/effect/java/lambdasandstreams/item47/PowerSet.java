package effect.java.lambdasandstreams.item47;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PowerSet {

	public static final <E> Collection<Set<E>> of(Set<E> s) {

		List<E> src = new ArrayList<>(s);

		if (src.size() > 30) {
			throw new IllegalArgumentException("Set too big" + s);
		}

		return new AbstractList<Set<E>>() {
			@Override
			public int size() {
				return 1 << src.size(); // 2 to the power src size
			}

			@Override
			public boolean contains(Object o) {
				return o instanceof Set && src.containsAll((Set) o);
			}

			@Override
			public Set<E> get(int index) {
				Set<E> result = new HashSet<>();
				for (int i = 0; index != 0; i++, index >>= 1) {
					if ((index & 1) == 1) {
						result.add(src.get(i));
					}
				}
				return result;
			}
		};
	}
	
	public static void main(String[] args) {
		Set<String> src = new HashSet<>();
		src.add("a");
		src.add("b");
		src.add("c");
		
		System.out.println(of(src));
		
	}

}
