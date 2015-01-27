package peter.liu.linkedlistcycle;



/**
 * Given a linked list, determine if it has a cycle in it.
 * 
 * Follow up: Can you solve it without using extra space?
 * 
 * @author pcnsh197
 *
 */
public class Solution {
//	public boolean hasCycle(ListNode head) {
//		Set<ListNode> set=new HashSet<>(128);
//		ListNode iterator=head;
//		while(iterator!=null){
//			if(!set.contains(iterator)){
//				set.add(iterator);
//				iterator=iterator.next;
//			}else{
//				return true;
//			}
//		}
//		return false;
//	}
	
	public boolean hasCycle(ListNode head) {
		if(head==null||head.next==null){
			return false;
		}
		ListNode slower=head;
		ListNode faster=head;
		while(faster!=null&&faster.next!=null){
			slower=slower.next;
			faster=faster.next.next;
			if(slower==faster){
				return true;
			}
			
		}
		return false;
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
