package peter.liu.findminimuminrotatedsortedarray;

/**
 * Suppose a sorted array is rotated at some pivot unknown to you beforehand.
 * 
 * (i.e., 0 1 2 4 5 6 7 might become 4 5 6 7 0 1 2).
 * 
 * Find the minimum element.
 * 
 * You may assume no duplicate exists in the array.
 * 
 * Subscribe to see which companies asked this question
 * 
 * @author liuyingjie
 *
 */
public class Solution {
	public int findMin(int[] nums) {
		int left=0;
		int right=nums.length-1;
		int mid=0;
		while(left<right){
			mid=(right-left)/2 +left;
			if(nums[mid]>nums[left]&&nums[mid]>nums[right]){
				if(nums[left]>nums[right]){
					left=mid+1;
				}else{
					right=mid-1;
				}
			}else if(nums[mid]<nums[left]&&nums[mid]<nums[right]){
				if(nums[left]>nums[right]){
					right=mid;
				}else{
					left=mid+1;
				}
			}else if(nums[mid]>nums[left]&&nums[mid]<nums[right]){
				right=mid-1;
			}else{
				break;
			}
		}
		return nums[left]>nums[right]?nums[right]:nums[left];

	}
}
