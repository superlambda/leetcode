package peter.liu.removenthnodefromendoflist;

import java.util.HashMap;
import java.util.Map;

/**
 * Given a linked list, remove the nth node from the end of list and return its
 * head.
 * 
 * For example,
 * 
 * Given linked list: 1->2->3->4->5, and n = 2.
 * 
 * After removing the second node from the end, the linked list becomes
 * 1->2->3->5.
 * 
 * Note: Given n will always be valid. Try to do this in one pass.
 * 
 * @author pcnsh197
 *
 */
public class Solution {
//	public ListNode removeNthFromEnd(ListNode head, int n) {
//		ListNode frontInterator = head;
//		ListNode tailInterator = head;
//		while (n > 0) {
//			frontInterator = frontInterator.next;
//			n--;
//		}
//		if (frontInterator == null) {
//			ListNode temp = head;
//			head = head.next;
//			temp.next = null;
//			return head;
//		}
//		while (frontInterator != null && frontInterator.next != null) {
//			frontInterator = frontInterator.next;
//			tailInterator = tailInterator.next;
//		}
//		ListNode temp = tailInterator.next;
//		if (temp != null) {
//			tailInterator.next = temp.next;
//			temp.next = null;
//		}
//		return head;
//	}
	
	public ListNode removeNthFromEnd(ListNode head, int n) {
		ListNode dummyHead=new ListNode(0);
		dummyHead.next=head;
		ListNode slow=dummyHead;
		ListNode fast=dummyHead;
		for(int i=0;i<=n;i++) {
			fast=fast.next;
		}
		while(fast!=null) {
			fast=fast.next;
			slow=slow.next;
		}
		slow.next=slow.next.next;
		return dummyHead.next;
	}
	
//	public ListNode removeNthFromEnd(ListNode head, int n) {
//		
//		Map<Integer, ListNode> map = new HashMap<>();
//		ListNode start=head;
//		int sequence=1;
//		while(start!=null) {
//			map.put(sequence, start);
//			start=start.next;
//			sequence++;
//		}
//		int moveSequence=sequence-n;
//		if(moveSequence==1) {
//			if(head.next==null) {
//				head=null;
//				return head;
//			}else {
//				ListNode temp=head;
//				head=head.next;
//				temp.next=null;
//				return head;
//			}
//		}
//		ListNode nodeToMove = map.get(moveSequence);
//		ListNode previousNode=map.get(moveSequence-1);
//		if(previousNode!=null&&nodeToMove!=null) {
//			previousNode.next=nodeToMove.next;
//			nodeToMove.next=null;
//		}
//		return head;
//	}
}

class ListNode {
	int val;
	ListNode next;

	ListNode() {
	}

	ListNode(int val) {
		this.val = val;
	}

	ListNode(int val, ListNode next) {
		this.val = val;
		this.next = next;
	}
}