package peter.liu.graycode;

import java.util.List;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testGrayCode() {
		List<Integer> result = new Solution().grayCode(3);
		for (Integer grayCode : result) {
			System.out.print(grayCode + " ");
		}
	}

}
