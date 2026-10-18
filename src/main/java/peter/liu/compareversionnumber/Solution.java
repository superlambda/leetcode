package peter.liu.compareversionnumber;

/**
 * Compare two version numbers version1 and version1. If version1 > version2
 * return 1, if version1 < version2 return -1, otherwise return 0.
 * 
 * You may assume that the version strings are non-empty and contain only digits
 * and the . character. The . character does not represent a decimal point and
 * is used to separate number sequences. For instance, 2.5 is not
 * "two and a half" or "half way to version three", it is the fifth second-level
 * revision of the second first-level revision.
 * 
 * Here is an example of version numbers ordering:
 * 
 * 0.1 < 1.1 < 1.2 < 13.37
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	public int compareVersion(String version1, String version2) {
		String[] a = version1.split("\\.");
		String[] b = version2.split("\\.");
		for (int i = 0; i < a.length; i++) {
			if (i < b.length) {
				int left = Integer.valueOf(a[i]);
				int right = Integer.valueOf(b[i]);
				if (left < right) {
					return -1;
				} else if (left > right) {
					return 1;
				} else {
					continue;
				}
			}
		}
		if (a.length < b.length) {
			for (int j = a.length; j < b.length; j++) {
				if (Integer.valueOf(b[j]) > 0) {
					return -1;
				}
			}
		} else if (a.length > b.length) {
			for (int j = b.length; j < a.length; j++) {
				if (Integer.valueOf(a[j]) > 0) {
					return 1;
				}
			}
		}
		return 0;
		
	}
}
