package peter.liu.reverselinkedlistII;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testReverseBetween() {
		ListNode node1 = new ListNode(1);
		ListNode node2 = new ListNode(2);
		ListNode node3 = new ListNode(3);
		ListNode node4 = new ListNode(4);
		ListNode node5 = new ListNode(5);
		node1.next = node2;
		node2.next = node3;
		node3.next = node4;
		node4.next = node5;
		ListNode result = new Solution().reverseBetween(node1, 1, 2);
		while (result != null) {
			System.out.print(result.val + " ");
			result = result.next;
		}
		
//		System.out.println(" ");
//		
//		result=new Solution().reverseBetween(result, 1, 2);
//		
//		while (result != null) {
//			System.out.print(result.val + " ");
//			result = result.next;
//		}
	}

}
