package peter.liu.maximalrectangle;

import java.util.Deque;
import java.util.LinkedList;

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
		
		if (matrix.length== 0){
			return 0;
		}
		int[] heightArray=new int[matrix[0].length];
		int maxArea=0;
		for(int i=0;i<matrix.length;i++){
			for(int j=0;j<matrix[i].length;j++){
				if(matrix[i][j]=='1'){
					heightArray[j] += 1;
				}else{
					heightArray[j] = 0;
				}
			}
			int area=maxHistogram(heightArray);
			if (area>maxArea){
				maxArea=area;
			}
		}
		
		return maxArea;
	}
	public int maxHistogram(int heights[]){
        Deque<Integer> stack = new LinkedList<Integer>();
        int maxArea = 0;
        int area = 0;
        int i;
        for(i=0; i < heights.length;){
            if(stack.isEmpty() || heights[stack.peekFirst()] <= heights[i]){
                stack.offerFirst(i++);
            }else{
                int top = stack.pollFirst();
                //if stack is empty means everything till i has to be
                //greater or equal to heights[top] so get area by
                //heights[top] * i;
                if(stack.isEmpty()){
                    area = heights[top] * i;
                }
                //if stack is not empty then everythin from i-1 to heights.peek() + 1
                //has to be greater or equal to heights[top]
                //so area = heights[top]*(i - stack.peek() - 1);
                else{
                    area = heights[top] * (i - stack.peekFirst() - 1);
                }
                if(area > maxArea){
                    maxArea = area;
                }
            }
        }
        while(!stack.isEmpty()){
            int top = stack.pollFirst();
            //if stack is empty means everything till i has to be
            //greater or equal to heights[top] so get area by
            //heights[top] * i;
            if(stack.isEmpty()){
                area = heights[top] * i;
            }
            //if stack is not empty then everything from i-1 to heights.peek() + 1
            //has to be greater or equal to heights[top]
            //so area = heights[top]*(i - stack.peek() - 1);
            else{
                area = heights[top] * (i - stack.peekFirst() - 1);
            }
        if(area > maxArea){
                maxArea = area;
            }
        }
        return maxArea;
    }

}
