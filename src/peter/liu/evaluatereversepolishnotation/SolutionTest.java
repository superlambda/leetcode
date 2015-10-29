package peter.liu.evaluatereversepolishnotation;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testEvalRPN() {
//		String[] tokens={"0","3","/"};
		String[] tokens={"10","6","9","3","+","-11","*","/","*","17","+","5","+"};
		System.out.println(new Solution().evalRPN(tokens));
	}

}
