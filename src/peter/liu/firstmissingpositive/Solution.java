package peter.liu.firstmissingpositive;

import java.util.LinkedList;
import java.util.List;

/**
 * Given an unsorted integer array, find the first missing positive integer.
 * 
 * For example, Given [1,2,0] return 3, and [3,4,-1,1] return 2.
 * 
 * Your algorithm should run in O(n) time and uses constant space.
 * 
 * @author liuyingjie
 *
 */
public class Solution {
	public int firstMissingPositive(int[] nums) {
		
		List<int[]> sliceList = new LinkedList<>();
		int[] start = { 1, Integer.MAX_VALUE };
		sliceList.add(start);
		for (int i = 0; i < nums.length; i++) {
			if (nums[i] > 0) {
				for (int j = 0; j < sliceList.size(); j++) {
					if (nums[i] >= sliceList.get(j)[0] && nums[i] <= sliceList.get(j)[1]) {
						if (nums[i] == sliceList.get(j)[0] && nums[i] == sliceList.get(j)[1]) {
							sliceList.remove(j);
						} else if (nums[i] == sliceList.get(j)[0]) {
							sliceList.get(j)[0] = nums[i] + 1;
						} else if (nums[i] == sliceList.get(j)[1]) {
							sliceList.get(j)[1] = nums[i] - 1;
						} else {
							int temp = sliceList.get(j)[1];
							sliceList.get(j)[1] = nums[i] - 1;
							int[] newSlice = { nums[i] + 1, temp };
							sliceList.add(j + 1, newSlice);
						}
						break;
					}
				}
			}
		}
		return sliceList.get(0)[0];
		
	}
}
