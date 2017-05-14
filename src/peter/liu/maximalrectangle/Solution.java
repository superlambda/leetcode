package peter.liu.maximalrectangle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * Given a 2D binary matrix filled with 0's and 1's, find the largest rectangle containing only 1's and return its area.

	For example, given the following matrix:
	
	1 0 1 0 0
	1 0 1 1 1
	1 1 1 1 1
	1 0 0 1 0
	Return 6.
 * @author liuyingjie
 *
 */
public class Solution {
	

	public int maximalRectangle(char[][] matrix) {
		
		int maxRectangle = 0;
		Map<String, Set<Pair>> vertexMap = new HashMap<>();
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if(i-2>=0&&matrix[i-2][j]=='1'){
					vertexMap.remove((i - 2) + "," + j);
				}
				
				if(i-1>=0&&j-1>=0&&matrix[i-1][j-1]=='1'){
					vertexMap.remove((i - 1) + "," + (j-1));
				}
				
				if (matrix[i][j] == '0') {
					continue;
				}
				
				String itself=i + "," + j;
				Set<Pair> set = new HashSet<>();
				Pair vertexitSelf =new Pair(i,j);
				set.add(vertexitSelf);

				Set<Pair> vertPreSet = null;
				Set<Pair> horizPreSet = null;
				if(i>=1){
					vertPreSet = vertexMap.get((i - 1) + "," + j);
				}
				if(j>=1){
					horizPreSet = vertexMap.get((i) + "," + (j - 1));
				}
				
				if(vertPreSet==null && horizPreSet==null){
					vertexMap.put(itself, set);
					maxRectangle = maxRectangle < 1? 1:maxRectangle;
					continue;
				}
				
				if(vertPreSet ==null){
					vertPreSet =  new HashSet<>();
				}
				if(horizPreSet ==null){
					horizPreSet =  new HashSet<>();
				}
				
				for(Pair vertex: vertPreSet){	
					if(horizPreSet.contains(vertex)){
						set.add(vertex);
						int area = (i - vertex.row + 1)*(j - vertex.column + 1);
						maxRectangle = maxRectangle < area ? area : maxRectangle;
					}else{
						if(vertex.column==j){
							set.add(vertex);
							int area = i - vertex.row + 1;
							maxRectangle = maxRectangle < area ? area : maxRectangle;
						}
					}
				}
			
				for(Pair vertex: horizPreSet){
					if(!vertPreSet.contains(vertex)){
						if(vertex.row==i){
							set.add(vertex);
							int area = j - vertex.column + 1;
							maxRectangle = maxRectangle < area ? area : maxRectangle;
						}
					}
				}	
				
				vertexMap.put(itself, set);
			}
		}
		return maxRectangle;
	}
	
//	private boolean contains(List<int[]> preList,int[] vertex){
//		for(int[] vertixToCheck: preList){
//			if(vertixToCheck[0]==vertex[0]&&vertixToCheck[1]==vertex[1]){
//				return true;
//			}
//		}
//		return false;
//	}
	
	class Pair{
		Integer row,column;
		
		public Pair(Integer i,Integer j){
			row =i;
			column=j;
		}
		
		public boolean equals(Object obj) {
			if (! (obj instanceof Pair)){
				return false;
			}
			Pair pair=(Pair) obj;
	        return this.row==pair.row&&this.column==pair.column;
	    }
		public int hashCode(){
			return row.hashCode()+column.hashCode();
		}
	}
	
	
}
