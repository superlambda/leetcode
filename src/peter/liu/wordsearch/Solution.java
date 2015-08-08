package peter.liu.wordsearch;

import java.util.HashSet;
import java.util.Set;

/**
 * Given a 2D board and a word, find if the word exists in the grid.
 * 
 * The word can be constructed from letters of sequentially adjacent cell, where
 * "adjacent" cells are those horizontally or vertically neighboring. The same
 * letter cell may not be used more than once.
 * 
 * For example, Given board =
 * 
 * [ ["ABCE"], ["SFCS"], ["ADEE"] ] word = "ABCCED", -> returns true, word =
 * "SEE", -> returns true, word = "ABCB", -> returns false.
 * 
 * @author superlambda
 *
 */
public class Solution {
	private String word;

	public boolean exist(char[][] board, String word) {
		this.word = word;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j] == this.word.charAt(0)
						&& checkExisting(board, new HashSet<String>(), i, j)) {
					return true;
				}
			}
		}
		return false;

	}

	private boolean checkExisting(char[][] board, Set<String> set, int i, int j) {
		if (set.size() == word.length()-1) {
			return true;
		}
		set.add(i + "," + j);
		
		if (j + 1 < board[0].length && !set.contains(i + "," + (j + 1))
				&& board[i][j + 1] == this.word.charAt(set.size())
				&& checkExisting(board, set, i, j + 1)) {
			return true;
		}
		if (i + 1 < board.length && !set.contains((i + 1) + "," + j)
				&& board[i + 1][j] == this.word.charAt(set.size())
				&& checkExisting(board, set, i + 1, j)) {
			return true;
		}
		if ((j - 1) >= 0 && !set.contains(i + "," + (j - 1))
				&& board[i][j - 1] == this.word.charAt(set.size())
				&& checkExisting(board, set, i, j - 1)) {
			return true;
		}
		if ((i - 1) >= 0 && !set.contains((i - 1) + "," + j)
				&& board[i - 1][j] == this.word.charAt(set.size())
				&& checkExisting(board, set, i - 1, j)) {
			return true;
		}
		set.remove(i + "," + j);
		return false;

	}
}
