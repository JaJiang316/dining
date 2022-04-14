import java.math.*;

public class Customer implements Runnable {
    private String id;
    private Thread thread;
    private long time;
    private volatile boolean served;

    public Customer(String id, long time) {
        setName("Customer " + id);
        this.thread = new Thread(this, id);
        this.time = time;
    }

    @Override
    public void run() {
        int commute = getCommute();
        setServed(false);
        try {
            Thread.sleep(commute);
            msg("has finished commuting to diner");
        } catch (InterruptedException e) {
        }
        msg("has decided to order");
        // int order = getOrder();
        int order = 1;
        if (order <= 2) {
            getOnLine(order);
        }
        while (getServed() == false) {
            try {
                Thread.sleep(1000);
                msg("is waiting for Employee to finish order");
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (served) {
            try {
                Main.left.getAndIncrement();
                Thread.sleep(getCommute()); // paying for the order
                msg("has paid finished picking up and is leaving");
            } catch (InterruptedException e) {
            }
        }
    }

    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "] " + getName() + ": " + m);
    }

    // generate commute time
    public int getCommute() {
        return (int) Math.floor(Math.random() * (12 - 8 + 1) + 8);
    }

    public void setName(String name) {
        this.id = name;
    }

    public String getName() {
        return id;
    }

    public int getOrder() {
        return (int) Math.floor(Math.random() * 10 + 1);
    }

    public void start() {
        thread.start();
    }

    public void setServed(boolean served) {
        this.served = served;
    }

    public boolean getServed() {
        return served;
    }

    public void getOnLine(int order) {
        msg("has decided to pick up and got on line.");
        try {
            Thread.sleep(order);
            Pickup_Employee.line.add(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void interrupt() {
        thread.interrupt();
    }
}
