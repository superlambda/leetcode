package peter.liu.spiralmatrix;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class SolutionTest {

	@Test
	public void testSpiralOrder() {
		int[][] matrix = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
		List<Integer> list = new LinkedList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(6);
		list.add(9);
		list.add(8);
		list.add(7);
		list.add(4);
		list.add(5);

		List<Integer> result = new Solution().spiralOrder(matrix);
		for (int i = 0; i < result.size(); i++) {
			Assert.assertTrue(list.get(i) == result.get(i));
		}

		int[][] matrix2 = { {2, 3 }};
		List<Integer> result2 = new Solution().spiralOrder(matrix2);
		for(int element:result2){
			System.out.print(element+" ");
		}
	}

}
