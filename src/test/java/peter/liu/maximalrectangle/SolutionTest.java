package peter.liu.maximalrectangle;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;


public class SolutionTest {

	@Test
	public void test() {
		char[][] matrix = { { '1', '0', '1', '0', '0' }, 
							{ '1', '0', '1', '1', '1' }, 
							{ '1', '1', '1', '1', '1' },
						    { '1', '0', '0', '1', '0' } };
		
		assertEquals(new Solution().maximalRectangle(matrix),6);
		
//		String ttt="1,2";
//		System.out.println(ttt.substring(2,3));
//		Pair i=new Pair(1,2);
//		Pair j=new Pair(1,2);;
//		Set<Pair> set= new HashSet<>();
//		set.add(i);
//		set.add(j);
//		System.out.println(set.size());
		
	}
	

}
