package peter.liu.firstmissingpositive;

/**
 * Given an unsorted integer array, find the first missing positive integer.
 * 
 * For example, Given [1,2,0] return 3, and [3,4,-1,1] return 2.
 * 
 * Your algorithm should run in O(n) time and uses constant space.
 * 
 * @author liuyingjie
 *
 */
public class Solution {
	
	/**
	 * Put each number in its right place.
	 * 
	 * For example:
	 * 
	 * When we find 5, then swap it with A[4].
	 * 
	 * At last, the first place where its number is not right, return the place
	 * + 1.
	 * 
	 * @param nums
	 * @return
	 */
	public int firstMissingPositive(int[] nums) {
		int n = nums.length;
		for (int i = 0; i < n; i++) {
			while (nums[i] > 0 && nums[i] <= n && nums[i] != nums[nums[i] - 1])
				swap(nums, i, nums[i] - 1);
		}
		for (int i = 0; i < n; i++)
			if (nums[i] != i + 1)
				return i + 1;
		return n + 1;
	}

	private void swap(int[] nums, int i, int j) {
		nums[i] ^= nums[j];
		nums[j] ^= nums[i];
		nums[i] ^= nums[j];
	}
	public static void main(String[] args){
		int a=3, b=50;
		a ^=b;
		b^=a;
		a ^=b;
		System.out.println(a+"----"+b);
	}
	// public int firstMissingPositive(int[] nums) {
	//
	// List<int[]> sliceList = new LinkedList<>();
	// int[] start = { 1, Integer.MAX_VALUE };
	// sliceList.add(start);
	// for (int i = 0; i < nums.length; i++) {
	// if (nums[i] > 0) {
	// for (int j = 0; j < sliceList.size(); j++) {
	// if (nums[i] >= sliceList.get(j)[0] && nums[i] <= sliceList.get(j)[1]) {
	// if (nums[i] == sliceList.get(j)[0] && nums[i] == sliceList.get(j)[1]) {
	// sliceList.remove(j);
	// } else if (nums[i] == sliceList.get(j)[0]) {
	// sliceList.get(j)[0] = nums[i] + 1;
	// } else if (nums[i] == sliceList.get(j)[1]) {
	// sliceList.get(j)[1] = nums[i] - 1;
	// } else {
	// int temp = sliceList.get(j)[1];
	// sliceList.get(j)[1] = nums[i] - 1;
	// int[] newSlice = { nums[i] + 1, temp };
	// sliceList.add(j + 1, newSlice);
	// }
	// break;
	// }
	// }
	// }
	// }
	// return sliceList.get(0)[0];
	//
	// }
}
