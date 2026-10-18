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

	public boolean exist(char[][] board, String word) {
		char[] words = word.toCharArray();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j] == words[0]
						&& checkExisting(board, new HashSet<String>(), i, j,
								words)) {
					return true;
				}
			}
		}
		return false;

	}

	private boolean checkExisting(char[][] board, Set<String> set, int i,
			int j, char[] word) {
		if (set.size() == word.length - 1) {
			return true;
		}
		String comb = i + "," + j;
		set.add(comb);

		int jPlus = j + 1;
		if (jPlus < board[0].length && !set.contains(i + "," + jPlus)
				&& board[i][jPlus] == word[set.size()]
				&& checkExisting(board, set, i, jPlus, word)) {
			return true;
		}
		int iPlus = i + 1;
		if (iPlus < board.length && !set.contains(iPlus + "," + j)
				&& board[iPlus][j] == word[set.size()]
				&& checkExisting(board, set, iPlus, j, word)) {
			return true;
		}
		int jMinus = j - 1;
		if (jMinus >= 0 && !set.contains(i + "," + jMinus)
				&& board[i][jMinus] == word[set.size()]
				&& checkExisting(board, set, i, jMinus, word)) {
			return true;
		}
		int iMinus = i - 1;
		if (iMinus >= 0 && !set.contains(iMinus + "," + j)
				&& board[iMinus][j] == word[set.size()]
				&& checkExisting(board, set, iMinus, j, word)) {
			return true;
		}
		set.remove(comb);
		return false;

	}

	// A better solution
	// public boolean exist(char[][] board, String word) {
	// char[] w = word.toCharArray();
	// for (int y=0; y<board.length; y++) {
	// for (int x=0; x<board[y].length; x++) {
	// if (exist(board, y, x, w, 0)) return true;
	// }
	// }
	// return false;
	// }
	//
	// private boolean exist(char[][] board, int y, int x, char[] word, int i) {
	// if (i == word.length) return true;
	// if (y<0 || x<0 || y == board.length || x == board[y].length) return
	// false;
	// if (board[y][x] != word[i]) return false;
	// board[y][x] ^= 256;
	// boolean exist = exist(board, y, x+1, word, i+1)
	// || exist(board, y, x-1, word, i+1)
	// || exist(board, y+1, x, word, i+1)
	// || exist(board, y-1, x, word, i+1);
	// board[y][x] ^= 256;
	// return exist;
	// }
}
