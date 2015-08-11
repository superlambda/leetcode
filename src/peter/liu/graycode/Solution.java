package peter.liu.graycode;

import java.util.LinkedList;
import java.util.List;

/**
 * The gray code is a binary numeral system where two successive values differ
 * in only one bit.
 * 
 * Given a non-negative integer n representing the total number of bits in the
 * code, print the sequence of gray code. A gray code sequence must begin with
 * 0.
 * 
 * For example, given n = 2, return [0,1,3,2]. Its gray code sequence is:
 * 
 * 00 - 0 01 - 1 11 - 3 10 - 2 Note: For a given n, a gray code sequence is not
 * uniquely defined.
 * 
 * For example, [0,2,3,1] is also a valid gray code sequence according to the
 * above definition.
 * 
 * For now, the judge is able to judge based on one instance of gray code
 * sequence. Sorry about that.
 * 
 * @author superlambda
 *
 */
public class Solution {
	public List<Integer> grayCode(int n) {
		List<Integer> result = new LinkedList<>();
		int[] array = new int[n];
		result.add(0);
		getGrayCode(result, array, n - 1);
		return result;
	}

	private void getGrayCode(List<Integer> result, int[] array, int startIndex) {
		if (result.size() == Math.pow(2, array.length)) {
			return;
		}
		if (startIndex == -1) {
			startIndex = array.length - 1;
		}
		array[startIndex] = array[startIndex] == 0 ? 1 : 0;
		int grayCode = 0;
		for (int j = 0; j < array.length; j++) {
			grayCode += array[j] * Math.pow(2, array.length - 1 - j);
		}
		if (!result.contains(grayCode)) {
			result.add(grayCode);
			startIndex=array.length-1;
		} else {
			array[startIndex] = array[startIndex] == 0 ? 1 : 0;
			startIndex--;
		}
		getGrayCode(result, array, startIndex);
	}
}
