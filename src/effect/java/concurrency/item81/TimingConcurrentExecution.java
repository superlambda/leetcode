package effect.java.concurrency.item81;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TimingConcurrentExecution {

	public static long time(Executor executor, int concurrency, Runnable action) throws InterruptedException {
		System.out.println("time 1");
		CountDownLatch ready = new CountDownLatch(concurrency);
		CountDownLatch start = new CountDownLatch(1);
		CountDownLatch done = new CountDownLatch(concurrency);
		System.out.println("time 2");
		for (int i = 0; i < concurrency; i++) {
			executor.execute(() -> {
				ready.countDown();
				try {
					start.await();
					action.run();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				} finally {
					done.countDown();
				}
			});
		}
		System.out.println("time 3");
		ready.await();
		long startNanos = System.nanoTime();
		System.out.println("time 4");
		start.countDown();
		done.await();
		return System.nanoTime()-startNanos;

	}

	public static void main(String[] args) throws InterruptedException {
		ExecutorService exec =  Executors.newFixedThreadPool(16);
		System.out.println(time(exec,16, new Runnable() {
			@Override
			public void run() {
				System.out.println("Action is executed");
			}
			
		}));

	}

}
