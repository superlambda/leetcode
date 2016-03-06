package peter.liu.substringwithconcatenationofallwords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	public List<Integer> findSubstring(String s, String[] words) {
		List<Integer> result=new ArrayList<>();
		if(words.length==0){
			return result;
		}
		int subLength=words.length*words[0].length();
		int wordLength=words[0].length();
		Map<String,Integer> wordMap=new HashMap<>();
		for(String word:words){
			if(!wordMap.containsKey(word)){
				wordMap.put(word, 1);
			}else{
				wordMap.put(word, wordMap.get(word)+1);
			}
		}
		
		for(int i=0;i<=s.length()-subLength;i++){
			
			Map<String,Integer> checkMap=new HashMap<>(wordMap);
			int startIndex=i;
			while(startIndex<=i+subLength){
				String subString=s.substring(startIndex,startIndex+wordLength);
				if(checkMap.containsKey(subString)){
					int count=checkMap.get(subString);
					if(count==1){
						checkMap.remove(subString);
					}else{
						checkMap.put(subString, count-1);
					}
					startIndex+=wordLength;
					if(checkMap.isEmpty()){
						result.add(i);
						break;
					}
				}else{
					break;
				}
				
			}
		}
		return result;
	}
}
