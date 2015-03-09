package peter.liu.majorityelement;

import java.util.HashMap;
import java.util.Map;

/**
 * Given an array of size n, find the majority element. The majority element is
 * the element that appears more than ⌊ n/2 ⌋ times.
 * 
 * You may assume that the array is non-empty and the majority element always
 * exist in the array.
 * 
 * Credits: Special thanks to @ts for adding this problem and creating all test
 * cases.
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	/**
	 * O(n) solution with O(n) capacity
	 * @param num
	 * @return
	 */
//	public int majorityElement(int[] num) {
//		Map<Integer, Integer> map = new HashMap<>();
//		for (int number : num) {
//			if (!map.containsKey(number)) {
//				map.put(number, 1);
//			} else {
//				int n = map.get(number) + 1;
//				if (n > num.length / 2) {
//					return number;
//				} else {
//					map.put(number, n);
//				}
//
//			}
//		}
//		return num[0];
//	}
	
	/**
	 * O(n) solution with O(1) capacity
	 * @param num
	 * @return
	 */
	public int majorityElement(int[] num) {
		int majority = num[0];
		int count = 1;
		for (int i = 1; i < num.length; i++) {
			if (count == 0) {
				majority = num[i];
				count++;
			} else if (majority == num[i]) {
				count++;
			} else {
				count--;
			}
		}
		return majority;
	}
}
