public class DineIn_Employee implements Runnable {
    private int served;
    private Thread thread;
    private long time;
    private String id;

    public DineIn_Employee(String id, long time) {
        setName("Dinein_Employee " + id);
        this.thread = new Thread(this, id);
        this.time = time;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub

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
