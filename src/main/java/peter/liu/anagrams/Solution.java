package peter.liu.anagrams;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Given an array of strings, return all groups of strings that are anagrams.
 * 
 * Note: All inputs will be in lower-case.
 * 
 * @author superlambda
 *
 */
public class Solution {

	public List<String> anagrams(String[] strs) {
		List<String> result = new LinkedList<>();
		Map<String, Integer> map = new HashMap<String, Integer>();
		Set<Integer> indexShouldBeAdded = new HashSet<>();
		for (int i = 0; i < strs.length; i++) {
			String str = strs[i];
			char[] array = str.toCharArray();
			Arrays.sort(array);
			String sortedStr = Arrays.toString(array);
			if (!map.keySet().contains(sortedStr)) {
				map.put(sortedStr, i);
			} else {
				if (!indexShouldBeAdded.contains(map.get(sortedStr))) {
					indexShouldBeAdded.add(map.get(sortedStr));
				}
				result.add(str);
			}
		}
		for (int index : indexShouldBeAdded) {
			result.add(strs[index]);
		}
		return result;

	}
}
