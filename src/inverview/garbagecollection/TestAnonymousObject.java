package inverview.garbagecollection;

/**
 * Anonymous object : The reference id of an anonymous object is not stored
 * anywhere. Hence, it becomes unreachable.
 * 
 * @author liuyingjie
 *
 */
public class TestAnonymousObject {
	String obj_name;

	public TestAnonymousObject(String obj_name) {
		this.obj_name = obj_name;
	}

	// Driver method
	public static void main(String args[]) {
		// anonymous object without reference id
		new TestAnonymousObject("t1");

		// calling garbage collector
		System.gc();
	}

	@Override
	protected void finalize() throws Throwable {
		// will print name of object
		System.out.println(this.obj_name + " successfully garbage collected");
	}

}
