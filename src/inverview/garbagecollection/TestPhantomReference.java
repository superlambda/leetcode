package inverview.garbagecollection;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

public class TestPhantomReference {

	// to store object name
	String obj_name;

	public TestPhantomReference(String obj_name) {
		this.obj_name = obj_name;
	}

	public static void main(String args[]) throws InterruptedException {
		TestPhantomReference t1 = new TestPhantomReference("t1");
		
		ReferenceQueue refQueue = new ReferenceQueue(); 
		PhantomReference<TestPhantomReference> phantomReference = new PhantomReference<TestPhantomReference>(t1,refQueue); 
		
		t1 = null;
		System.gc();
		Thread.sleep(1000);
		System.out.println("phantomReference: " + phantomReference.get());
		
	}

	@Override
	protected void finalize() throws Throwable {
		// will print name of object
		System.out.println(this.obj_name + " successfully garbage collected");
	}

}
