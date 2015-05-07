package peter.liu.powxn;

/**
 * Implement pow(x, n).
 * 
 * @author superlambda
 *
 */
public class Solution {
	public double myPow(double x, int n) {
		if (x == 1) {
			return x;
		}
		if (x == -1) {
			return n % 2 == 0 ? 1 : -1;
		}
		double result = x;
		if (n > 0) {
			while (n > 1) {
				result = result * x;
				n--;
				if (result == 0.0D) {
					return result;
				}
			}
			return result;
		} else if (n < 0) {
			n = -n;
			while (n > 1) {
				result = result * x;
				n--;
				if (result == 0.0D) {
					return Double.MAX_VALUE;
				}
			}
			return 1 / result;
		} else {
			if (x == 0) {
				return 0;
			} else {
				return 1;
			}
		}

	}

}
