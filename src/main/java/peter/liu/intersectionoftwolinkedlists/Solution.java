package peter.liu.intersectionoftwolinkedlists;


/**
 * Write a program to find the node at which the intersection of two singly
 * linked lists begins.
 * 
 * For example, the following two linked lists:
 * 
 * A: a1 → a2 ↘ c1 → c2 → c3 ↗ B: b1 → b2 → b3
 * 
 * begin to intersect at node c1.
 * 
 * Notes:
 * 
 * If the two linked lists have no intersection at all, return null. The linked
 * lists must retain their original structure after the function returns. You
 * may assume there are no cycles anywhere in the entire linked structure. Your
 * code should preferably run in O(n) time and use only O(1) memory.
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
		int aLength = getLengthOfList(headA);
		int bLength = getLengthOfList(headB);
		if (aLength == 0 || bLength == 0) {
			return null;
		}
		ListNode fistIterator = headA;
		ListNode secondIterator = headB;
		if (aLength > bLength) {
			int nodesToTrack = aLength - bLength;
			while (nodesToTrack > 0) {
				fistIterator = fistIterator.next;
				nodesToTrack--;
			}
		} else {
			int nodesToTrack = bLength - aLength;
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
