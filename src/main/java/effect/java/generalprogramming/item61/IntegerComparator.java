package effect.java.generalprogramming.item61;

import java.util.Comparator;

public class IntegerComparator {
	
	static Comparator<Integer> naturalOrder = (i, j) -> (i<j)? -1: (i ==j ? 0:1);
	public static void main(String[] args) {
		System.out.println(naturalOrder.compare(new Integer(42), new Integer(42)));
		System.out.println(naturalOrder.compare(Integer.valueOf(42), Integer.valueOf(42)));

	}

}
