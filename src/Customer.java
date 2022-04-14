public class Customer implements Runnable {
    private String id;
    private Thread thread;
    private long time;
    private volatile boolean served;
    private boolean available = false;

    public Customer(String id, long time) {
        setName("Customer " + id);
        this.thread = new Thread(this, id);
        this.time = time;
    }

    @Override
    public void run() {
        int commute = getCommute();
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
            while (available == false) {
                try {
                    if (thread.isInterrupted()) {
                        available = true;
                    } else {
                        Thread.sleep(1000);
                        msg("is waiting for Employee to finish order");
                    }
                } catch (InterruptedException e) {
                }
            }
            try {
                Main.left.getAndIncrement();
                Thread.sleep(getCommute()); // paying for the order
                msg("has paid and has finished picking up and is leaving");
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

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void getOnLine(int order) {
        msg("has decided to pick up and got on line.");
        Pickup_Employee.line.add(this);
    }

    public boolean isAlive() {
        if (thread.isAlive()) {
            return true;
        } else {
            return false;
        }
    }

    public void interrupt() {
        thread.interrupt();
    }
}
