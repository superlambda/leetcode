package peter.liu.symmetrictree;

import java.util.Stack;


/**
 * Given a binary tree, check whether it is a mirror of itself (ie, symmetric around its center).

For example, this binary tree is symmetric:

    1
   / \
  2   2
 / \ / \
3  4 4  3

But the following is not:

    1
   / \
  2   2
   \   \
   3    3

Note:
Bonus points if you could solve it both recursively and iteratively.

confused what "{1,#,2,3}" means? > read more on how binary tree is serialized on OJ.
 * @author pcnsh197
 *
 */
public class Solution {
	/**
	 * recursively
	 * @param root
	 * @return
	 */
//	public boolean isSymmetric(TreeNode root) {
//		return root != null ? test(root.left, root.right) : true;
//	}
	
	/**
	 * iteratively
	 * @param root
	 * @return
	 */
	public boolean isSymmetric(TreeNode root) {
		if (root == null) {
			return true;
		}
		Stack<TreeNode> left = new Stack<>();
		Stack<TreeNode> right = new Stack<>();
		if (root.left != null) {
			left.push(root.left);
		}
		if (root.right != null) {
			right.push(root.right);
		}

		while (!left.isEmpty() && !right.isEmpty()) {
			TreeNode leftNode = left.pop();
			TreeNode rightNode = right.pop();
			if (leftNode.val != rightNode.val) {
				return false;
			} else {
				if (leftNode.right != null && rightNode.left != null) {
					left.push(leftNode.right);
					right.push(rightNode.left);
				} else if (leftNode.right != null || rightNode.left != null) {
					return false;
				}
				if (leftNode.left != null && rightNode.right != null) {
					left.push(leftNode.left);
					right.push(rightNode.right);
				} else if (leftNode.left != null || rightNode.right != null) {
					return false;
				}
			}
		}
		if (!left.isEmpty() || !right.isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * recursively
	 * 
	 * @param p
	 * @param q
	 * @return
	 */
	private boolean test(TreeNode p, TreeNode q) {
		if (p == null && q == null) {
			return true;
		}
		if (p == null && q != null || p != null && q == null) {
			return false;
		}
		return p.val == q.val && test(p.left, q.right) && test(p.right, q.left);
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