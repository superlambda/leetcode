package peter.liu.reverselinkedlist;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testReverseList() {
		ListNode node1=new ListNode(1);
		ListNode node2=new ListNode(2);
		ListNode node3=new ListNode(3);
		node1.next=node2;
//		node2.next=node3;
		ListNode result=new Solution().reverseList(node1);
		while(result!=null){
			System.out.print(result.val +" ");
			result=result.next;
		}
		
	}

}
