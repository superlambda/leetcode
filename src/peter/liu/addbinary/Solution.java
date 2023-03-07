package peter.liu.addbinary;

import java.util.Stack;

/**
 * Given two binary strings, return their sum (also a binary string).
 * 
 * For example, a = "11" b = "1" Return "100".
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	public String addBinary(String a, String b) {
		char[] aAr = a.toCharArray();
		char[] bAr = b.toCharArray();
		Stack<Character> stack = new Stack<>();
		int aIndex = aAr.length - 1;
		int bIndex = bAr.length - 1;
		int finger = 0;
		
		
		while (aIndex >= 0 && bIndex >= 0) {
			int aNumber = aAr[aIndex] == '1' ? 1 : 0;
			int bNumber = bAr[bIndex] == '1' ? 1 : 0;
			int sum = finger + aNumber + bNumber;
			finger = putAndSet(sum, stack);
			aIndex--;
			bIndex--;
		}
		while (aIndex >= 0) {
			int aNumber = aAr[aIndex] == '1' ? 1 : 0;
			int sum = finger + aNumber;
			finger = putAndSet(sum, stack);
			aIndex--;
		}
		while (bIndex >= 0) {
			int bNumber = bAr[bIndex] == '1' ? 1 : 0;
			int sum = finger + bNumber;
			finger = putAndSet(sum, stack);
			bIndex--;
		}
		StringBuffer result = new StringBuffer();
		if (finger == 1) {
			result.append("1");
		}
		while (!stack.isEmpty()) {
			result.append(stack.pop());
		}
		return result.toString();
	}

	private int putAndSet(int sum, Stack<Character> stack) {
		switch (sum) {
		case 3:
			stack.add('1');
			return 1;
		case 2:
			stack.add('0');
			return 1;
		case 1:
			stack.add('1');
			return  0;
		default:
			stack.add('0');
			return 0;
		}
	}
}
