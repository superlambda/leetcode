package peter.liu.houserobber;

/**
 * You are a professional robber planning to rob houses along a street. Each
 * house has a certain amount of money stashed, the only constraint stopping you
 * from robbing each of them is that adjacent houses have security system
 * connected and it will automatically contact the police if two adjacent houses
 * were broken into on the same night.
 * 
 * Given a list of non-negative integers representing the amount of money of
 * each house, determine the maximum amount of money you can rob tonight without
 * alerting the police.
 * 
 * @author liuyingjie
 *
 */
public class Solution {
//	public int rob(int[] nums) {
//		if(nums.length==0){
//			return 0;
//		}
//		if(nums.length==1){
//			return nums[0];
//		}
//		if(nums.length==2){
//			return Math.max(nums[0], nums[1]);
//		}
//		int[]  max= new int[nums.length];
//		max[0]=nums[0];
//		max[1]=Math.max(nums[0], nums[1]);
//		for(int i=2;i<nums.length;i++){
//			max[i]=Math.max(max[i-1], max[i-2]+nums[i]);
//		}
//		return max[max.length-1];
//	}

	public int rob(int[] nums) {
		int[] maxAmount = new int[nums.length];
		maxAmount[0] = nums[0];
		if (nums.length == 1) {
			return maxAmount[0];
		}
		maxAmount[1] = Math.max(maxAmount[0], nums[1]);

		for (int i = 2; i < nums.length; i++) {
			maxAmount[i] = Math.max(maxAmount[i - 1], nums[i] + maxAmount[i - 2]);
		}
		return maxAmount[maxAmount.length - 1];

	}
}
