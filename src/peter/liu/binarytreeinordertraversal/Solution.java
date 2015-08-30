package peter.liu.binarytreeinordertraversal;

import java.util.LinkedList;
import java.util.List;


/**
 * Given a binary tree, return the inorder traversal of its nodes' values.
 *
 *  For example:
 *	Given binary tree {1,#,2,3},
 *	   1
 *	    \
 *	     2
 *	    /
 *	   3
 *	return [1,3,2].
 *	
 *	Note: Recursive solution is trivial, could you do it iteratively?
 *	
 * 	confused what "{1,#,2,3}" means? > read more on how binary tree is serialized on OJ.
 * @author superlambda
 *
 */
public class Solution {
	public List<Integer> inorderTraversalUsing(TreeNode root) {
		List<Integer> result = new LinkedList<>();
		
		return result;
	}
	
//	public List<Integer> inorderTraversalUsingRecurisive(TreeNode root) {
//		List<Integer> result = new LinkedList<>();
//		if (root != null) {
//			getNode(result, root);
//		}
//
//		return result;
//	}
//
//	private void getNode(List<Integer> result, TreeNode root) {
//		if (root.left != null) {
//			getNode(result, root.left);
//		}
//		result.add(root.val);
//		if (root.right != null) {
//			getNode(result, root.right);
//		}
//	}
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
