package inverview.catchexceptionthrownbyanotherthread;

/*
 * How can you catch an exception thrown by another thread in Java?
 */
public class CatchExceptionTest {

	public static void main(String[] args) {
		// create our uncaught exception handler
		Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
			public void uncaughtException(Thread th, Throwable ex) {
				System.out.println("Uncaught exception: " + ex);
			}
		};

		// create another thread
		Thread otherThread = new Thread() {
			public void run() {
				System.out.println("Sleeping ...");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					System.out.println("Interrupted.");
				}
				System.out.println("Throwing exception ...");
				throw new RuntimeException();
			}
		};

		// set our uncaught exception handler as the one to be used when the new thread
		// throws an uncaught exception
		otherThread.setUncaughtExceptionHandler(handler);

		// start the other thread - our uncaught exception handler will be invoked when
		// the other thread throws an uncaught exception
		otherThread.start();

	}

}
