import java.util.ArrayList;

public class DineIn_Employee implements Runnable {
    private Thread thread;
    private long time;
    private String id;
    private ArrayList<Table> assignedTables = new ArrayList<>();
    private int orders = 0;
    private Table[] tables = new Table[Main.numTables];
    public boolean alive = true;

    public DineIn_Employee(String id, long time, Table[] tables) { // constructor
        setName("Dinein_Employee " + id);
        this.thread = new Thread(this, id);
        this.time = time;
        this.tables = tables;
    }

    @Override
    public void run() {
        Main.employeeLine.add(this); // adds employee to line
        try {
            Thread.sleep((long) (Math.random() * 1000)); // employee commutes to restaurant
        } catch (InterruptedException e) {
        }
        while (Main.left.get() != Main.numCustomers) { // while all customers in line havent been served
            for (Table t : tables) { // for each table check if there is no employee assigned to it if there isnt
                                     // assign one
                if (t.getAssignedEmployee() == null) {
                    assignEmployee();
                    break;
                }
            }
            DineIn_Employee emp = Main.employeeLine.remove(); // swap through available employees
            Main.employeeLine.add(emp);
            if (!Main.dineLine.isEmpty() && Main.seated < 9) { // if there are customers waiting to be seated and all
                                                               // tables havent been used
                try {
                    Thread.sleep(2000); // allows other employees to finish
                } catch (InterruptedException e) {
                }
                assignCustomer(); // assigns customer to table
            } else if (Main.dineLine.isEmpty() && Main.employeeLine.peek() == this) { // if everybody has been seated
                for (Table t : Main.tables) { // for each table that the employee is assigned to check if a customer is
                                              // ordering and take their order
                    if (this.assignedTables.contains(t)) {
                        for (Customer c : t.customerSeated) {
                            if (c.isOrdering() == true) {
                                msg("is taking " + c.getName() + "'s order");
                                c.setEat(true);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                }
                                msg("is bringing food to " + c.getName());
                                c.setEating(true);
                                c.setOrdering(false);
                                orders++;
                            } else if (c.getPay() == true && c.isOrdering() == false) {
                                c.setPay(false);
                            }
                        }
                    }
                }
            } else { // if there are still customers waiting to be seated then serve the customers
                     // currently sitting
                for (Table t : Main.tables) { // for each table check if customers are still ordering then take their
                                              // order
                    if (t.getAvailable() == false && this.assignedTables.contains(t)) {
                        for (Customer c : t.customerSeated) {
                            if (c.isOrdering() == true) { // if customer is ordering take their order
                                msg("is taking " + c.getName() + "'s order");
                                c.setEat(true);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                }
                                msg("is bringing food to " + c.getName());
                                c.setEating(true);
                                c.setOrdering(false);

                                orders++;
                            } else if (c.getPay() == true && c.isOrdering() == false) { // if customer is paying
                                c.setPay(false);
                            } else if (c.getPay() == false && t.getSeats() == 0) { // if customer has paid then check if
                                                                                   // table is empty add available seats
                                                                                   // and set table to available
                                t.addSeats(3);
                                t.setAvailable();
                            }
                        }
                    }
                }
                for (Table t : tables) { // for each table
                    if (t.getSeats() == 0) { // if table has no seats set it to not available
                        t.setNotAvailable();
                    } else if (t.getAvailable() == true && t.getAssignedEmployee() == this) { // if table is available
                                                                                              // and employee is
                                                                                              // assigned to it
                        if (Main.dineLine.size() != 0) { // if there are customers waiting to be seated
                            assignCustomer(); // assign customer to table
                        }
                    }
                }
            }
        }
        while (Main.pickup_employee.done == false) { // while pickup is not done wait for them to finish
            try {
                Thread.sleep((long) (Math.random() * 1000));
            } catch (InterruptedException e) {
            }
        }
        msg("is leaving"); // leave diner
    }

    private void assignCustomer() { // assigns customer to table
        if (Main.dineLine.size() >= 3) { // if there are more than 3 customers in line
            for (Table table : assignedTables) { // for each table
                if (table.getSeats() > 0 && table.getAssignedEmployee() == this) { // if table has seats and employee is
                                                                                   // assigned to it
                    for (int i = table.getSeats(); i > 0; i--) { // for each seat in table remove customer from line and
                                                                 // add them to table
                        Customer c = Main.dineLine.remove();
                        table.addCustomer(c);
                        Main.seated++;
                        msg(c.getName() + " has been seated at " + table.getName());
                        c.setSeated();
                        c.interrupt(); // interrupt customer thread
                    }
                    table.setNotAvailable(); // set table to not available
                }
            }
        } else { // if there are less than 3 customers in line
            for (Table table : assignedTables) {
                while (Main.employeeLine.peek() != this) { // while employee is not at front of line make him in front
                                                           // of line
                    DineIn_Employee emp = Main.employeeLine.remove();
                    Main.employeeLine.add(emp);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                if (table.getSeats() > 0 && table.getAssignedEmployee() == this && Main.lineAvailable == true) { // if
                                                                                                                 // table
                                                                                                                 // has
                                                                                                                 // seats
                                                                                                                 // and
                                                                                                                 // employee
                                                                                                                 // is
                                                                                                                 // assigned
                                                                                                                 // to
                                                                                                                 // it
                                                                                                                 // and
                                                                                                                 // other
                                                                                                                 // employee
                                                                                                                 // isnt
                                                                                                                 // assigning
                                                                                                                 // customers
                                                                                                                 // to
                                                                                                                 // tables
                    Main.lineAvailable = false;
                    for (int i = 0; i < Main.dineLine.size(); i++) {
                        Customer c = Main.dineLine.remove(); // remove customer from line and assign them to table
                        table.addCustomer(c);
                        msg(c.getName() + " has been seated at " + table.getName());
                        c.setSeated();
                        Main.seated++;
                        c.interrupt();
                    }
                    table.setNotAvailable(); // set table to not available
                    Main.lineAvailable = true; // release hold on customer line
                }
            }
        }
    }

    public void assignEmployee() { // assigns employee to table
        msg("is getting a table");
        for (Table t : tables) {
            if (t.getAssignedEmployee() == null) { // if table has no employee assigned to it assign it to employee
                t.assignEmployee(this);
                msg("has been assigned to " + t.getName());
                assignedTables.add(t);
                break;
            }
        }
    }

    public void setName(String name) { // sets employee name
        this.id = name;
    }

    public String getName() { // returns employee name
        return id;
    }

    public void start() { // starts employee thread
        thread.start();
    }

    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "] " + getName() + ": " + m);
    }

    public int getOrders() { // returns number of orders
        return orders;
    }
}
