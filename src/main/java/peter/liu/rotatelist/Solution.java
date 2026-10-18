package peter.liu.rotatelist;

public class Solution {
	public ListNode rotateRight(ListNode head, int k) {
		ListNode trace = head;
		int length = 0;
		while (trace != null) {
			trace = trace.next;
			length++;
		}
		if (length <= 1 || k == 0) {
			return head;
		}
		k %= length;
		ListNode headItr = head;
		ListNode tailItr = head;
		while (k > 0) {
			headItr = headItr.next;
			k--;
		}
		while (headItr.next != null) {
			headItr = headItr.next;
			tailItr = tailItr.next;
		}
		headItr.next = head;
		head = tailItr.next;
		tailItr.next = null;
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
