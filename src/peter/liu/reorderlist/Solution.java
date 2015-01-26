package peter.liu.reorderlist;

/**
 * Given a singly linked list L: L0-->L1-->Ln-1-->Ln, reorder it to:
 * L0-->Ln-->L1-->Ln-1-->L2-->Ln-2..
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
		int halfLength = length / 2+1;
		ListNode secondHead = secondHalfList(head, halfLength);
		
		ListNode iterator=head;
		int numberToAdd=length-halfLength;
		while (numberToAdd>0) {
			ListNode temp = iterator.next;
			iterator.next = secondHead;
			secondHead=secondHead.next;
			iterator = iterator.next;
			iterator.next = temp;
			iterator = iterator.next;
			numberToAdd--;
		}
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

	private ListNode secondHalfList(ListNode head, int halfLength) {
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
		//reverse the second half list.
		ListNode secondListHead=iterator;
		iterator=iterator.next;
		secondListHead.next=null;
		while(iterator!=null){
			ListNode temp=iterator;
			iterator=iterator.next;
			temp.next=secondListHead;
			secondListHead=temp;
		}
		return secondListHead;
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