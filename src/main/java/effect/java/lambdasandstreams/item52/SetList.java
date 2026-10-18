package effect.java.lambdasandstreams.item52;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SetList {

	public static void main(String[] args) {
		Set<Integer> set = new TreeSet<>();
		List<Integer> list = new ArrayList<>();
		for(int i = -3; i<3; i++) {
			set.add(i);
			list.add(i);
		}
		
		for(int i = 0; i<3; i++) {
			set.remove(i);
			list.remove(i);
		}
		System.out.println(set + " " + list);
		new Thread(System.out::println).start();
		
//		ExecutorService exec = Executors.newCachedThreadPool();
//		exec.submit(System.out::println);
	}

}
