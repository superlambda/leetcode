package inverview.producerconsumer;

import java.util.concurrent.BlockingQueue;

public class Consumer extends Thread {
	private BlockingQueue<Integer> sharedQueue;

	public Consumer(BlockingQueue<Integer> aQueue) {
		super("CONSUMER");
		this.sharedQueue = aQueue;
	}

	public void run() {
		try {
			while (true) {
				Integer item = sharedQueue.take();
				System.out.println(getName() + " consumed " + item);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
