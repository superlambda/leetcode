package peter.liu.partitionlist;

/**
 * Given a linked list and a value x, partition it such that all nodes less than
 * x come before nodes greater than or equal to x.
 * 
 * You should preserve the original relative order of the nodes in each of the
 * two partitions.
 * 
 * For example, Given 1->4->3->2->5->2 and x = 3, return 1->2->2->4->3->5.
 * 
 * @author superlambda
 *
 */
public class Solution {
	public ListNode partition(ListNode head, int x) {
		if (head == null || head.next == null) {
			return head;
		}
		ListNode nodeIter = head;
		ListNode partitionHead = null;
		ListNode partitionIter = null;

		while (nodeIter != null && nodeIter.next != null) {
			if (nodeIter.next.val >= x) {
				nodeIter = nodeIter.next;
			} else {
				if (partitionHead == null) {
					partitionHead = nodeIter.next;
					partitionIter = partitionHead;
				} else {
					partitionIter.next = nodeIter.next;
					partitionIter = partitionIter.next;
				}
				nodeIter.next = nodeIter.next.next;
			}
		}
		if (head != null && head.val < x) {
			ListNode temp = head.next;
			head.next = partitionHead;
			partitionHead = head;
			head = temp;
		}
		if (partitionIter != null) {
			partitionIter.next = head;
		} else {
			if (partitionHead == null) {
				partitionHead = head;
			} else {
				partitionHead.next = head;
			}
		}
		return partitionHead;
	}
}

/**
 * Definition for singly-linked list.
 * */
class ListNode {
	int val;
	ListNode next;

	ListNode(int x) {
		val = x;
	}
}
