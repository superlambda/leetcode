package peter.liu.reversenodesinkgroup;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testReverseKGroup() {
		int[] a1 = { 1 };
		new Solution().reverseKGroup(makeListNode(a1), 2);
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
