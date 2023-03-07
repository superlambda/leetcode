package inverview.stringliteralvsobject;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class StringTest {

	@Test
	void test() {
		String constantString1 = "Baeldung";
		String constantString2 = "Baeldung";
		assertEquals(constantString1==constantString2, true);
		
		String constantString = "Baeldung";
		String newString = new String("Baeldung");
		assertEquals(newString==constantString, false);
		
		String newString2 = new String("Baeldung");
		String newString3 = new String("Baeldung");
		assertEquals(newString2==newString3, false);
		
		String internedConstantString = "interned Baeldung";
		String newString4 = new String("interned Baeldung");

		assertEquals(internedConstantString==newString4, false);

		String internedString = newString4.intern();
		assertEquals(internedConstantString==internedString, true);
		
	}

}
