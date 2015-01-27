package peter.liu.linkedlistcycleII;



/**
 * Given a linked list, return the node where the cycle begins. If there is no
 * cycle, return null.
 * 
 * Follow up: Can you solve it without using extra space?
 * 
 * @author pcnsh197
 *
 */
public class Solution {

	public ListNode detectCycle(ListNode head) {
		if (head == null || head.next == null) {
			return null;
		}
		ListNode slower = head;
		ListNode faster = head;
		ListNode secondHead = null;
		while (faster != null && faster.next != null) {
			slower = slower.next;
			faster = faster.next.next;
			if (slower == faster) {
				secondHead = slower.next;
				slower.next = null;
				break;
			}

		}
		if (secondHead != null) {
			int firstLength = getLengthOfList(head);
			int secondLength = getLengthOfList(secondHead);
			ListNode fistIterator = head;
			ListNode secondIterator = secondHead;
			if (firstLength > secondLength) {
				int nodesToTrack = firstLength - secondLength;
				while (nodesToTrack > 0) {
					fistIterator = fistIterator.next;
					nodesToTrack--;
				}
			} else {
				int nodesToTrack = secondLength - firstLength;
				while (nodesToTrack > 0) {
					secondIterator = secondIterator.next;
					nodesToTrack--;
				}
			}
			while (fistIterator != null & secondIterator != null) {
				if (fistIterator == secondIterator) {
					return fistIterator;
				}
				fistIterator = fistIterator.next;
				secondIterator = secondIterator.next;
			}

		}
		return null;
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
}

class ListNode {
	int val;
	ListNode next;

	ListNode(int x) {
		val = x;
		next = null;
	}
}
