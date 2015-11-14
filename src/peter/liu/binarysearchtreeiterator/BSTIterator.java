package peter.liu.binarysearchtreeiterator;

import java.util.Stack;

/**
 * 
 * Implement an iterator over a binary search tree (BST). Your iterator will be
 * initialized with the root node of a BST.
 * 
 * Calling next() will return the next smallest number in the BST.
 * 
 * Note: next() and hasNext() should run in average O(1) time and uses O(h)
 * memory, where h is the height of the tree.
 * 
 * @author liuyingjie
 *
 */
public class BSTIterator {
	Stack<TreeNode> stack=new Stack<>();
	int smallestNumber=Integer.MIN_VALUE;
	public BSTIterator(TreeNode root) {
		if(root!=null){
			stack.push(root);
		}
	}

	/** @return whether we have a next smallest number */
	public boolean hasNext() {
		return !stack.isEmpty();
	}

	/** @return the next smallest number */
	public int next() {
		while(stack.peek().left!=null&&stack.peek().left.val>smallestNumber){
			stack.push(stack.peek().left);
		}
		TreeNode smallestNode=stack.pop();
		if(smallestNode.right!=null){
			stack.push(smallestNode.right);
		}
		smallestNumber=smallestNode.val;
		return smallestNumber;
	}
}

/**
 * Definition for binary tree
 */
class TreeNode {
	int val;
	TreeNode left;
	TreeNode right;

	TreeNode(int x) {
		val = x;
	}
}

/**
 * Your BSTIterator will be called like this: BSTIterator i = new
 * BSTIterator(root); while (i.hasNext()) v[f()] = i.next();
 */
