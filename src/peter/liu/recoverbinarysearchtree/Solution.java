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
		List<Integer> numberList=new ArrayList<>();
		trace(root,list,numberList);
		numberList.sort((Integer h1, Integer h2) -> h1.compareTo(h2));
		for(int i=0;i<numberList.size();i++){
			if(list.get(i).val!=numberList.get(i)){
				list.get(i).val=numberList.get(i);
			}
		}
		
	}
	private void trace(TreeNode node,List<TreeNode> list,List<Integer> numberList){
		if(node==null){
			return;
		}
		trace(node.left,list,numberList);
		list.add(node);
		numberList.add(node.val);
		trace(node.right,list,numberList);
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