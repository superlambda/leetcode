package peter.liu.binarytreemaximumpathsum;

/**
 * Given a binary tree, find the maximum path sum.
 * 
 * For this problem, a path is defined as any sequence of nodes from some
 * starting node to any node in the tree along the parent-child connections. The
 * path must contain at least one node and does not need to go through the root.
 * 
 * For example: Given the below binary tree,
 * 
 *     1 
 *    / \ 
 *   2   3 Return 6.
 * 
 * @author liuyingjie
 *
 */
public class Solution {
	
	int maxValue = Integer.MIN_VALUE;;
    
	/**
	 * A path from start to end, goes up on the tree for 0 or more steps, then
	 * goes down for 0 or more steps. Once it goes down, it can't go up. Each
	 * path has a highest node, which is also the lowest common ancestor of all
	 * other nodes on the path. A recursive method maxPathDown(TreeNode node)
	 * (1) computes the maximum path sum with highest node is the input node,
	 * update maximum if necessary (2) returns the maximum sum of the path that
	 * can be extended to input node's parent.
	 * 
	 * @param root
	 * @return
	 */
    public int maxPathSum(TreeNode root) {
        maxPathDown(root);
        return maxValue;
    }
    
    private int maxPathDown(TreeNode node) {
        if (node == null) return 0;
        int left = Math.max(0, maxPathDown(node.left));
        int right = Math.max(0, maxPathDown(node.right));
        maxValue = Math.max(maxValue, left + right + node.val);
        return Math.max(left, right) + node.val;
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