package peter.liu.removeelement;

/**
 * Given an array and a value, remove all instances of that value in place and
 * return the new length.
 * 
 * The order of elements can be changed. It doesn't matter what you leave beyond
 * the new length.
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	public int removeElement(int[] A, int elem) {
		int i=0,length=A.length-1;
		while(i<=length){
			if(A[i]==elem){
				A[i]=A[length];
				length--;
			}else{
				i++;
			}
		}
		return length+1;
	}
}
