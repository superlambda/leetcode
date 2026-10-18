package peter.liu.addtwonumbers;

/**
 * You are given two linked lists representing two non-negative numbers. The
 * digits are stored in reverse order and each of their nodes contain a single
 * digit. Add the two numbers and return it as a linked list.
 * 
 * Input: (2 -> 4 -> 3) + (5 -> 6 -> 4) Output: 7 -> 0 -> 8
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
		ListNode first = l1;
		ListNode second = l2;
		int l1Length = 0;
		int l2Length = 0;
		while (first != null) {
			l1Length++;
			first = first.next;
		}
		while (second != null) {
			l2Length++;
			second = second.next;
		}
		int carryNumber = 0;
		if (l1Length < l2Length) {
			ListNode temp = l1;
			l1 = l2;
			l2 = temp;
		}
		first = l1;
		second = l2;
		while (first != null || second != null) {
			if (first != null && second != null) {
				first.val = first.val + second.val + carryNumber;
				if (first.val >= 10) {
					first.val -= 10;
					carryNumber = 1;
				} else {
					carryNumber = 0;
				}
				if (first.next == null && second.next == null
						&& carryNumber == 1) {
					ListNode tail = new ListNode(carryNumber);
					first.next = tail;
					break;
				}
				first = first.next;
				second = second.next;
			} else if (first != null && second == null) {
				if (first.val + carryNumber < 10) {
					first.val += carryNumber;
					break;
				}
				while (first != null && first.val + carryNumber >= 10) {
					first.val += carryNumber - 10;
					carryNumber = 1;
					if (first.next != null) {
						first = first.next;
					} else {
						ListNode tail = new ListNode(carryNumber);
						first.next = tail;
						carryNumber = 0;
						break;
						
						
					}
				}
				first.val += carryNumber;
				break;
			}
		}
		return l1;
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