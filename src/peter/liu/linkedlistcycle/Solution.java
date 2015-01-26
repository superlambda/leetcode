package peter.liu.linkedlistcycle;

import java.util.HashSet;
import java.util.Set;


/**
 * Given a linked list, determine if it has a cycle in it.
 * 
 * Follow up: Can you solve it without using extra space?
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	public boolean hasCycle(ListNode head) {
		Set<ListNode> set=new HashSet<>();
		ListNode iterator=head;
		while(iterator!=null){
			if(!set.contains(iterator)){
				set.add(iterator);
				iterator=iterator.next;
			}else{
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
