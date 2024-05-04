package rose.simplepattern.work.durable;

import rose.simplepattern.work.Worker;

public class DurableAndFariWorkEnvironment {
    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            String name = "fairWork[" + i + "]";
            FairWorker fairWorker = new FairWorker(name);
            fairWorker.run();
        }
    }
}
