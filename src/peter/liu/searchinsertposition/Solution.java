package peter.liu.searchinsertposition;

/**
 * Given a sorted array and a target value, return the index if the target is
 * found. If not, return the index where it would be if it were inserted in
 * order.
 * 
 * You may assume no duplicates in the array.
 * 
 * Here are few examples. [1,3,5,6], 5 → 2 [1,3,5,6], 2 → 1 [1,3,5,6], 7 → 4
 * [1,3,5,6], 0 → 0
 * 
 * @author superlambda
 *
 */
public class Solution {

	public int searchInsert(int[] A, int target) {
		int low = 0;
		int high = A.length - 1;
		int middle = 0;
		while (low <= high) {
			middle = (high - low) / 2 + low;
			if (A[middle] == target) {
				return middle;
			} else if (A[middle] > target) {
				high = middle - 1;
			} else {
				low = middle + 1;
			}
		}
		if (A[middle] > target) {
			return middle;
		} else {
			return middle + 1;
		}
	}

}
