package peter.liu.mergeksortedlists;

import java.util.ArrayList;
import java.util.List;

/**
 * Merge k sorted linked lists and return it as one sorted list. Analyze and
 * describe its complexity.
 * 
 * @author liuyingjie
 *
 */
public class Solution {
	public ListNode mergeKLists(ListNode[] lists) {
		
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
