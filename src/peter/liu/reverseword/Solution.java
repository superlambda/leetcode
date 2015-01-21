package peter.liu.reverseword;

/**
 * Given an input string, reverse the string word by word.
 * 
 * For example, Given s = "the sky is blue", return "blue is sky the".
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	public String reverseWords(String s) {
		String[] strArray = s.split(" ");
		StringBuilder sb = new StringBuilder();
		sb.append("");
		for (int j = strArray.length - 1; j >= 0; j--) {
			String value = strArray[j].trim();
			if (!"".equals(value)) {
				sb.append(value);
				if (j != 0) {
					sb.append(" ");
				}
			}

		}
		return sb.toString().trim();
	}

}
