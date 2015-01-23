package peter.liu.twosum;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testSortList() {
	    Solution s=new Solution();
	    int[] numbers={2, 7, 11, 15};
	    int target=9;
	    int[] result=s.twoSum(numbers, target);
	    assertTrue(result[0]==1);
	    assertTrue(result[1]==2);
	}

}
