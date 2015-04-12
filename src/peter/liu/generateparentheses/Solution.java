package peter.liu.generateparentheses;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Given n pairs of parentheses, write a function to generate all combinations
 * of well-formed parentheses.
 * 
 * For example, given n = 3, a solution set is:
 * 
 * "((()))", "(()())", "(())()", "()(())", "()()()"
 * 
 * @author superlambda
 *
 */
public class Solution {
	public List<String> generateParenthesis(int n) {
		List<String> result = new LinkedList<>();
		generate("(", n - 1, n, result, n*2);
		return result;

	}

	private void generate(String combination, int numberOfLeft,
			int numberofRight, List<String> result, int length) {
		if (combination.length() == length) {
			//validation is not needed.
//			if (isValidParenthese(combination)) {
				result.add(combination);
//			}
		} else {
			if (numberOfLeft > 0) {
				generate(combination + "(", numberOfLeft - 1, numberofRight,
						result, length);
			}
			if (numberofRight>numberOfLeft) {
				generate(combination + ")", numberOfLeft, numberofRight - 1,
						result, length);
			}
		}

	}

	private boolean isValidParenthese(String combination) {
		Stack<Character> stack = new Stack<>();
		for (char character : combination.toCharArray()) {
			if (character == '(') {
				stack.push(character);
			} else {
				if (!stack.isEmpty() && stack.peek() == '(') {
					stack.pop();
				} else {
					stack.push(character);
				}
			}
		}
		return stack.isEmpty();
	}
}