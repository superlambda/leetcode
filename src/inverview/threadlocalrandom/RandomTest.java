package inverview.threadlocalrandom;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RandomTest {

	public static void main(String[] args) throws InterruptedException {
		ExecutorService executor = Executors.newWorkStealingPool();
		List<Callable<Integer>> callables = new ArrayList<>();
		Random random = new Random();
		for (int i = 0; i < 1000; i++) {
		    callables.add(() -> {
		         return random.nextInt();
		    });
		}
		executor.invokeAll(callables);

	}

}
