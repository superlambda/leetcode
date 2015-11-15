package peter.liu.largestnumber;

import java.util.Arrays;

/**
 * Given a list of non negative integers, arrange them such that they form the
 * largest number.
 * 
 * For example, given [3, 30, 34, 5, 9], the largest formed number is 9534330.
 * 
 * Note: The result may be very large, so you need to return a string instead of
 * an integer.
 * 
 * @author liuyingjie
 *
 */
public class Solution {
	public String largestNumber(int[] nums) {
		Integer[] ns = new Integer[nums.length];
		for (int i = 0; i < nums.length; i++) {
			ns[i] = nums[i];
		}

		Arrays.parallelSort(ns, (Integer h1, Integer h2) -> {
			
			String s1 = String.valueOf(h1);
			String s2 = String.valueOf(h2);
			if(s1.length()==s2.length()){
				return s2.compareTo(s1);
			}
			return (s2 + s1).compareTo(s1 + s2);
		});

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ns.length; i++) {
			sb.append(ns[i]);
		}
		if (sb.charAt(0) == '0') {
			return "0";
		}
		return sb.toString();
	}
}
