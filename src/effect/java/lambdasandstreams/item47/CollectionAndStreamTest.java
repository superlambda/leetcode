package effect.java.lambdasandstreams.item47;

import java.util.stream.Stream;   
import java.util.stream.StreamSupport;

public class CollectionAndStreamTest {

	/*
	 * Adapter from Stream<E> to Iterable<E>
	 */
	public static <E> Iterable<E> iterableOf(Stream<E> stream) {
		return stream::iterator;
	}
	
	/*
	 * Adapter from Iterable<E> to Stream<E>
	 */
	public static <E> Stream<E> streamOf(Iterable<E> iterable) {
		return StreamSupport.stream(iterable.spliterator(), false);
	}

	public static void main(String[] args) {
//		for(ProcessHandle ph: (Iterable<ProcessHandle>) ProcessHandle.allProcesses()::iterator) {
//			System.out.println("pid: " + ph.pid());
//		}

		for (ProcessHandle ph : iterableOf(ProcessHandle.allProcesses())) {
			System.out.println("pid: " + ph.pid());
		}
	}

}
