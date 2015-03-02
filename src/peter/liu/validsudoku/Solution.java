package peter.liu.validsudoku;

import java.util.HashSet;
import java.util.Set;

/**
 * Determine if a Sudoku is valid, according to: Sudoku Puzzles - The Rules.
 * 
 * The Sudoku board could be partially filled, where empty cells are filled with
 * the character '.'. Note: A valid Sudoku board (partially filled) is not
 * necessarily solvable. Only the filled cells need to be validated.
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	public boolean isValidSudoku(char[][] board) {
		for (int i = 0; i < 9; i++) {
			Set<Character> checkSet = new HashSet<>();
			for (int j = 0; j < 9; j++) {
				if (board[i][j] == '.') {
					continue;
				}
				if (!checkSet.contains(board[i][j])) {
					checkSet.add(board[i][j]);
				} else {
					System.out.println("false 1");
					return false;
				}
			}
		}
		for (int i = 0; i < 9; i++) {
			Set<Character> checkSet = new HashSet<>();
			for (int j = 0; j < 9; j++) {
				if (board[j][i] == '.') {
					continue;
				}
				if (!checkSet.contains(board[j][i])) {
					checkSet.add(board[j][i]);
				} else {
					System.out.println("false 2");
					return false;
				}
			}
		}
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				Set<Character> checkSet = new HashSet<>();
				for (int si = i; si <= i + 2; si++) {
					for (int sj = j; sj <= j + 2; sj++) {
						if (board[si][sj] == '.') {
							continue;
						}
						if (!checkSet.contains(board[si][sj])) {
							checkSet.add(board[si][sj]);
						} else {
							System.out.println("false 3");
							return false;
						}
					}
				}
			}
		}
		return true;
	}
}
