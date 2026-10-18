package peter.liu.palindromelinkedlist;

public class Solution {
//	public boolean isPalindrome(ListNode head) {
//		
//		ListNode slow=head;
//		ListNode fast=head;
//		while(fast!=null&&fast.next!=null) {
//			fast=fast.next.next;
//			slow=slow.next;
//		}
//		
//		fast=head;
//		slow=reverse(slow);
//		while(slow!=null) {
//			if(fast.val!=slow.val) {
//				return false;
//			}
//			fast=fast.next;
//			slow=slow.next;
//		}
//		return true;
//	}
//	private ListNode reverse(ListNode head) {
//		ListNode prefix=null;
//		
//		while(head!=null) {
//			ListNode next=head.next;
//			head.next=prefix;
//			prefix=head;
//			head=next;
//		}
//		return prefix;
//	}
	
	private ListNode frontPointer;

    private boolean recursivelyCheck(ListNode currentNode) {
        if (currentNode != null) {
            if (!recursivelyCheck(currentNode.next)) return false;
            if (currentNode.val != frontPointer.val) return false; /* use stack to remember last n node*/
            frontPointer = frontPointer.next;
        }
        return true; /* null return true*/
    }

    public boolean isPalindrome(ListNode head) {
        frontPointer = head;
        return recursivelyCheck(head);
    }
}

class ListNode {
	int val;
	ListNode next;

	ListNode() {
	}

	ListNode(int val) {
		this.val = val;
	}

	ListNode(int val, ListNode next) {
		this.val = val;
		this.next = next;
	}
}
