package inverview.forkjoin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class CustomRecursiveTask extends RecursiveTask<Integer> {

	private static final long serialVersionUID = 8206412995104226090L;

	private int[] arr;

	private static final int THRESHOLD = 20;

	public CustomRecursiveTask(int[] arr) {
		this.arr = arr;
	}

	/*
	 * In this example, we use an array stored in the arr field of the
	 * CustomRecursiveTask class to represent the work. The createSubtasks() method
	 * recursively divides the task into smaller pieces of work until each piece is
	 * smaller than the threshold. Then the invokeAll() method submits the subtasks
	 * to the common pool and returns a list of Future.
	 * 
	 * To trigger execution, the join() method is called for each subtask.
	 * 
	 * We've accomplished this here using Java 8's Stream API. We use the sum()
	 * method as a representation of combining sub results into the final result.
	 */
	@Override
	protected Integer compute() {
		if (arr.length > THRESHOLD) {
			return ForkJoinTask.invokeAll(createSubtasks()).stream().mapToInt(ForkJoinTask::join).sum();
		} else {
			return processing(arr);
		}
	}

	private Collection<CustomRecursiveTask> createSubtasks() {
		List<CustomRecursiveTask> dividedTasks = new ArrayList<>();
		dividedTasks.add(new CustomRecursiveTask(Arrays.copyOfRange(arr, 0, arr.length / 2)));
		dividedTasks.add(new CustomRecursiveTask(Arrays.copyOfRange(arr, arr.length / 2, arr.length)));
		return dividedTasks;
	}

	private Integer processing(int[] arr) {
		return Arrays.stream(arr).filter(a -> a > 10 && a < 27).map(a -> a * 10).sum();
	}

}
