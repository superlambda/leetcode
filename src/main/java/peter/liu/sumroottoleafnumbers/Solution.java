package peter.liu.sumroottoleafnumbers;

/**
 * Given a binary tree containing digits from 0-9 only, each root-to-leaf path could represent a number.

An example is the root-to-leaf path 1->2->3 which represents the number 123.

Find the total sum of all root-to-leaf numbers.

For example,

    1
   / \
  2   3
The root-to-leaf path 1->2 represents the number 12.
The root-to-leaf path 1->3 represents the number 13.

Return the sum = 12 + 13 = 25.
 * @author superlambda
 *
 */
public class Solution {
	int sum = 0;

	public int sumNumbers(TreeNode root) {
		traceTree(root, new StringBuffer());
		return sum;
	}

	private void traceTree(TreeNode root, StringBuffer numberString) {
		if (root != null) {
			numberString.append(root.val);
			if (root.left == null && root.right == null) {
				sum += Integer.valueOf(numberString.toString());
			} else {
				if (root.left != null) {
					StringBuffer newNS = new StringBuffer(numberString);
					traceTree(root.left, newNS);
				}
				if (root.right != null) {
					StringBuffer newNS = new StringBuffer(numberString);
					traceTree(root.right, newNS);
				}
			}
		}
	}
}

/**
 * Definition for a binary tree node.
 */
class TreeNode {
	int val;
	TreeNode left;
	TreeNode right;

	TreeNode(int x) {
		val = x;
	}
}
