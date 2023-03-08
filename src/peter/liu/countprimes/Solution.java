package peter.liu.countprimes;

/**
 * Count the number of prime numbers less than a non-negative number, n.
 * 
 * @author liuyingjie
 *
 */
public class Solution {
	public int countPrimes(int n) {
		boolean[] isPrime = new boolean[n];
		for (int i = 2; i < n; i++) {
			isPrime[i] = true;
		}
		// Loop's ending condition is i * i < n instead of i < sqrt(n)
		// to avoid repeatedly calling an expensive function sqrt().
		for (int i = 2; i * i < n; i++) {
			if (isPrime[i]){
				for (int j = i; j * i < n; j++) {
					isPrime[j*i] = false;
				}
			}
		}
		int count = 0;
		for (int i = 2; i < n; i++) {
			if (isPrime[i]) {
				count++;
			}
		}
		return count;
	}
}
