package peter.liu.pathsumII;

import java.util.LinkedList;
import java.util.List;

/**
 * Given a binary tree and a sum, find all root-to-leaf paths where each path's
 * sum equals the given sum.
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	public List<List<Integer>> pathSum(TreeNode root, int sum) {

	}

	private List<List<Integer>> preOrderTraverse(TreeNode iterator,
			int pathSum, int sum) {
		if (iterator == null) {
			return null;
		}
		List<Integer> list = new LinkedList<>();
		list.add(iterator.val);
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
