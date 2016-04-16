package peter.liu.sudokusolver;

import java.util.HashMap;
import java.util.Map;
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
		for(int i=0;i<9;i++){
			horizionMap.put(i, new Stack<>());
			verticalMap.put(i, new Stack<>());
			matrixMap.put(i, new Stack<>());
		}
		for(int i=0;i<9;i++){
			Stack<Character> horizionSet=horizionMap.get(i);
			for(int j=0;j<9;j++){
				if(board[i][j]!='.'){
					horizionSet.push(board[i][j]);
					verticalMap.get(j).push(board[i][j]);
					matrixMap.get(getMatrixSetKey(i, j)).push(board[i][j]);
				}
			}
		}
		
		solved(0, 0, board);
	}
	
	

	private boolean solved(int i, int j, char[][] board) {
		if(board[i][j]!='.'){
			if(i==8&&j==8){
				return true;
			}else{
				Stack<Character> horizionSet=horizionMap.get(i);
				Stack<Character> verticalSet=verticalMap.get(j);
				Stack<Character> matrixSet=matrixMap.get(getMatrixSetKey(i, j));
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
			Stack<Character> matrixSet=matrixMap.get(getMatrixSetKey(i, j));
			for(char c='1'; c<='9';c++){
				if(horizionSet.search(c)==-1
						&&verticalSet.search(c)==-1
						&&matrixSet.search(c)==-1){
					board[i][j]=c;
					if (i == 8 && j == 8) {
						return true;
					}else{
						horizionSet.push(c);
						verticalSet.push(c);
						matrixSet.push(c);
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
	
	//Faster solution without hashMap
//	public void solveSudoku(char[][] board) {
//        if(board == null || board.length == 0)
//            return;
//        solve(board);
//    }
//
//    public boolean solve(char[][] board){
//        for(int i = 0; i < board.length; i++){
//            for(int j = 0; j < board[0].length; j++){
//                if(board[i][j] == '.'){
//                    for(char c = '1'; c <= '9'; c++){//trial. Try 1 through 9 for each cell
//                        if(isValid(board, i, j, c)){
//                            board[i][j] = c; //Put c for this cell
//
//                            if(solve(board)){
//                                return true; //If it's the solution return true
//                            }
//                            else{
//                                board[i][j] = '.'; //Otherwise go back
//                            }
//                        }
//                    }
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//
//    public boolean isValid(char[][] board, int i, int j, char c){
//        //Check colum
//        for(int row = 0; row < 9; row++){
//            if(board[row][j] == c){
//                return false;
//            }
//        }
//
//        //Check row
//        for(int col = 0; col < 9; col++){
//            if(board[i][col] == c){
//                return false;
//            }
//        }
//        //Check 3 x 3 block
//        for(int row = (i / 3) * 3; row < (i / 3) * 3 + 3; row++){
//            for(int col = (j / 3) * 3; col < (j / 3) * 3 + 3; col++){
//                if(board[row][col] == c){
//                    return false;
//                }
//            }
//        }
//                
//        return true;
//    }
	
}
