package peter.liu.flattenbinarytreetolinkedlist;

import java.util.ArrayList;
import java.util.List;


/**
 * Given a binary tree, flatten it to a linked list in-place.

For example,
Given

         1
        / \
       2   5
      / \   \
     3   4   6
The flattened tree should look like:
   1
    \
     2
      \
       3
        \
         4
          \
           5
            \
             6
 * @author superlambda
 *
 */
public class Solution {
	public void flatten(TreeNode root) {
		if(root==null){
			return;
		}
        List<TreeNode> list=new ArrayList<>();
        getList(list, root);
        TreeNode node=root;
        for(int i=1;i<list.size();i++){
        	node.right=list.get(i);
        	node.left=null;
        	node=node.right;
        }
        if(node!=null){
        	node.left=null;
        }
        
    }
	
	private void getList(List<TreeNode> list, TreeNode node) {
		list.add(node);
		if(node.left!=null){
			getList(list, node.left);
		}
		if(node.right!=null){
			getList(list, node.right);
		}
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
