package peter.liu.swapnodesinpairs;

/**
 * Given a linked list, swap every two adjacent nodes and return its head.
 * 
 * For example, Given 1->2->3->4, you should return the list as 2->1->4->3.
 * 
 * Your algorithm should use only constant space. You may not modify the values
 * in the list, only nodes itself can be changed.
 * 
 * @author superlambda
 *
 */
public class Solution {
	public ListNode swapPairs(ListNode head) {
		if (head == null || head.next == null) {
			return head;
		}
		ListNode temp = head.next;
		head.next = temp.next;
		temp.next = head;
		head = temp;
		ListNode first = head.next;
		while (first != null && first.next != null && first.next.next != null) {
			temp = first.next;
			first.next = temp.next;
			temp.next = first.next.next;
			first.next.next = temp;
			first = temp;
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
