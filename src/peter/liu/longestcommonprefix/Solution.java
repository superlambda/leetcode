package peter.liu.longestcommonprefix;

/**
 * Write a function to find the longest common prefix string amongst an array of
 * strings.
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	public String longestCommonPrefix(String[] strs) {
		if (strs.length == 0) {
			return "";
		}
		int minLength = strs[0].length();
		for (String str : strs) {
			if (minLength > str.length()) {
				minLength = str.length();
			}
		}
		String lcp = strs[0].substring(0, minLength);
		for (int i = 0; i < strs.length; i++) {
			while (strs[i].indexOf(lcp) != 0) {
				minLength--;
				lcp = strs[0].substring(0, minLength);
				if(lcp.equals("")) {
					return lcp;
				}
			}
		}
		return lcp;
	}
	
}
	
