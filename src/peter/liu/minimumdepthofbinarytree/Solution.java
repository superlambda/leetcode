package peter.liu.minimumdepthofbinarytree;

import java.util.LinkedList;
import java.util.List;

/**
 * Given a binary tree, find its minimum depth.
 * 
 * The minimum depth is the number of nodes along the shortest path from the
 * root node down to the nearest leaf node.
 * 
 * @author pcnsh197
 *
 */
public class Solution {
//	public int minDepth(TreeNode root) {
//		List<List<Integer>> pathList = new LinkedList<>();
//		if (root != null) {
//			preOrderTraverse(root, pathList, new LinkedList<>());
//		}
//		int minLength = 0;
//		for (List<Integer> path : pathList) {
//			if (minLength == 0) {
//				minLength = path.size();
//			} else if (minLength > path.size()) {
//				minLength = path.size();
//			}
//		}
//		return minLength;
//	}
//
//	private void preOrderTraverse(TreeNode iterator,
//			List<List<Integer>> pathList, List<Integer> path) {
//		path.add(iterator.val);
//		if (iterator.left == null && iterator.right == null) {
//			pathList.add(path);
//		}
//		if (iterator.left != null && iterator.right != null) {
//			preOrderTraverse(iterator.left, pathList, new LinkedList<Integer>(
//					path));
//			preOrderTraverse(iterator.right, pathList, new LinkedList<Integer>(
//					path));
//		} else if (iterator.left != null) {
//			preOrderTraverse(iterator.left, pathList, path);
//		} else if (iterator.right != null) {
//			preOrderTraverse(iterator.right, pathList, path);
//		}
//	}
	
	
	public int minDepth(TreeNode root) {
		List<Integer> pathList = new LinkedList<>();
		if (root != null) {
			preOrderTraverse(root, pathList, 0);
		}
		int minLength = pathList.size()>0?Integer.MAX_VALUE:0;
		for (int depth : pathList) {
			if (minLength > depth) {
				minLength = depth;
			}
		}
		return minLength;
	}

	private void preOrderTraverse(TreeNode iterator, List<Integer> pathList,
			int depth) {
		depth += 1;
		if (iterator.left == null && iterator.right == null) {
			pathList.add(depth);
		}
		if (iterator.left != null) {
			preOrderTraverse(iterator.left, pathList, depth);
		}
		if (iterator.right != null) {
			preOrderTraverse(iterator.right, pathList, depth);
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