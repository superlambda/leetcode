package peter.liu.minstack;

import java.util.LinkedList;

/**
 * Design a stack that supports push, pop, top, and retrieving the minimum
 * element in constant time.
 * 
 * push(x) -- Push element x onto stack. pop() -- Removes the element on top of
 * the stack. top() -- Get the top element. getMin() -- Retrieve the minimum
 * element in the stack.
 * 
 * @author pcnsh197
 *
 */
public class MinStack {
	private LinkedList<Integer> stack = new LinkedList<>();
	private int min = Integer.MAX_VALUE;

	public void push(int x) {
		if (min > x) {
			min = x;
		}
		stack.addFirst(x);
	}

	public void pop() {
		int value = stack.removeFirst();
		if (value == min) {
			min = Integer.MAX_VALUE;
			for (int val : stack) {
				if (min > val) {
					min = val;
				}
			}
		}
	}

	public int top() {
		return stack.getFirst();
	}

	public int getMin() {
		return min;
	}
}
