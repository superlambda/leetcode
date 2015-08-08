package peter.liu.subsets;

import java.util.List;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testSubsets() {
		int[] nums = { 1, 2, 3 };
		List<List<Integer>> result = new Solution().subsets(nums);
		for (List<Integer> comb : result) {
			for (Integer i : comb) {
				System.out.print(i + " ");
			}
			System.out.println("");
		}

	}

}
