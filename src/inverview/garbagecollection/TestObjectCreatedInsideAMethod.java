package inverview.garbagecollection;

/**
 * Object created inside a method : When a method is called it goes inside the
 * stack frame. When the method is popped from the stack, all its members dies
 * and if some objects were created inside it then these objects becomes
 * unreachable or anonymous after method execution and thus becomes eligible for
 * garbage collection
 * 
 * @author liuyingjie
 *
 */
public class TestObjectCreatedInsideAMethod {

	// to store object name
	String obj_name;

	public TestObjectCreatedInsideAMethod(String obj_name) {
		this.obj_name = obj_name;
	}

	static void show() {
		// object t1 inside method becomes unreachable when show() removed
		TestObjectCreatedInsideAMethod t1 = new TestObjectCreatedInsideAMethod("t1");
		display();

	}

	static void display() {
		// object t2 inside method becomes unreachable when display() removed
		TestObjectCreatedInsideAMethod t2 = new TestObjectCreatedInsideAMethod("t2");
	}

	// Driver method
	public static void main(String args[]) {
		// calling show()
		show();

		// calling garbage collector
		System.gc();
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
