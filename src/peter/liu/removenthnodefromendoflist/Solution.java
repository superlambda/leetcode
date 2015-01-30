package peter.liu.removenthnodefromendoflist;

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
	public ListNode removeNthFromEnd(ListNode head, int n) {
		ListNode frontInterator = head;
		ListNode tailInterator = head;
		while (n > 0) {
			frontInterator = frontInterator.next;
			n--;
		}
		if(frontInterator==null){
			ListNode temp=head;
			head=head.next;
			temp.next=null;
			return head;
		}
		while (frontInterator != null && frontInterator.next != null) {
			frontInterator = frontInterator.next;
			tailInterator = tailInterator.next;
		}
		ListNode temp = tailInterator.next;
		if (temp != null) {
			tailInterator.next = temp.next;
			temp.next = null;
		}
		return head;
	}
}

class ListNode {
	int val;
	ListNode next;

	ListNode(int x) {
		val = x;
		next = null;
	}
}