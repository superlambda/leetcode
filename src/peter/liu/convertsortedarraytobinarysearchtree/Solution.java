package peter.liu.convertsortedarraytobinarysearchtree;

/**
 * Given an array where elements are sorted in ascending order, convert it to a
 * height balanced BST.
 * 
 * @author superlambda
 *
 */
public class Solution {
	public TreeNode sortedArrayToBST(int[] nums) {
		if (nums == null) {
			return null;
		}
		return constructTreeFromArray(nums, 0, nums.length - 1);
	}
	
	private TreeNode constructTreeFromArray(int[] nums, int left, int right) {
		if(left>right) {
			return null;
		}
		int middlePoint= left + (right-left)/2;
		TreeNode node=new TreeNode(nums[middlePoint]);
		node.left=constructTreeFromArray(nums, left, middlePoint-1); 
		node.right=constructTreeFromArray(nums, middlePoint+1, right); 
		return node;
	}

//	private TreeNode getChildTree(int[] nums, int from, int to) {
//		int middle = (to - from) / 2 + from;
//		if(middle<from||middle>to){
//			return null;
//		}
//		TreeNode node = new TreeNode(nums[middle]);
//		if (middle > from) {
//			node.left = getChildTree(nums, from, middle - 1);
//		}
//		if (middle < to) {
//			node.right = getChildTree(nums, middle+1, to);
//		}
//		return node;
//	}
}

class TreeNode {
	int val;
	TreeNode left;
	TreeNode right;

	TreeNode(int x) {
		val = x;
	}
}