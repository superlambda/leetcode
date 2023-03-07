package effect.java.concurrency.item78;

import java.util.concurrent.TimeUnit;

public class StopThreadV2 {
	
	private static boolean stopRequested;
	
	private static synchronized void requestStop() {
		stopRequested = true;
	}
	
	private static synchronized boolean stopRequest() {
		return stopRequested;
	}
	
	public static void main(String[] args) throws InterruptedException {
		
		Thread backgroundThread = new Thread(()->{
			int i = 0;
			while(!stopRequest()) {
				i++;
			}
		});
		backgroundThread.start();
		TimeUnit.SECONDS.sleep(1);
		requestStop();
	}

}
