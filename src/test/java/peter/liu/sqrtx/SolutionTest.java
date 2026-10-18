package peter.liu.sqrtx;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SolutionTest {

	@Test
	public void testMySqrt() {
		System.out.println("result: "+Math.sqrt(Integer.MAX_VALUE));
		System.out.println("result: "+new Solution().mySqrt(2147483647));
	}

}
