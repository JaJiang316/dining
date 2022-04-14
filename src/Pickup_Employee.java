import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Pickup_Employee implements Runnable {
    private Thread thread;
    private long time;
    private String id;
    public static Queue<Customer> line;

    public Pickup_Employee(String id, long time) {
        setName("Pickup_Employee: " + id);
        this.thread = new Thread(this, id);
        this.time = time;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        line = new LinkedBlockingQueue<Customer>(Main.numCustomers);
        while (Main.left.get() != Main.numCustomers) {
            // System.out.println(line.toString());
            if (!line.isEmpty()) {
                Customer c = line.poll();
                try {
                    Thread.sleep(prepareOrder()); // works on order
                    msg("has finished preparing order");
                    // c.interrupt();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                msg("gave bills and order to Customer " + c.getName());
                c.setServed(true);
                // c.interrupt();
            }
        }
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
}
