package peter.liu.wordbreak;

import java.util.Set;

/**
 * Given a string s and a dictionary of words dict, determine if s can be
 * segmented into a space-separated sequence of one or more dictionary words.
 * 
 * For example, given s = "leetcode", dict = ["leet", "code"].
 * 
 * Return true because "leetcode" can be segmented as "leet code".
 * 
 * @author liuyingjie
 *
 */
public class Solution {
	public boolean wordBreak(String s, Set<String> wordDict) {
		if(s==null||s.equals("")){
			return true;
		}
		boolean[] checkArray=new boolean[s.length()+1];
		checkArray[0]=true;
		for(int i=1;i<checkArray.length;i++){
			checkArray[i]=false;
		}
		
		for(int j=0;j<s.length();j++){
			String subString=s.substring(0,j+1);
			for(String word: wordDict){
				if(subString.length()>=word.length()){
					if(checkArray[subString.length()-word.length()]&&subString.endsWith(word)){
						checkArray[j+1]=true;
						break;
					}
				}
			}
		}
		
		return checkArray[checkArray.length-1];
		
		
	}
}
