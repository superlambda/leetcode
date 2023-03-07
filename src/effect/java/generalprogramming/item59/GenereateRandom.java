package effect.java.generalprogramming.item59;

import java.util.Random;

public class GenereateRandom {
	static Random rnd = new Random();
	static int random(int n) {
		return Math.abs(rnd.nextInt()) % n;
	}

	public static void main(String[] args) {
		int n = 2 * (Integer.MAX_VALUE / 3);
		int low = 0;
		for(int i = 0; i < 1000000; i++) {
			if (random(n) < n/2) {
				low++;
			}
		}
		System.out.println(low);
		System.out.println(Integer.MAX_VALUE);
		System.out.println(Integer.MIN_VALUE);
		System.out.println(Math.abs(Integer.MIN_VALUE));
		System.out.println(Integer.MIN_VALUE % 13);

	}

}
