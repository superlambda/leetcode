package inverview.creatingthread;

public class ImplementRunnable implements Runnable {
	
	@Override
    public void run() {
		System.out.println("Thread " + Thread.currentThread().getId() + " is running"); 
    }

    public static void main(String[] args) {
		Thread t1 = new Thread(new ImplementRunnable());
        t1.start();
    }
}
