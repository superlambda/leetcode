package peter.liu.surroundedregions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Given a 2D board containing 'X' and 'O', capture all regions surrounded by 'X'.

	A region is captured by flipping all 'O's into 'X's in that surrounded region.
	
	For example,
	X X X X
	X O O X
	X X O X
	X O X X
	After running your function, the board should be:
	
	X X X X
	X X X X
	X X X X
	X O X X
 * @author superlambda
 *
 */
public class Solution {
	public void solve(char[][] board) {
		Set<String> notSurroundedSet=new HashSet<String>();
		Map<String,Set<String>> surroundeMap= new HashMap<>();
		for(int i=0;i<=board.length-1;i++){
			for(int j=0;j<=board[i].length-1;j++){
				if(board[i][j]=='O'){
					String key=i+","+j;
					if (i == 0 || i == board.length - 1 || j == 0
							|| j == board[0].length - 1) {
						notSurroundedSet.add(key);
					}
					Set<String> shouldBeConnectedSet = new HashSet<>();
					List<String> elementToCheck = new LinkedList<>();
					
					elementToCheck.add(key);
					if (i > 0) {
						elementToCheck.add((i - 1) + "," + j);
					}

					if (i < board.length - 1) {
						elementToCheck.add((i + 1) + "," + j);
					}

					if (j > 0) {
						elementToCheck.add(i + "," + (j - 1));
					}

					if (j < board[0].length - 1) {
						elementToCheck.add(i + "," + (j + 1));
					}
					
					boolean shouldAddToNotSurroundSet=false;
					for (String element : elementToCheck) {
						if (notSurroundedSet.contains(element)) {
							shouldAddToNotSurroundSet =true;
							break;
						}
					}
					
					//check connected
					for (String surroundKey : surroundeMap.keySet()) {
						Set<String> surroundSet = surroundeMap.get(surroundKey);
						for (String element : elementToCheck) {
							if (surroundSet.contains(element)) {
								shouldBeConnectedSet.add(surroundKey);
								break;
							}
						}
					}
					//union
					if (shouldBeConnectedSet.size() >= 1) {
						int biggetSize = 0;
						String biggestSizeKey = "";
						for (String k : shouldBeConnectedSet) {
							if (surroundeMap.get(k).size() > biggetSize) {
								biggetSize = surroundeMap.get(k).size();
								biggestSizeKey = k;
							}
						}
						for (String k : shouldBeConnectedSet) {
							if (!k.equals(biggestSizeKey)) {
								surroundeMap.get(biggestSizeKey).addAll(
										surroundeMap.get(k));
								surroundeMap.remove(k);
							}
						}
						if(shouldAddToNotSurroundSet){
							notSurroundedSet.addAll(surroundeMap.get(biggestSizeKey));
							surroundeMap.remove(biggestSizeKey);
							notSurroundedSet.add(key);
						}else{
							surroundeMap.get(biggestSizeKey).add(key);
						}
						
					}else{
						if(shouldAddToNotSurroundSet){
							notSurroundedSet.add(key);
						}else{
							Set<String> set=new HashSet<>();
							set.add(key);
							surroundeMap.put(key, set);
						}
					}
				}

			}
		}
		
		for (String surroundKey : surroundeMap.keySet()) {
			Set<String> surroundSet = surroundeMap.get(surroundKey);
			for (String element : surroundSet) {
				String[] index=element.split(",");
				board[Integer.valueOf(index[0])][Integer.valueOf(index[1])]='X';
			}
		}
		
    }
}
