package peter.liu.sortlist;

/**
 * Sort a linked list in O(n log n) time using constant space complexity.
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	public ListNode sortList(ListNode head) {
		if (head == null) {
			return null;
		}
		return binarySort(head);
	}

	private ListNode binarySort(ListNode head) {
		ListNode leftNode=removeFrist2Node(head);
		head=sort(head);
		while(leftNode!=null){
			ListNode iterator=leftNode;
			leftNode=removeFrist2Node(leftNode);
			head=merge(head, sort(iterator));
		}
		return head;
	}
	private ListNode removeFrist2Node(ListNode head){
		if (head.next != null && head.next.next != null) {
			ListNode iterator = head.next.next;
			head.next.next = null;
			return iterator;
		}
		return null;
	}

	private ListNode sort(ListNode head) {
		if (head.next != null && head.next.val < head.val) {
			ListNode temp = head.next;
			temp.next = head;
			head.next = null;
			head = temp;
		}
		return head;
	}

	private ListNode merge(ListNode first, ListNode second) {
		if(second.next==null&&second.val<=first.val){
			second.next=first;
			return second;
		}else if(second.next!=null&&second.next.val<=first.val){
			second.next.next=first;
			return second;
		}
		ListNode mergedHead = null;
		if (first.val < second.val) {
			mergedHead = first;
			first = first.next;
		} else {
			mergedHead = second;
			second = second.next;
		}
		mergedHead.next = null;
		ListNode iterator = mergedHead;
		while (first != null && second != null) {
			if (first.val < second.val) {
				iterator.next = first;
				first = first.next;
			} else {
				iterator.next = second;
				second = second.next;
			}
			iterator = iterator.next;
		}
		if (first != null) {
			iterator.next = first;
		}
		if (second != null) {
			iterator.next = second;
		}
		return mergedHead;
	}
}