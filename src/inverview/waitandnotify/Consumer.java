package inverview.waitandnotify;

import java.util.Queue;

public class Consumer extends Thread {
    private final Queue<Integer> sharedQ;

    public Consumer(Queue<Integer> sharedQ) {
        super("Consumer");
        this.sharedQ = sharedQ;
    }

    @Override
    public void run() {
        while(true) {
        	 System.out.println("Current thread holds lock before synchronized: " + Thread.holdsLock(sharedQ));
            synchronized (sharedQ) {
                //waiting condition - wait until Queue is not empty
                while (sharedQ.size() == 0) {
                    try {
                        System.out.println("Queue is empty, waiting");
                        System.out.println("Current thread holds lock: " + Thread.holdsLock(sharedQ));
                        sharedQ.wait();
                        System.out.println("Current thread holds lock after wait: " + Thread.holdsLock(sharedQ));
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                int number = sharedQ.poll();
                System.out.println("consuming : " + number );
                sharedQ.notify();
              
                //termination condition
                if(number == 3){
                	break; 
                }
            }
        }
    }

}
