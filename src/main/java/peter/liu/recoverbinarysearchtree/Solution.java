package peter.liu.recoverbinarysearchtree;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Two elements of a binary search tree (BST) are swapped by mistake.
 * 
 * Recover the tree without changing its structure.
 * 
 * Note: A solution using O(n) space is pretty straight forward. Could you
 * devise a constant space solution?
 * 
 * @author liuyingjie
 * 
 * 
 * 
 *                 0
 *             o       0
 *              
 *          o     0  0     0       
 *
 */
public class Solution {
	public void recoverTree(TreeNode root) {
		List<TreeNode> list=new ArrayList<>();
		trace(root,list);
		//A slowest way is trying to sort the list,
		TreeNode firstElement = null;
		TreeNode secondElement = null;
		/*
		 * The reason for this initialization is to avoid null pointer exception
		 * in the first comparison when prevElement has not been initialized
		 */
	    TreeNode prevElement = new TreeNode(Integer.MIN_VALUE);
	    
	    for(int i=0;i<list.size();i++){
	    	if (firstElement == null && prevElement.val >= list.get(i).val) {
	            firstElement = prevElement;
	        }
	        // If first element is found, assign the second element to the root (refer to 2 in the example above)
	        if (firstElement != null && prevElement.val >= list.get(i).val) {
	            secondElement = list.get(i);
	        }        
	        prevElement = list.get(i);
	    }
	    
	    int temp = firstElement.val;
        firstElement.val = secondElement.val;
        secondElement.val = temp;
		
	}
	private void trace(TreeNode node,List<TreeNode> list){
		if(node==null){
			return;
		}
		trace(node.left,list);
		list.add(node);
		trace(node.right,list);
	}
	
//	TreeNode firstElement = null;
//	TreeNode secondElement = null;
//	// The reason for this initialization is to avoid null pointer exception in
//	// the first comparison when prevElement has not been initialized
//	TreeNode prevElement = new TreeNode(Integer.MIN_VALUE);
//
//	public void recoverTree(TreeNode root) {
//
//		// In order traversal to find the two elements
//		traverse(root);
//
//		// Swap the values of the two nodes
//		int temp = firstElement.val;
//		firstElement.val = secondElement.val;
//		secondElement.val = temp;
//	}
//
//	private void traverse(TreeNode root) {
//
//		if (root == null){
//			return;
//		}
//		traverse(root.left);
//
//		// Start of "do some business",
//		// If first element has not been found, assign it to prevElement (refer
//		// to 6 in the example above)
//		if (firstElement == null && prevElement.val >= root.val) {
//			firstElement = prevElement;
//		}
//
//		// If first element is found, assign the second element to the root
//		// (refer to 2 in the example above)
//		if (firstElement != null && prevElement.val >= root.val) {
//			secondElement = root;
//		}
//		prevElement = root;
//
//		// End of "do some business"
//
//		traverse(root.right);
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