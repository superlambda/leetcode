package inverview.blankfinal;

/**
 *
 * The blank final variable in Java is a final variable that is not
 * initialized while declaration, instead they are initialized in a constructor.
 * Java program to demonstrate that blank final variable in Java must be
 * initialized in the constructor. Failing to initialized the blank final
 * variable will result in a compile-time error.
 * 
 * static and final variables are treated as compile-time constant and 
 * their value is replaced during compile time only.
 * 
 * @author http://java67.blogspot.com
 */
public class BlankFinalVariable {

	private final int blankFinalVariable; // must be initialized in a constructor

	public BlankFinalVariable() {
		this(6); // this is Ok
		// this.blankFinalVariable = 3;
	}

	public BlankFinalVariable(int number) {
		this.blankFinalVariable = number;
	}

	public static void main(String args[]) {
       BlankFinalVariable clazz = new BlankFinalVariable();
       System.err.println("Value of blank final variable in Java : " + clazz.blankFinalVariable);
   }

}
