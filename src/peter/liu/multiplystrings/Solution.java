package peter.liu.multiplystrings;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Given two numbers represented as strings, return multiplication of the
 * numbers as a string.
 * 
 * Note: The numbers can be arbitrarily large and are non-negative.
 * 
 * @author superlambda
 *
 */
public class Solution {
	public String multiply(String num1, String num2) {
		if (num1.equals("0") || num2.equals("0")) {
			return "0";
		}
		if (num1.equals("1")) {
			return num2;
		}
		if (num2.equals("1")) {
			return num1;
		}
		if (num2.length() > num1.length()) {
			String temp = num1;
			num1 = num2;
			num2 = temp;

		}
		int[] num1Array = new int[num1.length()];
		int[] num2Array = new int[num2.length()];

		for (int i = 0; i < num1.length(); i++) {
			num1Array[i] = Integer.valueOf(num1.substring(i, i + 1));
		}

		for (int i = 0; i < num2Array.length; i++) {
			num2Array[i] = Integer.valueOf(num2.substring(i, i + 1));
		}
		List<LinkedList<Integer>> resultList = new ArrayList<>();
		int maxListSize = 0;

		for (int k = num2Array.length - 1; k >= 0; k--) {
			int left = 0;
			LinkedList<Integer> list = new LinkedList<>();
			for (int j = num1Array.length - 1; j >= 0; j--) {
				int multiply = num1Array[j] * num2Array[k] + left;
				list.addFirst(multiply % 10);
				left = multiply / 10;
			}
			if (left > 0) {
				list.addFirst(left);
			}
			int numberOfZeroToAdd = num2Array.length - 1 - k;
			while (numberOfZeroToAdd > 0) {
				list.addLast(0);
				numberOfZeroToAdd--;
			}
			if (list.size() > maxListSize) {
				maxListSize = list.size();
			}
			resultList.add(list);
		}

		for (LinkedList<Integer> list : resultList) {
			while (list.size() < maxListSize) {
				list.addFirst(0);
			}
		}
		LinkedList<Integer> result = new LinkedList<>();
		int sum = 0;
		for (int i = 0; i < maxListSize; i++) {
			for (LinkedList<Integer> list : resultList) {
				if (!list.isEmpty()) {
					sum += list.removeLast();
				}
			}
			result.addFirst(sum % 10);
			sum = sum / 10;
		}
		if (sum > 0) {
			result.addFirst(sum);
		}
		StringBuffer sb = new StringBuffer();
		for (int number : result) {
			sb.append(number);
		}
		return sb.toString();
	}
}
