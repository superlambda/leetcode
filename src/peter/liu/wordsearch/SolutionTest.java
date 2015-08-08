package peter.liu.wordsearch;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testExist() {
		char[][] board = { { 'A', 'B', 'C', 'E' }, { 'S', 'F', 'C', 'S' },
				{ 'A', 'D', 'E', 'E' } };
		assertTrue(new Solution().exist(board, "ABCCED"));
		assertTrue(new Solution().exist(board, "SEE"));
		assertFalse(new Solution().exist(board, "ABCB"));

		char[][] board2 = { { 'a', 'a', 'a', 'a' }, { 'a', 'a', 'a', 'a' },
				{ 'a', 'a', 'a', 'a' },{ 'a', 'a', 'a', 'a' }, { 'a', 'a', 'a', 'b' }};
		assertFalse(new Solution().exist(board2, "aaaaaaaaaaaaaaaaaaaa"));
		
		char[][] board3 = { { 'a', 'b'}};
		assertTrue(new Solution().exist(board3, "ba"));
		
	}
}
