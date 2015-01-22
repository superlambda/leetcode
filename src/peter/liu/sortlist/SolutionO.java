package peter.liu.sortlist;

/**
 * Sort a linked list in O(n log n) time using constant space complexity.
 * 
 * @author pcnsh197
 *
 */
public class SolutionO {
	public ListNode sortList(ListNode head) {
		if(head==null){
			return null;
		}
		int length=0;
		ListNode iterator=head;
		while(iterator!=null){
			length++;
			iterator=iterator.next;
		}
		int halfLength=length/2;
		iterator=head;
		while(halfLength>0){
			if(halfLength!=1){
				iterator=iterator.next;
			}else{
				ListNode temp=iterator;
				iterator=iterator.next;
				temp.next=null;
			}
			
			halfLength--;
		}
		head=binarySort(head,length/2);
		iterator=binarySort(iterator,length-length/2);
		return merge(head,iterator);
	}
	private ListNode binarySort(ListNode head,int length){
		if(length<2){
			return head;
		}
		if(length==2){
			if(head.next.val<head.val){
				ListNode temp=head.next;
				temp.next=head;
				head.next=null;
				head=temp;
			}
			return head;
		}else{
			int halfLength=length/2;
			ListNode iterator=head;
			while(halfLength>0){
				if(halfLength!=1){
					iterator=iterator.next;
				}else{
					ListNode temp=iterator;
					iterator=iterator.next;
					temp.next=null;
				}
				
				halfLength--;
			}
			head=binarySort(head,length/2);
			iterator=binarySort(iterator,length-length/2);
			return merge(head,iterator);
		}
		
	}
	private ListNode merge(ListNode first,ListNode second){
		ListNode mergedHead=null;
		if(first.val<second.val){
			mergedHead=first;
			first=first.next;
		}else{
			mergedHead=second;
			second=second.next;
		}
		mergedHead.next=null;
		ListNode iterator=mergedHead;
		while(first!=null&&second!=null){
			if(first.val<second.val){
				iterator.next=first;
				first=first.next;
			}else{
				iterator.next=second;
				second=second.next;
			}
			iterator=iterator.next;
		}
		if(first!=null){
			iterator.next=first;
		}
		if(second!=null){
			iterator.next=second;
		}
		first=mergedHead;
		return first;
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