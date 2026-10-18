package peter.liu.constructbinarytreefrominorderandpostordertraversal;



/**
 * Given inorder and postorder traversal of a tree, construct the binary tree.
 * 
 * Note: You may assume that duplicates do not exist in the tree.
 * 
 * @author superlambda
 *
 */
public class Solution {
	private int indexOfPostOrder = 0;
	public TreeNode buildTree(int[] inorder, int[] postorder) {
		if (postorder == null || postorder.length == 0) {
			return null;
		}
		indexOfPostOrder=postorder.length-1;
		return getChildTree(postorder, inorder, 0, inorder.length);
    }
	
	private TreeNode getChildTree(int[] postorder, int[] inorder, int from,
			int to) {
		TreeNode node = new TreeNode(postorder[indexOfPostOrder]);
		int indexOfNode = getIndexOfNode(node.val, inorder, from, to);
		if (indexOfNode != -1) {
			if (indexOfNode < to-1) {
				--indexOfPostOrder;
				node.right = getChildTree(postorder, inorder, indexOfNode + 1,
						to);
			}
			if (indexOfNode > from) {
				--indexOfPostOrder;
				node.left = getChildTree(postorder, inorder, from, indexOfNode);
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
