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
		List<List<Integer>> pathList = new LinkedList<>();
		if (root != null) {
			preOrderTraverse(root, 0, sum, pathList, new LinkedList<>());
		}
		return pathList;
	}

	private void preOrderTraverse(TreeNode iterator, int pathSum, int sum,
			List<List<Integer>> pathList, List<Integer> path) {
		pathSum += iterator.val;
		path.add(iterator.val);
		if (iterator.left == null && iterator.right == null) {
			if (pathSum == sum) {
				pathList.add(path);
			}
			return;
		}
		if (iterator.left != null && iterator.right != null) {
			preOrderTraverse(iterator.left, pathSum, sum, pathList,
					new LinkedList<Integer>(path));
			preOrderTraverse(iterator.right, pathSum, sum, pathList,
					new LinkedList<Integer>(path));
		} else if (iterator.left != null) {
			preOrderTraverse(iterator.left, pathSum, sum, pathList, path);
		} else if (iterator.right != null) {
			preOrderTraverse(iterator.right, pathSum, sum, pathList, path);
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
