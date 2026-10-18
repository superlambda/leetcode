package peter.liu.removeduplicatesfromsortedlistII;

/**
 * Given a sorted linked list, delete all nodes that have duplicate numbers,
 * leaving only distinct numbers from the original list.
 * 
 * For example, Given 1->2->3->3->4->4->5, return 1->2->5. Given 1->1->1->2->3,
 * return 2->3.
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	public ListNode deleteDuplicates(ListNode head) {
		ListNode iterator = head;
		boolean shouldDelete = false;
		while (iterator != null && iterator.next != null) {
			while (iterator != null && iterator.next != null
					&& iterator.val == iterator.next.val) {
				ListNode temp=iterator.next;
				iterator.next = iterator.next.next;
				temp.next=null;
				shouldDelete = true;
			}
			if (shouldDelete) {
				iterator = iterator.next;
				head = iterator;
				shouldDelete = false;
			} else {
				head = iterator;
				break;
			}
		}

		shouldDelete = false;
		while (iterator != null && iterator.next != null
				&& iterator.next.next != null) {
			while (iterator.next != null && iterator.next.next != null
					&& iterator.next.val == iterator.next.next.val) {
				ListNode temp=iterator.next.next;
				iterator.next.next = iterator.next.next.next;
				temp.next=null;
				shouldDelete = true;
			}
			if (shouldDelete) {
				ListNode temp=iterator.next;
				iterator.next = iterator.next.next;
				temp.next=null;
				shouldDelete = false;
			} else {
				iterator = iterator.next;
			}
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