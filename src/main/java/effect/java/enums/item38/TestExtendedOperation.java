package effect.java.enums.item38;

import java.util.Arrays;
import java.util.Collection;

public class TestExtendedOperation {

	public static void main(String[] args) {
		test(BasicOperation.class, 2, 4);
		test(ExtendedOperation.class, 2, 4);
		testWithCollection(Arrays.asList(ExtendedOperation.values()), 2, 4);

	}

	private static <T extends Enum<T> & Operation> void test(Class<T> opEnumType, double x, double y) {
		for (Operation op : opEnumType.getEnumConstants()) {
			System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
		}

	}
	private static void testWithCollection(Collection<? extends Operation> opSet, double x, double y) {
		for (Operation op : opSet) {
			System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
		}

	}

}
