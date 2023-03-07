package inverview.garbagecollection;

/**
 * Nullifying the reference variable : When all the reference variables of an
 * object are changed to NULL, it becomes unreachable and thus becomes eligible
 * for garbage collection.
 *  
 * @author liuyingjie
 *
 */
public class TestNullifyingTheReferenceVariable {

	// to store object name
	String obj_name;

	public TestNullifyingTheReferenceVariable(String obj_name) {
		this.obj_name = obj_name;
	}

	// Driver method
	public static void main(String args[]) throws InterruptedException {
		TestNullifyingTheReferenceVariable t1 = new TestNullifyingTheReferenceVariable("t1");

		/* t1 being used for some purpose in program */

		/*
		 * When there is no more use of t1, make the object referred by t1 eligible for
		 * garbage collection
		 */
		t1 = null;

		// calling garbage collector
		System.gc();

		Thread.sleep(1000);
		System.out.println("Program exist");
	}

	@Override
	/*
	 * Overriding finalize method to check which object is garbage collected
	 */
	protected void finalize() throws Throwable {
		// will print name of object
		System.out.println(this.obj_name + " successfully garbage collected");
	}

}
