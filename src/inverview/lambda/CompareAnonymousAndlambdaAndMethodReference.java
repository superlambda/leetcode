package inverview.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CompareAnonymousAndlambdaAndMethodReference {

	public int transformAndAdd(List<Integer> l, Function<Integer, Integer> f) {
		int result = 0;
		for (Integer s : l)
			result += f.apply(s);

		return result;
	}

	public static void main(String[] args) {
		List<Integer> list = new ArrayList<>();
		
		// Add some element to list
		list.add(1);
		list.add(2);
		list.add(3);
		// Using an anonymous class
		CompareAnonymousAndlambdaAndMethodReference comp = new CompareAnonymousAndlambdaAndMethodReference();
		int result =comp.transformAndAdd(list, new Function<Integer, Integer>() {
		    public Integer apply(Integer i) {
		        return OpsUtil.doHalf(i);
		    }
		});
		System.out.println("result1: " +result);

		// Using a lambda expression
		result = comp.transformAndAdd(list, i -> OpsUtil.doHalf(i));
		System.out.println("result2: " +result);

		// Using a method reference
		result = comp.transformAndAdd(list, OpsUtil::doHalf);
		System.out.println("result3: " +result);

	}

}

class OpsUtil {

    public static Integer doHalf(Integer x) {
        return x / 2;
    }
    public static Integer doSquare(Integer x) {

        return x * x;
    }
    
}
