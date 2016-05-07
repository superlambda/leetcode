package peter.liu.jumpgameII;

import java.util.HashMap;
import java.util.Map;

/**
 * Given an array of non-negative integers, you are initially positioned at the
 * first index of the array.
 * 
 * Each element in the array represents your maximum jump length at that
 * position.
 * 
 * Your goal is to reach the last index in the minimum number of jumps.
 * 
 * For example: Given array A = [2,3,1,1,4]
 * 
 * The minimum number of jumps to reach the last index is 2. (Jump 1 step from
 * index 0 to 1, then 3 steps to the last index.)
 * 
 * Note: You can assume that you can always reach the last index.
 * 
 * @author liuyingjie
 *
 */
public class Solution {
	
	private int minimumStep=Integer.MAX_VALUE;
	
	private Map<String,Integer> checkedSet= new HashMap<>();
	public int jump(int[] nums) {
		jumpAt(nums, 0,0);
		return minimumStep;
    }
	
	private void jumpAt(int[] nums, int start, int step) {
		
		if(start<nums.length){
			
			while(start<nums.length-1&&nums[start]==1){
				start++;
				step++;
				
			}
			if(start>=nums.length-1){
				if(step<minimumStep){
					minimumStep=step;
				}
				return;
			}
			
			if(start+nums[start]>=nums.length-1){
				step++;
				if(step<minimumStep){
					minimumStep=step;
				}
				return;
			}
			
			boolean jumpped=false;
			for(int i=1;i<=nums[start];i++){
				String key=start+","+i;
				if(nums[start+i]>nums[start]-i&&(!checkedSet.containsKey(key)||checkedSet.get(key)>step)){
					jumpped=true;
					checkedSet.put(key,step);
					jumpAt(nums, start+i, step+1);
				}
			}
			
			if (!jumpped && start < nums.length && nums[start] > 0
					&& (!checkedSet.containsKey(start + "," + nums[start])
							|| checkedSet.get(start + "," + nums[start]) > step)) {

				checkedSet.put(start + "," + nums[start], step);
				start = start + nums[start];
				step++;
				jumpAt(nums, start, step);

			}
			
		}
	}
}
