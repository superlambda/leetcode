package peter.liu.validsudoku;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testSortList() {
		Solution s = new Solution();
		String[] ss = { "..5.....6", "....14...", ".........", ".....92..",
				"5....2...", ".......3.", "...54....", "3.....42.", "...27.6.." };
		char[][] cc = new char[9][9];
		for (int i = 0; i < 9; i++) {
			cc[i] = ss[i].toCharArray();
		}
		assertTrue(s.isValidSudoku(cc));
	}

}
