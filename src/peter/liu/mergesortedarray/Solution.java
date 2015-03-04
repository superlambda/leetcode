package peter.liu.mergesortedarray;

/**
 * Given two sorted integer arrays A and B, merge B into A as one sorted array.
 * 
 * Note: You may assume that A has enough space (size that is greater or equal
 * to m + n) to hold additional elements from B. The number of elements
 * initialized in A and B are m and n respectively.
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	/**
	 * From left to right 
	 * @param A
	 * @param m
	 * @param B
	 * @param n
	 */
//	public void merge(int A[], int m, int B[], int n) {
//		for(int i=m-1;i>=0;i--){
//			A[i+n]=A[i];
//		}
//		int indexA=n;
//		int indexB=0;
//		int index=0;
//		while(indexA<m+n&&indexB<n){
//			if(A[indexA]<B[indexB]){
//				A[index++]=A[indexA++];
//			}else{
//				A[index++]=B[indexB++];
//			}
//		}
//		while(indexB<n){
//			A[index++]=B[indexB++];
//		}
//	}
	
	/**
	 * From right to left.
	 * @param A
	 * @param m
	 * @param B
	 * @param n
	 */
	public void merge(int A[], int m, int B[], int n) {
		int indexA=m-1;
		int indexB=n-1;
		int index=m+n-1;
		while(indexA>=0&&indexB>=0){
			if(A[indexA]>B[indexB]){
				A[index--]=A[indexA--];
			}else{
				A[index--]=B[indexB--];
			}
		}
		while(indexB>=0){
			A[index--]=B[indexB--];
		}
	}
}
