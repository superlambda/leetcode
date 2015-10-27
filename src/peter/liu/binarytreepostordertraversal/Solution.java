package peter.liu.binarytreepostordertraversal;

import java.util.LinkedList;
import java.util.List;


/**
 * 
 * Given a binary tree, return the postorder traversal of its nodes' values.

For example:
Given binary tree {1,#,2,3},
   1
    \
     2
    /
   3
return [3,2,1].

Note: Recursive solution is trivial, could you do it iteratively?
 * @author liuyingjie
 *
 */
public class Solution {
	public List<Integer> postorderTraversal(TreeNode root) {
		List<Integer> result=new LinkedList<>();
		traverse(root,result);
		return result;
	}

	private void traverse(TreeNode node, List<Integer> result) {
		if (node != null) {
			if (node.left != null) {
				traverse(node.left, result);
			}
			if (node.right != null) {
				traverse(node.right, result);
			}
			result.add(node.val);
		}
	}
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