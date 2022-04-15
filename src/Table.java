import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Table {

    private String id;
    private int seats = Main.numSeats;
    private DineIn_Employee assignedEmployee = null;
    public ArrayList<Customer> customerSeated = new ArrayList<>();
    private static boolean available = true;
    private static int orders = 0;
    public static Queue<DineIn_Employee> employeeLine = new LinkedBlockingQueue<DineIn_Employee>(2);

    public Table(String i) {
        setName("Table " + i);
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
    }

    public void assignEmployee(DineIn_Employee e) {
        assignedEmployee = e;
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
}
