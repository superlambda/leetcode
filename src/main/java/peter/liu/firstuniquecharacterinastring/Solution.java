package peter.liu.firstuniquecharacterinastring;

import java.util.HashMap;

public class Solution {
	
	public int firstUniqChar(String s) {
//		Set<Character> set=new HashSet<>();
//		
//		for(int i=0;i<s.length();i++) {
//			if(set.contains(s.charAt(i))) {
//				continue;
//			} else {
//				set.add(s.charAt(i));
//				int j;
//				for(j=i+1; j<s.length();j++) {
//					if(s.charAt(i)==s.charAt(j)) {
//						break;
//					}
//				}
//				if(j==s.length()) {
//					return i;
//				}
//				
//			}
//		}
//		return -1;
		
		HashMap<Character, Integer> count = new HashMap<Character, Integer>();
        int n = s.length();
        // build hash map : character and how often it appears
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            count.put(c, count.getOrDefault(c, 0) + 1);
        }
        
        // find the index
        for (int i = 0; i < n; i++) {
            if (count.get(s.charAt(i)) == 1) 
                return i;
        }
        return -1;
        
    }

}
