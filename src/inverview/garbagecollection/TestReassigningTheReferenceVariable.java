package inverview.garbagecollection;

/**
 * Reassigning the reference variable: When reference id of one object is
 * referenced to reference id of some other object then the previous object has
 * no any longer reference to it and becomes unreachable and thus becomes
 * eligible for garbage collection.
 * 
 * @author liuyingjie
 *
 */
public class TestReassigningTheReferenceVariable {
	// to store object name
	String obj_name;

	public TestReassigningTheReferenceVariable(String obj_name) {
		this.obj_name = obj_name;
	}

	// Driver method
	public static void main(String args[]) throws InterruptedException {
		TestReassigningTheReferenceVariable t1 = new TestReassigningTheReferenceVariable("t1");
		TestReassigningTheReferenceVariable t2 = new TestReassigningTheReferenceVariable("t2");

		// t1 now referred to t2
		t1 = t2;

		// calling garbage collector
		System.gc();
		Thread.sleep(1000);
	}

	@Override
	protected void finalize() throws Throwable {
		// will print name of object
		System.out.println(this.obj_name + " successfully garbage collected");
	}

}
