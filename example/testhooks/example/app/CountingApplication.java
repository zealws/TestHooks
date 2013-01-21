package testhooks.example.app;

import testhooks.source.Hook;

public class CountingApplication extends Thread {

    private int count = 0;
    private Hook hook = new Hook("sample-app");

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException("Interrupted", e);
            }

            System.out.println("Application Count: " + count);

            count += 1;
            hook.add("count", count);
            hook.send();
        }
    }

    public static void main(String[] args) {
        CountingApplication app = new CountingApplication();
        app.run();
    }
}
