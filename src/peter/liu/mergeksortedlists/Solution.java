package peter.liu.mergeksortedlists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Merge k sorted linked lists and return it as one sorted list. Analyze and
 * describe its complexity.
 * 
 * @author liuyingjie
 *
 */
public class Solution {
	public ListNode mergeKListsSlow(ListNode[] lists) {
		
		List<ListNode> sortList=new ArrayList<>();
		for(ListNode node:lists){
			if(node!=null){
				if(sortList.isEmpty()){
					sortList.add(node);
				}else{
					int index=indexToAdd(sortList,node.val,0,sortList.size()-1);
					sortList.add(index, node);
				}
			}
		}
		ListNode head=new ListNode(0);
		ListNode iterator=head;
		while (!sortList.isEmpty()) {
			ListNode node = sortList.get(0);

			while (node != null && (sortList.size() == 1 || node.val <= sortList.get(1).val)) {
				iterator.next = node;
				iterator = iterator.next;
				node = node.next;
			}
			
			sortList.remove(0);
			
			if (node != null) {
				int index=indexToAdd(sortList,node.val,0,sortList.size()-1);
				sortList.add(index, node);
			}				
			
		}
		return head.next;
	}
	
	private int indexToAdd(List<ListNode> sortList,int val,int start,int end){
		int mid =0;
		while(start<=end){
			mid=(end-start)/2 +start;
			int midVal=sortList.get(mid).val;
			if(val==midVal){
				return mid;
			}else if(val<midVal){
				if(mid>0&&sortList.get(mid-1).val>val){
					end=mid-1;
				}else{
					return mid;
				}
			}else{
				if(mid<sortList.size()-1&&sortList.get(mid+1).val<val){
					start=mid+1;
				}else{
					return mid+1;
				}
				
			}
		}
		return mid;
	}
	
	//Divide and corner algorithm, so handsome!
	
	/**
	 * I think my code's complexity is also O(nlogk) and not using heap or
	 * priority queue, n means the total elements and k means the size of list.
	 * 
	 * The mergeTwoLists functiony in my code comes from the problem Merge Two
	 * Sorted Lists whose complexity obviously is O(n), n is the sum of length
	 * of l1 and l2.
	 * 
	 * To put it simpler, assume the k is 2^x, So the progress of combination is
	 * like a full binary tree, from bottom to top. So on every level of tree,
	 * the combination complexity is n, beacause every level have all n numbers
	 * without repetition. The level of tree is x, ie logk. So the complexity is
	 * O(nlogk).
	 * 
	 * for example, 8 ListNode, and the length of every ListNode is 
	 * x1, x2, x3,x4, x5, x6, x7, x8, total is n. 
	 * on level 3: x1+x2, x3+x4, x5+x6, x7+x8 sum: n 
	 * on level 2: x1+x2+x3+x4, x5+x6+x7+x8 sum: n 
	 * on level 1: x1+x2+x3+x4+x5+x6+x7+x8 sum: n total 3n, nlog8
	 * 
	 * @param list
	 * @return
	 */
	public ListNode mergeKLists(ListNode[] list) {
		List<ListNode> lists=Arrays.asList(list);
		return mergeKLists(lists);
	}
	
	public ListNode mergeKLists(List<ListNode> lists) {
        if (lists.size()==0) return null;
        if (lists.size()==1) return lists.get(0);
        if (lists.size()==2) return mergeTwoLists(lists.get(0), lists.get(1));
        return mergeTwoLists(mergeKLists(lists.subList(0, lists.size()/2)), 
            mergeKLists(lists.subList(lists.size()/2, lists.size())));
    }
	public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
		if (l1 == null) {
			return l2;
		}
		if (l2 == null) {
			return l1;
		}
		ListNode header = null;
		ListNode iterator = null;
		if (l1.val > l2.val) {
			iterator = header = l2;
			l2 = l2.next;
		} else {
			iterator = header = l1;
			l1 = l1.next;
		}
		while (l1 != null && l2 != null) {
			if (l1.val > l2.val) {
				iterator.next = l2;
				l2 = l2.next;
			} else {
				iterator.next = l1;
				l1 = l1.next;
			}
			iterator = iterator.next;
		}
		iterator.next = l1 != null ? l1 : l2;
		return header;
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
	}
}
