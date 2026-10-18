package peter.liu.removeduplicatesfromsortedarrayII;

/**
 * Follow up for "Remove Duplicates": What if duplicates are allowed at most
 * twice?
 * 
 * For example, Given sorted array nums = [1,1,1,2,2,3],
 * 
 * Your function should return length = 5, with the first five elements of nums
 * being 1, 1, 2, 2 and 3. It doesn't matter what you leave beyond the new
 * length.
 * 
 * @author superlambda
 *
 */
public class Solution {
	public int removeDuplicates(int[] nums) {
		int length = nums.length;
		int i = 0;
		int j = i + 1;
		while (i < nums.length - 1) {
			for (; j < nums.length; j++) {
				if (nums[j] != nums[i]) {
					nums[i + 1] = nums[j];
					j++;
					break;
				} else {
					if (j < nums.length - 1 && nums[j] == nums[j + 1]) {
						length--;
					}else{
						i++;
						nums[i] = nums[j];
					}
				}
			}
			i++;
		}
		return length;

	}

}
