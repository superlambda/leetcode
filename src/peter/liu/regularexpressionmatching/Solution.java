package peter.liu.regularexpressionmatching;

/**
 * Implement regular expression matching with support for '.' and '*'.
 * 
 * '.' Matches any single character. '*' Matches zero or more of the preceding
 * element.
 * 
 * The matching should cover the entire input string (not partial).
 * 
 * The function prototype should be: bool isMatch(const char *s, const char *p)
 * 
 * Some examples: isMatch("aa","a") → false isMatch("aa","aa") → true
 * isMatch("aaa","aa") → false isMatch("aa", "a*") → true isMatch("aa", ".*") →
 * true isMatch("ab", ".*") → true isMatch("aab", "c*a*b") → true Subscribe to
 * see which companies asked this question
 * 
 * @author liuyingjie
 *
 */
public class Solution {
	public boolean isMatch(String s, String p) {
		if (isSubStringMatch(s, p, 0, 0)) {
			return true;
		}
		return false;
	}

	private boolean isSubStringMatch(String s, String p, int sStartIndex, int pStartIndex) {
		if (sStartIndex == s.length()) {
			//all reaches end
			if (pStartIndex == p.length()) {
				return true;
			}
			//The left sub string are . or [a-z]
			for (; pStartIndex <= p.length() - 1; pStartIndex++) {
				if (p.charAt(pStartIndex) != '*'
						&& (pStartIndex == p.length() - 1 || p.charAt(pStartIndex + 1) != '*')) {
					return false;
				}
			}
			return true;
		}
		// s not reaches end but p reaches end
		if (pStartIndex == p.length()) {
			return false;
		}
		
		if (isMatchFirst(s, p, sStartIndex, pStartIndex)) {
			if (pStartIndex < p.length() - 1) {
				//consider * 0 or many times
				if (p.charAt(pStartIndex + 1) == '*') {
					if (isSubStringMatch(s, p, sStartIndex, pStartIndex + 2)
							|| isSubStringMatch(s, p, sStartIndex + 1, pStartIndex)) {
						return true;
					}

				} else {
					//increase both s and p
					if (isSubStringMatch(s, p, sStartIndex + 1, pStartIndex + 1)) {
						return true;
					}
				}
			}else{
				//p reaches end but s not
				if(sStartIndex<s.length()-1){
					return false;
				}else{
					// increase both s and p 
					//TODO to see if can be matched with line 59
					if (isSubStringMatch(s, p, sStartIndex + 1, pStartIndex + 1)) {
						return true;
					}
				}
			}
			
		} else {
			if (pStartIndex < p.length() - 1 && p.charAt(pStartIndex + 1) == '*'
					&& isSubStringMatch(s, p, sStartIndex, pStartIndex + 2)) {
				return true;
			}
		}
		return false;

	}

	private boolean isMatchFirst(String s, String p, int sStartIndex, int pStartIndex) {
		if (s.charAt(sStartIndex) == p.charAt(pStartIndex) || p.charAt(pStartIndex) == '.') {
			return true;
		} else {
			return false;
		}
	}
}
