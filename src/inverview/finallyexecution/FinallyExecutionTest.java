package inverview.finallyexecution;

public class FinallyExecutionTest {

	public static void main(String[] args) {
		try {
			divide(100, 0);
		} finally {
			System.out.println("finally in main");
		}
	}

	private static void divide(int n, int div) {
		try {
			int ans = n / div;
		} finally {
			System.out.println("finally of divide");
		}
	}

}
