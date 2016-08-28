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
		int minimumHeight = Integer.MAX_VALUE;
		for (int i = 0; i < heights.length; i++) {
			if (heights[i] == 0) {
				trace(0, heightStack);
				heightStack = new Stack<>();
				minimumHeight = Integer.MAX_VALUE;
			} else {
				minimumHeight = Math.min(minimumHeight, heights[i]);
				heightStack.push(heights[i]);
				largestArea = Math.max(largestArea, minimumHeight * heightStack.size());
			}
		}
		if (!heightStack.isEmpty()) {
			trace(minimumHeight, heightStack);
		}
		return largestArea;

	}

	private void trace(int checkValue, Stack<Integer> heightStack) {
		if (heightStack.size() == 1) {
			largestArea = Math.max(largestArea, heightStack.pop());
		} else {
			int minimumHeight = Integer.MAX_VALUE;
			Stack<Integer> popedStack = new Stack<>();
			while ((!heightStack.isEmpty()) && heightStack.peek() > checkValue) {
				minimumHeight = Math.min(minimumHeight, heightStack.peek());
				popedStack.push(heightStack.pop());
			}

			if (!popedStack.isEmpty()) {
				largestArea = Math.max(largestArea, minimumHeight * popedStack.size());
				trace(minimumHeight, popedStack);
			}

			if (!heightStack.isEmpty()) {
				heightStack.pop();
				if (!heightStack.isEmpty()) {
					int minimumValue = getMinimumValue(heightStack);
					largestArea = Math.max(largestArea, minimumValue * heightStack.size());
					trace(minimumValue, heightStack);
				}
			}
		}

	}

	private int getMinimumValue(Stack<Integer> heightStack) {
		int minimumValue = Integer.MAX_VALUE;
		for (int i = 0; i < heightStack.size(); i++) {
			minimumValue = Math.min(minimumValue, heightStack.get(i));
		}
		return minimumValue;
	}

}
