package peter.liu.populatingnextrightpointersineachnode2;

/**
 * Follow up for problem "Populating Next Right Pointers in Each Node".

What if the given tree could be any binary tree? Would your previous solution still work?

Note:

You may only use constant extra space.
For example,
Given the following binary tree,
         1
       /  \
      2    3
     / \    \
    4   5    7
After calling your function, the tree should look like:
         1 -> NULL
       /  \
      2 -> 3 -> NULL
     / \    \
    4-> 5 -> 7 -> NULL
    
 * @author liuyingjie
 *
 */
public class Solution {
	public void connect(TreeLinkNode root) {
		TreeLinkNode levelStart = root;
		while (levelStart != null) {
			TreeLinkNode current = levelStart;
			while (current != null) {
				if (current.left != null) {
					if(current.right!=null){
						current.left.next = current.right;
					}else {
						TreeLinkNode next=current.next;
						while(( next!= null)){
							if(next.left ==null && next.right==null){
								next =next.next;
								continue;
							}
							if(next.left!=null){
								current.left.next = next.left;
							}else{
								current.left.next = next.right;
							}
							break;
						}
					}
				}
				if (current.right != null && current.next != null) {
					TreeLinkNode next=current.next;
					while(( next!= null)){
						if(next.left ==null && next.right==null){
							next =next.next;
							continue;
						}
						if(next.left!=null){
							current.right.next = next.left;
						}else{
							current.right.next = next.right;
						}
						break;
					}
				}
				current = current.next;
			}
			while(levelStart!=null&&levelStart.left==null &&levelStart.right==null){
				levelStart=levelStart.next;
			}
			if(levelStart!=null){
				if(levelStart.left!=null){
					levelStart = levelStart.left;
				}else{
					levelStart = levelStart.right;
				}
			}
			
		}
    }
}

/**
 * Definition for binary tree with next pointer.
 */ 
 class TreeLinkNode {
      int val;
      TreeLinkNode left, right, next;
      TreeLinkNode(int x) { val = x; }
 }
