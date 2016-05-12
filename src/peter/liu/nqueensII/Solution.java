package peter.liu.nqueensII;

import java.util.ArrayList;
import java.util.List;

/**
 * Follow up for N-Queens problem.
 * 
 * Now, instead outputting board configurations, return the total number of
 * distinct solutions.
 * 
 * @author liuyingjie
 *
 */
public class Solution {
	private int count=0;
	public int totalNQueens(int n) {
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
		solve(new ArrayList<Integer>(), preDefinedLines, n);
		return count;
    }
	
	private void solve(List<Integer> checkList, String[] preDefinedLines, int n) {
		if (checkList.size() < n) {
			for (int i = 0; i < n; i++) {
				if (canContinue(checkList, i,n)) {
					
					if(checkList.size()==n-1){
						count++;
					}else{
						List<Integer> newCheckList = new ArrayList<>(checkList);
						newCheckList.add(i);
						solve( newCheckList, preDefinedLines, n);
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
