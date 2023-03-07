package inverview.lambda;

import java.util.function.Predicate;

public class TestLambdaWithOneParameter {

	public static void main(String[] args) {
		Validator validator = new Validator();

		String city = "New York";

		boolean isValid = validator.isDataValid(city, (String info) -> {

			String regx = "^[a-zA-Z0-9]*$";

			return info.matches(regx);

		});

		System.out.println(" The value returned from lambda is: " + isValid);

	}

	private static class Validator {
		public boolean isDataValid(String data, Predicate<String> predicate) {
			return predicate.test(data);
		}

	}

}
