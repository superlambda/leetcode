package peter.liu.excelsheetcolumnnumber;

/**
 * Related to question Excel Sheet Column Title
 * 
 * Given a column title as appear in an Excel sheet, return its corresponding
 * column number.
 * 
 * For example:
 * 
 * A -> 1 B -> 2 C -> 3 ... Z -> 26 AA -> 27 AB -> 28
 * 
 * Credits: Special thanks to @ts for adding this problem and creating all test
 * cases.
 * 
 * @author pcnsh197
 *
 */
public class Solution {

	public int titleToNumber(String s) {
		char[] charAr = s.toCharArray();
		int result = 0;
		for (int i = charAr.length - 1; i >= 0; i--) {
			result += (charAr[i] - 'A' + 1)
					* Math.pow(26, charAr.length - 1 - i);
		}
		return result;
	}
}
