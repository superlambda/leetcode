package peter.liu.balancedbinarytree;



/**
 * Given a binary tree, determine if it is height-balanced.
 * 
 * For this problem, a height-balanced binary tree is defined as a binary tree
 * in which the depth of the two subtrees of every node never differ by more
 * than 1.
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	public boolean isBalanced(TreeNode root) {
		if (root == null) {
			return true;
		}
		return isBalanced(root.left)
				&& isBalanced(root.right)
				&& Math.abs(getHeightOfNode(root.left)
						- getHeightOfNode(root.right)) <= 1;
	}

	private int getHeightOfNode(TreeNode iterator) {
		if (iterator == null) {
			return 0;
		}
		int leftHeight = 1 + getHeightOfNode(iterator.left);
		int rightHeight = 1 + getHeightOfNode(iterator.right);
		return leftHeight > rightHeight ? leftHeight : rightHeight;
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