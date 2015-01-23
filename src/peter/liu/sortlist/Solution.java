package peter.liu.sortlist;

/**
 * Sort a linked list in O(n log n) time using constant space complexity.
 * 
 * @author pcnsh197
 *
 */
public class Solution {
    public ListNode sortList(ListNode head) {
        int length=getLengthOfList(head);
        return binarySort(head,length);
    }
    
    private ListNode binarySort(ListNode head,int length){
        if(length<2){
            return head;
        }
        if(length==2){
            return sortFunction(head);
        }else{
            int halfLength=length/2;
            ListNode iterator=splitList(head,halfLength);
            head=binarySort(head,length/2);
            iterator=binarySort(iterator,length-length/2);
            return merge(head,iterator);
        }
    }
    
    private int getLengthOfList(ListNode head){
        int length=0;
        ListNode iterator=head;
        while(iterator!=null){
            length++;
            iterator=iterator.next;
        }
        return length;
    }
    
    private ListNode splitList(ListNode head,int halfLength){
        ListNode iterator=head;
        while(halfLength>0){
            if(halfLength!=1){
                iterator=iterator.next;
            }else{
                ListNode temp=iterator;
                iterator=iterator.next;
                temp.next=null;
            }
            halfLength--;
        }
        return iterator;
    }
    private ListNode sortFunction(ListNode head) {
        if (head.next.val < head.val) {
            ListNode temp = head.next;
            temp.next = head;
            head.next = null;
            head = temp;
        }
        return head;
    }
    private ListNode merge(ListNode first,ListNode second){
        ListNode mergedHead=null;
        if(first.val<second.val){
            mergedHead=first;
            first=first.next;
        }else{
            mergedHead=second;
            second=second.next;
        }
        ListNode iterator=mergedHead;
        while(first!=null&&second!=null){
            if(first.val<second.val){
                iterator.next=first;
                first=first.next;
            }else{
                iterator.next=second;
                second=second.next;
            }
            iterator=iterator.next;
        }
        iterator.next=(first!=null)?first:second;
        return mergedHead;
    }
}


/**
 * Definition for singly-linked list.
 */

class ListNode {
    int val;
    ListNode next;

    ListNode(int x) {
        val = x;
        next = null;
    }
}