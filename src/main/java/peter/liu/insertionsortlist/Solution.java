package peter.liu.insertionsortlist;

/**
 * Sort a linked list using insertion sort.
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	public ListNode insertionSortList(ListNode head) {
		if (head == null || head.next == null) {
			return head;
		} else {
			ListNode iterator = head.next;
			head.next = null;
			while (iterator != null) {
				ListNode toBeInserted = iterator;
				iterator = iterator.next;
				if (toBeInserted.val <= head.val) {
					toBeInserted.next = head;
					head = toBeInserted;
				} else {
					ListNode newIterator = head;
					while (newIterator != null
							&& newIterator.val < toBeInserted.val) {
						if (newIterator.next == null) {
							newIterator.next = toBeInserted;
							toBeInserted.next=null;
							break;
						} else {
							if (newIterator.next.val < toBeInserted.val) {
								newIterator = newIterator.next;
							} else {
								toBeInserted.next = newIterator.next;
								newIterator.next = toBeInserted;
								break;
							}
						}
					}
				}
			}
			return head;
		}
		
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