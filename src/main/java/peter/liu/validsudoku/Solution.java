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
//	public boolean isValidSudoku(char[][] board) {
//		for (int i = 0; i < 9; i++) {
//			Set<Character> horizionCheckSet = new HashSet<>(12);
//			Set<Character> verticalCheckSet = new HashSet<>(12);
//			for (int j = 0; j < 9; j++) {
//				if (board[i][j] == '.' && board[j][i] == '.') {
//					continue;
//				}
//				if (board[i][j] != '.') {
//					if (!horizionCheckSet.contains(board[i][j])) {
//						horizionCheckSet.add(board[i][j]);
//					} else {
//						return false;
//					}
//				}
//				if (board[j][i] != '.') {
//					if (!verticalCheckSet.contains(board[j][i])) {
//						verticalCheckSet.add(board[j][i]);
//					} else {
//						return false;
//					}
//				}
//			}
//			if(i%3==0&&i<7){
//				for (int k = 0; k < 7; k+=3) {
//					Set<Character> matrixCheckSet = new HashSet<>(12);
//					for (int si = i; si <= i + 2; si++) {
//						for (int sj = k; sj <= k + 2; sj++) {
//							if (board[si][sj] == '.') {
//								continue;
//							}
//							if (!matrixCheckSet.contains(board[si][sj])) {
//								matrixCheckSet.add(board[si][sj]);
//							} else {
//								return false;
//							}
//						}
//					}
//				}
//			}
//		}
//		return true;
//	}
	
	public boolean isValidSudoku(char[][] board) {
		
		Set<String> set = new HashSet<>();
		for(int i=0; i<9; i++) {
			for(int j=0; j<9; j++) {
				if(board[i][j]!='.') {
					if(!set.add(board[i][j]+"row "+i)||
					   !set.add(board[i][j]+"column "+j)||
					   !set.add(board[i][j]+"subbox "+i/3 +j/3)) {
						return false;
					}
					
				}
			}
		}
		return true;
	}
}
