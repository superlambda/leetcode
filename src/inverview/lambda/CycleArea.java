package inverview.lambda;

import java.util.Scanner;

public class CycleArea {

	public static void main(String[] args) {
		try (Scanner kb = new Scanner(System.in)) {
			System.out.println("Enter the value of radius");
			int r = kb.nextInt();
			MyInterface ref = () -> 3.1415;
			System.out.println("Area of Circle with given radius is = " + r * r * ref.getPiValue());

		}
	}

}
