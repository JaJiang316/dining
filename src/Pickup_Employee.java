import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Pickup_Employee implements Runnable {
    private Thread thread;
    private long time;
    private String id;
    public static Queue<Customer> line = new LinkedBlockingQueue<Customer>(Main.numCustomers);
    private static Customer c;
    private volatile boolean occupied = true;
    public boolean done = false;

    public Pickup_Employee(String id, long time) {
        setName("Pickup_Employee " + id);
        this.thread = new Thread(this, id);
        this.time = time;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        while (!line.isEmpty()) {
            if (occupied) {
                occupied = false;
                c = (Customer) line.remove();
                msg("is serving " + c.getName());
                try {
                    Thread.sleep(prepareOrder()); // works on order
                    msg("has finished preparing order");
                } catch (InterruptedException e) {
                }
                msg("gave bills and order to " + c.getName());
                c.setAvailable(true);
                c.interrupt();
                occupied = true;
            }
        }
        while (Main.left.get() != Main.numCustomers) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        msg("has finished serving all customers and is leaving");
        done = true;
    }

    public void join() throws InterruptedException {
        Thread.currentThread().join();
    }

    public void setName(String name) {
        this.id = name;
    }

    public String getName() {
        return id;
    }

    public void start() {
        thread.start();
    }

    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "] " + getName() + ": " + m);
    }

    public int prepareOrder() {
        return (int) Math.floor(Math.random() * (12 - 8 + 1) + 8);
    }

    public static boolean isAlive() {
        return Thread.currentThread().isAlive();
    }
}
