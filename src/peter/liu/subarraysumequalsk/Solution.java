package peter.liu.subarraysumequalsk;

import java.util.HashMap;
import java.util.Map;

public class Solution {

	public int subarraySum(int[] nums, int k) {

		int sum = 0;
		int count = 0;
		Map<Integer, Integer> arraySum = new HashMap<>();

		for (int i = 0; i < nums.length; i++) {
			sum += nums[i];
			if (sum == k) {
				count++;
			}
			if (arraySum.containsKey(sum-k)) {
				count += arraySum.get(sum-k);
			}
			if (arraySum.containsKey(sum)) {
				arraySum.put(sum, arraySum.get(sum)+1);
			} else {
				arraySum.put(sum, 1);
			}
			
			System.out.println("sum: " + sum + " count: "+ count + "-" + arraySum);
		}

		return count;

	}

}
//2,-2,3,0,4,-7
//2 -2
//0
//2,-2,3,0,4,-7
//3,0,4,-7
