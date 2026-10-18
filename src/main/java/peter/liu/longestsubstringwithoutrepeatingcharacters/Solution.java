package peter.liu.longestsubstringwithoutrepeatingcharacters;

/**
 * Given a string, find the length of the longest substring without repeating
 * characters. For example, the longest substring without repeating letters for
 * "abcabcbb" is "abc", which the length is 3. For "bbbbb" the longest substring
 * is "b", with the length of 1.
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	public int lengthOfLongestSubstring(String s) {
		if (s.length() <= 1) {
			return s.length();
		}
		int length = 1;
		int head = 1;
		int tail = 0;
		while (tail <= head && head <= s.length() - 1) {
			if (s.substring(tail, head).indexOf(s.substring(head, head + 1)) == -1) {
				head++;
				int subLength = s.substring(tail, head).length();
				if (length < subLength) {
					length = subLength;
				}
			} else {
				tail++;
				if (tail == head) {
					head++;
				}
			}
		}
		return length;
	}
}
