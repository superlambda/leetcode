package peter.liu.partitionlist;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testPartition() {
		ListNode head = new ListNode(1);
		head.next=new ListNode(2);
		ListNode result=new Solution().partition(head, 2);
		while(result!=null){
			System.out.println(result.val+" ");
			result=result.next;
		}
	}

}
