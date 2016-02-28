package peter.liu.mergeksortedlists;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testMergeKLists() {
		// ListNode[] lists={new ListNode(1),new ListNode(0)};
		int[] a1 = { -8, -7, -7, -5, 1, 1, 3, 4 };
		int[] a2 = { -2 };
		int[] a3 = { -10, -10, -7, 0, 1, 3 };
		int[] a4 = { 2 };
		ListNode[] lists = { makeListNode(a1), makeListNode(a2),
				makeListNode(a3),
				makeListNode(a4)};
	
		ListNode node=new Solution().mergeKLists(lists);
		while(node!=null){
			System.out.print(node.val+",");
			node=node.next;
		}
	}
	
	private ListNode makeListNode(int[] array){
		ListNode head=new ListNode(array[0]);
		ListNode iterator=head;
		for(int i=1;i<array.length;i++){
			iterator.next=new ListNode(array[i]);
			iterator=iterator.next;
		}
		return head;
	}

}
