package peter.liu.reorderlist;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testReorderList() {
		ListNode node=new ListNode(1);
		ListNode iterator=node;
		iterator.next=new ListNode(2);
		iterator=iterator.next;
		iterator.next=new ListNode(3);
		iterator=iterator.next;
		iterator.next=new ListNode(4);
		iterator=iterator.next;
		iterator.next=new ListNode(5);
		new Solution().reorderList(node);
//		while(node!=null){
//			System.out.print(node.val+" ");
//			node=node.next;
//		}
		int[] a=new int[]{1,5,2,4,3};
		for(int value:a){
			System.out.print(value+" ");
			assertTrue(value==node.val);
			node=node.next;
		}
	}

}
