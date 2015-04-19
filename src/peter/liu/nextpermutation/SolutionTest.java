package peter.liu.nextpermutation;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testNextPermutation() {
		int[] num = { 1, 2, 3 };
		new Solution().nextPermutation(num);
		int[] result = { 1, 3, 2 };
		for (int i = 0; i < num.length; i++) {
			assertEquals(num[i], result[i]);
		}

		int[] num2 = { 1, 3, 2 };
		new Solution().nextPermutation(num2);
		int[] result2 = { 2, 1, 3 };
		for (int i = 0; i < num2.length; i++) {
			assertEquals(num2[i], result2[i]);
		}
	}

}
