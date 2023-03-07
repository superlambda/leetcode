package peter.liu.rotatearray;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testRotate() {
//		int[] array={1,2,3,4,5,6,7};
//		new Solution().rotate(array, 3);
//		
//		int[] array2={1,2,3};
//		new Solution().rotate(array2, 2);
//		
//		for(int i=0; i<array2.length;i++) {
//			System.out.print(array2[i]);
//		}

		int[] array3 = { 1,2,3,4,5,6,7 };
		new Solution().rotate(array3, 3);

		for (int i = 0; i < array3.length; i++) {
			System.out.print(array3[i]);
		}
	}

}
