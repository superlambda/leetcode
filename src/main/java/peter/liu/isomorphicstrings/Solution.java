package peter.liu.isomorphicstrings;

import java.util.HashMap;
import java.util.Map;

/**
 * Given two strings s and t, determine if they are isomorphic.
 * 
 * Two strings are isomorphic if the characters in s can be replaced to get t.
 * 
 * All occurrences of a character must be replaced with another character while
 * preserving the order of characters. No two characters may map to the same
 * character but a character may map to itself.
 * 
 * For example, Given "egg", "add", return true.
 * 
 * Given "foo", "bar", return false.
 * 
 * Given "paper", "title", return true.
 * 
 * Note: You may assume both s and t have the same length.
 * 
 * Subscribe to see which companies asked this question
 * 
 * @author liuyingjie
 *
 */
public class Solution {
	public boolean isIsomorphic(String s, String t) {

		if (s.length() != t.length()) {
			return false;
		}

		Map<Character, Character> map = new HashMap<>();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			Character sc = s.charAt(i);
			Character tc = t.charAt(i);
			if (!map.containsKey(sc) && !map.containsValue(tc)) {
				map.put(sc, tc);
				sb.append(String.valueOf(tc));
			} else if (map.containsKey(sc) && map.get(sc).equals(tc)) {
				sb.append(String.valueOf(tc));
			} else {
				return false;
			}
		}
		return sb.toString().equals(t);

	}
}
