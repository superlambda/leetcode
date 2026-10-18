package peter.liu.longestpalindromicsubstring;

/**
 * Given a string S, find the longest palindromic substring in S. You may assume
 * that the maximum length of S is 1000, and there exists one unique longest
 * palindromic substring.
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	/**
	 * Slow solution from discussion, O(n2) time
	 * @return
	 */
	// public String longestPalindrome(String s) {
	// char[] charAr = s.toCharArray();
	// int length = charAr.length;
	// while (length > 0) {
	// for (int i = 0; i <= charAr.length - length; i++) {
	// boolean found = true;
	// for (int head = i, tail = i + length - 1; head <= tail; head++, tail--) {
	// if (charAr[head] != charAr[tail]) {
	// found = false;
	// break;
	// }
	// }
	// if (found) {
	// return s.substring(i, i + length);
	// }
	// }
	// length--;
	// }
	// return "";
	// }
	public boolean isPalindrome(String s, int startIndex, int endIndex) {
		for (int i = startIndex, j = endIndex; i <= j; i++, j--) {
			if (s.charAt(i) != s.charAt(j))
				return false;
		}
		return true;
	}

	/**
	 * Fast solution from discussion, O(n) time
	 */
	public String longestPalindrome(String s) {
		int n = s.length();
		int longestLen = 0;
		int longestIndex = 0;

		for (int currentIndex = 0; currentIndex < n; currentIndex++) {
			if (isPalindrome(s, currentIndex - longestLen, currentIndex)) {
				longestLen += 1;
				longestIndex = currentIndex;
			} else if (currentIndex - longestLen - 1 >= 0
					&& isPalindrome(s, currentIndex - longestLen - 1,
							currentIndex)) {
				longestLen += 2;
				longestIndex = currentIndex;
			}
		}
		longestIndex++;
		return s.substring(longestIndex - longestLen, longestIndex);
	}

}
