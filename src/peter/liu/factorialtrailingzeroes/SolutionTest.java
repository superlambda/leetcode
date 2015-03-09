package peter.liu.factorialtrailingzeroes;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void test() {
		int count=0;
		for(int i=5;i<=125;i++){
			int value=i;
			while(value%5==0){
				System.out.print(" "+value);
				value/=5;
				count++;
			}
		}
		System.out.println("count: "+count);
	}

}
