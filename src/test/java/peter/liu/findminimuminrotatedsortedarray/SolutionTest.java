package peter.liu.findminimuminrotatedsortedarray;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SolutionTest {

	@Test
	public void testFindMin() {
		int[] nums={3,1,2};
		System.out.println(new Solution().findMin(nums));
	}

}
