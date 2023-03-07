package inverview.doubleminvalue;

import java.util.Arrays;

public class TestDoubleMin {

	public static void main(String[] args) {
		System.out.println(Math.min(Double.MIN_VALUE, 0.0d));
		System.out.println(Double.MIN_VALUE);

		System.out.println(1.0 / 0.0);

		char[] chars = new char[] { '\u0097' };
		String str = new String(chars);
		byte[] bytes = str.getBytes();
		System.out.println(Arrays.toString(bytes));

	}

}
