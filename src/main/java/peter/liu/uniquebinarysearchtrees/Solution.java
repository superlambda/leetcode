package peter.liu.uniquebinarysearchtrees;

/**
 * Given n, how many structurally unique BST's (binary search trees) that store values 1...n?

For example,
Given n = 3, there are a total of 5 unique BST's.

   1         3     3      2      1
    \       /     /      / \      \
     3     2     1      1   3      2
    /     /       \                 \
   2     1         2                 3
 * @author superlambda
 *
 */
public class Solution {
	//dp solution copied from discussion.
	public int numTrees(int n) {
		int[] a = new int[n+1];
        a[0] = a[1] = 1;
        for(int i = 2; i <= n; i++){
            for(int j = 0; j < i; j++){  
                a[i] += a[j]*a[i - 1 - j];
            }
        }
        return a[n];
	}
}
