package peter.liu.simplifypath;

import java.util.LinkedList;
import java.util.Stack;

/**
 * Given an absolute path for a file (Unix-style), simplify it.
 * 
 * For example, path = "/home/", => "/home" path = "/a/./b/../../c/", => "/c"
 * 
 * @author superlambda
 *
 */
public class Solution {

	public String simplifyPath(String path) {
		Stack<Character> stack = new Stack<>();
		char[] charArray = path.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			char character = charArray[i];
			if (character == '.') {
				if (stack.isEmpty() || stack.lastElement() != '.') {
					stack.push(character);
				} else {
					if (i < charArray.length - 1 && charArray[i + 1] == '.') {
						stack.push('.');
						continue;
					}
					if (i < charArray.length - 1 && charArray[i + 1] == '/') {
						if (stack.size() > 0 && stack.lastElement() == '/')
							continue;
					}

					stack.pop();
					if (stack.size() == 1 && stack.lastElement() == '/') {
						if ((i < charArray.length - 1 && charArray[i + 1] == '/')
								|| i == charArray.length - 1) {
							continue;
						}
					}
					if (stack.size() > 1 && stack.lastElement() == '/') {
						stack.pop();
						while (stack.size() > 1 && stack.lastElement() != '/') {
							stack.pop();
						}
					} else {
						stack.push('.');
						stack.push('.');
					}
				}
			} else if (character == '/') {
				if (stack.size() > 0 && stack.lastElement() == '/') {
					continue;
				}
				if (stack.size() > 1 && stack.lastElement() == '.') {
					stack.pop();
					if (stack.lastElement() == '/') {
						continue;
					} else {
						stack.push('.');
						stack.push(character);
					}

				} else {
					stack.push(character);
				}
			} else {
				stack.push(character);
			}
		}
		LinkedList<Character> queue = new LinkedList<>();
		while (stack.size() > 0) {
			queue.addFirst(stack.pop());
		}
		while (queue.size() > 1 && queue.getLast().equals(new Character('/'))) {
			queue.removeLast();
		}

		int numberRemoved = 0;
		while (queue.size() > 1 && queue.getLast().equals(new Character('.'))) {
			queue.removeLast();
			numberRemoved++;

		}
		if (numberRemoved <= 2) {
			while (queue.size() > 1
					&& queue.getLast().equals(new Character('/'))) {
				queue.removeLast();
			}
		}
		StringBuffer sb = new StringBuffer();
		for (Character ch : queue) {
			sb.append(ch);
		}

		if (numberRemoved > 2) {
			while (numberRemoved > 0) {
				sb.append('.');
				numberRemoved--;
			}
		}

		return sb.toString();

	}
}
