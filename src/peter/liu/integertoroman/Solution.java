package peter.liu.integertoroman;

/**
 * Given an integer, convert it to a roman numeral.
 * 
 * Input is guaranteed to be within the range from 1 to 3999.
 * 
 * @author pcnsh197
 *
 */
public class Solution {

	public String intToRoman(int num) {
		StringBuffer sb = new StringBuffer();
		int[] numArray = { 1000, 500, 100, 50, 10, 5, 1 };
		int[] numberToSubstract = { 1, 10, 100 };
		for (int i = 0; i < numArray.length; i++) {
			if (num <= 0) {
				break;
			}
			
			if (num / numArray[i] > 0 && num / numArray[i] <= 3) {
				while (num >= numArray[i]) {
					num -= numArray[i];
					sb.append(getRomanValue(numArray[i]));
				}
			} else {
				if (i > 0) {
					for (int j = 0; j < numberToSubstract.length; j++) {
						if (numArray[i - 1] - numberToSubstract[j] > 0
								&& num >= numArray[i - 1]
										- numberToSubstract[j]) {
							num -= numArray[i - 1] - numberToSubstract[j];
							sb.append(getRomanValue(numberToSubstract[j]))
									.append(getRomanValue(numArray[i - 1]));
							break;
						}
					}
				}
			}
		}
		return sb.toString();
	}

	public char getRomanValue(int number) {
		switch (number) {
		case 1:
			return 'I';
		case 5:
			return 'V';
		case 10:
			return 'X';
		case 50:
			return 'L';
		case 100:
			return 'C';
		case 500:
			return 'D';
		case 1000:
			return 'M';
		default:
			throw new RuntimeException("The number is: " + number);
		}
	}
}
