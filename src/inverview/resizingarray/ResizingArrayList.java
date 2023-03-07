package inverview.resizingarray;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ResizingArrayList {

	/*
	 * Exception in thread "main" java.lang.reflect.InaccessibleObjectException:
	 * Unable to make field transient java.lang.Object[]
	 * java.util.ArrayList.elementData accessible: module java.base does not
	 * "opens java.util" to unnamed module @3c60b7e7 at
	 * java.base/java.lang.reflect.AccessibleObject.checkCanSetAccessible(
	 * AccessibleObject.java:354) at
	 * java.base/java.lang.reflect.AccessibleObject.checkCanSetAccessible(
	 * AccessibleObject.java:297) at
	 * java.base/java.lang.reflect.Field.checkCanSetAccessible(Field.java:178) at
	 * java.base/java.lang.reflect.Field.setAccessible(Field.java:172) at
	 * inverview.resizingarray.ResizingArrayList.findCapacity(ResizingArrayList.java
	 * :23) at
	 * inverview.resizingarray.ResizingArrayList.main(ResizingArrayList.java:11)
	 */
	public static void main(String[] args) throws Exception {
		ArrayList<Integer> list = new ArrayList<>();
		list.add(1);
		System.out.println("Size: " + list.size() + " Capacity: " + findCapacity(list));
		for (int i = 1; i < 10; i++) {
			list.add(i + 1);
		}
		System.out.println("Size: " + list.size() + " Capacity: " + findCapacity(list));
		list.add(11);
		System.out.println("Size: " + list.size() + " Capacity: " + findCapacity(list));
		list.ensureCapacity(200);
		list.trimToSize();
		

	}

	public static int findCapacity(ArrayList<Integer> list) throws Exception {
//		Field field = ArrayList.class.getDeclaredField("elementData");
//		field.setAccessible(true);
//		return ((Object[]) field.get(list)).length;
		return list.size();
	}

}
