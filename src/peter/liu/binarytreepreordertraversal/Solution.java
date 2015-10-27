package peter.liu.binarytreepreordertraversal;

import java.util.LinkedList;
import java.util.List;

/**
 * Given a binary tree, return the preorder traversal of its nodes' values.

For example:
Given binary tree {1,#,2,3},
   1
    \
     2
    /
   3
return [1,2,3].

Note: Recursive solution is trivial, could you do it iteratively?
 * @author liuyingjie
 *
 */
public class Solution {
	public List<Integer> preorderTraversal(TreeNode root) {
		List<Integer> result=new LinkedList<>();
		traverse(root,result);
		return result;

	}
	private void traverse(TreeNode node,List<Integer> result){
		if(node!=null){
			result.add(node.val);
			if(node.left!=null){
				traverse(node.left,result);
			}
			if(node.right!=null){
				traverse(node.right,result);
			}
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
