package peter.liu.plusone;

/**
 * Given a non-negative number represented as an array of digits, plus one to
 * the number.
 * 
 * The digits are stored such that the most significant digit is at the head of
 * the list.
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	public int[] plusOne(int[] digits) {
		digits[digits.length - 1] += 1;
		for (int j = digits.length - 1; j > 0; j--) {
			if (digits[j] > 9) {
				digits[j - 1] += 1;
				digits[j] = 0;
			}else{
				break;
			}
		}
		if (digits[0] <= 9) {
			return digits;
		} else {
			digits[0] = 0;
			int[] result = new int[digits.length+1];
			result[0] = 1;
			for (int i = 0; i < digits.length; i++) {
				result[i + 1] = digits[i];
			}
			return result;
		}
	}
}
