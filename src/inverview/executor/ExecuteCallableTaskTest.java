package inverview.executor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ExecuteCallableTaskTest {

	public static void main(String[] args) throws ExecutionException {
		// Demo Callable task
		Callable<String> callableTask = () -> {
			TimeUnit.MILLISECONDS.sleep(1000);
			return "Current time :: " + LocalDateTime.now();
		};
		/*
		 * Future submit(callableTask) – submits a value-returning task for execution and returns a 
		 * 								 Future representing the pending results of the task.
		 * List<Future> invokeAll(Collection tasks) – executes the given tasks, returning a list of 
		 * 											  Futures holding their status and results when all complete. 
		 * 											  Notice that result is available only when all tasks are completed.
		 *										 	  Note that a completed task could have terminated either normally 
		 *											  or by throwing an exception.
		 *List<Future> invokeAll(Collection tasks, timeOut, timeUnit) – executes the given tasks, returning a 
		 *																list of Futures holding their status 
		 *																and results when all complete or the 
		 *															    timeout expires.
		 */
		// Executor service instance
		ExecutorService executor = Executors.newFixedThreadPool(1);

		List<Callable<String>> tasksList = Arrays.asList(callableTask, callableTask, callableTask);

		// 1. execute tasks list using invokeAll() method
		/* Notice that tasks have been completed with a delay of 1 second 
		 * because there is only one task in the thread pool. But when 
		 * you run the program, all first 3 print statements appear at 
		 * the same time because even if the tasks are complete, they 
		 * wait for other tasks to complete in the list.
		 */
		try {
			List<Future<String>> results = executor.invokeAll(tasksList);

			for (Future<String> result : results) {
				System.out.println(result.get());
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		// 2. execute individual tasks using submit() method
		Future<String> result = executor.submit(callableTask);

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
//		executor.shutdownNow();

	}

}
