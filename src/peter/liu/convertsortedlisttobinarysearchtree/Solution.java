package peter.liu.convertsortedlisttobinarysearchtree;

import java.util.ArrayList;
import java.util.List;

/**
 * Given a singly linked list where elements are sorted in ascending order,
 * convert it to a height balanced BST.
 * 
 * @author superlambda
 *
 */
public class Solution {
	public TreeNode sortedListToBST(ListNode head) {
		List<Integer> numList = new ArrayList<>();
		while (head != null) {
			numList.add(head.val);
			head = head.next;
		}
		if (numList.isEmpty()) {
			return null;
		}
		return getChildTree(numList, 0, numList.size() - 1);
	}

	private TreeNode getChildTree(List<Integer> numList, int from, int to) {
		int middle = (to - from) / 2 + from;
		if (middle < from || middle > to) {
			return null;
		}
		TreeNode node = new TreeNode(numList.get(middle));
		if (middle > from) {
			node.left = getChildTree(numList, from, middle - 1);
		}
		if (middle < to) {
			node.right = getChildTree(numList, middle + 1, to);
		}
		return node;
	}
}

class TreeNode {
	int val;
	TreeNode left;
	TreeNode right;

	TreeNode(int x) {
		val = x;
	}
}

class ListNode {
	int val;
	ListNode next;

	ListNode(int x) {
		val = x;
	}
}