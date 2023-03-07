package inverview.executor;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ExecutingRunnableTaskTest {
	public static void main(String[] args) {
		// Demo task
		Runnable runnableTask = () -> {
			try {
				TimeUnit.MILLISECONDS.sleep(1000);
				System.out.println("Current time :: " + LocalDateTime.now());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
		
//		void execute(Runnable task) – executes the given command at some time in the future.
//		Future submit(Runnable task) – submits a runnable task for execution and returns a 
//									   Future representing that task. The Future’s get() method 
//									   will return null upon successful completion.
//		Future submit(Runnable task, T result) – Submits a runnable task for execution and returns 
//												 a Future representing that task. The Future’s get() 
//												 method will return the given result upon successful completion.
				
		// Executor service instance
		ExecutorService executor = Executors.newFixedThreadPool(10);

		// 1. execute task using execute() method
		executor.execute(runnableTask);

		// 2. execute task using submit() method
		Future<String> result = executor.submit(runnableTask, "DONE");

		while (result.isDone() == false) {
			try {
				System.out.println("The method return value : " + result.get());
				break;
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}

			// Sleep for 1 second
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// Shut down the executor service
		executor.shutdownNow();
	}

}
