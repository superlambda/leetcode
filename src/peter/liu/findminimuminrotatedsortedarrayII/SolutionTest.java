package peter.liu.findminimuminrotatedsortedarrayII;

import static org.junit.Assert.*;

import org.junit.Test;


public class SolutionTest {

	@Test
	public void testFindMin() {
//		int[] nums={2,0,1,1,1};
		int[] nums={2,2,2,0,0,1};
		System.out.println(new Solution().findMin(nums));
	}

}
