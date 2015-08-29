package peter.liu.restoreipaddresses;

import java.util.LinkedList;
import java.util.List;

/**
 * Given a string containing only digits, restore it by returning all possible
 * valid IP address combinations.
 * 
 * For example: Given "25525511135",
 * 
 * return ["255.255.11.135", "255.255.111.35"]. (Order does not matter)
 * 
 * @author superlambda
 *
 */
public class Solution {

	public List<String> restoreIpAddresses(String s) {
		List<String> result = new LinkedList<>();
		StringBuffer leadingIpString = new StringBuffer();
		getIpAddress(result, leadingIpString, 0, 0, s);
		return result;
	}

	private void getIpAddress(List<String> result,
			StringBuffer leadingIpString, int numberAdded, int startIndex,
			String s) {
		if (startIndex > s.length() - 1) {
			return;
		}

		if (numberAdded == 3) {
			if (s.length() - startIndex > 3) {
				return;
			}
			if (s.length() - startIndex >= 2 && s.charAt(startIndex) == '0') {
				return;
			}
			int number = Integer.valueOf(s.substring(startIndex));
			if (number <= 255) {
				leadingIpString.append(number);
				result.add(leadingIpString.toString());
			} else {
				return;
			}
		} else {
			int leftLength = s.length() - startIndex;
			if (leftLength >= 1) {
				StringBuffer tempOneLeading = new StringBuffer(leadingIpString);
				tempOneLeading.append(s.substring(startIndex, startIndex + 1))
						.append(".");
				getIpAddress(result, tempOneLeading, numberAdded + 1,
						startIndex + 1, s);
			}
			if (leftLength >= 2) {
				if (s.charAt(startIndex) == '0') {
					return;
				}
				StringBuffer tempTwoLeading = new StringBuffer(leadingIpString);
				tempTwoLeading.append(s.substring(startIndex, startIndex + 2))
						.append(".");
				getIpAddress(result, tempTwoLeading, numberAdded + 1,
						startIndex + 2, s);
			}
			if (leftLength >= 3) {
				if (s.charAt(startIndex) == '0') {
					return;
				}
				int number = Integer.valueOf(s.substring(startIndex,
						startIndex + 3));
				if (number <= 255) {
					StringBuffer tempThreeLeading = new StringBuffer(
							leadingIpString);
					tempThreeLeading.append(number).append(".");
					getIpAddress(result, tempThreeLeading, numberAdded + 1,
							startIndex + 3, s);
				}
			}

		}
	}
}
