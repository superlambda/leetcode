package peter.liu.medianoftwosortedarrays;

/**
 * There are two sorted arrays nums1 and nums2 of size m and n respectively.
 * Find the median of the two sorted arrays. The overall run time complexity
 * should be O(log (m+n)).
 * 
 * @author liuyingjie
 *
 */
public class Solution {

	public double findMedianSortedArrays(int[] nums1, int[] nums2) {
		/* A[0, 1, 2, ..., n-1, n] */
        /* A[0, 1, 2, ..., m-1, m] */
        int k = (nums1.length + nums2.length + 1) / 2;
        double v = (double)findKth(nums1, 0, nums1.length-1, nums2, 0, nums2.length-1, k);

        if ((nums1.length + nums2.length) % 2 == 0) {
            int k2 = k+1;
            double v2 = (double)findKth(nums1, 0, nums1.length-1, nums2, 0, nums2.length-1, k2);
            v = (v + v2) / 2;
        }

        return v;
		
	}
	

    // find the kth element in the two sorted arrays
    // let us say: nums1[aMid] <= nums2[bMid], x: mid len of a, y: mid len of b, then we can know
    // 
    // (1) there will be at least (x + 1 + y) elements before bMid
    // (2) there will be at least (m - x - 1 + n - y) = m + n - (x + y +1) elements after aMid
    // therefore
    // if k <= x + y + 1, find the kth element in a and b, but unconsidering bMid and its suffix
    // if k > x + y + 1, find the k - (x + 1) th element in a and b, but unconsidering aMid and its prefix
     private int findKth(int nums1[], int fLeft, int fRight, int nums2[], int sLeft, int sRight, int k) {
        if (fLeft > fRight) return nums2[sLeft + k - 1];
        if (sLeft > sRight) return nums1[fLeft + k - 1];

        int aMid = (fLeft + fRight) / 2;
        int bMid = (sLeft + sRight) / 2;

        if (nums1[aMid] <= nums2[bMid]) {
            if (k <= (aMid - fLeft) + (bMid - sLeft) + 1) {
                return findKth(nums1, fLeft, fRight, nums2, sLeft, bMid - 1, k);
            }
            else{
                return findKth(nums1, aMid + 1, fRight, nums2, sLeft, sRight, k - (aMid - fLeft) - 1);
            }
        }
        else { // nums1[aMid] > nums2[bMid]
            if (k <= (aMid - fLeft) + (bMid - sLeft) + 1){ 
                return findKth(nums1, fLeft, aMid - 1, nums2, sLeft, sRight, k);
            }
            else{
                return findKth(nums1, fLeft, fRight, nums2, bMid + 1, sRight, k - (bMid - sLeft) - 1);
            }
        }
    }

}
