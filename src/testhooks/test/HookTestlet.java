package testhooks.test;

import org.junit.After;
import org.junit.Before;

public abstract class HookTestlet {

    public static HookManager hookManager = HookManager.getInstance();

    @Before
    public void setup() {
        cleanup();
        hookManager.propagateConf();
        hookManager.startServer();
    }

    @After
    public void cleanup() {
        hookManager.cleanDbs();
        hookManager.reset();
    }

}
