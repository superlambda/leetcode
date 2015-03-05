package peter.liu.binarytreelevelordertraversalII;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Given a binary tree, return the bottom-up level order traversal of its nodes' values. (ie, from left to right, level by level from leaf to root).

For example:
Given binary tree {3,9,20,#,#,15,7},

    3
   / \
  9  20
    /  \
   15   7

return its bottom-up level order traversal as:

[
  [15,7],
  [9,20],
  [3]
]

 * @author pcnsh197
 *
 */
public class Solution {
	public List<List<Integer>> levelOrderBottom(TreeNode root) {
		LinkedList<List<Integer>> result = new LinkedList<>();
		if (root != null) {
			List<Integer> list = new ArrayList<>();
			list.add(root.val);
			result.add(list);
		} else {
			return result;
		}
		LinkedList<TreeNode> queue = new LinkedList<>();
		if (root.left != null) {
			queue.addLast(root.left);
		}
		if (root.right != null) {
			queue.addLast(root.right);
		}
		while (!queue.isEmpty()) {
			LinkedList<Integer> list = new LinkedList<>();
			LinkedList<TreeNode> temp = new LinkedList<>();
			for (TreeNode node : queue) {
				list.addLast(node.val);
				if (node.left != null) {
					temp.addLast(node.left);
				}
				if (node.right != null) {
					temp.addLast(node.right);
				}
			}
			result.addFirst(list);
			queue = temp;
		}
		return result;
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
