package inverview.random;

import java.util.Random;

public class RandomTest {

	public static void main(String[] args) {
		Random random = new Random();
		random.ints().limit(10).forEach(System.out::println);

	}

}
