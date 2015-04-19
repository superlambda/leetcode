package peter.liu.searchforarange;

/**
 * Given a sorted array of integers, find the starting and ending position of a
 * given target value.
 * 
 * Your algorithm's runtime complexity must be in the order of O(log n).
 * 
 * If the target is not found in the array, return [-1, -1].
 * 
 * For example, Given [5, 7, 7, 8, 8, 10] and target value 8, return [3, 4].
 * 
 * @author superlambda
 *
 */
public class Solution {

	/**
	 * My solution
	 * @param A
	 * @param target
	 * @return
	 */
	public int[] searchRange(int[] A, int target) {
		int low = 0;
		int high = A.length - 1;
		while (low <= high) {
			int middle = (high - low) / 2 + low;
			if (A[middle] == target) {
				int rightTrace = middle;
				int leftTrace = middle;
				while (rightTrace + 1 < A.length
						&& A[rightTrace + 1] == A[middle]) {
					rightTrace++;
				}
				while (leftTrace - 1 >= 0 && A[leftTrace - 1] == A[middle]) {
					leftTrace--;
				}
				return new int[] { leftTrace, rightTrace };
			} else if (A[middle] > target) {
				high = middle - 1;
			} else {
				low = middle + 1;
			}
		}
		return new int[] { -1, -1 };
	}

//	public int[] searchRange(int[] A, int target) {
//		int start = firstGreaterOrEqual(A, target);
//		if (start == A.length || A[start] != target) {
//			return new int[] { -1, -1 };
//		}
//		return new int[] { start, firstGreaterOrEqual(A, target + 1) - 1 };
//	}
//
//	private int firstGreaterOrEqual(int[] A, int target) {
//		int low = 0;
//		int high = A.length;
//		while (low < high) {
//			int middle = (high - low) / 2 + low;
//			if (A[middle] < target) {
//				low = middle + 1;
//			} else {
//				high = middle;
//			}
//		}
//		return low;
//	}

}
