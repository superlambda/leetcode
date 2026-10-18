package peter.liu.reverselinkedlistII;

/**
 * Reverse a linked list from position m to n. Do it in-place and in one-pass.
 * 
 * For example: Given 1->2->3->4->5->NULL, m = 2 and n = 4,
 * 
 * return 1->4->3->2->5->NULL.
 * 
 * Note: Given m, n satisfy the following condition: 1 ≤ m ≤ n ≤ length of list.
 * 
 * @author superlambda
 *
 */
public class Solution {
	public ListNode reverseBetween(ListNode head, int m, int n) {
		boolean misOne = false;
		if (m == 1) {
			ListNode tempHead = new ListNode(1);
			tempHead.next = head;
			head = tempHead;
			m++;
			n++;
			misOne = true;
		}
		int counting = 1;
		ListNode iterator = head;
		while (counting < m - 1) {
			iterator = iterator.next;
			counting++;
		}

		int timeToDO = n - m;
		counting = 1;

		if (counting <= timeToDO) {
			ListNode mNode = iterator.next;
			ListNode nodeToAdd = mNode.next;
			while (counting <= timeToDO) {
				ListNode temp = iterator.next;
				iterator.next = nodeToAdd;
				nodeToAdd = nodeToAdd.next;
				iterator.next.next = temp;
				counting++;
			}
			mNode.next = nodeToAdd;
		}
		if (misOne) {
			head = head.next;
		}

		return head;

	}
}

class ListNode {
	int val;
	ListNode next;

	ListNode(int x) {
		val = x;
	}
}
