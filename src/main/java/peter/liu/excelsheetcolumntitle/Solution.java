package peter.liu.excelsheetcolumntitle;


/**
 * Given a positive integer, return its corresponding column title as appear in an Excel sheet.

For example:

    1 -> A
    2 -> B
    3 -> C
    ...
    26 -> Z
    27 -> AA
    28 -> AB 
 * @author pcnsh197
 *
 */
public class Solution {

	public String convertToTitle(int n) {
		int fac = 26;
		String s = "";
		while (n > 0) {
			n--;
			s = (char) (n % 26 + 'A') + s;
			n /= fac;
		}
		return s;
	}
}
