package peter.liu.findpeakelement;

/**
 * A peak element is an element that is greater than its neighbors.
 * 
 * Given an input array where num[i] ≠ num[i+1], find a peak element and return
 * its index.
 * 
 * The array may contain multiple peaks, in that case return the index to any
 * one of the peaks is fine.
 * 
 * You may imagine that num[-1] = num[n] = -∞.
 * 
 * For example, in array [1, 2, 3, 1], 3 is a peak element and your function
 * should return the index number 2.
 * 
 * @author Peter
 *
 */
public class Solution {
	public int findPeakElement(int[] num) {
        int length = num.length;
        if (length == 1) {
            return 0;
        }
        if (length == 2) {
            return num[0] > num[1] ? 0 : 1;
        }
        return binarySearch(num, length, 0, length - 1);
    }

    private static int binarySearch(int[] a, int length, int low, int high) {
        int mid = low + (high - low) / 2;
        int midVal = a[mid];
        int left=mid-1;
        int right=mid+1;
        if (right < length && left > -1) {
            if (a[left] < midVal && a[right] < midVal) {
                return mid;
            } else {
                if (low <= left) {
                    int leftSearch = binarySearch(a, length, low, left);
                    if (leftSearch >= 0) {
                        return leftSearch;
                    }
                }
                if (right <= high) {
                    int rightSearch = binarySearch(a, length, right, high);
                    if (rightSearch > 0) {
                        return rightSearch;
                    }
                }
                return -1;
            }
        } else if (mid == 0) {
            if (midVal > a[right]) {
                return mid;
            } else if (a[right] > a[mid + 2]) {
                return right;
            } else {
                return -1;
            }

        } else if (mid == length - 1) {
            if (midVal > a[left]) {
                return mid;
            } else if (a[left] > a[mid - 2]) {
                return left;
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

}
