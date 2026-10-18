package peter.liu.dividetwointegers;

/**
 * Divide two integers without using multiplication, division and mod operator.
 * 
 * If it is overflow, return MAX_INT.
 * 
 * @author superlambda
 *
 */
public class Solution {

	public int divide(int dividend, int divisor) {
		if (divisor == 0) {
			if (dividend >= 0) {
				return Integer.MAX_VALUE;
			} else {
				return Integer.MIN_VALUE;
			}
		}
		if (divisor == 1) {
			return dividend;
		}
		if (divisor == -1) {
			if (dividend == Integer.MIN_VALUE) {
				return Integer.MAX_VALUE;
			} else {
				return 0 - dividend;
			}
		}
		boolean negative = (dividend > 0 && divisor < 0)
				|| (dividend < 0 && divisor > 0);

		long longDividend = 0;
		long longDivisor = 0;
		if (dividend == Integer.MIN_VALUE) {
			longDividend = Long.valueOf(Integer.MAX_VALUE) + 1;
		} else {
			longDividend = Math.abs(dividend);
		}

		if (divisor == Integer.MIN_VALUE) {
			longDivisor = Long.valueOf(Integer.MAX_VALUE) + 1;
		} else {
			longDivisor = Math.abs(divisor);
		}

		if (negative) {
			return 0 - binarySearch(longDividend, longDivisor);
		} else {
			return binarySearch(longDividend, longDivisor);
		}
	}

	private int binarySearch(long dividend, long divisor) {
		if (dividend == divisor) {
			return 1;
		}
		if (dividend < divisor) {
			return 0;
		}
		int result = 0;
		int base = 1;

		while (dividend - divisor * base >= 0) {
			int temp = base;
			base = base << 1;
			if (dividend < divisor * base) {
				dividend -= divisor * temp;
				result += temp;
				base = 1;
			} else if (base == Math.pow(2, 30)) {
				dividend -= divisor * base;
				result += base;
				base = 1;
			}
		}

		return result;
	}
}
