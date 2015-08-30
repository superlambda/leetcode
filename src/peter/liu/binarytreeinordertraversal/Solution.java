package peter.liu.binarytreeinordertraversal;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * Given a binary tree, return the inorder traversal of its nodes' values.
 *
 * For example: Given binary tree {1,#,2,3}, 1 \ 2 / 3 return [1,3,2].
 *
 * Note: Recursive solution is trivial, could you do it iteratively?
 *
 * confused what "{1,#,2,3}" means? > read more on how binary tree is serialized
 * on OJ.
 * 
 * @author superlambda
 *
 */
public class Solution {
	public List<Integer> inorderTraversalUsing(TreeNode root) {
		List<Integer> result = new LinkedList<>();
		Stack<TreeNode> stack = new Stack<>();
		if (root != null) {
			stack.push(root);
		}
		Set<TreeNode> set = new HashSet<>();
		while (!stack.isEmpty()) {
			TreeNode node = stack.pop();
			if (node.left == null && node.right == null) {
				result.add(node.val);
			} else if (node.left != null && node.right != null) {
				if (!set.contains(node)) {
					stack.push(node.right);
					stack.push(node);
					stack.push(node.left);
					set.add(node);
				} else {
					result.add(node.val);
				}

			} else if (node.left != null && node.right == null) {
				if (!set.contains(node)) {
					stack.push(node);
					stack.push(node.left);
					set.add(node);
				} else {
					result.add(node.val);
				}

			} else if (node.left == null && node.right != null) {
				result.add(node.val);
				stack.push(node.right);
			}
		}

		return result;
	}

	public static void main(String[] args) {
		// TreeNode root=new TreeNode(1);
		// root.right=new TreeNode(2);
		// root.right.left=new TreeNode(3);
		// List<Integer> result=new Solution().inorderTraversalUsing(root);
		// for(Integer i: result){
		// System.out.println(i);
		// }

		TreeNode root = new TreeNode(4);
		root.left = new TreeNode(1);
		root.left.left = new TreeNode(2);
		root.left.left.left = new TreeNode(3);
		List<Integer> result = new Solution().inorderTraversalUsing(root);
		for (Integer i : result) {
			System.out.println(i);
		}
	}

	// public List<Integer> inorderTraversalUsingRecurisive(TreeNode root) {
	// List<Integer> result = new LinkedList<>();
	// if (root != null) {
	// getNode(result, root);
	// }
	//
	// return result;
	// }
	//
	// private void getNode(List<Integer> result, TreeNode root) {
	// if (root.left != null) {
	// getNode(result, root.left);
	// }
	// result.add(root.val);
	// if (root.right != null) {
	// getNode(result, root.right);
	// }
	// }
}

/**
 * Definition for a binary tree node.
 */
class TreeNode {
	int val;
	TreeNode left;
	TreeNode right;

	TreeNode(int x) {
		val = x;
	}
}
