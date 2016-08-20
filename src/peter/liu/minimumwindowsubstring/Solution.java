package peter.liu.minimumwindowsubstring;

import java.util.HashMap;

/**
 * Given a string S and a string T, find the minimum window in S which will
 * contain all the characters in T in complexity O(n).
 * 
 * For example, S = "ADOBECODEBANC" T = "ABC" Minimum window is "BANC".
 * 
 * Note: If there is no such window in S that covers all characters in T, return
 * the empty string "".
 * 
 * If there are multiple such windows, you are guaranteed that there will always
 * be only one unique minimum window in S.
 * 
 * @author liuyingjie
 *
 */
public class Solution {
	public String minWindow(String s, String t) {
		String minString = "";
		if(s==""){
			return minString;
		}
		HashMap<Character,Integer> tMap=new HashMap<>();
		for(int i=0;i<t.length();i++){
			Character tc= t.charAt(i);
			if(tMap.containsKey(tc)){
				tMap.put(tc, tMap.get(tc)+1);
			}else{
				tMap.put(tc,1);;
			}
		}
		
		HashMap<Character,Integer> checkMap=new HashMap<>(tMap);
		HashMap<Character,Integer> sMap=new HashMap<>();
		int startIndex=0;
		int endIndex=0;
		while(!checkMap.isEmpty()&&endIndex<s.length()){
			Character sc= s.charAt(endIndex);
			if(tMap.containsKey(sc)){
				if(sMap.containsKey(sc)){
					sMap.put(sc, sMap.get(sc)+1);
				}else{
					sMap.put(sc,1);;
				}
				if(checkMap.containsKey(sc)){
					int count=checkMap.get(sc);
					if(count==1){
						checkMap.remove(sc);
						if(checkMap.isEmpty()){
							break;
						}
					}else{
						checkMap.put(sc, count-1);
					}
					
				}
			}
			endIndex++;
		}
		if(checkMap.isEmpty()){
			minString=s.substring(startIndex,endIndex+1);
		}else{
			return minString;
		}
		
		while(endIndex<s.length()&&endIndex+1-startIndex>=t.length()){
			while (((!tMap.containsKey(s.charAt(startIndex))) || (tMap.containsKey(s.charAt(startIndex))
					&& sMap.get(s.charAt(startIndex)) > tMap.get(s.charAt(startIndex))))
					&& endIndex + 1 - startIndex >= t.length()) {
				
				if (tMap.containsKey(s.charAt(startIndex))) {
					sMap.put(s.charAt(startIndex), sMap.get(s.charAt(startIndex)) - 1);
				}
				startIndex++;
			}
			if(endIndex+1-startIndex<minString.length()){
				minString=s.substring(startIndex,endIndex+1);
			}
			endIndex++;
			while (endIndex < s.length() && s.charAt(endIndex) != s.charAt(startIndex)) {
				char c = s.charAt(endIndex);
				if (sMap.containsKey(c)) {
					sMap.put(c, sMap.get(c) + 1);
				}
				endIndex++;
			}
			if(endIndex<s.length()){
				startIndex=startIndex+1;
				if(endIndex+1-startIndex<minString.length()){
					minString=s.substring(startIndex,endIndex+1);
				}
			}
		}
		return minString;
    }

}
