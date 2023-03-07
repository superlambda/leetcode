package peter.liu.deletenodeinalinkedlist;

public class Solution {
	
	/* One time pass */
	public void deleteNode(ListNode node) {
		
//		ListNode start=node;
//		while(node!=null&&node.next!=null) {
//			node.val=node.next.val;
//			start=node;
//			node=node.next;
//		}
//		if(start!=null) {
//			start.next=null;
//		}
		ListNode nextNode = node.next;
		node.val=nextNode.val;
		node.next=nextNode.next;
		nextNode.next=null;
	}

}

/**
 * Definition for singly-linked list.
 */
class ListNode {
	int val;
	ListNode next;

	ListNode(int x) {
		val = x;
	}
}
