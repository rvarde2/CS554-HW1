package homework1;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

class philosopherThread  extends Thread{
    private Thread thread;
    private int index;
    private String threadName;
    private static ReentrantLock  chopstick_lock = new ReentrantLock();
    public static ArrayList<Boolean> chopsticks = new ArrayList<Boolean>();
    public static int timeRequiredToEat = 1000;//time in ms
    public static int timeRequiredForUnitThinking = 500;//time in ms
    public static int totalNumberOfPhilosophers = 0; //Can also use length of chopsticks

    philosopherThread(int index){
        this.index = index;
        this.threadName="Thread_"+index;
    }
    public synchronized void run(){
        //Loop to wait for Chopsticks
        while(true){
            if(getChopsticks()){
                break;
            }
            try {
                Thread.sleep(philosopherThread.timeRequiredForUnitThinking);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //Chopsticks Acquired, Philosopher can eat now
        try {
            Thread.sleep(philosopherThread.timeRequiredToEat);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //After eating Philosopher return Chopsticks
        returnChopsticks();
    }
    public void start(){
        if(thread==null){
            thread = new Thread(this,threadName);
            thread.start();
        }
    }
    private Boolean getChopsticks(){
        Boolean status = Boolean.FALSE;
        philosopherThread.chopstick_lock.lock();
        try {
            if (index == (philosopherThread.totalNumberOfPhilosophers - 1)) {
                if (philosopherThread.chopsticks.get(index) == Boolean.FALSE && philosopherThread.chopsticks.get(0) == Boolean.FALSE){
                    philosopherThread.chopsticks.set(index, Boolean.TRUE);
                    philosopherThread.chopsticks.set(0, Boolean.TRUE);
                    status = Boolean.TRUE;
                    System.out.println("Philosopher "+ index + " started eating with chopsticks " + index + " and 0");
                }
            }
            else {
                if (philosopherThread.chopsticks.get(index) == Boolean.FALSE && philosopherThread.chopsticks.get(index+1) == Boolean.FALSE){
                    philosopherThread.chopsticks.set(index, Boolean.TRUE);
                    philosopherThread.chopsticks.set(index+1, Boolean.TRUE);
                    status = Boolean.TRUE;
                    System.out.println("Philosopher "+ index + " started eating with chopsticks " + index + " and "+ (index+1));
                }
            }
        }
        finally {
            philosopherThread.chopstick_lock.unlock();
        }
        return status;
    }
    private void returnChopsticks(){
        philosopherThread.chopstick_lock.lock();
        try {
            if (index == (philosopherThread.totalNumberOfPhilosophers - 1)) {
                philosopherThread.chopsticks.set(index, Boolean.FALSE);
                philosopherThread.chopsticks.set(0, Boolean.FALSE);
                System.out.println("Philosopher "+ index + " completed eating with chopsticks " + index + " and 0");
            }
            else {
                philosopherThread.chopsticks.set(index, Boolean.FALSE);
                philosopherThread.chopsticks.set(index+1, Boolean.FALSE);
                System.out.println("Philosopher "+ index + " completed eating with chopsticks " + index + " and "+ (index+1));
            }
        }
        finally{
            philosopherThread.chopstick_lock.unlock();
        }
    }
}
public class hw1 {
    public static void main(String[] args) throws InterruptedException {
        System.out.print("Enter the Number of Philosophers:");
        Scanner input = new Scanner(System.in);
        int nop = input.nextInt();
        if(nop<2){
            System.out.print("Expecting atleast 2 Philosophers");
            System.exit(0);
        }
        ArrayList<philosopherThread> philosopherThreads = new ArrayList<philosopherThread>();
        philosopherThread.totalNumberOfPhilosophers = nop;
        for(int itr=0;itr<nop;itr++){
            philosopherThread.chopsticks.add(Boolean.FALSE);
            philosopherThread thread = new philosopherThread(itr);
            thread.start();
            philosopherThreads.add(thread);
        }
        for(int itr=0;itr<nop;itr++){
            philosopherThreads.get(itr).join();
        }
    }
}
