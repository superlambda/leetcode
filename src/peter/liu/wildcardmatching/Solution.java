package peter.liu.wildcardmatching;

import java.util.HashSet;
import java.util.Set;

/**
 * Implement wildcard pattern matching with support for '?' and '*'.
 * 
 * '?' Matches any single character. '*' Matches any sequence of characters
 * (including the empty sequence).
 * 
 * The matching should cover the entire input string (not partial).
 * 
 * The function prototype should be: bool isMatch(const char *s, const char *p)
 * 
 * Some examples: isMatch("aa","a") → false isMatch("aa","aa") → true
 * isMatch("aaa","aa") → false isMatch("aa", "*") → true isMatch("aa", "a*") →
 * true isMatch("ab", "?*") → true isMatch("aab", "c*a*b") → false
 * 
 * @author liuyingjie
 *
 */
public class Solution {
	
	/**
	 * DP solution from others
	 * @param s
	 * @param p
	 * @return
	 */
	public boolean isMatch(String s, String p) {
        int sLength = s.length(), pLength = p.length();
        int count = 0;
        for (int i = 0; i < pLength; i++) {
            if (p.charAt(i) == '*'){
            	count++;
            }
        }
        if (count==0 && sLength != pLength){
        	return false;
        } else if (pLength - count > sLength) {
        	return false;
        }

        boolean[] match = new boolean[sLength+1];
        match[0] = true;
        for (int i = 0; i < sLength; i++) {
            match[i+1] = false;
        }
        
        
        //Really hard to understand, don't understand until now.
        for (int i = 0; i < pLength; i++) {
            if (p.charAt(i) == '*') {
            	
                for (int j = 0; j < sLength; j++) {
                    match[j+1] = match[j] || match[j+1]; 
                }
            } else {
                for (int j = sLength-1; j >= 0; j--) {
                    match[j+1] = (p.charAt(i) == '?' || p.charAt(i) == s.charAt(j)) && match[j];
                }
                match[0] = false;
            }
        }
        return match[sLength];
    }
	
	private int[] numberOfWildCard=  null;
	private Set<String> calculatedSet=new HashSet<>();
//	public boolean isMatch(String s, String p) {
//		StringBuffer sb=new StringBuffer();
//		int pt=0;
//		while(pt<p.length()){
//			if(pt==0){
//				sb.append(p.charAt(pt));
//			}else{
//				if(!(p.charAt(pt)=='*'&&p.charAt(pt-1)=='*')){
//					sb.append(p.charAt(pt));
//				}
//			}
//			pt++;
//		}
//		p=sb.toString();
//		setNumberOfWildCard(p);
//        return checkMatch(s,p,0,0);
//    }
	
	private void setNumberOfWildCard(String p){
		int count=0;
		numberOfWildCard= new int[p.length()];
		for(int i=p.length()-1;i>=0;i--){
			if(p.charAt(i)=='*'){
				count++;
			}
			numberOfWildCard[i] =count;
		}
	}
	
	private boolean checkMatch(String s, String p, int st, int pt) {
		
		String key=st+","+pt;
		
		if(calculatedSet.contains(key)){
			return false;
		}
		if (st < s.length() && pt < p.length() && p.charAt(pt) != '?' && p.charAt(pt) != '*'
				&& p.charAt(pt) != s.charAt(st)) {
			return false;
		}
		while (st < s.length() && pt < p.length() && (s.charAt(st) == p.charAt(pt) || p.charAt(pt) == '?')) {
			st++;
			pt++;
		}
		
		if (st < s.length() && pt < p.length() && p.charAt(pt) != '?' && p.charAt(pt) != '*'
				&& p.charAt(pt) != s.charAt(st)) {
			return false;
		}
		
		if (st == s.length() && pt == p.length()) {
			return true;
		}
		if(st==s.length()&&p.length()-pt==numberOfWildCard[pt]){
			return true;
		}
		if (st == s.length() || pt == p.length()) {
			return false;
		}
		
		int numberOfTry=s.length()-st-(p.length()-pt-numberOfWildCard[pt]);
		
		int start=0;
		
		while(start<=numberOfTry){
			if((!calculatedSet.contains(key))&&checkMatch(s, p, st+start, pt+1)){
				return true;
			}
			start++;
		}
		calculatedSet.add(key);		
		return false;

	}
}
