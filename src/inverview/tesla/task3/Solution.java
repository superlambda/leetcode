package inverview.tesla.task3;

import java.util.HashMap;

public class Solution {

	public int solution(int[] A) {
		int n = A.length;
		HashMap<Integer, Integer> hash = new HashMap<>();
		long count = 0;
        int sum = 0;
		for (int i = 0; i < n; i++) {
			sum += A[i];
			if (sum == 0) {
				count++;
			}
			if (hash.get(sum) != null) {
				count += hash.get(sum);
			}
			if (hash.get(sum) != null) {
				hash.put(sum, hash.get(sum) + 1);
			} else {
				hash.put(sum, 1);
			}
		}
		
		if(count>1000000000) {
			return -1;
		}
		return (int)count;
	}

}
