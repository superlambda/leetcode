package inverview.garbagecollection;

import java.lang.ref.WeakReference;

public class TestWeakReference {

	// to store object name
	String obj_name;

	public TestWeakReference(String obj_name) {
		this.obj_name = obj_name;
	}

	// Driver method
	public static void main(String args[]) throws InterruptedException {
		TestWeakReference t1 = new TestWeakReference("t1");

		WeakReference<TestWeakReference> weakReference = new WeakReference<TestWeakReference>(t1); 
		t1 = null;
		// calling garbage collector
		System.gc();
		Thread.sleep(1000);
		System.out.println("weakReference: " + weakReference.get());
	}

	@Override
	protected void finalize() throws Throwable {
		// will print name of object
		System.out.println(this.obj_name + " successfully garbage collected");
	}

}
