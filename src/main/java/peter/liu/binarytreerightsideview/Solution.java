package peter.liu.binarytreerightsideview;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * Given a binary tree, imagine yourself standing on the right side of it, return the values of the nodes you can see ordered from top to bottom.

For example:
Given the following binary tree,
   1            <---
 /   \
2     3         <---
 \     \
  5     4       <---
You should return [1, 3, 4].
 * @author liuyingjie
 *
 */
public class Solution {
	public List<Integer> rightSideView(TreeNode root) {
		List<Integer> result = new LinkedList<>();
		trace(root,1,result);
		return result;
	}
	private void trace(TreeNode node,int depth, List<Integer> result){
		if(node!=null){
			if(result.size()<depth){
				result.add(node.val);
			}
			depth++;
			trace(node.right,depth,result);
			trace(node.left,depth,result);
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
 
