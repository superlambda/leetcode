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
	public int removeDuplicates(int[] A) {
		int length = A.length;
		int i = 0;
		int j = i + 1;
		while (i < A.length - 1) {
			for (; j < A.length; j++) {
				if (A[j] != A[i]) {
					A[i + 1] = A[j];
					j++;
					break;
				}else{
					length--;
				}
			}
			i++;
		}
		return length;
	}
}
