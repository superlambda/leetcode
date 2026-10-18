package peter.liu.largestrectangleinhistogram;

import java.util.Stack;

/**
 * Given n non-negative integers representing the histogram's bar height where
 * the width of each bar is 1, find the area of largest rectangle in the
 * histogram.
 * 
 * For example, Given heights = [2,1,5,6,2,3], return 10.
 * 
 * @author liuyingjie
 *
 */
public class Solution {
	private int largestArea = 0;

	public int largestRectangleArea(int[] heights) {
		Stack<Integer> heightStack = new Stack<>();
		Stack<Integer> minimumHeightStack = new Stack<>();
		int minimumHeight = Integer.MAX_VALUE;
		for (int i = 0; i < heights.length; i++) {
			if (heights[i] == 0) {
				if(!minimumHeightStack.isEmpty()){
					trace(minimumHeightStack, heightStack);
				}
				heightStack = new Stack<>();
				minimumHeightStack=new Stack<>();
				minimumHeight = Integer.MAX_VALUE;
			} else {
				minimumHeight = Math.min(minimumHeight, heights[i]);
				heightStack.push(heights[i]);
				minimumHeightStack.push(minimumHeight);
				largestArea = Math.max(largestArea, minimumHeight * heightStack.size());
			}
		}
		if (!heightStack.isEmpty()) {
			trace(minimumHeightStack, heightStack);
		}
		return largestArea;

	}

	private void trace(Stack<Integer> minimumHeightStack, Stack<Integer> heightStack) {

		
		Stack<Integer> popedStack = new Stack<>();
		Stack<Integer> popedMinimumHeightStack = new Stack<>();
		//split
		int checkValue = minimumHeightStack.peek(); 
		int minimumHeight=Integer.MAX_VALUE;
		while ((!heightStack.isEmpty()) && heightStack.peek() > checkValue) {
			minimumHeight = Math.min(minimumHeight, heightStack.peek());
			popedStack.push(heightStack.pop());
			popedMinimumHeightStack.push(minimumHeight);
			minimumHeightStack.pop();
		}

		if (!popedStack.isEmpty()) {
			largestArea = Math.max(largestArea, minimumHeight * popedStack.size());
			while((!popedMinimumHeightStack.isEmpty())&&popedMinimumHeightStack.peek().equals(popedStack.peek())){
				popedMinimumHeightStack.pop();
				popedStack.pop();
				if(!popedStack.isEmpty()){
					largestArea = Math.max(largestArea, popedMinimumHeightStack.peek() * popedMinimumHeightStack.size());
				}
				
			}
			
			if(!popedStack.isEmpty()){
				trace(popedMinimumHeightStack, popedStack);
			}
			
		}
		//left value
		if (!heightStack.isEmpty()) {
			heightStack.pop();
			minimumHeightStack.pop();
			if (!heightStack.isEmpty()) {
				if (heightStack.size() == 1) {
					largestArea = Math.max(largestArea, heightStack.pop());
				} else {
					largestArea = Math.max(largestArea, minimumHeightStack.peek() * heightStack.size());
					while((!minimumHeightStack.isEmpty())&&minimumHeightStack.peek().equals(heightStack.peek())){
						minimumHeightStack.pop();
						heightStack.pop();
						if(!heightStack.isEmpty()){
							largestArea = Math.max(largestArea, minimumHeightStack.peek() * minimumHeightStack.size());
						}
						
					}
					if(!minimumHeightStack.isEmpty()){
						trace(minimumHeightStack, heightStack);
					}
				}
			}
		}
	}

}
