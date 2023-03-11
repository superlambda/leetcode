package inverview.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestThreadPoolAlreadyFilled {

	public static void main(String[] args) {

		BlockingQueue<Runnable> linkedBlockingDeque = new LinkedBlockingDeque<Runnable>(10);
		ExecutorService executor = new ThreadPoolExecutor(10, 10, 30, TimeUnit.SECONDS, linkedBlockingDeque);

		try {
			List<Future<String>> list = new ArrayList<Future<String>>();
			Callable<String> callable = new MyCallable();
			for (int i = 0; i < 100; i++) {
				Future<String> future = executor.submit(callable);
				list.add(future);
			}
		} finally {
			executor.shutdown();
		}
	}

}

class MyCallable implements Callable<String> {

	@Override
	public String call() throws Exception {
		while (true) {
			Thread.sleep(1000);
		}

	}

}
