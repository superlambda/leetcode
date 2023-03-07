package inverview.busyspinning;

public class BusyWaitingSpinningApp {
	
	public static void main(String[] args) {
		Producer p = new Producer();

		(new Thread(p, "Producer")).start();
		(new Thread(new Consumer(p), "Consumer")).start();
	}

}
