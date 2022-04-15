import java.util.ArrayList;

public class DineIn_Employee implements Runnable {
    private int served;
    private Thread thread;
    private long time;
    private String id;
    private ArrayList<Table> assignedTables = new ArrayList<>();
    private volatile boolean occupied = true;
    private boolean availableTable = false;

    public DineIn_Employee(String id, long time) {
        setName("Dinein_Employee " + id);
        this.thread = new Thread(this, id);
        this.time = time;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub3
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        Table.employeeLine.add(this);
        while (Main.left.get() <= Main.numCustomers) {
            if (Table.employeeLine.peek() == this && !Main.dineLine.isEmpty()) {
                assignEmployee();
                boolean seated = assignCustomer();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                Table.employeeLine.remove(this);
                Table.employeeLine.add(this);
            } else if (Table.employeeLine.peek() == this && Main.dineLine.isEmpty()) {
                for (Table t : Main.tables) {
                    for (Customer c : t.customerSeated) {
                        if (c.isOrdering() == true && !occupied) {
                            occupied = true;
                            msg("is taking " + c.getName() + "'s order");
                            c.interrupt();
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                            }
                            msg("is bringing food to " + c.getName());
                            c.interrupt();
                            c.setOrdering(false);
                            occupied = false;
                        }
                    }
                }
            }
        }
    }

    private boolean assignCustomer() {
        if (Main.dineLine.size() >= 3) {
            for (Table table : assignedTables) {
                if (table.getSeats() > 0 && table.getAssignedEmployee() == this) {
                    for (int i = table.getSeats(); i > 0; i--) {
                        Customer c = Main.dineLine.remove();
                        table.addCustomer(c);
                        msg(c.getName() + " has been seated at " + table.getName());
                        c.setSeated();
                        c.interrupt();
                        Main.numSeats--;
                        table.setSeats();
                    }
                    table.setNotAvailable();
                }
            }
        } else {
            for (Table table : assignedTables) {
                if (table.getSeats() > 0 && table.getAssignedEmployee() == this) {
                    int size = Main.dineLine.size();
                    for (int i = 0; i < size; i++) {
                        // System.out.println(Main.dineLine.toString());
                        Customer c = Main.dineLine.remove();
                        table.addCustomer(c);
                        msg(c.getName() + " has been seated at " + table.getName());
                        c.setSeated();
                        c.interrupt();
                        Main.numSeats--;
                        table.setSeats();
                    }
                    table.setNotAvailable();
                }
            }

        }
        return true;
    }

    private void assignEmployee() {
        for (int i = 0; i < Main.tables.length; i++) {
            if (Main.tables[i].getAssignedEmployee() == null) {
                Main.tables[i].assignEmployee(this);
                assignedTables.add(Main.tables[i]);
                break;
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
}
