import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static int numCustomers = 17;
    public static int numTables = 3;
    public static int numSeats = 3;
    public static volatile int table = 0;
    public static int totalSeats = numTables * numSeats;
    public static int num_table_employee = 2;
    public static Customer[] customers;
    public static Table[] tables = new Table[numTables];
    public static AtomicInteger left = new AtomicInteger();
    public static int seated = 0;
    public static boolean lineAvailable = true;
    public static Pickup_Employee pickup_employee;
    public static DineIn_Employee[] dineIn_employees;
    public static Queue<Customer> dineLine = new LinkedBlockingQueue<Customer>(Main.numCustomers);
    public static Queue<DineIn_Employee> employeeLine = new LinkedBlockingQueue<DineIn_Employee>(2);

    public static long time = System.currentTimeMillis();

    public static void main(String[] args) throws Exception {
        customers = new Customer[numCustomers];
        dineIn_employees = new DineIn_Employee[num_table_employee];

        for (int i = 0; i < numTables; i++) { // create tables
            tables[i] = new Table(Integer.toString(i), time);
        }

        System.out.println("Employees getting ready...");
        pickup_employee = new Pickup_Employee(Integer.toString(num_table_employee), time); // create pickup employee and
                                                                                           // start it
        pickup_employee.start();

        for (int i = 0; i < num_table_employee; i++) { // create din-in employees and start them
            dineIn_employees[i] = new DineIn_Employee(Integer.toString(i), time, tables);
            dineIn_employees[i].start();
        }

        System.out.println("Customers commuting to diner..."); // create customers and start them
        for (int i = 0; i < numCustomers; i++) {
            customers[i] = new Customer(Integer.toString(i), time);
            customers[i].start();
        }

    }
}
