package inverview.lambda;

public class TestCube {

	public static void main(String[] args) {
		int x = 5; 
		Cube c = (int a)->a*a*a;  
		int result = c.calculate(x); 
		System.out.println(result); 
	}

}
