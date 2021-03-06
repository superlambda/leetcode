package peter.liu.nqueens;

import java.util.ArrayList;
import java.util.List;

/**
 * The n-queens puzzle is the problem of placing n queens on an n×n chessboard such that no two queens attack each other.

Given an integer n, return all distinct solutions to the n-queens puzzle.

Each solution contains a distinct board configuration of the n-queens' placement, where 'Q' and '.' both indicate a queen and an empty space respectively.

For example,
There exist two distinct solutions to the 4-queens puzzle:

[
 [".Q..",  // Solution 1
  "...Q",
  "Q...",
  "..Q."],

 ["..Q.",  // Solution 2
  "Q...",
  "...Q",
  ".Q.."]
]
 * @author liuyingjie
 *
 */
public class Solution {
	public List<List<String>> solveNQueens(int n) {
		List<List<String>> result = new ArrayList<>();
		String[] preDefinedLines = new String[n];
		for (int i = 0; i < n; i++) {
			StringBuffer newLine = new StringBuffer();
			for (int j = 0; j < n; j++) {
				if (j == i) {
					newLine.append('Q');
				} else {
					newLine.append('.');
				}
			}
			preDefinedLines[i] = newLine.toString();
		}
		solve(result, new ArrayList<Integer>(), preDefinedLines, n);
		return result;
	}

	private void solve(List<List<String>> result, List<Integer> checkList, String[] preDefinedLines, int n) {
		if (checkList.size() < n) {
			for (int i = 0; i < n; i++) {
				if (canContinue(checkList, i,n)) {
					
					if(checkList.size()==n-1){
						List<String> oneResult = new ArrayList<>();
						for (Integer index : checkList) {
							oneResult.add(preDefinedLines[index]);
						}
						oneResult.add(preDefinedLines[i]);
						result.add(oneResult);
						
					}else{
						List<Integer> newCheckList = new ArrayList<>(checkList);
						newCheckList.add(i);
						solve(result, newCheckList, preDefinedLines, n);
					}
				}
			}
		}
	}
	private boolean canContinue(List<Integer> checkList, int i,int n){
		if(checkList.contains(i)){
			return false;
		}
		if(checkList.size()>0){
			
			for(int index=checkList.size()-1;index>=0;index--){
				int queenIndex=checkList.get(index);
				if(i-(checkList.size()-index)==queenIndex){
					return false;
				}
				if(i+(checkList.size()-index)==queenIndex){
					return false;
				}
			}
		}
		return true;
		
	}
	
}
