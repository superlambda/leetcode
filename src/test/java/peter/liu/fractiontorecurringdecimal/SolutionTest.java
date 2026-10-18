package peter.liu.fractiontorecurringdecimal;

import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class SolutionTest {

	@Test
	public void testFractionToDecimal() {
		System.out.println(new Solution().fractionToDecimal(-2147483648,
				1));
	}

}
