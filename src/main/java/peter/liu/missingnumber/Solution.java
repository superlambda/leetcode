package peter.liu.missingnumber;

public class Solution {

	public int missingNumber(int[] nums) {
		
		int[] mapping = new int[nums.length+1];
		
		for(int i=0; i<nums.length;i++) {
			mapping[nums[i]] = nums[i];
		}
		
		for(int i=1; i<mapping.length;i++) {
			if(mapping[i]==0) {
				return i;
			}
		}
		
		return 0;
	}

}
