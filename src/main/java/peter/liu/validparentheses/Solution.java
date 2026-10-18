package peter.liu.validparentheses;

import java.util.Stack;

/**
 * Given a string containing just the characters '(', ')', '{', '}', '[' and
 * ']', determine if the input string is valid.
 * 
 * The brackets must close in the correct order, "()" and "()[]{}" are all valid
 * but "(]" and "([)]" are not.
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	public boolean isValid(String s) {
		char[] charAr = s.toCharArray();
		Stack<Character> stack = new Stack<Character>();
		if (charAr.length > 0) {
			stack.push(charAr[0]);
		} else {
			return true;
		}
		for (int i = 1; i < charAr.length; i++) {
			if (stack.isEmpty()) {
				stack.push(charAr[i]);
				continue;
			}
			switch (charAr[i]) {
			case ')':
				if (stack.peek() != '(') {
					stack.push(charAr[i]);
				} else {
					stack.pop();
				}
				break;
			case '}':
				if (stack.peek() != '{') {
					stack.push(charAr[i]);
				} else {
					stack.pop();
				}
				break;
			case ']':
				if (stack.peek() != '[') {
					stack.push(charAr[i]);
				} else {
					stack.pop();
				}
				break;
			default:
				stack.push(charAr[i]);
				break;
			}
		}
		return stack.isEmpty() ? true : false;
	}

	public boolean isValidNew(String s) {
		char[] charAr = s.toCharArray();
		Stack<Character> stack = new Stack<Character>();

		for (int i = 0; i < charAr.length; i++) {
			if (charAr[i] != ')' && charAr[i] != '}' && charAr[i] != ']') {
				stack.push(charAr[i]);
			} else {
				if (stack.isEmpty()) {
					return false;
				}

				switch (charAr[i]) {
				case ')':
					if (stack.pop() != '(') {
						return false;
					}
					break;
				case '}':
					if (stack.pop() != '{') {
						return false;
					}
					break;
				case ']':
					if (stack.pop() != '[') {
						return false;
					}
					break;
				}
			}
		}
		return stack.isEmpty();

	}
}
