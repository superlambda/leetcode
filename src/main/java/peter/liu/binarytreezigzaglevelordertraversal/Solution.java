package peter.liu.binarytreezigzaglevelordertraversal;

import java.util.LinkedList;
import java.util.List;


/**
 * Given a binary tree, return the zigzag level order traversal of its nodes' values. (ie, from left to right, then right to left for the next level and alternate between).

For example:
Given binary tree {3,9,20,#,#,15,7},
    3
   / \
  9  20
    /  \
   15   7
return its zigzag level order traversal as:
[
  [3],
  [20,9],
  [15,7]
]
confused what "{1,#,2,3}" means? > read more on how binary tree is serialized on OJ.
 * @author superlambda
 *
 */
public class Solution {
//	public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
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
//		int i=1;
//		while (!queue.isEmpty()) {
//			LinkedList<Integer> list = new LinkedList<>();
//			LinkedList<TreeNode> temp = new LinkedList<>();
//			i++;
//			for (TreeNode node : queue) {
//				if(i%2==0){
//					list.addFirst(node.val);
//				}else{
//					list.addLast(node.val);
//				}
//				
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
//    }
	
	public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
		List<List<Integer>> result =new LinkedList<>();
		zigaTraverse(result,root, 1);
		return result;
	}
	
	private void zigaTraverse(List<List<Integer>> result,TreeNode root, int level){
		if(root==null){
			return;
		}
		if(result.size()<level){
			result.add(new LinkedList<Integer>());
		}
		List<Integer> levelList=result.get(level-1);
		if(level%2==0){
			levelList.add(0, root.val);
		}else{
			levelList.add(root.val);
		}
		if(root.left!=null){
			zigaTraverse(result,root.left, level+1);
		}
		if(root.right!=null){
			zigaTraverse(result,root.right, level+1);
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
