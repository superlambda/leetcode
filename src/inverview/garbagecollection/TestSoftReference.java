package inverview.garbagecollection;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

public class TestSoftReference {

	// to store object name
	String obj_name;

	public TestSoftReference(String obj_name) {
		this.obj_name = obj_name;
	}

	// Driver method
	public static void main(String args[]) throws InterruptedException {
		TestSoftReference t1 = new TestSoftReference("t1");

		SoftReference<TestSoftReference> softReference = new SoftReference<TestSoftReference>(t1); 
		t1 = null;
		
		List<byte[]> bytes = new ArrayList<byte[]>();
        while(true) {
//        	System.out.println("softReference: " + softReference.get());
            bytes.add(new byte[8*1024*1024]);
            Thread.sleep(1);
        }
		
	}

	@Override
	protected void finalize() throws Throwable {
		// will print name of object
		System.out.println(this.obj_name + " successfully garbage collected");
	}

}
