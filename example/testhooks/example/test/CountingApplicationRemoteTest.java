package testhooks.example.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import testhooks.test.HookTestlet;

public class CountingApplicationRemoteTest extends HookTestlet {

    public CountingApplicationRemoteTest() {
        setInitTime(2000);
    }

    @Test
    public void countIncrements() throws InterruptedException {
        int initCount = Integer.parseInt(hookManager.get("sample-app", "count"));
        Thread.sleep(1000);
        int finalCount = Integer.parseInt(hookManager.get("sample-app", "count"));
        assertEquals(initCount + 1, finalCount);
    }
}
