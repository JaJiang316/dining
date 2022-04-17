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
    private boolean paying = true;

    public Customer(String id, long time) { // constructor
        setName("Customer " + id);
        this.thread = new Thread(this, id);
        this.time = time;
    }

    @Override
    public void run() {
        int commute = getCommute();
        try { // customer commutes to restaurant
            Thread.sleep(commute);
            msg("has finished commuting to diner");
        } catch (InterruptedException e) {
        }
        msg("has decided to order");
        int order = getOrder(); // customer orders
        if (order <= 2) { // if order is less than or equal to 2 pick up
            getOnLine(); // customer gets on line
            while (available == false) { // while employee is not available
                try {
                    if (thread.isInterrupted()) { // once employee is available interrupt the customer
                        available = true;
                    } else {
                        Thread.sleep(1000); // wait for employee to become available
                        msg("is waiting for Employee to finish order");
                    }
                } catch (InterruptedException e) {
                }
            }
            try {
                Main.left.getAndIncrement(); // increment the number of customers left to serve
                Thread.sleep(getCommute()); // paying for the order
                msg("has paid and has finished picking up and is leaving");
            } catch (InterruptedException e) {
            }
        } else { // if customer chooses to dinein
            dineIn(); // gets on line
            while (!seated) { // while the customer is not seated
                try {
                    if (thread.isInterrupted()) { // once customer is seated interrupt the customer
                        seated = true;
                    } else {
                        Thread.sleep(1000);
                        msg("is waiting for Employee to seat");
                    }
                } catch (InterruptedException e) {
                }
            }
            Thread.currentThread().setPriority(10);
            try {
                msg("is deciding on what to eat");
                Thread.sleep((long) (Math.random() * 1000)); // customer decides what to eat
            } catch (InterruptedException e) {
            }
            Thread.currentThread().setPriority(5);
            msg("has decided on what to eat");
            isOrdering = true;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            while (!eat) { // waiting for employee to finish order
                try {
                    Thread.sleep(1000);
                    msg("is waiting for their order to be served");
                } catch (InterruptedException e) {
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            while (!eating) { // while customer hasnt been served yet
            }
            isOrdering = false; // customer is no longer ordering as they have been served
            msg("is eating their food");
            try {
                Thread.sleep((long) (Math.random() * 1000)); // simulate eating
            } catch (InterruptedException e) {
            }
            Thread.yield();
            Thread.yield();
            while (paying == true) { // while customer is waiting to pay
                try {
                    Thread.sleep(1000);
                    msg("is waiting to pay");
                } catch (InterruptedException e) {
                }
            }
            msg("has paid and is now leaving"); // customer has paid and is leaving
            Main.left.getAndIncrement();
        }
    }

    public void dineIn() { // customer gets on line
        msg("has decided to dine in and got on line.");
        Main.dineLine.add(this);
    }

    public void setEat(boolean eat) {
        this.eat = eat;
    }

    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "] " + getName() + ": " + m);
    }

    // generate commute time
    public int getCommute() {
        return (int) Math.floor(Math.random() * (12 - 8 + 1) + 8);
    }

    public void setName(String name) { // set name
        this.id = name;
    }

    public String getName() { // get name
        return id;
    }

    public boolean getPay() { // get pay
        return paying;
    }

    public int getOrder() { // get order
        return (int) Math.floor(Math.random() * 10 + 1);
    }

    public void start() { // start thread
        thread.start();
    }

    public void setServed(boolean served) { // set served
        this.served = served;
    }

    public boolean getServed() { // get served
        return served;
    }

    public boolean isOrdering() { // get is ordering
        return isOrdering;
    }

    public void setEating(boolean eating) { // set eating
        this.eating = eating;
    }

    public boolean getEating() { // get eating
        return eating;
    }

    public void setSeated() { // set seated
        this.seated = true;
    }

    public void setOrdering(boolean ordering) { // set ordering
        this.isOrdering = ordering;
    }

    public void setAvailable(boolean available) { // set available
        this.available = available;
    }

    public void getOnLine() { // get on line
        msg("has decided to pick up and got on line.");
        Pickup_Employee.line.add(this);
    }

    public boolean isAlive() { // is alive
        if (thread.isAlive()) {
            return true;
        } else {
            return false;
        }
    }

    public void interrupt() { // interrupt thread
        thread.interrupt();
    }

    public void setPay(boolean pay) { // set pay
        this.paying = pay;
    }
}
