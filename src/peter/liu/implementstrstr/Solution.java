package peter.liu.implementstrstr;

/**
 * Implement strStr().
 * 
 * Returns the index of the first occurrence of needle in haystack, or -1 if
 * needle is not part of haystack.
 */
public class Solution {
	public int strStr(String haystack, String needle) {
		if(haystack==null||needle==null){
			return -1;
		}
		if(needle.length()==0){
			return 0;
		}
		int needleLength=needle.length();
		for (int i = 0; i <= haystack.length() - needleLength; i++) {
			for (int j = 0; j < needleLength; j++) {
				if (haystack.charAt(i + j) != needle.charAt(j)) {
					break;
				} else if (j == needleLength - 1) {
					return i;
				}
			}
		}
		return -1;
	}
}
