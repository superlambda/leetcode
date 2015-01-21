package peter.liu.findpeakelement;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testFindPeakElement() {
		Solution s = new Solution();
		int[] num1 = { 1 };
		int[] num2 = { 1, 2 };
		int[] num3 = { 2, 1 };
		int[] num4 = { 1, 3, 2 };
		int[] num5 = { 1, 2, 3, 1 };
		int[] num6 = { Integer.MIN_VALUE, 1, 2, 3, 1, Integer.MAX_VALUE };
		int[] num7 = { Integer.MIN_VALUE, Integer.MAX_VALUE };
		int[] num8 = { Integer.MAX_VALUE, Integer.MIN_VALUE };
		int[] num9 = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		int[] num10 = { 9, 8, 7, 6, 5, 4, 3, 2, 1 };
		int[] num11 = { 7, 8, 6, 9, 1, 22, 3, 18, 1 };
		int[] num12 = { 9, 10, 7, 6, 5, 4, 3, 2, 1, 7 };
		int[] num13 = {3,4,3,2,1};
		// System.out.println(s.findPeakElement(num1));
		// System.out.println(s.findPeakElement(num2));
		// System.out.println(s.findPeakElement(num3));
		// System.out.println(s.findPeakElement(num4));
		// System.out.println(s.findPeakElement(num9));
		assertTrue(s.findPeakElement(num1) == 0);
		assertTrue(s.findPeakElement(num2) == 1);
		assertTrue(s.findPeakElement(num3) == 0);
		assertTrue(s.findPeakElement(num4) == 1);
		assertTrue(s.findPeakElement(num5) == 2);
		assertTrue(s.findPeakElement(num6) == 3);
		assertTrue(s.findPeakElement(num7) == 1);
		assertTrue(s.findPeakElement(num8) == 0);
		assertTrue(s.findPeakElement(num9) == 8);
		assertTrue(s.findPeakElement(num10) == 0);
		int index = s.findPeakElement(num11);
		assertTrue(index == 1 || index == 3 || index == 5 || index == 7);
		int index2 = s.findPeakElement(num12);
		assertTrue(index2 == 1 || index2 == 9);
		assertTrue(s.findPeakElement(num13) == 1);
	}

}
