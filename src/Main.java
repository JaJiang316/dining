import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static int numCustomers = 7;
    public static int numTables = 3;
    public static int numSeats = 3;
    public static int totalSeats = numTables * numSeats;
    public static int num_table_employee = 2;
    public static Customer[] customers;
    public static Table[] tables = new Table[numTables];
    public static AtomicInteger left = new AtomicInteger();
    public static Pickup_Employee pickup_employee;
    public static DineIn_Employee[] dineIn_employees;
    public static Queue<Customer> dineLine = new LinkedBlockingQueue<Customer>(Main.numCustomers);

    public static long time = System.currentTimeMillis();

    public static void main(String[] args) throws Exception {
        customers = new Customer[numCustomers];
        dineIn_employees = new DineIn_Employee[num_table_employee];

        for (int i = 0; i < numTables; i++) {
            tables[i] = new Table(Integer.toString(i));
        }

        System.out.println("Employees getting ready...");
        pickup_employee = new Pickup_Employee(Integer.toString(num_table_employee), time);
        pickup_employee.start();

        for (int i = 0; i < num_table_employee; i++) {
            dineIn_employees[i] = new DineIn_Employee(Integer.toString(i), time);
        }

        for (int i = 0; i < num_table_employee; i++) {
            dineIn_employees[i].start();
        }

        System.out.println("Customers commuting to diner...");
        for (int i = 0; i < numCustomers; i++) {
            customers[i] = new Customer(Integer.toString(i), time);
        }

        for (int i = 0; i < numCustomers; i++) {
            customers[i].start();
        }

    }
}
