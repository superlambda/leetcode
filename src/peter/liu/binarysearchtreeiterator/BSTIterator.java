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
	//You stupid guy, you have to use a value to decide if you should push left first....
//	int smallestNumber=Integer.MIN_VALUE;
	public BSTIterator(TreeNode root) {
		pushAll(root);
	}

	/** @return whether we have a next smallest number */
	public boolean hasNext() {
		return !stack.isEmpty();
	}
	private void pushAll(TreeNode node){
		while(node!=null){
			stack.push(node);
			node=node.left;
		}
	}

	/** @return the next smallest number */
	public int next() {
		TreeNode smallestNode=stack.pop();
		pushAll(smallestNode.right);
		return smallestNode.val;
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
