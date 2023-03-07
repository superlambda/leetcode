package peter.liu.movezero;

public class Solution {
	
	public void moveZeroes(int[] nums) {
		int i=0;
		int j=0;
		while(j<nums.length) {
			if(nums[j]!=0) {
				nums[i] = nums[j];
				/* Set the non-zero to zero in one loop*/
                if(i!=j){
				    nums[j] =0;
                }
                i++;
			}
			j++;
		}
        
    }

}
