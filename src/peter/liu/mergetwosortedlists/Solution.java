package peter.liu.mergetwosortedlists;

/**
 * Merge two sorted linked lists and return it as a new list. The new list
 * should be made by splicing together the nodes of the first two lists.
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
		if (l1 == null) {
			return l2;
		}
		if (l2 == null) {
			return l1;
		}
		ListNode header = null;
		ListNode iterator = null;
		if (l1.val > l2.val) {
			iterator = header = l2;
			l2 = l2.next;
		} else {
			iterator = header = l1;
			l1 = l1.next;
		}
		while (l1 != null && l2 != null) {
			if (l1.val > l2.val) {
				iterator.next = l2;
				l2 = l2.next;
			} else {
				iterator.next = l1;
				l1 = l1.next;
			}
			iterator = iterator.next;
		}
		iterator.next = l1 != null ? l1 : l2;
		return header;
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
