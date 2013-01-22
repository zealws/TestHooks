package testhooks.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import testhooks.test.HookManager;
import testhooks.test.HookTestlet;

public class ApplicationsRemoteTest extends HookTestlet {

    static {
        HookManager.initializeDb("jdbc:postgresql://localhost:5432/testhooks?user=testhooks&password=testhooks");
        HookManager.initializeDb("jdbc:postgresql://localhost:5432/testhookz?user=testhooks&password=testhooks");
    }

    @Test
    public void countIncrements() throws InterruptedException {
        Thread.sleep(2000); // Wait for first request to come in.
        assertNotNull("Could not initialize application after 2s (is it running?)", hookManager.get("counting-app", "count"));
        int initCount = Integer.parseInt(hookManager.get("counting-app", "count"));
        Thread.sleep(1000);
        int finalCount = Integer.parseInt(hookManager.get("counting-app", "count"));
        assertEquals(initCount + 1, finalCount);
    }

    @Test
    public void countDecrements() throws InterruptedException {
        Thread.sleep(2000); // Wait for first request to come in.
        assertNotNull("Could not initialize application after 2s (is it running?)", hookManager.get("countdown-app", "count"));
        int initCount = Integer.parseInt(hookManager.get("countdown-app", "count"));
        Thread.sleep(1000);
        int finalCount = Integer.parseInt(hookManager.get("countdown-app", "count"));
        assertEquals(initCount - 1, finalCount);
    }
}
