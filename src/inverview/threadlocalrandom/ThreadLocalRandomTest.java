package inverview.threadlocalrandom;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class ThreadLocalRandomTest {

	public static void main(String[] args) throws InterruptedException {
		ExecutorService executor = Executors.newWorkStealingPool();
		List<Callable<Integer>> callables = new ArrayList<>();
		for (int i = 0; i < 1000; i++) {
		    callables.add(() -> {
		        return ThreadLocalRandom.current().nextInt();
		    });
		}
		executor.invokeAll(callables);
	}

}
