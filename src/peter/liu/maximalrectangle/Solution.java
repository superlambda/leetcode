package peter.liu.maximalrectangle;

import java.util.ArrayList;
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
		Map<String, List<int[]>> vertexMap = new HashMap<String, List<int[]>>();
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if(i-2>=0){
					vertexMap.remove((i - 2) + "," + j);
				}
				if (matrix[i][j] == '0') {
					continue;
				}

				if (i != 0 && j == 0||(j>0&&matrix[i][j-1]=='0')) {
					if (i>0&&matrix[i - 1][j] == '1') {
						List<int[]> vertPreList = vertexMap.get((i - 1) + "," + j);
						List<int[]> list = new ArrayList<>();
						for(int k=0;k<vertPreList.size();k++){
							int[] vertex = vertPreList.get(k);
							if(vertex[1]==j){
								list.add(vertex);
								int area = i - vertex[0] + 1;
								maxRectangle = maxRectangle < area ? area : maxRectangle;
							}
						}
					
						vertexMap.put(i + "," + j, list);
					}

				} else if ((i == 0 && j != 0) || (i > 0 && matrix[i - 1][j] == '0')) {
					if (j>0&&matrix[i][j - 1] == '1') {
						List<int[]> horizPreList = vertexMap.get((i) + "," + (j - 1));
						List<int[]> list = new ArrayList<>();
						
						for(int k=0;k<horizPreList.size();k++){
							int[] vertex = horizPreList.get(k);
							if(vertex[0]==i){
								list.add(vertex);
								int area = j - vertex[1] + 1;
								maxRectangle = maxRectangle < area ? area : maxRectangle;
							}
						}
						
						vertexMap.put(i + "," + j, list);
					}

				}else if(i>0&&j>0&&matrix[i-1][j]=='1'&&matrix[i][j-1]=='1'){
					
					List<int[]> vertPreList = vertexMap.get((i - 1) + "," + j);
					List<int[]> horizPreList = vertexMap.get((i) + "," + (j - 1));
					
					List<int[]> list = new ArrayList<>();
					for(int k=0;k<vertPreList.size();k++){
						int[] vertex = vertPreList.get(k);
						if(contains(horizPreList,vertex)){
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
					for(int k=0;k<horizPreList.size();k++){
						int[] vertex = horizPreList.get(k);
						if(!contains(vertPreList,vertex)){
							if(vertex[0]==i){
								list.add(vertex);
								int area = j - vertex[1] + 1;
								maxRectangle = maxRectangle < area ? area : maxRectangle;
							}
						}
					}
					vertexMap.put(i + "," + j, list);
				}
				
				List<int[]> list = vertexMap.get(i + "," + j);
				if(list ==null){
					list= new ArrayList<>();
				}
				int[] vertexitSelf ={i,j};
				list.add(vertexitSelf);
				maxRectangle = maxRectangle < 1? 1:maxRectangle;
				vertexMap.put(i+","+j, list);
			}
		}
		return maxRectangle;
	}
	
	private boolean contains(List<int[]> preList,int[] vertex){
		for(int i = 0;i<preList.size();i++){
			int[] vertixToCheck=preList.get(i);
			if(vertixToCheck[0]==vertex[0]&&vertixToCheck[1]==vertex[1]){
				return true;
			}
		}
		return false;
	}
	
	
}
