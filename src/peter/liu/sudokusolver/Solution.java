package peter.liu.sudokusolver;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Write a program to solve a Sudoku puzzle by filling the empty cells.
 * 
 * Empty cells are indicated by the character '.'.
 * 
 * You may assume that there will be only one unique solution.
 * 
 * @author liuyingjie
 *
 */
public class Solution {
	
	private Map<Integer,Stack<Character>>horizionMap=new HashMap<>();
	private Map<Integer,Stack<Character>>verticalMap=new HashMap<>();
	private Map<Integer,Stack<Character>>matrixMap=new HashMap<>();
	public void solveSudoku(char[][] board) {
		
		char[][] workBoard=new char[9][9];
		for(int i=0;i<9;i++){
			workBoard[i]=board[i].clone();
			Stack<Character> horizionSet=horizionMap.get(i);
			if(horizionSet==null){
				horizionSet=new Stack<>();
				horizionMap.put(i, horizionSet);
			}
			for(int j=0;j<9;j++){
				Stack<Character> verticalSet=verticalMap.get(j);
				if(verticalSet==null){
					verticalSet=new Stack<>();
					verticalMap.put(j, verticalSet);
				}		
				int matrixSetKey=getMatrixSetKey(i, j);
				Stack<Character> matrixSet=matrixMap.get(matrixSetKey);
				
				if(matrixSet==null){
					matrixSet=new Stack<>();
					matrixMap.put(matrixSetKey, matrixSet);
				}				
				if(board[i][j]!='.'){	
					horizionSet.push(board[i][j]);
					verticalSet.push(board[i][j]);
					matrixSet.push(board[i][j]);
				}
			}
		}
		
		solved(0, 0, workBoard);
		for (int i = 0; i < 9; i++) {
			board[i]=workBoard[i].clone();
		}
	}
	
	

	private boolean solved(int i, int j, char[][] board) {
		if(board[i][j]!='.'){
			if(i==8&&j==8){
				if (isValidSudoku(board)){
					return true;
				}else{
					return false;
				}
			}else{
				Stack<Character> horizionSet=horizionMap.get(i);
				Stack<Character> verticalSet=verticalMap.get(j);
				int matrixSetKey=getMatrixSetKey(i, j);
				Stack<Character> matrixSet=matrixMap.get(matrixSetKey);
				horizionSet.push(board[i][j]);
				verticalSet.push(board[i][j]);
				matrixSet.push(board[i][j]);
				
				if(j<8){
					j++;
				}else if(j==8&&i<8){
					i++;
					j=0;
				}
				if(solved(i, j, board)){
					return true;
				}else{
					horizionSet.pop();
					verticalSet.pop();
					matrixSet.pop();
				}
			}
			
		}else{
			Stack<Character> horizionSet=horizionMap.get(i);
			Stack<Character> verticalSet=verticalMap.get(j);
			int matrixSetKey=getMatrixSetKey(i, j);
			Stack<Character> matrixSet=matrixMap.get(matrixSetKey);
			for(char c='1'; c<='9';c++){
				if(horizionSet.search(c)==-1
						&&verticalSet.search(c)==-1
						&&matrixSet.search(c)==-1){
					horizionSet.push(c);
					verticalSet.push(c);
					matrixSet.push(c);
					board[i][j]=c;
					if (i == 8 && j == 8) {
						if (isValidSudoku(board)) {
							return true;
						}else{
							horizionSet.pop();
							verticalSet.pop();
							matrixSet.pop();
							board[i][j]='.';
						}
					}else{
						int newI=i;
						int newJ=j;
						if(newJ<8){
							newJ++;
						}else if(newJ==8&&newI<8){
							newI++;
							newJ=0;
						}
						if (solved(newI, newJ, board)){
							return true;
						}else{
							horizionSet.pop();
							verticalSet.pop();
							matrixSet.pop();
							board[i][j]='.';
						}
					}
					
				}
			}
			
		}
		
		return false;

	}
	
	public boolean isValidSudoku(char[][] board) {
		for (int i = 0; i < 9; i++) {
			Set<Character> horizionCheckSet = new HashSet<>(12);
			Set<Character> verticalCheckSet = new HashSet<>(12);
			for (int j = 0; j < 9; j++) {
				if (board[i][j] == '.' && board[j][i] == '.') {
					continue;
				}
				if (board[i][j] != '.') {
					if (!horizionCheckSet.contains(board[i][j])) {
						horizionCheckSet.add(board[i][j]);
					} else {
						return false;
					}
				}
				if (board[j][i] != '.') {
					if (!verticalCheckSet.contains(board[j][i])) {
						verticalCheckSet.add(board[j][i]);
					} else {
						return false;
					}
				}
			}
			if(i%3==0&&i<7){
				for (int k = 0; k < 7; k+=3) {
					Set<Character> matrixCheckSet = new HashSet<>(12);
					for (int si = i; si <= i + 2; si++) {
						for (int sj = k; sj <= k + 2; sj++) {
							if (board[si][sj] == '.') {
								continue;
							}
							if (!matrixCheckSet.contains(board[si][sj])) {
								matrixCheckSet.add(board[si][sj]);
							} else {
								return false;
							}
						}
					}
				}
			}
		}
		return true;
	}

	private int getMatrixSetKey(int i, int j) {
		if (i < 3 && j < 3) {
			return 0;
		} else if (i < 3 && j >= 3 && j < 6) {
			return 1;
		} else if (i < 3 && j >= 6) {
			return 2;
		} else if (i >= 3 && i < 6 && j < 3) {
			return 3;
		} else if (i >= 3 && i < 6 && j >= 3 && j < 6) {
			return 4;
		} else if (i >= 3 && i < 6 && j >= 6) {
			return 5;
		} else if (i >= 6 && j < 3) {
			return 6;
		} else if (i >= 6 && j >= 3 && j < 6) {
			return 7;
		} else if (i >= 6 && j >= 6) {
			return 8;
		}

		return -1;
	}
	
}
