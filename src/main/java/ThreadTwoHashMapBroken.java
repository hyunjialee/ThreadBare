import java.util.HashMap;
import java.util.Set;

public class ThreadTwoHashMapBroken extends Thread {
    HashMap<String, Thread> threadMap;

    public ThreadTwoHashMapBroken(String name) {
        super(name);
        this.threadMap = new HashMap<>();
    }

    @Override
    public void run() {
        System.out.println("ThreadTwoHashMapB - START "+Thread.currentThread().getName());
        try {
            Thread.sleep(1000);
            //Get database connection, delete unused data from DB
            doDBProcessing();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ThreadTwoHashMapB - END "+Thread.currentThread().getName());
    }

    private void doDBProcessing() throws InterruptedException {
        Thread.sleep(5000);
    }


    // Run this
    public static void main(String[] args){
        ThreadTwoHashMapBroken tm = new ThreadTwoHashMapBroken(""+10);

        // What's wrong with this idea??...
        new Thread("Run of " + 6){
            public void run(){
                tm.runMapOfSize(6);
            }
        }.start();
        new Thread("Run of " + 8){
            public void run(){
                tm.runMapOfSize(8);
            }
        }.start();

    }

    private void runMapOfSize(int size) {
        System.out.println("Constructing HashMap of Size "+size);
        Integer threadCount = size;

        for (int i = 0; i < threadCount; i++) {
            this.threadMap.put("T"+ i, new ThreadTwoHashMapBroken("T"+ i));
        }
        System.out.println("Starting Threads in HashMap");
        Set<String> names = this.threadMap.keySet();
        for (String name : names) {
            this.threadMap.get(name).start();
        }
        System.out.println("Thread HashMap, all have been started");
    }
}

/**
 Constructing HashMap of Size 8
 Constructing HashMap of Size 6
 Starting Threads in HashMap
 Starting Threads in HashMap
 ThreadTwoHashMapB - START T4
 ThreadTwoHashMapB - START T5
 ThreadTwoHashMapB - START T6
 ThreadTwoHashMapB - START T7
 ThreadTwoHashMapB - START T0
 ThreadTwoHashMapB - START T1
 ThreadTwoHashMapB - START T2
 Thread HashMap, all have been started
 ThreadTwoHashMapB - START T3
 Exception in thread "Run of 6" java.lang.IllegalThreadStateException
 at java.base/java.lang.Thread.start(Thread.java:1525)
 at ThreadTwoHashMapBroken.runMapOfSize(ThreadTwoHashMapBroken.java:58)
 at ThreadTwoHashMapBroken.access$000(ThreadTwoHashMapBroken.java:4)
 at ThreadTwoHashMapBroken$1.run(ThreadTwoHashMapBroken.java:37)
 ThreadTwoHashMapB - END T4
 ThreadTwoHashMapB - END T5
 ThreadTwoHashMapB - END T1
 ThreadTwoHashMapB - END T2
 ThreadTwoHashMapB - END T3
 ThreadTwoHashMapB - END T7
 ThreadTwoHashMapB - END T6
 ThreadTwoHashMapB - END T0


 - Two threads are trying to run the same thread
 Process finished with exit code */