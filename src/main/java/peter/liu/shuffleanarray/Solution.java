package peter.liu.shuffleanarray;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Solution {

	private final int[] nums;

	public Solution(int[] nums) {
		this.nums = nums;
	}

	public int[] reset() {
		return this.nums;
	}

	public int[] shuffle() {
		int[] copy = Arrays.copyOf(nums, nums.length);
		for (int i = copy.length - 1; i > 0; i--) {
			int index = ThreadLocalRandom.current().nextInt(i + 1);
			int temp = copy[i];
			copy[i] = copy[index];
			copy[index] = temp;
		}
		return copy;
	}

}
