package peter.liu.findminimuminrotatedsortedarrayII;

/**
 * Follow up for "Find Minimum in Rotated Sorted Array": What if duplicates are
 * allowed?
 * 
 * Would this affect the run-time complexity? How and why? Suppose a sorted
 * array is rotated at some pivot unknown to you beforehand.
 * 
 * (i.e., 0 1 2 4 5 6 7 might become 4 5 6 7 0 1 2).
 * 
 * Find the minimum element.
 * 
 * The array may contain duplicates.
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
			while(nums[left]==nums[right]&&left<right){
				left++;
			}
			mid=(right-left)/2 +left;
			if(nums[mid]>=nums[left]&&nums[mid]>nums[right]){
				if(nums[left]>nums[right]){
					left=mid+1;
				}else{
					right=mid-1;
				}
			}else if(nums[mid]<nums[left]&&nums[mid]<=nums[right]){
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
	
	//solution from post
	public int anotherFindMin(int[] nums) {

		int left = 0, right = nums.length - 1;
		while (left < right) {
			int mid = (left + right) / 2;
			if (nums[mid] < nums[right]) {
				right = mid;
			} else if (nums[mid] > nums[right]) {
				left = mid + 1;
			} else {
				right--; // nums[mid]=nums[r] no idea, but we can eliminate
							// nums[r];
			}
		}
		return nums[left];

	}

}
