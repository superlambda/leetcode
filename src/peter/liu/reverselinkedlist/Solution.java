package peter.liu.reverselinkedlist;

/**
 * Reverse a singly linked list.
 * 
 * @author superlambda
 *
 */
public class Solution {
//	public ListNode reverseList(ListNode head) {
//		if(head==null||head.next==null){
//			return head;
//		}
//		ListNode iterator=head;
//		ListNode tempNode=head;
//		ListNode tail=null;
//		while(iterator!=null){
//			tempNode=iterator;
//			iterator=iterator.next;
//			tempNode.next=tail;
//			tail=tempNode;
//		}
//		return tail;
//	}
	
	public ListNode reverseList(ListNode head) {
		ListNode dummyHead=new ListNode(0);
		while(head!=null) {
			ListNode temp=head;
			head=head.next;
			temp.next=dummyHead.next;
			dummyHead.next=temp;
		}
		return dummyHead.next;
		
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
	}
}
