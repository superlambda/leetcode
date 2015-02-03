package peter.liu.stringtointeger;

/**
 * Implement atoi to convert a string to an integer.
 * 
 * Hint: Carefully consider all possible input cases. If you want a challenge,
 * please do not see below and ask yourself what are the possible input cases.
 * 
 * Notes: It is intended for this problem to be specified vaguely (ie, no given
 * input specs). You are responsible to gather all the input requirements up
 * front.
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	
	
	private String max = String.valueOf(Integer.MAX_VALUE);
	private String min = String.valueOf(Integer.MIN_VALUE).substring(1,
			String.valueOf(Integer.MIN_VALUE).length());

	public int atoi(String str) {
		if (str.contains("+") && str.contains("-")) {
			return 0;
		}
		char[] chars = str.toCharArray();
		StringBuilder sb = new StringBuilder();
		boolean signFound = false;
		for (char c : chars) {
			if ((c == '-' ||c=='+')&& sb.length() == 0) {
				sb.append(c);
				signFound = true;
			} else if (c >= '0' && c <= '9') {
				sb.append(c);
			} else {
				if(c!=' '){
					break;
				}
				if (signFound && sb.length() > 1) {
					break;
				}
				if (sb.length() > 0) {
					break;
				}
			}
		}
		String number = sb.toString();
		if (sb.length() == 0 || (sb.length() == 1)
				&& (number.equals("-") || number.equals("+"))) {
			return 0;
		}
		if (!number.startsWith("-")) {
			if(number.startsWith("+")){
				number=number.substring(1,number.length());
			}
			if (number.length() > max.length()) {
				return Integer.MAX_VALUE;
			} else {
				long result = Long.parseLong(number);
				if (result < Integer.MAX_VALUE) {
					return (int) result;
				} else {
					return Integer.MAX_VALUE;
				}
			}
		} else {
			if (number.length() > min.length() + 1) {
				return Integer.MIN_VALUE;
			} else {
				long result = Long.parseLong(number);
				if (result < Integer.MIN_VALUE) {
					return Integer.MIN_VALUE;
				} else {
					return (int) result;
				}
			}
		}
	}
}
