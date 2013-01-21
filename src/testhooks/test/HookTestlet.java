package testhooks.test;

import org.junit.After;
import org.junit.Before;

import testhooks.common.HookConfig;

public abstract class HookTestlet {

    public HookManager hookManager = HookManager.getInstance();

    @Before
    public void setup() {
        cleanup();
        hookManager.startServer();
        HookConfig.toDb();
    }

    @After
    public void cleanup() {
        hookManager.reset();
        HookConfig.cleanAll();
    }

}
