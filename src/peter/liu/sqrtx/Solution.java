package peter.liu.sqrtx;


/**
 * Implement int sqrt(int x).
 * 
 * Compute and return the square root of x.
 * 
 * @author superlambda
 *
 */
public class Solution {

	public int mySqrt(int x) {
		if (x == 0 || x == 1) {
			return x;
		}
		int low = 1;
		int high = x;
		int mid = 0;
		int maxResult = (int) Math.sqrt(Integer.MAX_VALUE);
		while (low < high) {
			mid = low + (high - low) / 2;
			if (mid > maxResult) {
				high = mid - 1;
				continue;
			}
			int power = mid * mid;
			if (power == x) {
				return mid;
			} else if (power < x) {
				low = mid + 1;
			} else {
				high = mid - 1;
			}
		}
		if (mid > maxResult) {
			while (mid > maxResult) {
				mid--;
			}
			return mid;
		}

		if (mid * mid > x) {
			while (mid*mid > x) {
				mid--;
			}
			return mid;
		} else {
			if ((mid + 1) * (mid + 1) <= x) {
				return mid + 1;
			}
			return mid;
		}
	}
}
