package peter.liu.reorderlist;

/**
 * Given a singly linked list L: L0→L1→…→Ln-1→Ln, reorder it to:
 * L0→Ln→L1→Ln-1→L2→Ln-2→…
 * 
 * You must do this in-place without altering the nodes' values.
 * 
 * For example, Given {1,2,3,4}, reorder it to {1,4,2,3}.
 * 
 * @author pcnsh197
 *
 */
public class Solution {

	public void reorderList(ListNode head) {
		int length = getLengthOfList(head);
		if (length <= 2) {
			return;
		}
		int halfLength = length / 2;
		ListNode iterator = splitList(head, halfLength);
		ListNode secondHead=iterator;
		while (iterator != null) {
			ListNode next = iterator.next;
			if (next != null) {
				iterator=next.next;
				next.next=secondHead;
				secondHead=next;
			}
		}
		merge(head, iterator);
	}

	private int getLengthOfList(ListNode head) {
		int length = 0;
		ListNode iterator = head;
		while (iterator != null) {
			length++;
			iterator = iterator.next;
		}
		return length;
	}

	private ListNode splitList(ListNode head, int halfLength) {
		ListNode iterator = head;
		while (halfLength > 0) {
			if (halfLength != 1) {
				iterator = iterator.next;
			} else {
				ListNode temp = iterator;
				iterator = iterator.next;
				temp.next = null;
			}
			halfLength--;
		}
		return iterator;
	}
	private ListNode merge(ListNode first, ListNode second) {
		ListNode mergedHead = first;
		first = first.next;;
		ListNode iterator = mergedHead;
		while (first != null && second != null) {
			iterator.next = second;
			second = second.next;
			iterator.next = first;
			first = first.next;
		}
		iterator.next = (first != null) ? first : second;
		return mergedHead;
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
		next = null;
	}
}