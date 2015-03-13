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
		int carryNumber = 0;
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
						carryNumber=0;
						break;
					}
				}
				if (first.val + carryNumber < 10) {
					first.val += carryNumber;
				}
				break;
			} else if (second != null && first == null) {
				first = l1;
				while (first.next != null) {
					first = first.next;
				}
				first.next = second;
				if (second.val + carryNumber < 10) {
					second.val += carryNumber;
					break;
				}
				while (second != null && second.val + carryNumber >= 10) {
					second.val += carryNumber - 10;
					carryNumber = 1;
					if (second.next != null) {
						second = second.next;
					} else {
						ListNode tail = new ListNode(carryNumber);
						second.next = tail;
						carryNumber=0;
						break;
					}
				}
				if (second.val + carryNumber < 10) {
					second.val += carryNumber;
				}
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