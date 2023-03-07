package inverview.nomura.sumzero;

public class Solution {
	
	public int solution(int[] A) {
		int numbers = 0;
		for(int i =0; i< A.length; i++) {
			int sum=0;
			for(int j=i;j<A.length;j++) {
				sum+=A[j];
				if(sum==0) {
					numbers++;
					if(numbers>1000000000) {
						return -1;
					}
				}
			}
		}
		return numbers>1000000000? -1:numbers;
		
	}

}
