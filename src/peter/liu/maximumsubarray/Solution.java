package peter.liu.maximumsubarray;

/**
 * Find the contiguous subarray within an array (containing at least one number)
 * which has the largest sum.
 * 
 * For example, given the array [−2,1,−3,4,−1,2,1,−5,4], the contiguous subarray
 * [4,−1,2,1] has the largest sum = 6.
 * 
 * @author superlambda
 *
 */
public class Solution {
	public int maxSubArray(int[] nums) {
		int max = Integer.MIN_VALUE, sum = 0;
		for (int i = 0; i < nums.length; i++) {
			if (sum < 0) {
				sum = nums[i];
			} else {
				sum += nums[i];
			}
			if (sum > max) {
				max = sum;
			}
		}
		return max;
//		return divide_conquer(0,nums.length-1,nums);
	}
	
	public  int maxSubArray2(int[] nums) {
		int maxSoFar=nums[0], maxEndingHere=nums[0];
	    for (int i=1;i<nums.length;++i){
	        maxEndingHere= Math.max(maxEndingHere+nums[i],nums[i]);
	        maxSoFar=Math.max(maxSoFar, maxEndingHere); 
	    }
	    return maxSoFar;
	}
	
	
	public  int maxSubArray3(int[] nums) {
		int maxSoFar=nums[0];
		int maxEndingHere=nums[0];
		for(int i=1;i<nums.length;i++) {
			maxEndingHere = Math.max(maxEndingHere+nums[i], nums[i]);
			maxSoFar=Math.max(maxEndingHere, maxSoFar);
		}
		return maxSoFar;
	}
	
	
	int divide_conquer(int left,int right,int nums[]){
		if(left > right){
		    return Integer.MIN_VALUE;
		}
		int mid = (left + right)/2;
		int lmax = divide_conquer(left,mid-1,nums);
		int rmax = divide_conquer(mid+1,right,nums);
		int maxv = Math.max(lmax,rmax);
		int sum = 0;
		int mlmax = 0;
		for(int i = mid - 1; i >= left; --i){
		    sum+=nums[i];
		    if(sum > mlmax){
		        mlmax = sum;
		    }
		}
		sum = 0;
		int mrmax = 0;
		for(int i = mid + 1; i <= right; ++i){
		    sum += nums[i];
		    if(sum > mrmax){
		        mrmax = sum;
		    }
		}
		maxv = Math.max(maxv, mlmax + mrmax + nums[mid]);
		return maxv;
	}

}
