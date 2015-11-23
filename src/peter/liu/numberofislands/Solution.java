package peter.liu.numberofislands;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Given a 2d grid map of '1's (land) and '0's (water), count the number of
 * islands. An island is surrounded by water and is formed by connecting
 * adjacent lands horizontally or vertically. You may assume all four edges of
 * the grid are all surrounded by water.
 * 
 * Example 1:
 * 
 * 11110 
 * 11010 
 * 11000 
 * 00000 Answer: 1
 * 
 * Example 2:
 * 
 * 11000 
 * 11000 
 * 00100 
 * 00011 Answer: 3
 * 
 * @author liuyingjie
 *
 */
public class Solution {
	private Map<String,Set<String>> islandMap=new HashMap<>();
	private Map<String,String> candidateToIslandMap = new HashMap<>();
	public int numIslandsByPeter(char[][] grid) {
		for(int i=0;i<grid.length;i++){
			for(int j=0;j<grid[0].length;j++){
				if(grid[i][j]=='1'){
					String key=i+","+j;
					if(!candidateToIslandMap.containsKey(key)){
						
						boolean shouldUnion=false;
						boolean leftFound=false;
						boolean upFound=false;
						String leftKey=null;
						String upKey=null;
						if(i>0&&grid[i-1][j]=='1'){
							leftFound=true;
							leftKey=(i-1)+","+j;
						}
						
						if(j>0&&grid[i][j-1]=='1'){
							upFound=true;
							upKey=i+","+(j-1);
							
						}
						shouldUnion=leftFound&&upFound;
						if(shouldUnion){
							Set<String> finalSet=new HashSet<>();
							finalSet.add(key);
							Set<String> island=islandMap.get(candidateToIslandMap.get(leftKey));
							if(island!=null){
								finalSet.addAll(island);
								islandMap.remove(candidateToIslandMap.get(leftKey));
							}
							island=islandMap.get(candidateToIslandMap.get(upKey));
							if(island!=null){
								finalSet.addAll(island);
								islandMap.remove(candidateToIslandMap.get(upKey));
							}
							islandMap.put(key, finalSet);
							for(String element:finalSet){
								candidateToIslandMap.put(element, key);
							}
						}else if(leftFound){
							candidateToIslandMap.put(key, candidateToIslandMap.get(leftKey));
							islandMap.get(candidateToIslandMap.get(leftKey)).add(key);
						}else if(upFound){
							candidateToIslandMap.put(key, candidateToIslandMap.get(upKey));
							islandMap.get(candidateToIslandMap.get(upKey)).add(key);
						}else{
							candidateToIslandMap.put(key, key);
							Set<String> newIsland=new HashSet<String>();
							newIsland.add(key);
							islandMap.put(key, newIsland);
						}

					}
				}
			}
		}
		return islandMap.keySet().size();
	}
	
	public int numIslands(char[][] grid) {
		int count = 0;

		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (grid[i][j] == '1') {
					count++;
					clearRestOfLand(grid, i, j);
				}
			}
		}
		return count;
	}

	private void clearRestOfLand(char[][] grid, int i, int j) {
		if (i < 0 || j < 0 || i >= grid.length || j >= grid[i].length || grid[i][j] == '0'){
			return;
		}

		grid[i][j] = '0';
		clearRestOfLand(grid, i + 1, j);
		clearRestOfLand(grid, i - 1, j);
		clearRestOfLand(grid, i, j + 1);
		clearRestOfLand(grid, i, j - 1);
		return;
	}
}
