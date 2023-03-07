package inverview.lambda;

public class TestLambdaWithoutParameter {

	public static void main(String[] args) {
		Statement s = () -> {
			return "Hello World. Welcome to Simplilearn.";
		};

		System.out.println(s.greet());

	}

}
