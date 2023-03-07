package peter.liu.binarytreelevelordertraversal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Given a binary tree, return the level order traversal of its nodes' values. (ie, from left to right, level by level).

For example:
Given binary tree {3,9,20,#,#,15,7},

    3
   / \
  9  20
    /  \
   15   7

return its level order traversal as:

[
  [3],
  [9,20],
  [15,7]
]

confused what "{1,#,2,3}" means? > read more on how binary tree is serialized on OJ.
 * @author pcnsh197
 *
 */
public class Solution {
//	public List<List<Integer>> levelOrder(TreeNode root) {
//		List<List<Integer>> result = new ArrayList<>();
//		if (root != null) {
//			List<Integer> list = new ArrayList<>();
//			list.add(root.val);
//			result.add(list);
//		} else {
//			return result;
//		}
//		LinkedList<TreeNode> queue = new LinkedList<>();
//		if (root.left != null) {
//			queue.addLast(root.left);
//		}
//		if (root.right != null) {
//			queue.addLast(root.right);
//		}
//		while (!queue.isEmpty()) {
//			LinkedList<Integer> list = new LinkedList<>();
//			LinkedList<TreeNode> temp = new LinkedList<>();
//			for (TreeNode node : queue) {
//				list.addLast(node.val);
//				if (node.left != null) {
//					temp.addLast(node.left);
//				}
//				if (node.right != null) {
//					temp.addLast(node.right);
//				}
//			}
//			result.add(list);
//			queue = temp;
//		}
//		return result;
//	}
	
	public List<List<Integer>> levelOrder(TreeNode root) {
		List<List<Integer>> result = new LinkedList<>();
		traverse(root, 1, result); 
		return result;
    }
	
	private void traverse(TreeNode node, int level, List<List<Integer>> result) {
		if (node==null) {
			return;
		}
		if(result.size()<level) {
			List<Integer> levelNode= new LinkedList<>();
			levelNode.add(node.val);
			result.add(levelNode);
		}else {
			List<Integer> levelNode= result.get(level-1);
			levelNode.add(node.val);
		}
		traverse(node.left, level+1, result); 
		traverse(node.right, level+1, result); 
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
