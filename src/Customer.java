public class Customer implements Runnable {
    private String id;
    private Thread thread;
    private long time;
    private volatile boolean served;
    private boolean available = false;
    private boolean isOrdering = false;
    private boolean seated = false;
    private boolean eat = false;
    private boolean eating = false;
    private boolean paying = false;

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
        int order = 3;
        if (order <= 2) {
            getOnLine();
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
        } else { // customer chooses to dinein
            dineIn();
            while (!seated) {
                try {
                    if (thread.isInterrupted()) {
                        seated = true;
                    } else {
                        Thread.sleep(1000);
                        msg("is waiting for Employee to seat");
                    }
                } catch (InterruptedException e) {
                    System.out.println("seated Thread interrupted");
                }
            }
            Thread.currentThread().setPriority(10);
            try {
                msg("is deciding on what to eat");
                Thread.sleep((long) (Math.random() * 1000));
            } catch (InterruptedException e) {
            }
            Thread.currentThread().setPriority(5);
            msg("has decided on what to eat");
            isOrdering = true;
            while (!eat) {
                try {
                    if (thread.isInterrupted()) {
                        System.out.println("hello");
                        eat = true;
                    } else {
                        Thread.sleep(1000);
                        msg("is waiting for their order to be served");
                    }
                } catch (InterruptedException e) {
                    System.out.println("eat Thread interrupted");
                }
            }
            while (!eating) {
                try {
                    if (thread.isInterrupted()) {
                        eating = true;
                        msg("is eating their food");
                        Thread.sleep((long) (Math.random() * 1000));
                        Thread.yield();
                        Thread.yield();
                    } else {
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    System.out.println("eating Thread interrupted");
                }
            }
            while (!paying) {
                try {
                    if (thread.isInterrupted()) {
                        paying = true;
                        msg("has paid and is now leaving");
                        Main.left.getAndIncrement();
                    } else {
                        Thread.sleep(2000);
                    }
                } catch (InterruptedException e) {
                }
            }

        }
    }

    public void dineIn() {
        msg("has decided to dine in and got on line.");
        Main.dineLine.add(this);
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

    public boolean getPaying() {
        return paying;
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

    public boolean isOrdering() {
        return isOrdering;
    }

    public void setEating(boolean eating) {
        this.eating = eating;
    }

    public boolean getEating() {
        return eating;
    }

    public void setSeated() {
        this.seated = true;
    }

    public void setOrdering(boolean ordering) {
        this.isOrdering = ordering;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void getOnLine() {
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
