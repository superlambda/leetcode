package peter.liu.permutationsequence;

import java.util.LinkedList;

/**
 * The set [1,2,3,…,n] contains a total of n! unique permutations.
 * 
 * By listing and labeling all of the permutations in order, We get the
 * following sequence (ie, for n = 3):
 * 
 * "123" "132" "213" "231" "312" "321" Given n and k, return the kth permutation
 * sequence.
 * 
 * Note: Given n will be between 1 and 9 inclusive.
 * 
 * @author superlambda
 *
 */
public class Solution {

	/**
	 * The logic is as follows: for n numbers the permutations can be divided to
	 * (n-1)! groups, thus k/(n-1)! indicates the index of current number, and
	 * k%(n-1)! denotes remaining sequence (to the right). We keep doing this
	 * until n reaches 0, then we get n numbers permutations that is kth.
	 * 
	 * TODO this is not my solution.
	 * @param n
	 * @param k
	 * @return
	 */
	public String getPermutation(int n, int k) {
		LinkedList<Integer> list = new LinkedList<>();
		for (int i = 1; i <= n; i++) {
			list.add(i);
		}
		int fact = 1;
		for (int i = 2; i <= n; i++) {
			fact *= i; // factorial
		}

		StringBuilder strBuilder = new StringBuilder();
		for (k--; n > 0; n--) {
			fact /= n;
			strBuilder.append(list.remove(k / fact));
			k %= fact;
		}

		return strBuilder.toString();
	}

	// private void getNextPermutation(List<Integer> pList,
	// List<Integer> leftList, int length, int k) {
	// for (int i = 0; i < leftList.size(); i++) {
	// List<Integer> ppList = new ArrayList<>(pList);
	// ppList.add(leftList.get(i));
	// if (ppList.size() < length) {
	// if (numberCount < k) {
	// List<Integer> lleftList = new ArrayList<>(leftList);
	// lleftList.remove(i);
	// getNextPermutation(ppList, lleftList, length, k);
	// }
	// } else {
	// numberCount += 1;
	// if (numberCount == k) {
	// result.addAll(ppList);
	// break;
	// }
	// }
	// }
	// }

}
