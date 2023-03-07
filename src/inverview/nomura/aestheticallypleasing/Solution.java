package inverview.nomura.aestheticallypleasing;

public class Solution {
	
	public int solution(int[] A) {
		int numberOfWays = 0;
		if(A.length%2==0) {
			if(isPleasing(A)) {
				return 0;
			}else {
				return -1;
			}
			
		}
		int[] temp = new int[A.length-1];
		for(int i=0; i<A.length; i++) {
			int index=0;
			for(int j=0; j<A.length; j++) {
				if(j != i) {
					temp[index] = A[j];
					index++;
				}
			}
			if(isPleasing(temp)) {
				numberOfWays++;
			}
			
		}
		return numberOfWays;
	}
	
	private boolean isPleasing(int[] array) {
		for(int i =1; i <array.length - 1; i++) {
			if((array[i] > array[i-1] && array[i] < array[i+1]) ||
			   (array[i] < array[i-1] && array[i] > array[i+1])) {
				return false;
			}
		}
		return true;
	}

}
