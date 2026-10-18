package peter.liu.minstack;

import java.util.Stack;

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
	private Stack<Integer> stack = new Stack<>();
	private Stack<Integer> minumStack = new Stack<>();

	public void push(int x) {
		if (minumStack.isEmpty()||minumStack.peek()>=x) {
			minumStack.push(x);
		}
		stack.push(x);
	}

	public void pop() {
		int value = stack.pop();
		if (value == minumStack.peek()) {
			minumStack.pop();
		}
	}

	public int top() {
		return stack.peek();
	}

	public int getMin() {
		return minumStack.peek();
	}
}
