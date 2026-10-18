package peter.liu.mergetwosortedlists;

/**
 * Merge two sorted linked lists and return it as a new list. The new list
 * should be made by splicing together the nodes of the first two lists.
 * 
 * @author pcnsh197
 *
 */
public class Solution {
//	public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
//		if (l1 == null) {
//			return l2;
//		}
//		if (l2 == null) {
//			return l1;
//		}
//		ListNode header = null;
//		ListNode iterator = null;
//		if (l1.val > l2.val) {
//			iterator = header = l2;
//			l2 = l2.next;
//		} else {
//			iterator = header = l1;
//			l1 = l1.next;
//		}
//		while (l1 != null && l2 != null) {
//			if (l1.val > l2.val) {
//				iterator.next = l2;
//				l2 = l2.next;
//			} else {
//				iterator.next = l1;
//				l1 = l1.next;
//			}
//			iterator = iterator.next;
//		}
//		iterator.next = l1 != null ? l1 : l2;
//		return header;
//	}
	public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
		if(list1==null) {
			return list2;
		}
		if(list2==null) {
			return list1;
		}
		ListNode dummyHead=new ListNode(0);
		ListNode tail=dummyHead;
		ListNode temp;
		while(list1!=null&&list2!=null) {
			if(list1.val<list2.val) {
				temp=list1;
				list1=list1.next;
				tail.next=temp;
				tail=tail.next;
				
			}else if(list1.val>list2.val) {
				temp=list2;
				list2=list2.next;
				tail.next=temp;
				tail=tail.next;
				
			} else {
				temp=list1;
				list1=list1.next;
				tail.next=temp;
				tail=tail.next;
				temp=list2;
				list2=list2.next;
				tail.next=temp;
				tail=tail.next;
			}
		}
		if(list1!=null) {
			tail.next=list1;
		}
		if(list2!=null) {
			tail.next=list2;
		}
		
		return dummyHead.next;
     
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
