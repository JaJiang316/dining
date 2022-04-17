import java.util.ArrayList;

public class Table {

    private String id;
    private int seats = Main.numSeats;
    private DineIn_Employee assignedEmployee = null;
    private volatile boolean assigning = true;
    public ArrayList<Customer> customerSeated = new ArrayList<>();
    private static boolean available = true;
    private long time;

    public Table(String i, long time) {
        setName("Table " + i);
        this.time = time;
    }

    public void setName(String string) {
        this.id = string;
    }

    public String getName() {
        return id;
    }

    public void addCustomer(Customer c) {
        if (seats > 0) {
            seats--;
            customerSeated.add(c);
            c.setSeated();
        }
        if (seats == 0) {
            setNotAvailable();
        }
    }

    public void assignEmployee(DineIn_Employee e) {
        while (assigning) {
            assigning = false;
            if (assignedEmployee == null) {
                msg("Assigning " + e.getName() + " to " + this.getName());
                assignedEmployee = e;
            } else {
                msg("Employee " + e.getName() + " is already assigned to " + this.getName());
            }
        }
    }

    public Object getAssignedEmployee() {
        return assignedEmployee;
    }

    public int getSeats() {
        return seats;
    }

    public void addSeats(int i) {
        seats += i;
    }

    public void setSeats() {
        this.seats = seats - 1;
    }

    public void setNotAvailable() {
        available = false;
    }

    public void setAvailable() {
        available = true;
    }

    public boolean getAvailable() {
        return available;
    }

    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "] " + getName() + ": " + m);
    }
}
