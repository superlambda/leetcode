package effect.java.annotation.item39;

import java.lang.reflect.Method;

import effect.java.annotation.ExceptionTest;

public class RunExceptionTests {

	public static void main(String[] args) throws ClassNotFoundException {
		int tests = 0;
		int passed = 0;
		Class<?>  testClass =  Class.forName("effect.java.annotation.item39.Sample2");
		for (Method m: testClass.getDeclaredMethods()) {
			if(m.isAnnotationPresent(ExceptionTest.class)) {
				tests ++;
				try {
					m.invoke(null);
					System.out.printf("Test %s failed: no exception%n", m);
				} catch (Throwable wrappedExc) {
					Throwable exc =  wrappedExc.getCause();
					int oldPassed = passed;
					Class <? extends Throwable>[] excTypes = m.getAnnotation(ExceptionTest.class).value();
					for (Class<? extends Throwable> excType: excTypes) {
						if (excType.isInstance(exc)) {
							passed ++;
							break;
						}
					}
					if (passed == oldPassed) {
						System.out.printf("Test %s failed: %s %n", m, exc);
					}
				}
			}
		}
		System.out.printf("Passed: %d, Failed: %d%n", passed, tests - passed);

	}

}
