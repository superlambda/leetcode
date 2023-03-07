package inverview.producerconsumer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Producer Consumer Problem solution using BlockingQueue in Java.
 * BlockingQueue not only provide a data structure to store data but
 * also gives you flow control, require for inter thread communication.
 */

public class ProducerConsumerSolution {

	public static void main(String[] args) {
		BlockingQueue<Integer> sharedQ = new LinkedBlockingQueue<Integer>();
		Producer p = new Producer(sharedQ);
		Consumer c = new Consumer(sharedQ);
		p.start();
		c.start();
	}

}
