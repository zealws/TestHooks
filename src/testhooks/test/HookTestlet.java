package testhooks.test;

import org.junit.After;
import org.junit.Before;

public abstract class HookTestlet {

    public HookManager hookManager = HookManager.getInstance();
    private int initTime = 1000;

    public void setInitTime(int millis) {
        initTime = millis;
    }

    @Before
    public void setup() throws InterruptedException {
        hookManager.startServer();

        // Wait for application to warm up and start initialize the hooks server.
        Thread.sleep(initTime);
        if (!hookManager.initialized())
            throw new RuntimeException("Hook manager failed to be initialized after 1000ms");
    }

    @After
    public void cleanup() {
        hookManager.reset();
    }

}
