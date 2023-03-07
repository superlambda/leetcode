package peter.liu.removeduplicatesfromsortedarray;

/**
 * Given a sorted array, remove the duplicates in place such that each element
 * appear only once and return the new length.
 * 
 * Do not allocate extra space for another array, you must do this in place with
 * constant memory.
 * 
 * For example, Given input array A = [1,1,2],
 * 
 * Your function should return length = 2, and A is now [1,2].
 * 
 * @author pcnsh197
 *
 */
public class Solution {
//	public int removeDuplicates(int[] A) {
//		int length = A.length;
//		int i = 0;
//		int j = i + 1;
//		while (i < A.length - 1) {
//			for (; j < A.length; j++) {
//				if (A[j] != A[i]) {
//					A[i + 1] = A[j];
//					j++;
//					break;
//				}else{
//					length--;
//				}
//			}
//			i++;
//		}
//		return length;
//	}
	
//	public int removeDuplicates(int[] nums) {
//        if(nums == null|| nums.length == 0) {
//            return 0;
//        }
//        int resultLength = nums.length;
//        int originalLength = resultLength;
//        int i = 0;
//        int j = i + 1;
//        for(; i < originalLength -1; i++) {
//            for(; j < originalLength; j++){
//                if(nums[j] != nums[i]){
//                    nums[i+1] = nums[j];
//                    j++;
//                    break;
//                } else{
//                    resultLength--;
//                }
//            }
//        }
//        return resultLength;
//    }
	
	/* 
	 * First Index is responsible for writing unique values in our input array, 
	 * while Second Index will read the input array and pass all the distinct
	 * elements to First Index
	 */
	public int removeDuplicates(int[] nums) {
		/* For corner case */
        if(nums==null || nums.length == 0) {
        	return 0;
        }
        /* 
         * insertIndex indicates the index that the greater element
         * should be inserted. 
         */
        int insertIndex = 1;
        int originalLength = nums.length;
        /* i is the cursor to loop the whole array */
        for (int i = insertIndex; i<originalLength; i++) {
//        	if(nums[i] != nums[insertIndex-1]) {
        	if(nums[i] != nums[i-1]) { /* More easy to understand */
        		nums[insertIndex] = nums[i];
                insertIndex++;
        	}
        }
  
        return insertIndex;
    }
}
