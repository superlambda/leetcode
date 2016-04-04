package peter.liu.longestvalidparentheses;

/**
 * Given a string containing just the characters '(' and ')', find the length of
 * the longest valid (well-formed) parentheses substring.
 * 
 * For "(()", the longest valid parentheses substring is "()", which has length
 * = 2.
 * 
 * Another example is ")()())", where the longest valid parentheses substring is
 * "()()", which has length = 4.
 * 
 * @author liuyingjie
 *
 */
public class Solution {
	
	public int longestValidParentheses(String s) {
		int longest = 0;
		int[] memory = new int[s.length()];
		for (int index = memory.length - 1; index >= 0; index--) {
			char character = s.charAt(index);
			if (character == ')') {
				continue;
			} else {
				if (index < s.length() - 1) {
					if (memory[index + 1] > 0) {
						int lengthOfIndexPlusOne = memory[index + 1];
						if (index + lengthOfIndexPlusOne + 1 < s.length()
								&& s.charAt(index + lengthOfIndexPlusOne + 1) == ')') {
							memory[index] = 2 + memory[index + 1];
							if(index+memory[index]<s.length()&&memory[index+memory[index]]>0){
								memory[index]+=memory[index+memory[index]];
							}
						} else {
							memory[index] = -1;
						}
					} else {
						if (s.charAt(index + 1) == ')') {
							if (index + 2 < s.length() && memory[index + 2] > 0) {
								memory[index] = 2 + memory[index + 2];
							} else {
								memory[index] = 2;
							}
						} else {
							memory[index] = -1;
						}
					}

				} else {
					memory[index] = -1;
				}
			}

			if (memory[index] > longest) {
				longest = memory[index];
			}
		}
		return longest;
	}

}
