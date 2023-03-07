package inverview.stringliteralvsobject;


/**
 * 
 * @author liuyingjie
 * Thanks to the immutability of Strings in Java, the JVM can optimize the amount 
 * of memory allocated for them by storing only one copy of each literal String in
 * the pool. This process is called interning.
 * In general, we should use the String literal notation when possible. It is 
 * easier to read and it gives the compiler a chance to optimize our code.
 * Before Java 7, the JVM placed the Java String Pool in the PermGen space, which has a 
 * fixed size — it can't be expanded at runtime and is not eligible for garbage collection.
 * The risk of interning Strings in the PermGen (instead of the Heap) is that we can get an 
 * OutOfMemory error from the JVM if we intern too many Strings. From Java 7 onwards, the 
 * Java String Pool is stored in the Heap space, which is garbage collected by the JVM. 
 * The advantage of this approach is the reduced risk of OutOfMemory error because 
 * unreferenced Strings will be removed from the pool, thereby releasing memory.
 */
public class ComparePerformance {
	public static void main(String args[]) {
		// Initialization time for String
		// Literal
		long start1 = System.currentTimeMillis();

		for (int i = 0; i < 10000; i++) {
			String s1 = "GeeksForGeeks";
			String s2 = "Welcome";
		}

		long end1 = System.currentTimeMillis();
		long total_time = end1 - start1;

		System.out.println("Time taken to execute" + " string literal = " + total_time);

		// Initialization time for String
		// object
		long start2 = System.currentTimeMillis();

		for (int i = 0; i < 10000; i++) {
			String s3 = new String("GeeksForGeeks");
			String s4 = new String("Welcome");
		}

		long end2 = System.currentTimeMillis();
		long total_time1 = end2 - start2;

		System.out.println("Time taken to execute" + " string object=" + total_time1);
	}
}
