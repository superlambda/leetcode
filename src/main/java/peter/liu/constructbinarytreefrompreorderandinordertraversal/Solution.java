package peter.liu.constructbinarytreefrompreorderandinordertraversal;


/**
 * Given preorder and inorder traversal of a tree, construct the binary tree.
 * 
 * Note: You may assume that duplicates do not exist in the tree.
 * 
 * @author superlambda
 *
 */
public class Solution {
	private int indexOfPreOrder = 0;

	public TreeNode buildTree(int[] preorder, int[] inorder) {
		if (preorder == null || preorder.length == 0) {
			return null;
		}
		return getChildTree(preorder, inorder, 0, inorder.length);
	}

	private TreeNode getChildTree(int[] preorder, int[] inorder, int from,
			int to) {
		TreeNode node = new TreeNode(preorder[indexOfPreOrder]);
		int indexOfNode = getIndexOfNode(node.val, inorder, from, to);
		if (indexOfNode != -1) {
			if (indexOfNode > from) {
				++indexOfPreOrder;
				node.left = getChildTree(preorder, inorder, from, indexOfNode);
			}
			if (indexOfNode < to-1) {
				++indexOfPreOrder;
				node.right = getChildTree(preorder, inorder, indexOfNode + 1,
						to);
			}
		}
		return node;
	}

	private int getIndexOfNode(int val, int[] inorder, int from, int to) {
		for (int i = from; i < to; i++) {
			if (inorder[i] == val) {
				return i;
			}
		}
		return -1;
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
