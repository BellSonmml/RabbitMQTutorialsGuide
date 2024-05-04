package rose.simplepattern.work;

public class WorkSpace {
    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            String name = "worker[" + i + "]";
            Worker worker = new Worker(name);
            worker.run();
        }
    }
}
