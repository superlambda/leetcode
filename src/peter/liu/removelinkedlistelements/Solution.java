package peter.liu.removelinkedlistelements;

/**
 * Remove all elements from a linked list of integers that have value val.

Example
Given: 1 --> 2 --> 6 --> 3 --> 4 --> 5 --> 6, val = 6
Return: 1 --> 2 --> 3 --> 4 --> 5

Credits:
Special thanks to @mithmatt for adding this problem and creating all test cases.

Subscribe to see which companies asked this question
 * @author liuyingjie
 *
 */
public class Solution {
	public ListNode removeElements(ListNode head, int val) {
		ListNode tempHead = new ListNode(0);
		tempHead.next=head;
		ListNode iterator=tempHead;
		while(iterator.next!=null){
			if(iterator.next.val==val){
				iterator.next=iterator.next.next;
			}else{
				iterator=iterator.next;
			}
		}
		return tempHead.next;        
    }

}

/**
 * Definition for singly-linked list.
 */ 
class ListNode {
      int val;
      ListNode next;
      ListNode(int x) { val = x; }
}

