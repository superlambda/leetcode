package peter.liu.maximalrectangle;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		Map<String, List<int[]>> vertexMap = new HashMap<>(793);
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
				List<int[]> list = new LinkedList<>();
				int[] vertexitSelf ={i,j};
				list.add(vertexitSelf);

				List<int[]> vertPreList = null;
				List<int[]> horizPreList = null;
				if(i>=1){
					vertPreList = vertexMap.get((i - 1) + "," + j);
				}
				if(j>=1){
					horizPreList = vertexMap.get((i) + "," + (j - 1));
				}
				
				if(vertPreList==null && horizPreList==null){
					vertexMap.put(itself, list);
					maxRectangle = maxRectangle < 1? 1:maxRectangle;
					continue;
				}
				
				if(vertPreList ==null){
					vertPreList =  new LinkedList<>();
				}
				if(horizPreList ==null){
					horizPreList =  new LinkedList<>();
				}
				
				for(int[] vertex: vertPreList){	
					if((!horizPreList.isEmpty())&&contains(horizPreList,vertex)){
						list.add(vertex);
						int area = (i - vertex[0] + 1)*(j - vertex[1] + 1);
						maxRectangle = maxRectangle < area ? area : maxRectangle;
					}else{
						if(vertex[1]==j){
							list.add(vertex);
							int area = i - vertex[0] + 1;
							maxRectangle = maxRectangle < area ? area : maxRectangle;
						}
					}
				}
			
				for(int[] vertex: horizPreList){
					if(vertPreList.isEmpty()||(!contains(vertPreList,vertex))){
						if(vertex[0]==i){
							list.add(vertex);
							int area = j - vertex[1] + 1;
							maxRectangle = maxRectangle < area ? area : maxRectangle;
						}
					}
				}	
				
				vertexMap.put(itself, list);
			}
		}
		return maxRectangle;
	}
	
	private boolean contains(List<int[]> preList,int[] vertex){
		for(int[] vertixToCheck: preList){
			if(vertixToCheck[0]==vertex[0]&&vertixToCheck[1]==vertex[1]){
				return true;
			}
		}
		return false;
	}

}
