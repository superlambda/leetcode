package inverview.creatingthread;

public class ExtendThread extends Thread {

    //method where the thread execution will start 
    public void run(){
        System.out.println("Thread " + Thread.currentThread().getId() + " is running"); 
    }

    //let’s see how to start the threads
    public static void main(String[] args){
       Thread t1 = new ExtendThread();
       Thread t2 = new ExtendThread();
       t1.start();  //start the first thread. This calls the run() method.
       t2.start(); //this starts the 2nd thread. This calls the run() method.  
    }

}
