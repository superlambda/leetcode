package peter.liu.constructbinarytreefrompreorderandinordertraversal;

import java.util.Arrays;

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
		return getChildTree(preorder, inorder);
	}

	private TreeNode getChildTree(int[] preorder, int[] inorder) {
		TreeNode node = new TreeNode(preorder[indexOfPreOrder]);
		int indexOfNode = getIndexOfNode(node.val, inorder);
		if (indexOfNode != -1) {
			int[] leftInorder = Arrays.copyOfRange(inorder, 0, indexOfNode);
			if (leftInorder != null && leftInorder.length > 0) {
				++indexOfPreOrder;
				node.left = getChildTree(preorder, leftInorder);
			}

			int[] rightInorder = Arrays.copyOfRange(inorder, indexOfNode + 1,
					inorder.length);
			if (rightInorder != null && rightInorder.length > 0) {
				++indexOfPreOrder;
				node.right = getChildTree(preorder, rightInorder);
			}
		}
		return node;
	}

	private int getIndexOfNode(int val, int[] inorder) {
		for (int i = 0; i < inorder.length; i++) {
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
