import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumerApp {

    static Queue<Integer> queue;
    final static int LIMIT = 10;
    Object lock = new Object();

    ProducerConsumerApp() {
        queue = new LinkedList<>();
    }

    public static void main(String[] args) throws InterruptedException {
        ProducerConsumerApp p = new ProducerConsumerApp();

        Thread t1 = new Thread(() -> {
            try {
                p.produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                p.consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

    public void produce() throws InterruptedException {
        while (true) {
            synchronized(lock) {
                if (queue.size() == LIMIT) {
                    lock.wait();
                } else {
                    int val = (int) (Math.random() * LIMIT);
                    queue.add(val);
                    System.out.println("Produced: " + val + "   Queue: " + queue.size());
                    lock.notify();
                }
            }
            Thread.sleep(500);
        }
    }

    public void consume() throws InterruptedException {
        while (true) {
            synchronized (lock) {
                if (queue.size() == 0) {
                    lock.wait();
                } else {
                    int val = queue.poll();
                    System.out.println("\t\t\t\t\tConsumed: " + val + "   Queue: " + queue.size());
                    lock.notify();
                }
            }
            Thread.sleep(1000);
        }
    }
}
