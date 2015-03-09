package peter.liu.factorialtrailingzeroes;


/**
 * Given an integer n, return the number of trailing zeroes in n!.
 * 
 * Note: Your solution should be in logarithmic time complexity.
 * 
 * Credits: Special thanks to @ts for adding this problem and creating all test
 * cases.
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	public int trailingZeroes(int n) {
		int result = 0;
		while (n > 1) {
			n /= 5;
			result += n;
		}
		return result;
	}
}
