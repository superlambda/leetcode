package peter.liu.lettercombinationsofphonenumber;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * Given a digit string, return all possible letter combinations that the number
 * could represent.
 * 
 * A mapping of digit to letters (just like on the telephone buttons) is given
 * below.
 * 
 * 
 * 
 * Input:Digit string "23" Output: ["ad", "ae", "af", "bd", "be", "bf", "cd",
 * "ce", "cf"]. Note: Although the above answer is in lexicographical order,
 * your answer could be in any order you want.
 * 
 * @author superlambda
 *
 */
public class Solution {

	public List<String> letterCombinations(String digits) {
		char[] digitsArray = digits.toCharArray();
		List<char[]> letterArrayList = new ArrayList<>();
		List<Stack<Character>> stacks = new ArrayList<>();
		for (char digit : digitsArray) {
			char[] letterArray = getletterArray(digit);
			if (letterArray.length == 0) {
				continue;
			}
			Stack<Character> letterStack = new Stack<>();
			for (char letter : letterArray) {
				letterStack.push(letter);
			}
			stacks.add(letterStack);
			letterArrayList.add(letterArray);
		}
		Set<String> resultSet = new HashSet<>();
		String letterCombination = "";
		while (letterCombination.length() < letterArrayList.size()) {
			letterCombination += stacks.get(letterCombination.length()).pop();
			if (letterCombination.length() == letterArrayList.size()) {
				if(!resultSet.contains(letterCombination)){
					resultSet.add(letterCombination);
				}else{
					break;
				}
				for (int j = letterCombination.length() - 1; j >= 0; j--) {
					if (stacks.get(j).isEmpty()) {
						Stack<Character> letterStack = new Stack<>();
						for (char letter : letterArrayList.get(j)) {
							letterStack.push(letter);
						}
						stacks.set(j, letterStack);
					} else {
						letterCombination = letterCombination.substring(0, j);
						break;
					}
				}
			}
		}
		return new ArrayList<String>(resultSet);
	}

	private char[] getletterArray(char digit) {
		switch (digit) {
		case '1':
			return new char[] {};
		case '2':
			return new char[] { 'a', 'b', 'c' };
		case '3':
			return new char[] { 'd', 'e', 'f' };
		case '4':
			return new char[] { 'g', 'h', 'i' };
		case '5':
			return new char[] { 'j', 'k', 'l' };
		case '6':
			return new char[] { 'm', 'n', 'o' };
		case '7':
			return new char[] { 'p', 'q', 'r', 's' };
		case '8':
			return new char[] { 't', 'u', 'v' };
		case '9':
			return new char[] { 'w', 'x', 'y', 'z' };
		case '0':
			return new char[] { ' ' };
		default:
			return new char[] {};
		}
	}

}
