package inverview.concurrentmodification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ConcurrentModificationTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> list= Collections.synchronizedList(new ArrayList<>());
		
		list.add("a");
		list.add("b");
		for(Iterator<String> ite=list.iterator(); ite.hasNext();ite.next()) {
			list.remove("a");
		}

	}

}
