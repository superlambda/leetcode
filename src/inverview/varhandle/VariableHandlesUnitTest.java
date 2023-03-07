package inverview.varhandle;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

import org.junit.jupiter.api.Test;

public class VariableHandlesUnitTest {
	public int publicTestVariable = 1;
	private int privateTestVariable = 1;
	public int variableToSet = 1;
	public int variableToCompareAndSet = 1;
	public int variableToGetAndAdd = 0;
	public byte variableToBitwiseOr = 0;

	@Test
	public void test1() throws NoSuchFieldException, IllegalAccessException {
		VarHandle PUBLIC_TEST_VARIABLE = MethodHandles.lookup().in(VariableHandlesUnitTest.class)
				.findVarHandle(VariableHandlesUnitTest.class, "publicTestVariable", int.class);

		assertEquals(1, PUBLIC_TEST_VARIABLE.coordinateTypes().size());
		assertEquals(VariableHandlesUnitTest.class, PUBLIC_TEST_VARIABLE.coordinateTypes().get(0));
		
		assertEquals(1, (int) PUBLIC_TEST_VARIABLE.get(this));
		
		PUBLIC_TEST_VARIABLE.set(this, 15);
		assertEquals(15, (int) PUBLIC_TEST_VARIABLE.get(this));
		
//		//TODO why
//		PUBLIC_TEST_VARIABLE.compareAndSet(this, 15, 100);
//		assertEquals(100, (int) PUBLIC_TEST_VARIABLE.get(this));
		
		int before = (int) PUBLIC_TEST_VARIABLE.getAndAdd(this, 200);
		assertEquals(15, before);
		assertEquals(215, (int) PUBLIC_TEST_VARIABLE.get(this));
		
	}

	@Test
	public void testPrivateLookup() throws NoSuchFieldException, IllegalAccessException {
		VarHandle PRIVATE_TEST_VARIABLE = MethodHandles
				.privateLookupIn(VariableHandlesUnitTest.class, MethodHandles.lookup())
				.findVarHandle(VariableHandlesUnitTest.class, "privateTestVariable", int.class);

		assertEquals(1, PRIVATE_TEST_VARIABLE.coordinateTypes().size());
		assertEquals(VariableHandlesUnitTest.class, PRIVATE_TEST_VARIABLE.coordinateTypes().get(0));
	}
	
	@Test
	public void testArray() {
		VarHandle arrayVarHandle = MethodHandles.arrayElementVarHandle(int[].class);

		assertEquals(2, arrayVarHandle.coordinateTypes().size());
		assertEquals(int[].class, arrayVarHandle.coordinateTypes().get(0));
		System.out.println(arrayVarHandle.coordinateTypes().get(1));
	}

}
