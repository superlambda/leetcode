package inverview.blockingmethod;

import java.io.IOException;

public class BlockingCallTest {

	public static void main(String[] args) throws IOException {
		System.out.println("Calling blocking method in Java");
		int input = System.in.read();
		System.out.println("Blocking method is finished input: " + input);

	}

}
