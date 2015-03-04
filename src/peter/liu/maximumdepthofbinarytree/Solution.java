package peter.liu.maximumdepthofbinarytree;

/**
 * Given a binary tree, find its maximum depth.
 * 
 * The maximum depth is the number of nodes along the longest path from the root
 * node down to the farthest leaf node.
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	public int maxDepth(TreeNode root) {
		if(root==null){
			return 0;
		}else{
			int maxLeft=maxDepth(root.left);
			int maxRight=maxDepth(root.right);
			return 1+Math.max(maxLeft, maxRight);
		}
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
