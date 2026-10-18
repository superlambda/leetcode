package peter.liu.validanagram;

public class Solution {
	
	public boolean isAnagram(String s, String t) {
        if(s.length()!=t.length()) {
            return false;
        }
        char[] arrayS = s.toCharArray();
        char[] arrayT = t.toCharArray();
        
        for(int i=0;i<arrayS.length; i++) {
        	if(arrayT[i]!=arrayS[i]) {
        		int j;
        		for(j=i+1; j<arrayT.length;j++) {
        			if(arrayT[j]==arrayS[i]) {
        				arrayT[j]=arrayT[i];
        				arrayT[i]=arrayS[i];
        				break;
        			}
        		}
        		if(j==arrayT.length) {
        			return false;
        		}
        	}
        }
        return true;
        
    }
	
	public boolean isAnagram2(String s, String t) {
        if(s.length()!=t.length()) {
            return false;
        }
        int[] charCount = new int[26];
        
        for(int i=0; i<s.length();i++) {
        	charCount[s.charAt(i)-'a']++;
        	charCount[t.charAt(i)-'a']--;
        }
        for(int i=0;i<charCount.length;i++) {
        	if(charCount[i]!=0) {
        		return false;
        	}
        }
        return true;
    }

}
