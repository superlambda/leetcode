package peter.liu.containsduplicate;

import java.util.HashSet;
import java.util.Set;

public class Solution {
	
	public boolean containsDuplicate(int[] nums) {
		
		Set<Integer> set = new HashSet<>();
		
		for(int i=0;i< nums.length; i++) {
			if(!set.contains(nums[i])) {
				set.add(nums[i]);
			} else {
				return true;
			}
			
		}
		return false;
        
    }

}
