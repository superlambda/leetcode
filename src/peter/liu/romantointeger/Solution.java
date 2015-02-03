package peter.liu.romantointeger;

/**
 * Given a roman numeral, convert it to an integer.
 * 
 * Input is guaranteed to be within the range from 1 to 3999.
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	public int romanToInt(String s) {
		if (s.length() == 1) {
			return getRomanValue(s.charAt(0));
		}
		char[] chars = s.toCharArray();
		int base = 0;
		int temp=getRomanValue(s.charAt(0));
		for (int i = 1; i < chars.length; i++) {
			if (getRomanValue(s.charAt(i)) == getRomanValue(s.charAt(i - 1))) {
				temp += getRomanValue(s.charAt(i));
			} else if (getRomanValue(s.charAt(i)) > getRomanValue(s
					.charAt(i - 1))) {
				base+= getRomanValue(s.charAt(i))- temp;
				temp=0;
			} else {
				base +=temp;
				temp=getRomanValue(s.charAt(i));
			}
		}
		return base+temp;
	}

	public int getRomanValue(char c) {
		switch (c) {
		case 'I':
			return 1;
		case 'V':
			return 5;
		case 'X':
			return 10;
		case 'L':
			return 50;
		case 'C':
			return 100;
		case 'D':
			return 500;
		case 'M':
			return 1000;
		default:
			return 0;
		}
	}

}
