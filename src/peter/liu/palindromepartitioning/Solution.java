package peter.liu.palindromepartitioning;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Given a string s, partition s such that every substring of the partition is a
 * palindrome.
 * 
 * Return all possible palindrome partitioning of s.
 * 
 * For example, given s = "aab", Return
 * 
 * [ ["aa","b"], ["a","a","b"] ]
 * 
 * @author superlambda
 *
 */
public class Solution {
	public List<List<String>> partition(String s) {
		List<List<String>> result = new ArrayList<>();
		trace(result, new LinkedList<String>(),0, s);
		return result;
	}

	private void trace(List<List<String>> result, List<String> partitionList,
			int startIndex, String s) {
		for(int i=startIndex;i<s.length();i++){
			String subString=s.substring(startIndex,i+1);
			if(isPalindrome(subString)){
				List<String> newPartitionList=new LinkedList<>(partitionList);
				newPartitionList.add(subString);
				if(i==s.length()-1){
					result.add(newPartitionList);
				}else{
					trace(result,newPartitionList,i+1, s);
				}
			}
		}

	}

	private boolean isPalindrome(String s) {
		return s.equals(new StringBuffer(s).reverse().toString());
	}
}
