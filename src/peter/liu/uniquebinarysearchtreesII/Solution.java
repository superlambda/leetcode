package peter.liu.uniquebinarysearchtreesII;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Given n, generate all structurally unique BST's (binary search trees) that store values 1...n.

For example,
Given n = 3, your program should return all 5 unique BST's shown below.

   1         3     3      2      1
    \       /     /      / \      \
     3     2     1      1   3      2
    /     /       \                 \
   2     1         2                 3
confused what "{1,#,2,3}" means? > read more on how binary tree is serialized on OJ.
 * @author superlambda
 *
 */
public class Solution {
	public List<TreeNode> generateTrees(int n) {
		List<Integer> numberList= new LinkedList<>();
		for(int i=1;i<=n;i++){
			numberList.add(i);
		}
		List<TreeNode> result=new LinkedList<>();
		buildBST(result,null, numberList);
		if(result.isEmpty()){
			result.add(null);
		}
		return result;
        
    }
	private void buildBST(List<TreeNode> result,TreeNode root, List<Integer> leftNumber){
		for(int i=0;i<leftNumber.size();i++){
			int number=leftNumber.get(i);
			if(leftNumber.size()==1){
				root = insertOneNode(root, number);
				if (!isResultAlreadyExist(result, root)) {
					result.add(root);
				}
			}else{
				TreeNode newRoot=copyTreeNodeAndInsertOneNode(root,number);
				List<Integer> tempList=new ArrayList<>(leftNumber);
				tempList.remove(i);
				buildBST(result,newRoot, tempList);
			}
		}
	}
	private boolean isResultAlreadyExist(List<TreeNode> result,TreeNode root){
		TreeNode nodeToAdd=root;
		for(TreeNode node:result){
			if(areTheSame(node,nodeToAdd)){
				return true;
			}
		}
		return false;
	}

	private boolean areTheSame(TreeNode one, TreeNode two) {
		if (one == null && two == null) {
			return true;
		}
		if (one == null || two == null) {
			return false;
		}
		return one.val == two.val && areTheSame(one.left, two.left)
				&& areTheSame(one.right, two.right);
	}
	
	private TreeNode insertOneNode(TreeNode root,int number){
		if(root==null){
			root=new TreeNode(number);
			return root;
		}
		TreeNode node=root;
		while(node!=null){
			if(node.val>number){
				if(node.left!=null){
					node=node.left;
				}else{
					break;
				}
			}else{
				if(node.right!=null){
					node=node.right;
				}else{
					break;
				}
			}
		}
		if(node.val>number){
			node.left=new TreeNode(number);
		}else{
			node.right=new TreeNode(number);
		}
		return root;
	}
	private TreeNode copyTreeNodeAndInsertOneNode(TreeNode root,int number){
		TreeNode newRoot=copyTreeNode(root);
		newRoot=insertOneNode(newRoot,number);
		return newRoot;
	}
	private TreeNode copyTreeNode(TreeNode root){ 
		if(root==null){
			return null;
		}
		TreeNode newRoot=new TreeNode(root.val);
		newRoot.left=copyTreeNode(root.left);
		newRoot.right=copyTreeNode(root.right);
		return newRoot;
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
