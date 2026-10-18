package peter.liu.fizzbuzz;

import java.util.LinkedList;
import java.util.List;

public class Solution {
	public List<String> fizzBuzz(int n) {

		String fizzBuzz = "FizzBuzz".intern();
		String fizz = "Fizz".intern();
		String buzz = "Buzz".intern();

		LinkedList<String> result = new LinkedList<>();
		for (int i = 1; i <= n; i++) {
			int mode3 = i % 3;
			int mode5 = i % 5;
			if (mode3 == 0 && mode5 == 0) {
				result.addLast(fizzBuzz);
			} else if (mode3 == 0) {
				result.addLast(fizz);
			} else if (mode5 == 0) {
				result.addLast(buzz);
			} else {
				result.add(String.valueOf(i));
			}
		}
		return result;

	}
}
