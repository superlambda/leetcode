package peter.liu.substringwithconcatenationofallwords;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * You are given a string, s, and a list of words, words, that are all of the
 * same length. Find all starting indices of substring(s) in s that is a
 * concatenation of each word in words exactly once and without any intervening
 * characters.
 * 
 * For example, given: s: "barfoothefoobarman" words: ["foo", "bar"]
 * 
 * You should return the indices: [0,9]. (order does not matter).
 * 
 * Subscribe to see which companies asked this question
 * 
 * @author liuyingjie
 *
 */
public class Solution {
	// public List<Integer> findSubstring(String s, String[] words) {
	// List<Integer> result = new ArrayList<>();
	// if (words.length == 0) {
	// return result;
	// }
	// int wordLength = words[0].length();
	// int subLength = words.length * wordLength;
	// Map<String, Integer> wordMap = new HashMap<>();
	// for (String word : words) {
	// if (!wordMap.containsKey(word)) {
	// wordMap.put(word, 1);
	// } else {
	// wordMap.put(word, wordMap.get(word) + 1);
	// }
	// }
	//
	// for (int i = 0; i <= s.length() - subLength; i++) {
	// int startIndex = i;
	// if (wordMap.containsKey(s.substring(startIndex, startIndex +
	// wordLength))) {
	// Map<String, Integer> checkMap = new HashMap<>();
	// while (startIndex < i + subLength) {
	// String subString = s.substring(startIndex, startIndex + wordLength);
	// if (wordMap.containsKey(subString)) {
	// checkMap.put(subString, !checkMap.containsKey(subString) ? 1 :
	// checkMap.get(subString) + 1);
	// startIndex += wordLength;
	// } else {
	// break;
	// }
	// }
	// if (checkMap.equals(wordMap)) {
	// result.add(i);
	// }
	// }
	//
	// }
	// return result;
	// }

	public List<Integer> findSubstring(String s, String[] words) {
		List<Integer> res = new LinkedList<>();
		if (words.length == 0 || s.length() < words.length * words[0].length()){
			return res;
		}
		int strLength = s.length(), arrayLength = words.length, wordLength = words[0].length();
		Map<String, Integer> map = new HashMap<>(), curMap = new HashMap<>();
		for (String word : words) {
			if (map.containsKey(word))
				map.put(word, map.get(word) + 1);
			else
				map.put(word, 1);
		}
		String str = null, tmp = null;
		for (int i = 0; i < wordLength; i++) {
			int count = 0; // remark: reset count
			for (int left = i, right = i; right + wordLength <= strLength; right += wordLength) {
				str = s.substring(right, right + wordLength);
				if (map.containsKey(str)) {
					if (curMap.containsKey(str)){
						curMap.put(str, curMap.get(str) + 1);
					}else{
						curMap.put(str, 1);
					}
					if (curMap.get(str) <= map.get(str)){
						count++;
					}
					while (curMap.get(str) > map.get(str)) {
						tmp = s.substring(left, left + wordLength);
						curMap.put(tmp, curMap.get(tmp) - 1);
						left += wordLength;
						if (curMap.get(tmp) < map.get(tmp)){
							count--;
						}
					}
					if (count == arrayLength) {
						res.add(left);
						tmp = s.substring(left, left + wordLength);
						curMap.put(tmp, curMap.get(tmp) - 1);
						left += wordLength;
						count--;
					}
				} else {
					curMap.clear();
					count = 0;
					left = right + wordLength;
				}
			}
			curMap.clear();
		}
		return res;
	}
}
