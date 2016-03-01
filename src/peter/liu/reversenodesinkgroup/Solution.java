package peter.liu.reversenodesinkgroup;


/**
 * Given a linked list, reverse the nodes of a linked list k at a time and
 * return its modified list.
 * 
 * If the number of nodes is not a multiple of k then left-out nodes in the end
 * should remain as it is.
 * 
 * You may not alter the values in the nodes, only nodes itself may be changed.
 * 
 * Only constant memory is allowed.
 * 
 * For example, Given this linked list: 1->2->3->4->5
 * 
 * For k = 2, you should return: 2->1->4->3->5
 * 
 * For k = 3, you should return: 3->2->1->4->5
 * 
 * Subscribe to see which companies asked this question
 * 
 * @author liuyingjie
 *
 */
public class Solution {
	public ListNode reverseKGroup(ListNode head, int k) {
		
		ListNode fakeHead=new ListNode(0);
		ListNode tail=null;
		int i=1;
		
		ListNode tempHead=null;
		while(i<=k&&head!=null){
			if(tempHead==null){
				tempHead=head;
			}
			head=head.next;
			i++;
			if(i>k){
				ListNode[] result=reverseNodes(tempHead,k);
				if(fakeHead.next==null){
					fakeHead.next=result[0];
					tail=result[1];
				}else{
					tail.next=result[0];
					tail=result[1];
				}
				i=1;
				tempHead=null;
			}
		}
		if(fakeHead.next==null){
			fakeHead.next=tempHead;
		}else if(i>1&&tempHead!=null&&tail!=null){
			tail.next=tempHead;
		}
		return fakeHead.next;
		
	}
	
	public ListNode[] reverseNodes(ListNode head,int k){
		ListNode fakeHead=new ListNode(0);
		ListNode tail=null;
		int i=1;
		while(i<=k&&head!=null){
			if(tail==null){
				tail=head;
			}
			ListNode temp=head.next;
			head.next=fakeHead.next;
			fakeHead.next=head;
			head=temp;
			i++;
		}
		tail.next=null;
		return new ListNode[]{fakeHead.next,tail};
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
