package peter.liu.combinations;

import java.util.List;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testCombine() {
		List<List<Integer>> result = new Solution().combine(4, 2);
		for (List<Integer> comb : result) {
			for (Integer i : comb) {
				System.out.print(i + " ");
			}
			System.out.println("");
		}
	}

}
