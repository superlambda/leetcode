package inverview.override;

public class Sub extends Super {
	@Override
	public int getNumber() {
		return 2;
	}

	public static void main(String args[]) {

		int x = 4;

		System.out.println(x++);

		System.out.println(Math.round(3.7));
		System.out.println(Math.ceil(3.7));
	}

}
