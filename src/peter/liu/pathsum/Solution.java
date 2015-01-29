package peter.liu.pathsum;

/**
 * Given a binary tree and a sum, determine if the tree has a root-to-leaf path
 * such that adding up all the values along the path equals the given sum.
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	public boolean hasPathSum(TreeNode root, int sum) {
		int pathSum = 0;
		return preOrderTraverse(root, pathSum, sum);
	}

	private boolean preOrderTraverse(TreeNode iterator, int pathSum, int sum) {
		if (iterator == null) {
			return false;
		}
		pathSum += iterator.val;
		if (pathSum == sum && iterator.left == null && iterator.right == null) {
			return true;
		}
		if (preOrderTraverse(iterator.left, pathSum, sum)) {
			return true;
		} else {
			return preOrderTraverse(iterator.right, pathSum, sum);
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
