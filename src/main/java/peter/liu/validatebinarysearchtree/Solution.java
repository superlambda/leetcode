package peter.liu.validatebinarysearchtree;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Given a binary tree, determine if it is a valid binary search tree (BST).
 * 
 * Assume a BST is defined as follows:
 * 
 * The left subtree of a node contains only nodes with keys less than the node's
 * key. The right subtree of a node contains only nodes with keys greater than
 * the node's key. Both the left and right subtrees must also be binary search
 * trees.
 * 
 * @author superlambda
 *
 */

public class Solution {
//	private Map<TreeNode,Integer> maxMap=new HashMap<>();
//	private Map<TreeNode,Integer> minMap=new HashMap<>();
//	public boolean isValidBST(TreeNode root) {
//		if(root==null||(root.left==null&&root.right==null)){
//			return true;
//		}
//		boolean isValid=true;
//		
//		if(root.left!=null){
//			isValid = isValid &&isValidBST(root.left)&&root.val>getMaxLeft(root.left);
//		}
//		if(root.right!=null){
//			isValid = isValid &&isValidBST(root.right)&&root.val<getMinRight(root.right);
//		}
//		return isValid;
//	}
//	private int getMaxLeft(TreeNode root){
//		if(maxMap.containsKey(root)){
//			return maxMap.get(root);
//		}
//		int max=root.val;
//		if(root.left!=null){
//			max=Math.max(getMaxLeft(root.left), max);
//		}
//		if(root.right!=null){
//			max=Math.max(getMaxLeft(root.right), max);
//		}
//		maxMap.put(root, max);
//		return max;
//	}
//	
//	private int getMinRight(TreeNode root){
//		if(minMap.containsKey(root)){
//			return minMap.get(root);
//		}
//		int min=root.val;
//		if(root.left!=null){
//			min=Math.min(getMinRight(root.left), min);
//		}
//		if(root.right!=null){
//			min=Math.min(getMinRight(root.right), min);
//		}
//		minMap.put(root, min);
//		return min;
//	}
	
	public boolean isValidBST(TreeNode root) {
		Stack<TreeNode> stack=new Stack<>();
		return isValid(root,stack);
    }
	
	private boolean isValid(TreeNode node,Stack<TreeNode> stack) {
		if(node==null) {
			return true;
		}
		if (!isValid(node.left,stack)) {
			return false;
		}
		if(!stack.isEmpty()&&stack.pop().val>=node.val) {
			return false;
		}
		stack.push(node);
		return isValid(node.right,stack);
	}
	
}

class TreeNode {
	int val;
	TreeNode left;
	TreeNode right;

	TreeNode(int x) {
		val = x;
	}
}
