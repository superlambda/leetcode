package peter.liu.fractiontorecurringdecimal;

import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testFractionToDecimal() {
		System.out.println(new Solution().fractionToDecimal(-2147483648,
				1));
	}

}
