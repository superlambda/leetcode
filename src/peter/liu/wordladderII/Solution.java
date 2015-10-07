package peter.liu.wordladderII;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Given two words (beginWord and endWord), and a dictionary's word list, find
 * all shortest transformation sequence(s) from beginWord to endWord, such that:
 * 
 * Only one letter can be changed at a time Each intermediate word must exist in
 * the word list For example,
 * 
 * Given: beginWord = "hit" endWord = "cog" wordList =
 * ["hot","dot","dog","lot","log"] Return [ ["hit","hot","dot","dog","cog"],
 * ["hit","hot","lot","log","cog"] ] Note: All words have the same length. All
 * words contain only lowercase alphabetic characters.
 * 
 * @author superlambda
 *
 */
public class Solution {
	int shortestSequence = Integer.MAX_VALUE;

	public List<List<String>> findLadders(String beginWord, String endWord,
			Set<String> wordList) {
		LinkedList<Set<String>> resultSet = new LinkedList<>();
		wordList.add(endWord);
		boolean found = search(resultSet, beginWord, endWord, wordList);

		List<List<String>> result = new LinkedList<>();
		if (!found) {
			return result;
		} else {
			shortestSequence = resultSet.size();
			LinkedList<String> candidate = new LinkedList<>();
			candidate.add(endWord);
			resultSet.removeLast();
			return fiterResult(result, candidate, resultSet);
		}
	}
	
	private List<List<String>> fiterResult(List<List<String>> result,
			LinkedList<String> candidate, LinkedList<Set<String>> resultSet) {
		while (!resultSet.isEmpty()) {
			Set<String> set = resultSet.removeLast();
			String firstWord = candidate.getFirst();
			for (String word : set) {
				if (onlyOneDifference(firstWord, word)) {
					LinkedList<String> newCandidate = new LinkedList<>(candidate);
					newCandidate.addFirst(word);
					if (newCandidate.size() == shortestSequence) {
						result.add(newCandidate);
					} else {
						LinkedList<Set<String>> newResultSet = new LinkedList<>(
								resultSet);
						fiterResult(result, newCandidate, newResultSet);
					}
				}
			}
		}
		return result;
	}

	public boolean search(LinkedList<Set<String>> resultSet, String beginWord,
			String endWord, Set<String> wordList) {
		Set<String> reached = new HashSet<String>();
		reached.add(beginWord);
		resultSet.add(reached);
		wordList.add(endWord);
		while (!reached.contains(endWord)) {
			Set<String> toAdd = new HashSet<String>();
			for (String each : reached) {
				for (int i = 0; i < each.length(); i++) {
					char[] chars = each.toCharArray();
					for (char ch = 'a'; ch <= 'z'; ch++) {
						chars[i] = ch;
						String word = new String(chars);
						if (wordList.contains(word)) {
							toAdd.add(word);
							wordList.remove(word);
						}
					}
				}
			}
			if (toAdd.isEmpty()) {
				return false;
			}
			reached = toAdd;
			if (reached.contains(endWord)) {
				reached = new HashSet<>();
				reached.add(endWord);
			}
			resultSet.add(reached);
		}
		return true;
	}

	private boolean onlyOneDifference(String one, String two) {
		int count = 0;
		for (int i = 0; i < one.length(); i++) {
			if (one.charAt(i) != two.charAt(i)) {
				count++;
			}
			if (count > 1) {
				return false;
			}
		}
		return count == 1;
	}
}
