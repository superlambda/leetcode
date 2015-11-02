package peter.liu.maximumproductsubarray;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testMaxProduct() {
		int[] nums = {-2,1,0};
		System.out.println(new Solution().maxProduct(nums));
	}

}
