package peter.liu.nqueens;

import java.util.List;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testSolveNQueens() {
		List<List<String>> result=new Solution().solveNQueens(8);
		System.out.println("Result:"+result.size());
//		for(List<String> oneResult:result){
//			for(String line:oneResult){
//				System.out.println(line);
//			}
//			System.out.println("--------------------");
//		}
	}

}
