package peter.liu.sortlist;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testSortList() {
	    SolutionO s=new SolutionO();
		ListNode node=null;
		assertEquals(s.sortList(node),null);
		node=new ListNode(5);
		assertEquals(s.sortList(node).val,5);
		node.next=new ListNode(3);
		assertEquals(s.sortList(node).val,3);
		
		node=new ListNode(5);
		ListNode iterator=node;
		iterator.next=new ListNode(7);
		iterator=iterator.next;
		iterator.next=new ListNode(-1);
		iterator=iterator.next;
		iterator.next=new ListNode(8);
		iterator=iterator.next;
		iterator.next=new ListNode(7);
		iterator=iterator.next;
		iterator.next=new ListNode(9);
		iterator=iterator.next;
		iterator.next=new ListNode(220);
		iterator=iterator.next;
		iterator.next=new ListNode(Integer.MIN_VALUE);
		iterator=iterator.next;
		iterator.next=new ListNode(Integer.MAX_VALUE);
		iterator=iterator.next;
		iterator.next=new ListNode(33);
		iterator=iterator.next;
		iterator.next=new ListNode(9);
		iterator=iterator.next;
		iterator.next=new ListNode(0);
		iterator=iterator.next;
		node=s.sortList(node);
		iterator=node;
		while(iterator!=null&&iterator.next!=null){
			System.out.print(iterator.val+" ");
			assertTrue(iterator.val<=iterator.next.val);
			iterator=iterator.next;
		}
		System.out.print(iterator.val);
		
	}

}
