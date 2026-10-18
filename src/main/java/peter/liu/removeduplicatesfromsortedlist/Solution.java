package peter.liu.removeduplicatesfromsortedlist;

/**
 * Given a sorted linked list, delete all duplicates such that each element
 * appear only once.
 * 
 * For example, Given 1->1->2, return 1->2. Given 1->1->2->3->3, return 1->2->3.
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	public ListNode deleteDuplicates(ListNode head) {
		ListNode iterator = head;
		while (iterator != null && iterator.next != null) {
			if (iterator.val == iterator.next.val) {
				iterator.next = iterator.next.next;
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
